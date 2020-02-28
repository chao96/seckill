package com.seckill.server.config;

import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.Map;

/**
 * 通用化 Rabbitmq 配置
 */
@Configuration
public class RabbitmqConfig {

    private final static Logger log = LoggerFactory.getLogger(RabbitmqConfig.class);

    @Autowired
    private Environment env;

    // 连接工厂
    @Autowired
    private CachingConnectionFactory connectionFactory;

    // 消费者监听器容器工厂（配置消费者实例）
    @Autowired
    private SimpleRabbitListenerContainerFactoryConfigurer factoryConfigurer;

    /**
     * 单一消费者
     *
     * @return
     */
    @Bean(name = "singleListenerContainer")
    public SimpleRabbitListenerContainerFactory listenerContainer() {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        // 消息传输格式
        factory.setMessageConverter(new Jackson2JsonMessageConverter());
        // 只允许一个线程消费
        factory.setConcurrentConsumers(1);
        factory.setMaxConcurrentConsumers(1);
        // 每次只拉一条消息
        factory.setPrefetchCount(1);
        // 设置事务当中可以处理的消息数量
        factory.setTxSize(1);
        return factory;
    }

    /**
     * 多个消费者
     *
     * @return
     */
    @Bean(name = "multiListenerContainer")
    public SimpleRabbitListenerContainerFactory multiListenerContainer() {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factoryConfigurer.configure(factory, connectionFactory);
        // 消息传输格式
        factory.setMessageConverter(new Jackson2JsonMessageConverter());
        // 设置consumer端的应答模式(确认消费模式-NONE)
        factory.setAcknowledgeMode(AcknowledgeMode.NONE);
        // 设置消费者实例数
        factory.setConcurrentConsumers(env.getProperty("spring.rabbitmq.listener.simple.concurrency", int.class));
        factory.setMaxConcurrentConsumers(env.getProperty("spring.rabbitmq.listener.simple.max-concurrency", int.class));
        // 设置一次拉取消息数量
        factory.setPrefetchCount(env.getProperty("spring.rabbitmq.listener.simple.prefetch", int.class));
        return factory;
    }

    @Bean
    public RabbitTemplate rabbitTemplate() {
        // 消息确认机制
        connectionFactory.setPublisherConfirms(true);
        connectionFactory.setPublisherReturns(true);

        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);

        /*
            当mandatory标志位设置为true时，如果exchange根据自身类型和消息routingKey无法找到一个合适的queue存储消息，
            那么broker会调用basic.return方法将消息返还给生产者;当mandatory设置为false时，
            出现上述情况broker会直接将消息丢弃;通俗的讲，mandatory标志告诉broker代理服务器至少将消息route到一个队列中，
            否则就将消息return给发送者;
         */
        // 设为true使ReturnCallback生效
        rabbitTemplate.setMandatory(true);
        // setReturnCallback、setConfirmCallback：return和confirm机制的回调接口
        rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
            @Override
            public void confirm(CorrelationData correlationData, boolean ack, String cause) {
                log.info("消息发送成功:correlationData({}),ack({}),cause({})", correlationData, ack, cause);
            }
        });
        rabbitTemplate.setReturnCallback(new RabbitTemplate.ReturnCallback() {
            @Override
            public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
                log.warn("消息丢失:exchange({}),route({}),replyCode({}),replyText({}),message:{}", exchange, routingKey, replyCode, replyText, message);
            }
        });
        return rabbitTemplate;
    }


    //构建异步发送邮箱通知的消息模型
    @Bean
    public Queue successEmailQueue() {
        return new Queue(env.getProperty("mq.kill.item.success.email.queue"), true);
    }

    @Bean
    public TopicExchange successEmailExchange() {
        return new TopicExchange(env.getProperty("mq.kill.item.success.email.exchange"), true, false);
    }

    // 绑定队列到交换机的某个路由上
    @Bean
    public Binding successEmailBinding() {
        return BindingBuilder.bind(successEmailQueue()).to(successEmailExchange()).with(env.getProperty("mq.kill.item.success.email.routing.key"));
    }


    //构建秒杀成功之后-订单超时未支付的死信队列消息模型
    @Bean
    public Queue successKillDeadQueue() {
        Map<String, Object> argsMap = Maps.newHashMap();
        // 死信交换机、路由
        argsMap.put("x-dead-letter-exchange", env.getProperty("mq.kill.item.success.kill.dead.exchange"));
        argsMap.put("x-dead-letter-routing-key", env.getProperty("mq.kill.item.success.kill.dead.routing.key"));
        // ttl
        // argsMap.put("x-message-ttl", 10000);
        return new Queue(env.getProperty("mq.kill.item.success.kill.dead.queue"), true, false, false, argsMap);
    }

    //基本交换机
    @Bean
    public TopicExchange successKillDeadProdExchange() {
        return new TopicExchange(env.getProperty("mq.kill.item.success.kill.dead.prod.exchange"), true, false);
    }

    //创建基本交换机+基本路由 -> 死信队列 的绑定
    @Bean
    public Binding successKillDeadProdBinding() {
        return BindingBuilder.bind(successKillDeadQueue()).to(successKillDeadProdExchange()).with(env.getProperty("mq.kill.item.success.kill.dead.prod.routing.key"));
    }

    //真正的队列
    @Bean
    public Queue successKillRealQueue() {
        return new Queue(env.getProperty("mq.kill.item.success.kill.dead.real.queue"), true);
    }

    //死信交换机
    @Bean
    public TopicExchange successKillDeadExchange() {
        return new TopicExchange(env.getProperty("mq.kill.item.success.kill.dead.exchange"), true, false);
    }

    //死信交换机+死信路由->真正队列 的绑定
    @Bean
    public Binding successKillDeadBinding() {
        return BindingBuilder.bind(successKillRealQueue()).to(successKillDeadExchange()).with(env.getProperty("mq.kill.item.success.kill.dead.routing.key"));
    }
}






























































































