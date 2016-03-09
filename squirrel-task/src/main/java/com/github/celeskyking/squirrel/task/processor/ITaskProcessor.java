package com.github.celeskyking.squirrel.task.processor;

import com.github.celeskyking.squirrel.exception.RouteKeyNotMatchException;
import com.github.celeskyking.squirrel.exception.TaskNotExistException;
import com.github.celeskyking.squirrel.exception.TaskNotRunnableException;
import com.github.celeskyking.squirrel.task.TaskSignature;

/**
 * Created by tianqing.wang
 * DATE : 16/1/27
 * TIME : 下午3:11
 * PROJECT : squirrel
 * PACKAGE : com.github.celeskyking.squirrel.task
 *
 * @author <a href="mailto:celeskyking@163.com">tianqing.wang</a>
 */
public interface ITaskProcessor {

    void process(TaskSignature task) throws RouteKeyNotMatchException, TaskNotExistException, TaskNotRunnableException;
}
