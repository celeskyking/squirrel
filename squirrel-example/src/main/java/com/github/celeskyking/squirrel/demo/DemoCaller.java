package com.github.celeskyking.squirrel.demo;

import com.alibaba.fastjson.JSON;
import com.github.celeskyking.squirrel.Squirrel;
import com.github.celeskyking.squirrel.TaskBuilder;
import com.github.celeskyking.squirrel.caller.ICallback;
import com.github.celeskyking.squirrel.caller.ICaller;
import com.github.celeskyking.squirrel.caller.ICallerEventHandler;
import com.github.celeskyking.squirrel.discovery.DiscoveryService;
import com.github.celeskyking.squirrel.discovery.consul.ConsulService;
import com.github.celeskyking.squirrel.task.Task;
import com.github.celeskyking.squirrel.task.TaskSignature;
import com.google.common.net.HostAndPort;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by tianqing.wang
 * DATE : 16-2-16
 * TIME : 下午4:53
 * PROJECT : squirrel
 * PACKAGE : com.qunar.squirrel.demo
 *
 * @author <a href="mailto:celeskyking@163.com">tianqing.wang</a>
 */
public class DemoCaller {

    public static void main(String[] args) {
        Squirrel squirrel = Squirrel.builder()
                .withBroker("nsq://l-noahmaster2.beta.cn0.qunar.com:4161")
                .withResultBackend("nsq://l-noahmaster2.beta.cn0.qunar.com:4161")
                .build();
        DiscoveryService discoveryService = ConsulService.builder()
                .withHostAndPort(HostAndPort.fromParts("l-qtp1.corp.beta.cn0.qunar.com",8500))
                .withToken(null)
                .withSessionTTL("10s")
                .build();
        final ICaller caller = squirrel.newCaller("demo-caller","demo-worker",discoveryService);
        caller.distributeLock(true);
        caller.addResultCallback("hello_task", new ICallback() {
            @Override
            public void handle(TaskSignature taskSignature) {
                System.out.println("接收到了回调:"+ JSON.toJSONString(taskSignature));
            }
        });
        caller.withStartedCallback(new ICallerEventHandler() {
            @Override
            public void handle() {
                Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(new Runnable() {
                    @Override
                    public void run() {
                        Task task = TaskBuilder.builder().arg("tianqing.wang").name("hello_task").build();
                        caller.sendTask(task);
                    }
                },10,10, TimeUnit.SECONDS);

            }
        });
        caller.start();
    }
}
