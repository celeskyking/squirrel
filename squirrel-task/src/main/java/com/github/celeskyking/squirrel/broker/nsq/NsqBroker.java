package com.github.celeskyking.squirrel.broker.nsq;

import com.alibaba.fastjson.JSON;
import com.github.celeskyking.squirrel.broker.IBroker;
import com.github.celeskyking.squirrel.job.IJobber;
import com.github.celeskyking.squirrel.job.JobListener;
import com.github.celeskyking.squirrel.serialize.ITaskDecoder;
import com.github.celeskyking.squirrel.serialize.ITaskEncoder;
import com.github.celeskyking.squirrel.task.TaskSignature;
import com.github.celeskyking.squirrel.task.processor.ITaskProcessor;
import com.github.celeskyking.squirrel.trigger.TriggerInfo;
import com.github.celeskyking.squirrel.worker.WorkerInfo;
import com.google.common.net.HostAndPort;
import com.google.common.util.concurrent.MoreExecutors;
import com.qunar.nsq.client.NSQConsumer;
import com.qunar.nsq.client.NSQProducer;
import com.qunar.nsq.client.ServerAddress;
import com.qunar.nsq.client.lookup.DefaultNSQLookup;
import com.github.celeskyking.squirrel.discovery.DiscoveryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

/**
 * Created by tianqing.wang
 * DATE : 16/2/5
 * TIME : 下午2:49
 * PROJECT : squirrel
 * PACKAGE : com.qunar.squirrel.broker
 *
 * @author <a href="mailto:celeskyking@163.com">tianqing.wang</a>
 */
public class NsqBroker implements IBroker {


    private boolean isProducerRunning = false;


    private Logger logger = LoggerFactory.getLogger(NsqBroker.class);


    private NsqConfig config;


    private ITaskEncoder encoder;


    private ITaskDecoder decoder;


    private DefaultNSQLookup nsqLookup;

    /**
     * 生产者
     * */
    private NSQProducer producer;


    private NSQProducer triggerProducer;


    private boolean triggering = false;


    private NSQConsumer triggerConsumer;


    /**
     * 消费者
     * */
    private NSQConsumer consumer;


    public NsqBroker(NsqConfig brokerConfig,
                          ITaskEncoder encoder,
                          ITaskDecoder decoder){
        this.config = brokerConfig;
        this.encoder = encoder;
        this.decoder = decoder;
        this.nsqLookup = buildNsqLookup();
    }



    @Override
    public void startConsuming(WorkerInfo workerInfo, final ITaskProcessor processor) throws Throwable {
        logger.info("nsq consumer启动...");
        this.consumer = new NSQConsumer(buildNsqLookup(),
                this.config.getTaskTopic()+"_"+workerInfo.getWorkerName(), workerInfo.getWorkerName(),
                new TaskMessageCallBack(this,processor));
        consumer.setExecutor(getConfig().getExecutorService());
        consumer.start();
    }

    @Override
    public void publish(TaskSignature task) throws Throwable{
        if(!isProducerRunning){
            startProducing();
        }
        producer.produce(getConfig().getTaskTopic()+"_"+task.getWorker(),getEncoder().encode(task));
        producer.start();
    }

    @Override
    public void publish(TriggerInfo triggerInfo) throws Throwable {
        if(!triggering){
            startTriggering();
        }
        String topic = config.getTriggerTopic()+"_"+triggerInfo.getWorker();
        this.triggerProducer.produce(topic,JSON.toJSONBytes(triggerInfo));
    }

    @Override
    public void startConsumeTriggering(IJobber jobber, JobListener listener) {
        logger.info("nsq trigger consumer启动...");
        this.triggerConsumer = new NSQConsumer(buildNsqLookup(),
                this.config.getTriggerTopic()+"_"+jobber.getWorker()+"_"+jobber.getName(), jobber.getName(),
                new IReceivedTriggerCallback(jobber,listener));
        triggerConsumer.start();
    }

    @Override
    public void stopConsumeTriggering() {
        this.triggerConsumer.shutdown();
    }


    private void startTriggering(){
        Set<ServerAddress> addresses = this.nsqLookup.nodes();
        for(ServerAddress address : addresses){
            this.triggerProducer = new NSQProducer().addAddress(address.getHost(),address.getPort());
        }
        this.triggerProducer.start();
        this.triggering=true;
    }

    /**
     * 生产者线程池
     * */
    public void startProducing(){
        Set<ServerAddress> addresses = this.nsqLookup.nodes();
        for(ServerAddress address : addresses){
            this.producer = new NSQProducer().addAddress(address.getHost(),address.getPort());
        }
        this.producer.start();
        this.isProducerRunning=true;
    }

    public void stopProducing(){
        this.producer.shutdown();
    }

    @Override
    public void stopConsuming() {
        this.consumer.shutdown();
    }

    @Override
    public void onReceiveTriggerRegistry(final WorkerInfo workerInfo, final DiscoveryService discoveryService) {
        logger.info("nsq consumer启动...");
        new Thread(new Runnable() {
            @Override
            public void run() {
                discoveryService.distributeLock(NsqBroker.this,workerInfo);

            }
        }).start();
        NSQConsumer triggerConsumer = new NSQConsumer(buildNsqLookup(),
                this.config.getTriggerTopic() + "_" + workerInfo.getWorkerName(), workerInfo.getWorkerName(),
                new TriggerCallback(discoveryService, this, workerInfo));
        triggerConsumer.setExecutor(MoreExecutors.newDirectExecutorService());
        triggerConsumer.start();
    }




    @Override
    public void trigger(WorkerInfo workerInfo,TriggerInfo triggerInfo) throws Throwable{
        logger.info("触发定时任务任务:{}",JSON.toJSONString(triggerInfo));
        if(!isProducerRunning){
            startProducing();
        }
        String topic = config.getTriggerTopic()+"_"+workerInfo.getWorkerName()+"_"+triggerInfo.getCaller();
        this.producer.produce(topic, JSON.toJSONBytes(triggerInfo));
    }


    private DefaultNSQLookup buildNsqLookup(){
        DefaultNSQLookup lookup = new DefaultNSQLookup();
        for(HostAndPort hostAndPort : this.config.getLookupAddress()){
            lookup.addLookupAddress(hostAndPort.getHostText(),hostAndPort.getPortOrDefault(4161));
        }
        return lookup;
    }

    public NsqConfig getConfig() {
        return config;
    }

    public void setConfig(NsqConfig config) {
        this.config = config;
    }

    public ITaskEncoder getEncoder() {
        return encoder;
    }

    public void setEncoder(ITaskEncoder encoder) {
        this.encoder = encoder;
    }

    public ITaskDecoder getDecoder() {
        return decoder;
    }

    public void setDecoder(ITaskDecoder decoder) {
        this.decoder = decoder;
    }

    public NSQConsumer getConsumer() {
        return consumer;
    }

    public void setConsumer(NSQConsumer consumer) {
        this.consumer = consumer;
    }

}
