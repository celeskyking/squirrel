package com.github.celeskyking.squirrel.task.executor;

import com.github.celeskyking.squirrel.task.Context;
import com.github.celeskyking.squirrel.task.listener.TaskEventListener;
import com.github.celeskyking.squirrel.task.listener.TaskListener;
import com.google.common.base.Stopwatch;
import com.github.celeskyking.squirrel.task.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.concurrent.TimeUnit;


/**
 * Created by tianqing.wang
 * DATE : 16/2/3
 * TIME : 上午11:02
 * PROJECT : squirrel
 * PACKAGE : com.github.celeskyking.squirrel
 *
 * @author <a href="mailto:celeskyking@163.com">tianqing.wang</a>
 */
public abstract class TaskContextExecutor<T extends IMessage> implements ITaskExecutor {

    private String name;

    public TaskContextExecutor(String name){
        this.name = name;
    }

    private TaskListener taskListener = new TaskListener();


    private Logger logger = LoggerFactory.getLogger(TaskContextExecutor.class);

    @Override
    public Result execute(Context context) {
        taskListener.fireTaskStarted(context);
        Result result = new Result();
        result.setStartTime(new Date());
        Stopwatch stopWatch = Stopwatch.createStarted();
        try{
            IMessage message = onExecute(context);
            result.setMessage(message.message());
            result.setSuccess(true);
            taskListener.fireTaskSuccess(context);
        }catch (Throwable e){
            logger.error("",e);
            result.setSuccess(false);
            taskListener.fireTaskFailure(context,e);
        }finally {
            result.setEndTime(new Date());
            result.setDuration(stopWatch.elapsed(TimeUnit.MILLISECONDS));
            taskListener.fireTaskFinished(context,result);
        }
        return result;
    }

    /**
     * 通过直接定义Task的上下文来执行任务
     * @param context 请求来的上下文信息
     * @return 返回的执行结果
     * */
    public abstract T onExecute(Context context) throws Throwable;


    /**
     * 添加监听器
     * @param listener 任务监听器
     * */
    public void addListener(TaskEventListener listener){
        this.taskListener.addListener(listener);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
