package com.github.celeskyking.squirrel.task.listener;

import com.github.celeskyking.squirrel.task.Context;
import com.google.common.collect.Lists;
import com.github.celeskyking.squirrel.task.Result;

import java.util.List;

/**
 * Created by tianqing.wang
 * DATE : 16/2/3
 * TIME : 上午11:47
 * PROJECT : squirrel
 * PACKAGE : com.qunar.squirrel.task.listener
 *
 * @author <a href="mailto:celeskyking@163.com">tianqing.wang</a>
 */
public class TaskListener {

    public List<TaskEventListener> eventListeners = Lists.newArrayList();

    public void addListener(TaskEventListener listener){
        this.eventListeners.add(listener);
    }


    public void fireTaskStarted(Context context){
        for(TaskEventListener taskEventListener : eventListeners){
            taskEventListener.fireTaskStarted(context);
        }
    }

    public void fireTaskFinished(Context context,Result result){
        for(TaskEventListener taskEventListener : eventListeners){
            taskEventListener.fireTaskFinished(context,result);
        }
    }

    public void fireTaskFailure(Context context,Throwable throwable){
        for(TaskEventListener taskEventListener : eventListeners){
            taskEventListener.fireTaskFailure(context,throwable);
        }
    }

    public void fireTaskSuccess(Context context){
        for(TaskEventListener taskEventListener : eventListeners){
            taskEventListener.fireTaskSuccess(context);
        }
    }

}
