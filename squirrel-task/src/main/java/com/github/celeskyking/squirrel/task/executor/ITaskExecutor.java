package com.github.celeskyking.squirrel.task.executor;

import com.github.celeskyking.squirrel.task.Context;
import com.github.celeskyking.squirrel.task.TaskDescription;
import com.github.celeskyking.squirrel.task.Result;

/**
 * Created by tianqing.wang
 * DATE : 16/2/3
 * TIME : 上午11:00
 * PROJECT : squirrel
 * PACKAGE : com.github.celeskyking.squirrel
 *
 * @author <a href="mailto:celeskyking@163.com">tianqing.wang</a>
 * 一个可执行的接口
 */
public interface ITaskExecutor {


    /**
     * 执行任务
     * @param context 任务请求的上下文对象
     * @return 返回执行之后的结果,所有的返回值要
     * */
    Result execute(Context context);


    /**
     * 返回该task的描述
     * */
    TaskDescription getTaskDescription();

}
