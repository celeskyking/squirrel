package com.github.celeskyking.squirrel.disruptor;


import com.alibaba.fastjson.JSON;
import com.github.celeskyking.squirrel.Response;
import com.github.celeskyking.squirrel.caller.ICaller;
import com.github.celeskyking.squirrel.task.Task;
import com.lmax.disruptor.EventHandler;
import com.github.celeskyking.squirrel.exception.TaskException;


/**
 * Created by tianqing.wang
 * DATE : 16-2-17
 * TIME : 下午4:24
 * PROJECT : squirrel
 * PACKAGE : com.qunar.squirrel.disruptor
 *
 * @author <a href="mailto:celeskyking@163.com">tianqing.wang</a>
 */
public class TaskDisruptorHandler implements EventHandler<Task> {

    private ICaller caller;

    public TaskDisruptorHandler(ICaller caller){
        this.caller = caller;
    }


    @Override
    public void onEvent(Task event, long sequence, boolean endOfBatch) throws Exception {
        Response response  = caller.sendTask(event);
        if(!response.isOk()){
            throw new TaskException("该task执行失败,task:"+ JSON.toJSONString(event));
        }
    }
}
