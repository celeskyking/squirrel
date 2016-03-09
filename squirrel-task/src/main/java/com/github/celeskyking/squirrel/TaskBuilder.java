package com.github.celeskyking.squirrel;

import com.github.celeskyking.squirrel.task.Task;
import com.github.celeskyking.squirrel.task.TaskArg;
import org.apache.commons.lang3.StringUtils;

import java.util.UUID;

/**
 * Created by tianqing.wang
 * DATE : 16-2-16
 * TIME : 下午4:19
 * PROJECT : squirrel
 * PACKAGE : com.github.celeskyking.squirrel
 *
 * @author <a href="mailto:celeskyking@163.com">tianqing.wang</a>
 */
public class TaskBuilder {

    private Task task;

    private TaskBuilder(){
        this.task = new Task();
    }

    public static TaskBuilder builder(){
        return new TaskBuilder();
    }

    public TaskBuilder arg(TaskArg arg){
        this.task.getTaskArgs().add(arg);
        return this;
    }

    public TaskBuilder arg(Object object){
        this.task.getTaskArgs().add(TaskArg.of(object));
        return this;
    }

    public TaskBuilder name(String name){
        this.task.setTaskName(name);
        return this;
    }

    public TaskBuilder id(String id){
        this.task.setTaskId(id);
        return this;
    }

    public Task build(){
        if(StringUtils.isEmpty(task.getTaskId())){
            task.setTaskId(uuid());
        }
        if(StringUtils.isEmpty(task.getTaskName())){
            throw new RuntimeException("task的名称不能够为空");
        }
        return task;
    }


    private String uuid(){
        UUID uuid = UUID.randomUUID();
        return uuid.toString().replace("-","");
    }
}
