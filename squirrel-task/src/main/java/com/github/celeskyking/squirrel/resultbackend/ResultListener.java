package com.github.celeskyking.squirrel.resultbackend;

import com.github.celeskyking.squirrel.task.TaskSignature;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * Created by tianqing.wang
 * DATE : 16-2-16
 * TIME : 下午2:39
 * PROJECT : squirrel
 * PACKAGE : com.github.celeskyking.squirrel.resultbackend
 *
 * @author <a href="mailto:celeskyking@163.com">tianqing.wang</a>
 */
public class ResultListener {

    List<ResultEventListener> eventListeners = Lists.newArrayList();


    public void register(ResultEventListener eventListener){
        this.eventListeners.add(eventListener);
    }

    /**
     * 当任务处于执行中状态的时候触发
     * */
    public void fireTaskPending(TaskSignature taskSignature){
        for(ResultEventListener eventListener : eventListeners){
            eventListener.fireTaskPending(taskSignature);
        }
    }

    /**
     * 当任务处于接受状态的时候触发
     * */
    public void fireTaskReceived(TaskSignature taskSignature){
        for(ResultEventListener eventListener : eventListeners){
            eventListener.fireTaskReceived(taskSignature);
        }
    }


    /**
     * 当任务处于success的时候触发
     * */
    public void fireTaskSuccess(TaskSignature taskSignature){
        for(ResultEventListener eventListener : eventListeners){
            eventListener.fireTaskSuccess(taskSignature);
        }
    }

    /**
     * 当任务处于failure的时候触发
     * */
    public void fireTaskFailure(TaskSignature taskSignature){
        for(ResultEventListener eventListener : eventListeners){
            eventListener.fireTaskFailure(taskSignature);
        }
    }

    /**
     * 当任务处于started的时候触发
     * */
    public void fireTaskStarted(TaskSignature taskSignature){
        for(ResultEventListener eventListener : eventListeners){
            eventListener.fireTaskStarted(taskSignature);
        }
    }


}
