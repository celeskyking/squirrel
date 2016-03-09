package com.github.celeskyking.squirrel.demo;

import com.alibaba.fastjson.JSON;
import com.github.celeskyking.squirrel.Squirrel;
import com.github.celeskyking.squirrel.TaskBuilder;
import com.github.celeskyking.squirrel.caller.*;
import com.github.celeskyking.squirrel.discovery.DiscoveryService;
import com.github.celeskyking.squirrel.discovery.consul.ConsulService;
import com.github.celeskyking.squirrel.disruptor.TaskProducer;
import com.github.celeskyking.squirrel.task.Task;
import com.github.celeskyking.squirrel.task.TaskSignature;
import com.google.common.net.HostAndPort;

/**
 * Created by tianqing.wang
 * DATE : 16-2-17
 * TIME : 下午7:05
 * PROJECT : squirrel
 * PACKAGE : com.github.celeskyking.squirrel.demo
 *
 * @author <a href="mailto:celeskyking@163.com">tianqing.wang</a>
 */
public class DemoDisruptorCaller {


    public static void main(String[] args) {
        Squirrel squirrel = Squirrel.builder()
                .withBroker("nsq://localhost:4161")
                .withResultBackend("nsq://localhost:4161")
                .build();
        DiscoveryService discoveryService = ConsulService.builder()
                .withHostAndPort(HostAndPort.fromParts("localhost",8500))
                .withToken(null)
                .withSessionTTL("10s")
                .build();
        final DisruptorCaller caller = squirrel.newDisruptorCaller("demo-caller","demo-worker",discoveryService, new ISenderListener() {
            @Override
            public void handleEventException(Throwable ex, ICaller caller, Task task) {
                System.out.println("不可能");
            }

            @Override
            public void handleOnStartException(Throwable ex, ICaller caller) {
                caller.stop();
            }

            @Override
            public void handleOnShutdownException(Throwable ex, ICaller caller) {
                caller.stop();
            }
        });
        caller.addResultCallback("hello_task", new ICallback() {
            @Override
            public void handle(TaskSignature taskSignature) {
                System.out.println("接收到了回调:"+ JSON.toJSONString(taskSignature));
            }
        });
        caller.withStartedCallback(new ICallerEventHandler() {
            @Override
            public void handle() {
                while(true){
                    Task task = TaskBuilder.builder().arg("tianqing.wang").name("hello_task").build();
                    TaskProducer producer = caller.newProducer();
                    TaskProducer producer1 = caller.newProducer();
                    producer.onData(task);
                    producer1.onData(task);
                }
            }
        });
        caller.start();
    }
}
