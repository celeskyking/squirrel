package com.github.celeskyking.squirrel;

import com.github.celeskyking.squirrel.broker.IBroker;
import com.github.celeskyking.squirrel.broker.nsq.NsqBroker;
import com.github.celeskyking.squirrel.broker.nsq.NsqConfig;
import com.github.celeskyking.squirrel.caller.DefaultCaller;
import com.github.celeskyking.squirrel.caller.DisruptorCaller;
import com.github.celeskyking.squirrel.caller.ICaller;
import com.github.celeskyking.squirrel.caller.ISenderListener;
import com.github.celeskyking.squirrel.discovery.DiscoveryService;
import com.github.celeskyking.squirrel.job.DefaultJobExecutorBuilder;
import com.github.celeskyking.squirrel.job.IJobExecutorBuilder;
import com.github.celeskyking.squirrel.job.IJobber;
import com.github.celeskyking.squirrel.job.Jobber;
import com.github.celeskyking.squirrel.resultbackend.IResultBackend;
import com.github.celeskyking.squirrel.resultbackend.nsq.NSQResultBackend;
import com.github.celeskyking.squirrel.serialize.JsonTaskDecoder;
import com.github.celeskyking.squirrel.serialize.JsonTaskEncoder;
import com.github.celeskyking.squirrel.task.TaskContext;
import com.github.celeskyking.squirrel.worker.DefaultWorker;
import com.github.celeskyking.squirrel.worker.IWorker;
import com.google.common.net.HostAndPort;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by tianqing.wang
 * DATE : 16/1/31
 * TIME : 下午12:21
 * PROJECT : squirrel
 * PACKAGE : com.qunar.squirrel
 *
 * @author <a href="mailto:celeskyking@163.com">tianqing.wang</a>
 */
public class Squirrel {


    private IResultBackend resultBackend;


    /**
     * broker 主要用来记录任务的注册者,来维护整个
     * */
    private IBroker broker;

    /**
     * 是否启用监控
     * */
    private boolean monitoring;


    private Squirrel(IBroker broker,IResultBackend resultBackend){
        this.broker = broker;
        this.resultBackend = resultBackend;
        this.monitoring = false;
    }

    private Squirrel(IBroker broker,IResultBackend resultBackend,boolean monitoring){
        this.broker = broker;
        this.resultBackend = resultBackend;
        this.monitoring = monitoring;
    }

    public ICaller newCaller(String name, String worker, DiscoveryService discoveryService){
        return new DefaultCaller(name,worker,broker,resultBackend,discoveryService);
    }

    public DisruptorCaller newDisruptorCaller(String name, String worker, DiscoveryService discoveryService, ISenderListener senderListener){
        return new DisruptorCaller(name,worker,broker,resultBackend,discoveryService,senderListener);
    }

    public IWorker newWorker(String name, DiscoveryService service) throws Throwable {
        TaskContext taskContext = new TaskContext();
        return new DefaultWorker(name,broker,resultBackend, taskContext,service);
    }


    public IJobber newJobber(String name, String worker, DiscoveryService discoveryService) throws Throwable {
        return new Jobber(name, worker,discoveryService, this.broker,new DefaultJobExecutorBuilder());
    }

    public IJobber newJobber(String name, String worker, DiscoveryService discoveryService, IJobExecutorBuilder jobExecutorBuilder) throws Throwable {
        return new Jobber(name,worker,discoveryService,broker,jobExecutorBuilder);
    }

    public boolean isMonitoring() {
        return monitoring;
    }

    public void setMonitoring(boolean monitoring) {
        this.monitoring = monitoring;
    }

    public IResultBackend getResultBackend() {
        return resultBackend;
    }

    public void setResultBackend(IResultBackend resultBackend) {
        this.resultBackend = resultBackend;
    }

    public IBroker getBroker() {
        return broker;
    }

    public void setBroker(IBroker broker) {
        this.broker = broker;
    }

    public static Builder builder(){
        return new Builder();
    }


    public static class Builder{

        private IBroker broker;

        private IResultBackend resultBackend;

        private boolean monitoring = false;

        public Builder withBroker(IBroker broker){
            this.broker = broker;
            return this;
        }

        public Builder withBroker(String broker){
            if(broker.startsWith("nsq://")){
                String address = StringUtils.substringAfter(broker,"nsq://");
                HostAndPort hostAndPort = HostAndPort.fromString(address);
                NsqConfig config = NsqConfig.builder().withLookup(hostAndPort).build();
                this.broker =  new NsqBroker(config,new JsonTaskEncoder(),new JsonTaskDecoder());
            }
            return this;
        }

        public Builder withResultBackend(IResultBackend resultBackend){
            this.resultBackend = resultBackend;
            return this;
        }

        public Builder withMonitor(boolean status){
            this.monitoring = status;
            return this;
        }

        public Builder withResultBackend(String resultBackend){
            if(resultBackend.startsWith("nsq://")){
                String address = StringUtils.substringAfter(resultBackend,"nsq://");
                HostAndPort hostAndPort = HostAndPort.fromString(address);
                NsqConfig config = NsqConfig.builder().withLookup(hostAndPort).build();
                this.resultBackend = new NSQResultBackend(config,new JsonTaskEncoder(),new JsonTaskDecoder());
            }
            return this;
        }


        public Squirrel build(){
            return new Squirrel(broker,resultBackend);
        }

    }






}
