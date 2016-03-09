package com.github.celeskyking.squirrel.task.processor;

import com.github.celeskyking.squirrel.broker.IBroker;
import com.github.celeskyking.squirrel.exception.RouteKeyNotMatchException;
import com.github.celeskyking.squirrel.exception.TaskNotExistException;
import com.github.celeskyking.squirrel.exception.TaskNotRunnableException;
import com.github.celeskyking.squirrel.resultbackend.IResultBackend;
import com.github.celeskyking.squirrel.task.*;

/**
 * Created by tianqing.wang
 * DATE : 16/2/3
 * TIME : 下午4:10
 * PROJECT : squirrel
 * PACKAGE : com.qunar.squirrel.task.processor
 *
 * @author <a href="mailto:celeskyking@163.com">tianqing.wang</a>
 */
public class TaskRouter implements ITaskProcessor{

    private IBroker broker;

    private IResultBackend resultBackend;

    private TaskContext taskContext;



    public TaskRouter(IBroker broker,IResultBackend resultBackend,
                      TaskContext taskContext){
        this.broker = broker;
        this.resultBackend = resultBackend;
        this.taskContext = taskContext;
    }

    @Override
    public void process(TaskSignature task) throws RouteKeyNotMatchException, TaskNotExistException, TaskNotRunnableException {
        resultBackend.setStateReceived(task);
        Context context = new Context();
        context.add(Context.REQUEST_ADDRESS,task.getRequestAddress());
        context.add(Context.UUID,task.getId());
        TaskProcessor taskProcessor = new TaskProcessor(resultBackend,taskContext);
        Result result = taskProcessor.process(context,task);
        if(result.isSuccess()){
            task.getTask().setResult(result);
            task.getTask().setState(TaskState.SUCCESS.getDesc());
            resultBackend.setStateSuccess(task);
        }else{
            task.getTask().setResult(result);
            task.getTask().setState(TaskState.FAILURE.getDesc());
            resultBackend.setStateFailure(task);
        }

    }

    public IBroker getBroker() {
        return broker;
    }

    public void setBroker(IBroker broker) {
        this.broker = broker;
    }

    public IResultBackend getResultBackend() {
        return resultBackend;
    }

    public void setResultBackend(IResultBackend resultBackend) {
        this.resultBackend = resultBackend;
    }
}
