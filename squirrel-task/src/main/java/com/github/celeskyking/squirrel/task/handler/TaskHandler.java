package com.github.celeskyking.squirrel.task.handler;

import com.github.celeskyking.squirrel.annotation.Task;
import com.github.celeskyking.squirrel.task.*;
import com.google.common.collect.Lists;
import com.github.celeskyking.squirrel.exception.MessageTypeError;
import com.github.celeskyking.squirrel.task.executor.IMessage;
import com.github.celeskyking.squirrel.task.executor.TaskContextExecutor;
import jodd.paramo.MethodParameter;
import jodd.paramo.Paramo;

import java.lang.reflect.Method;
import java.util.List;

/**
 * Created by tianqing.wang
 * DATE : 16/2/2
 * TIME : 下午4:32
 * PROJECT : squirrel
 * PACKAGE : com.qunar.squirrel.task.handler
 *
 * @author <a href="mailto:celeskyking@163.com">tianqing.wang</a>
 */
public class TaskHandler extends TaskContextExecutor{

    private Method method;

    private Object instance;

    private TaskDescription taskDescription;


    public TaskHandler(Object instance,Method method) throws MessageTypeError {
        super(method.getAnnotation(Task.class).name());
        this.instance = instance;
        this.method = method;
        checkMethodReturnType();
        this.taskDescription = build();
    }


    private TaskDescription build(){
        TaskDescription taskDescription = new TaskDescription();
        taskDescription.setTaskType(TaskType.ASYNC.getCode());
        taskDescription.setTaskName(method.getAnnotation(Task.class).name());
        taskDescription.setDescription(method.getAnnotation(Task.class).description());
        taskDescription.setStatus(true);
        MethodParameter[] parameters = Paramo.resolveParameters(method);
        List<TaskArgDescription> argDescriptions = Lists.newArrayList();
        Class[] types = this.method.getParameterTypes();
        for(int i=0;i<types.length;i++){
            TaskArgDescription description = new TaskArgDescription();
            description.setIndex(i);
            description.setArgName(parameters[i].getName());
            description.setArgType(ArgType.with(types[i]).getDesc());
            argDescriptions.add(description);
        }
        taskDescription.setArgDescriptions(argDescriptions);
        return taskDescription;
    }

    /**
     * 该方法校验方法是否返回了继承自IMessage接口的
     * */
    private void checkMethodReturnType() throws MessageTypeError {
        Class<?> returnType = method.getReturnType();
        if(!returnType.isAssignableFrom(IMessage.class)){
            throw new MessageTypeError("方法返回值必须是IMessage类型或者子类");
        }
    }


    @Override
    public IMessage onExecute(Context context) throws Throwable {
        if(method.getParameterTypes().length>0){
            Object[] params = context.getTaskArgs();
            return (IMessage) method.invoke(instance,params);
        }else{
            return (IMessage) method.invoke(instance);
        }
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }


    public Object getInstance() {
        return instance;
    }

    public void setInstance(Object instance) {
        this.instance = instance;
    }

    public TaskDescription getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(TaskDescription taskDescription) {
        this.taskDescription = taskDescription;
    }
}
