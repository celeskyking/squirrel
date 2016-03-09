package com.github.celeskyking.squirrel.task.listener;

import com.github.celeskyking.squirrel.task.Context;
import com.github.celeskyking.squirrel.task.Result;

/**
 * Created by tianqing.wang
 * DATE : 16/2/3
 * TIME : 上午11:41
 * PROJECT : squirrel
 * PACKAGE : com.github.celeskyking.squirrel.task.listener
 *
 * @author <a href="mailto:celeskyking@163.com">tianqing.wang</a>
 * <p>
 *     任务执行的监听器,包括monitor的实现都是通过TaskListener来实现的.
 *     用来监听某个具体的任务执行的时候错误信息
 * </p>
 *
 */
public interface TaskEventListener {

    void fireTaskStarted(Context context);

    void fireTaskFinished(Context context, Result result);

    void fireTaskFailure(Context context, Throwable throwable);

    void fireTaskSuccess(Context context);


}
