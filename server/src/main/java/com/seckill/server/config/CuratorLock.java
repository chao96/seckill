package com.seckill.server.config;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.retry.RetryNTimes;

/**
 * Zookeeper利用Curator实现分布式锁
 * @author xuchao
 * @since 2020/2/27 23:09
 */
public class CuratorLock {

    public static void main(String[] args) throws Exception {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        CuratorFramework client = CuratorFrameworkFactory.newClient("127.0.0.1:2181",retryPolicy);
        client.start();
        CuratorFramework client2 = CuratorFrameworkFactory.newClient("127.0.0.1:2181",retryPolicy);
        client2.start();
        //创建分布式锁, 锁空间的根节点路径为/curator/lock
        InterProcessMutex mutex  = new InterProcessMutex(client,"/curator/lock");
        final InterProcessMutex mutex2  = new InterProcessMutex(client2,"/curator/lock");

        mutex.acquire();
        //获得了锁, 进行业务流程
        System.out.println("clent Enter mutex");

        Thread client2Th = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    mutex2.acquire();
                    System.out.println("client2 Enter mutex");
                    mutex2.release();
                    System.out.println("client2 release lock");

                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        });
        client2Th.start();
        //完成业务流程, 释放锁
        Thread.sleep(5000);
        mutex.release();
        System.out.println("client release lock");
        client2Th.join();

        //关闭客户端
        client.close();
    }
}
