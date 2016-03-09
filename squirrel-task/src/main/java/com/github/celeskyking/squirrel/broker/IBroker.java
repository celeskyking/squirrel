package com.github.celeskyking.squirrel.broker;

import com.github.celeskyking.squirrel.job.IJobber;
import com.github.celeskyking.squirrel.job.JobListener;
import com.github.celeskyking.squirrel.task.TaskSignature;
import com.github.celeskyking.squirrel.task.processor.ITaskProcessor;
import com.github.celeskyking.squirrel.trigger.TriggerInfo;
import com.github.celeskyking.squirrel.worker.WorkerInfo;
import com.github.celeskyking.squirrel.discovery.DiscoveryService;

/**
 * Created by tianqing.wang
 * DATE : 16/1/27
 * TIME : 下午12:07
 * PROJECT : squirrel
 * PACKAGE : com.github.celeskyking.squirrel.broker
 *
 * @author <a href="mailto:celeskyking@163.com">tianqing.wang</a>
 *
 * IBroker 主要用来发送任务消息,任务的参数通过二进制的格式来定义,通过protobuf来进行rpc的序列化操作.
 * 请求通过固定的topic或者queue来发送到broker,通过broker来保证任务一定被消费到,
 * worker会监听该topic或者queue来获取task的信息,来执行任务,然后将执行结果发送到nsq来持久化, sender通过监听
 * result的结果来实现callback的过程
 * resultBackend 也是可以定义的 默认是nsq
 * 默认的系统只支持redis和nsq两种mq,至于amqp的协议,以后有需求再考虑是否实现.
 * 以后考虑实现qmq的任务队列
 */
public interface IBroker {


    /**
     * 初始化consumer, 连接broker并且启动消费tasks的任务
     * */
    void startConsuming(WorkerInfo workerInfo, ITaskProcessor processor) throws Throwable;

    /**
     * 发布任务,向队列添加任务请求
     * @param task 任务对象,ITask对象是一个序列化对象,为了保证消息快速发送,
     *             通过一定的序列化方式来进行压缩
     *             * */
    void publish(TaskSignature task) throws Throwable;



    /**
     * 注册定时任务,会默认的分配到多worker之上。
     * */
    void publish(TriggerInfo triggerInfo) throws  Throwable;



    /**
     * 开始消费定时任务
     * */
    void startConsumeTriggering(IJobber jobber, JobListener jobListener);



    /**
     * 停止定时任务
     * */
    void stopConsumeTriggering();


    /**
     * 停止消费者继续消费
     * */
    void stopConsuming();


    /**
     * worker实现定时执行的功能，只提供触发。
     * */
    void onReceiveTriggerRegistry(WorkerInfo workerInfo, DiscoveryService discoveryService);





    /**
     * 触发定时任务的执行
     * */
    void trigger(WorkerInfo workerInfo, TriggerInfo triggerInfo) throws Throwable;




}
