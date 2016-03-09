package com.github.celeskyking.squirrel.task;

import com.github.celeskyking.squirrel.annotation.*;
import com.github.celeskyking.squirrel.instrument.ClassFilter;
import com.github.celeskyking.squirrel.instrument.ClassFinder;
import com.github.celeskyking.squirrel.task.executor.ITaskExecutor;
import com.github.celeskyking.squirrel.task.handler.TaskHandler;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by tianqing.wang
 * DATE : 16/2/3
 * TIME : 上午1:12
 * PROJECT : squirrel
 * PACKAGE : com.github.celeskyking.squirrel
 *
 * @author <a href="mailto:celeskyking@163.com">tianqing.wang</a>
 *
 * <p>
 *      存储当前所有task任务的信息,一个task上线文环境
 *      所有的本地定义的task会默认的加载到TaskContext里面,
 *      Task任务的定义,通过固定的方法就可以试下
 *      TaskContext 有一个初始化的方法,就是来加载默认的任务的.
 * </p>
 *
 */
public class TaskContext {
    /**
     * 用来存储可执行的Task,每个worker分配的task可以不一样
     * */
    private Map<String,ITaskExecutor> taskMap = Maps.newHashMap();


    public TaskContext() throws Throwable {
        load();
    }

    public TaskDescription getDescription(String taskName){
        ITaskExecutor taskHandler = taskMap.get(taskName);
        if(taskHandler!=null){
            return taskHandler.getTaskDescription();
        }
        return null;
    }

    public void addTask(String name,ITaskExecutor taskHandler){
        taskMap.put(name,taskHandler);
    }

    public ITaskExecutor getTask(String taskName){
        return taskMap.get(taskName);
    }

    /**
     * 加载预定义的Task
     * */
    public void load() throws Throwable {
        ClassFilter filter = ClassFilter.filter(ClassFilter.withAnnotation(Tasks.class).and(ClassFilter.isNotAbstract()).and(ClassFilter.isNotInterface()));
        Set<Class<?>> classes = ClassFinder.finder().filter(filter).find();
        for(Class<?> clazz :classes){
            Method[] methods = clazz.getMethods();
            for(Method method : methods){
                if(method.isAnnotationPresent(com.github.celeskyking.squirrel.annotation.Task.class)){
                    TaskHandler taskHandler = new TaskHandler(clazz.newInstance(),method);
                    taskMap.put(taskHandler.getName(),taskHandler);
                }
            }
        }
    }

    public boolean contains(String taskName){
        return taskMap.get(taskName)!=null;
    }

    public List<TaskDescription> getTaskDescriptions(){
        Collection<ITaskExecutor> handers = taskMap.values();
        List<TaskDescription> descriptions = Lists.newArrayList();
        for(ITaskExecutor taskHandler : handers){
            TaskDescription description = taskHandler.getTaskDescription();
            descriptions.add(description);
        }
        return descriptions;
    }
}
