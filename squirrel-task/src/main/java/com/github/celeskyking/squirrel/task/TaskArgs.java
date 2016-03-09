package com.github.celeskyking.squirrel.task;

import com.google.common.collect.Lists;
import org.msgpack.annotation.Message;

import java.io.Serializable;
import java.util.List;

/**
 * Created by tianqing.wang
 * DATE : 16/2/1
 * TIME : 上午11:25
 * PROJECT : squirrel
 * PACKAGE : com.qunar.squirrel.task
 *
 * @author <a href="mailto:celeskyking@163.com">tianqing.wang</a>
 */
@Message
public class TaskArgs implements Serializable{

    private List<TaskArg> taskArgs = Lists.newArrayList();

    public TaskArgs arg(Object value){
        this.taskArgs.add(TaskArg.of(value));
        return this;
    }

    public TaskArgs arg(TaskArg taskArg){
        this.taskArgs.add(taskArg);
        return this;
    }

    public List<TaskArg> args(){
        return this.taskArgs;
    }

    public int size(){
        return taskArgs.size();
    }

    public List<TaskArg> getTaskArgs() {
        return taskArgs;
    }

    public void setTaskArgs(List<TaskArg> taskArgs) {
        this.taskArgs = taskArgs;
    }
}
