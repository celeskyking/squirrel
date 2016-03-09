package com.github.celeskyking.squirrel.discovery;

import com.github.celeskyking.squirrel.broker.IBroker;
import com.github.celeskyking.squirrel.caller.CallerInfo;
import com.github.celeskyking.squirrel.caller.ICaller;
import com.github.celeskyking.squirrel.job.ITriggerInfoCallback;
import com.github.celeskyking.squirrel.job.JobberInfo;
import com.github.celeskyking.squirrel.task.TaskDescription;
import com.github.celeskyking.squirrel.trigger.TriggerInfo;
import com.github.celeskyking.squirrel.worker.WorkerInfo;

import java.util.List;

/**
 * Created by tianqing.wang
 * DATE : 16/1/27
 * TIME : 下午3:05
 * PROJECT : squirrel
 * PACKAGE : com.github.celeskyking.squirrel.discovery
 *
 * @author <a href="mailto:celeskyking@163.com">tianqing.wang</a>
 * <p>主要用于worker的负载均衡处理,sender通过服务发现的形式来查找worker,保证worker的高可用</p>
 */
public interface DiscoveryService {


    /**
     * 下线task,不区分worker,非物理下线，只是状态下线。
     * @param taskName 任务名称
     * @param workerInfo 下线当前worker的task，不能够下线别的worker的task
     * */
    boolean deRegister(String taskName,WorkerInfo workerInfo);


    /**
     * task能够下线就能够上线，区分worker，非物理下线，只是状态下线。
     * */
    boolean register(String taskName,WorkerInfo workerInfo);


    /**
     * 注册jobber的信息,如果worker触发任务调度的时候，通过jobber是否在线，来确定是否触发
     * 任务调度通知。
     * */
    boolean register(JobberInfo jobber);

    /**
     * 当caller启动的时候,如果同一个caller,只会启动一个生产者,防止重复的生产task,当一个caller
     * 会重新选举一个新的leader.
     * */
    boolean register(CallerInfo caller);


    /***
     * 注册trigger信息
     * */
    boolean register(TriggerInfo triggerInfo);



    /**
     * 上线worker
     * */
    boolean online(WorkerInfo workerInfo);


    /**
     * 下线worker
     * */
    boolean offline(WorkerInfo workerInfo);


    /**
     * @param routeKey worker名称
     * @return 返回所有的worker的所有的信息
     * */
    List<WorkerInfo> getWorkers(String routeKey);


    List<CallerInfo> getCallers(String worker);

    TriggerInfo getTrigger(String worker,String jobber,String jobName);


    List<JobberInfo> getJobbers(String worker,String jobber);

    /**
     * 获取所有的task,不区分worker
     * */
    List<TaskDescription> getTasks(String workerName);


    /**
     * 获取所有名称为taskName的任务
     * */
    List<TaskDescription> getTasks(String workerName,String taskName);


    /**
     * 分布式锁,当caller是分布式系统的时候，会触发该方法。该方法阻止多个caller同时启动
     * */
    void distributeLock(ICaller caller);



    void distributeLock(IBroker broker, WorkerInfo workerInfo);

    /**
     * 监听triggers
     * */
    void watchingTriggers(String worker, String jobber, long waitTimes,ITriggerInfoCallback callback) throws Throwable;

    void stopWatchingTriggers();

}
