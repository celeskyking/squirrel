package com.github.celeskyking.squirrel.resultbackend;

import com.github.celeskyking.squirrel.task.TaskSignature;

/**
 * Created by tianqing.wang
 * DATE : 16/1/27
 * TIME : 下午3:04
 * PROJECT : squirrel
 * PACKAGE : com.github.celeskyking.squirrel.resultbackend
 *
 * @author <a href="mailto:celeskyking@163.com">tianqing.wang</a>
 *
 * <p>
 *     主要用于结果的存储,记录task的不同的任务状态
 * </p>
 */
public interface IResultBackend {

    /**
     * 设置任务执行成功
     * @param task 被调度的任务
     * */
    void setStateSuccess(TaskSignature task);

    /**
     * 设置任务执行中
     * @param task 被调度的任务
     * */
    void setStatePending(TaskSignature task);

    /**
     * @param task 被调度的任务
     * */
    void setStateReceived(TaskSignature task);

    /**
     * 设置任务开始执行状态
     * @param  task 被调度的任务
     * */
    void setStateStarted(TaskSignature task);

    /**
     * 设置任务状态为失败
     * @param task 被调度的任务
     * */
    void setStateFailure(TaskSignature task);

    /**
     * 启动resultBackup的工作进程
     * */
    void startConsumer(String caller);

    /**
     * 停止resultBackup的动作进程
     * */
    void stopConsumer();


    void register(ResultEventListener resultEventListener);


}
