package com.github.celeskyking.squirrel.caller;

import com.github.celeskyking.squirrel.Response;
import com.github.celeskyking.squirrel.broker.IBroker;
import com.github.celeskyking.squirrel.exception.ArgTypeNotMatchException;
import com.github.celeskyking.squirrel.monitor.IMonitor;
import com.github.celeskyking.squirrel.resultbackend.ResultEventListener;
import com.github.celeskyking.squirrel.task.Task;

/**
 * Created by tianqing.wang
 * DATE : 16/1/28
 * TIME : 上午10:19
 * PROJECT : squirrel
 * PACKAGE : com.github.celeskyking.squirrel.client
 *
 * @author <a href="mailto:celeskyking@163.com">tianqing.wang</a>
 *<p>
 *     目前不支持回调后结果的当前方法的callback,因为会有消息丢失的可能.
 *     所以为了保证统一的接受,所有的task执行结果统一放在总体回调中.
 *</p>
 *
 *
 *
 */
public interface ICaller {
    /**
     * 发送任务,默认为异步发送,并不通过生产者消费者方式发送,sendTask可能会被阻塞
     * @param task 需要执行的任务信息
     * */
    Response sendTask(Task task) throws ArgTypeNotMatchException;


    void distributeLock(boolean distributeLock);

    /**
     * 启动客户端
     * */
    void start();

    /**
     * 停止客户端
     * */
    void stop();

    /**
     * 增加客户端启动监听
     * */
    void addEventListener(ResultEventListener listener);

    /**
     * 获取broker的信息
     * */
    IBroker getBroker();

    /**
     * 获取caller的名称
     * */
    String getName();

    /**
     * 根据不同的task注册不同的解析方案
     * */
    void addResultCallback(String taskName,ICallback callback);


    String getWorker();


    void withStartedCallback(ICallerEventHandler handler);


    CallerInfo callerInfo();


    void monitor(IMonitor monitor);

}
