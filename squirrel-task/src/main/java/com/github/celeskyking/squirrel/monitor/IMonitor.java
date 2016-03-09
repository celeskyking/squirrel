package com.github.celeskyking.squirrel.monitor;

import com.github.celeskyking.squirrel.caller.CallerInfo;
import com.github.celeskyking.squirrel.job.JobberInfo;
import com.github.celeskyking.squirrel.task.TaskSignature;
import com.github.celeskyking.squirrel.trigger.TriggerInfo;
import com.github.celeskyking.squirrel.worker.WorkerInfo;

import java.util.List;

/**
 * Created by tianqing.wang
 * DATE : 16-2-17
 * TIME : 下午7:25
 * PROJECT : squirrel
 * PACKAGE : com.qunar.squirrel.monitor
 *
 * @author <a href="mailto:celeskyking@163.com">tianqing.wang</a>
 * 该类只做为监听的一个规范接口，不做任何的实现，实现需要通过持久化来做任务调度的状态和历史记录，数据量有些大，
 * 所以暂时保留接口
 */
public interface IMonitor {

    /**
     * 通过uuid来获取执行结果
     * */
    TaskSignature record(String uuid);

    /**
     * 订阅任务的执行,有了该指定任务的结果,则发送到异步通知地址
     * @param worker worker名称
     * @param taskName 任务名称
     * @param callback 回调地址
     * <p>
     *   如果订阅了该任务的执行，该任务所有的执行情况，并且包括执行的每一个周期都会触发回调通知。
     *   如果有网络延迟或者中断，有可能造成任务的时序错误，最终结果要以Success和Failure为准。
     * </p>
     *
     * */
    void subscribe(String worker,String taskName,String callback);

    /**
     *
     * @param worker worker名称,只订阅worker代表了订阅worker的所有的任务执行
     * @param callback 回调地址
     * */
    void subscribe(String worker,String callback);


    /**
     * 向订阅者发送任务执行的消息
     * */
    void publish(TaskSignature signature);


    /**
     * 收集执行的情况
     * */
    void collect(TaskSignature signature);

    /**
     * 收集定时任务的情况
     * */
    void collect(TriggerInfo triggerInfo);


    /**
     * 获取所有的name为worker的信息,主要是remoteAddress不同
     * */
    List<WorkerInfo> getWorkers(String worker);

    /**
     * 获取所有的worker
     **/
    List<WorkerInfo> getWorkers();

    /**
     * 获取所有的caller
     * */
    List<CallerInfo> getCallers();

    /**
     * 获取所有的在worker下的caller
     * */
    List<CallerInfo> getCallers(String worker);
    /**
     * 获取所有的jobbers
     * */
    List<JobberInfo> getJobbers();

    /**
     * 获取指定的worker下面的jobbers
     * */
    List<JobberInfo> getJobbers(String worker);

    /**
     * 暂停
     * */
    void pauseJob(String worker,String jobber,String jobName);


    /**
     * 移除jobber的某个job
     * */
    void removeJob(String worker,String jobber,String jobName);


    /**
     * 执行jobber
     * */
    void resumeJob(String worker,String jobber,String jobName);
}
