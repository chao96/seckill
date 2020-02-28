package com.seckill.server;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ImportResource;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author xuchao
 * @since 2020/2/26 15:01
 */
@SpringBootApplication
@ImportResource(value = {"classpath:spring/spring-jdbc.xml"})
@MapperScan(basePackages = "com.seckill.model.mapper")
@EnableScheduling
public class MainAppApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(MainAppApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(MainAppApplication.class, args);
    }
}
