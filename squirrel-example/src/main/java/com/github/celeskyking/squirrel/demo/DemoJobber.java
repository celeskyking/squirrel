package com.github.celeskyking.squirrel.demo;

import com.github.celeskyking.squirrel.Squirrel;
import com.github.celeskyking.squirrel.discovery.DiscoveryService;
import com.github.celeskyking.squirrel.discovery.consul.ConsulService;
import com.github.celeskyking.squirrel.job.IJobber;
import com.github.celeskyking.squirrel.job.JobBuilder;
import com.google.common.net.HostAndPort;

/**
 * Created by tianqing.wang
 * DATE : 16-2-19
 * TIME : 下午5:28
 * PROJECT : squirrel
 * PACKAGE : com.qunar.squirrel.demo
 *
 * @author <a href="mailto:celeskyking@163.com">tianqing.wang</a>
 */
public class DemoJobber {

    public static void main(String[] args) throws Throwable {
        Squirrel squirrel = Squirrel.builder()
                .withBroker("nsq://l-noahmaster2.beta.cn0.qunar.com:4161")
                .withResultBackend("nsq://l-noahmaster2.beta.cn0.qunar.com:4161")
                .build();
        DiscoveryService discoveryService = ConsulService.builder()
                .withHostAndPort(HostAndPort.fromParts("l-qtp1.corp.beta.cn0.qunar.com",8500))
                .withToken(null)
                .withSessionTTL("20s")
                .build();
        final IJobber jobber = squirrel.newJobber("demo-jobber","demo-worker",discoveryService);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    jobber.publish(JobBuilder.builder()
                            .withJobName("test-job")
                            .withCronExpress("*/5 * * * * ?")
                            .withDescription("测试一下发布job")
                            .withData("name","hello,world")
                            .withTarget(DemoJobExecutorTWO.class.getName()).build());
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }
        }).start();
        jobber.start();
    }
}
