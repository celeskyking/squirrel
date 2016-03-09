package com.github.celeskyking.squirrel.task.processor;

import com.alibaba.fastjson.JSON;
import com.github.celeskyking.squirrel.helper.TaskArgHelper;
import com.github.celeskyking.squirrel.task.*;
import com.github.celeskyking.squirrel.resultbackend.IResultBackend;
import com.github.celeskyking.squirrel.task.executor.ITaskExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by tianqing.wang
 * DATE : 16/2/3
 * TIME : 下午5:54
 * PROJECT : squirrel
 * PACKAGE : com.github.celeskyking.squirrel.task.processor
 *
 * @author <a href="mailto:celeskyking@163.com">tianqing.wang</a>
 */
public class TaskProcessor {

    private Logger logger = LoggerFactory.getLogger(TaskProcessor.class);

    private IResultBackend resultBackend;

    private TaskContext taskContext;

    public TaskProcessor(IResultBackend resultBackend, TaskContext taskContext){
        this.resultBackend = resultBackend;
        this.taskContext = taskContext;
    }

    public Result process(Context context, TaskSignature taskSignature){
        Task task = taskSignature.getTask();
        resultBackend.setStateStarted(taskSignature);
        logger.info("执行任务--{},来源地址:{},taskName:{},args:{}",
                taskSignature.getId(),
                context.getRequestAddress(),
                task.getTaskName(), JSON.toJSONString(task.getTaskArgs()));
        Context newContext = TaskArgHelper.asContext(task.getTaskArgs(),context);
        ITaskExecutor taskExecutor = taskContext.getTask(task.getTaskName());
        if(taskExecutor!=null){
            //执行指定的任务
            //需要定制worker并行数量,所以任务量要能够被控制,目前代码未实现consumer,等做完生产者和消费者,该调度就能够被
            //正确的执行了,所有的对象返回全部为Future或者通过Actor来实现,保留实现,等待重写
            Result result = taskExecutor.execute(newContext);
            logger.info("执行任务结束--{}，result:{}",taskSignature.getId(),JSON.toJSONString(result));
            return result;
        }
        return null;
    }
}
