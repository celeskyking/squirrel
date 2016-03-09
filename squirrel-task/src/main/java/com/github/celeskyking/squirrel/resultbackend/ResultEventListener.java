package com.github.celeskyking.squirrel.resultbackend;

import com.github.celeskyking.squirrel.task.TaskSignature;

/**
 * Created by tianqing.wang
 * DATE : 16-2-16
 * TIME : 下午12:18
 * PROJECT : squirrel
 * PACKAGE : com.github.celeskyking.squirrel.resultbackend
 *
 * @author <a href="mailto:celeskyking@163.com">tianqing.wang</a>
 */
public interface ResultEventListener {

    /**
     * 当任务处于执行中状态的时候触发
     * */
    void fireTaskPending(TaskSignature taskSignature);

    /**
     * 当任务处于接受状态的时候触发
     * */
    void fireTaskReceived(TaskSignature taskSignature);


    /**
     * 当任务处于success的时候触发
     * */
    void fireTaskSuccess(TaskSignature taskSignature);

    /**
     * 当任务处于failure的时候触发
     * */
    void fireTaskFailure(TaskSignature taskSignature);

    /**
     * 当任务处于started的时候触发
     * */
    void fireTaskStarted(TaskSignature taskSignature);

}
