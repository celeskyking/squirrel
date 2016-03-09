package com.github.celeskyking.squirrel.demo;

import com.github.celeskyking.squirrel.Squirrel;
import com.github.celeskyking.squirrel.discovery.DiscoveryService;
import com.github.celeskyking.squirrel.discovery.consul.ConsulService;
import com.github.celeskyking.squirrel.worker.IWorker;
import com.google.common.net.HostAndPort;


/**
 * Created by tianqing.wang
 * DATE : 16-2-16
 * TIME : 下午4:53
 * PROJECT : squirrel
 * PACKAGE : com.qunar.squirrel.demo
 *
 * @author <a href="mailto:celeskyking@163.com">tianqing.wang</a>
 */
public class DemoWorker {

    public static void main(String[] args) throws Throwable {
        String workerName = "demo-worker";
        Squirrel squirrel = Squirrel.builder()
                .withBroker("nsq://l-noahmaster2.beta.cn0.qunar.com:4161")
                .withResultBackend("nsq://l-noahmaster2.beta.cn0.qunar.com:4161")
                .build();
        DiscoveryService discoveryService = ConsulService.builder()
                .withHostAndPort(HostAndPort.fromParts("l-qtp1.corp.beta.cn0.qunar.com",8500))
                .withToken(null)
                .withSessionTTL("20s")
                .build();
        IWorker worker = squirrel.newWorker(workerName,discoveryService);
        worker.launch(true);
    }

}
