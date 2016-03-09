package com.github.celeskyking.squirrel.disruptor;

import com.lmax.disruptor.ExceptionHandler;
import com.github.celeskyking.squirrel.caller.ICaller;
import com.github.celeskyking.squirrel.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by tianqing.wang
 * DATE : 16-2-17
 * TIME : 下午6:14
 * PROJECT : squirrel
 * PACKAGE : com.github.celeskyking.squirrel.disruptor
 *
 * @author <a href="mailto:celeskyking@163.com">tianqing.wang</a>
 */
public class TaskExceptionHandler implements ExceptionHandler<Task> {

    private ICaller caller;

    public TaskExceptionHandler(ICaller caller){
        this.caller = caller;
    }

    private Logger logger = LoggerFactory.getLogger(TaskExceptionHandler.class);

    @Override
    public void handleEventException(Throwable ex, long sequence, Task event) {

    }

    @Override
    public void handleOnStartException(Throwable ex) {

    }

    @Override
    public void handleOnShutdownException(Throwable ex) {

    }
}
