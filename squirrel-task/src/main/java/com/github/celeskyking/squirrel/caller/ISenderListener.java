package com.github.celeskyking.squirrel.caller;

import com.github.celeskyking.squirrel.task.Task;

/**
 * Created by tianqing.wang
 * DATE : 16-2-17
 * TIME : 下午6:27
 * PROJECT : squirrel
 * PACKAGE : com.github.celeskyking.squirrel.caller
 *
 * @author <a href="mailto:celeskyking@163.com">tianqing.wang</a>
 */
public interface ISenderListener {


    void handleEventException(Throwable ex, ICaller caller,Task task);


    void handleOnStartException(Throwable ex,ICaller caller);


    void handleOnShutdownException(Throwable ex,ICaller caller) ;
}
