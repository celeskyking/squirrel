package com.github.celeskyking.squirrel.task;

import com.google.common.collect.Lists;
import org.msgpack.annotation.Message;

import java.io.Serializable;
import java.util.List;

/**
 * Created by tianqing.wang
 * DATE : 16/1/27
 * TIME : 下午12:07
 * PROJECT : squirrel
 * PACKAGE : com.github.celeskyking.squirrel.task
 *
 * @author <a href="mailto:celeskyking@163.com">tianqing.wang</a>
 */
@Message
public class Task implements Serializable {


    public Task(){

    }

    /**
     * 任务名称
     * */
    private String taskName;

    /**
     * 任务的唯一id
     * */
    private String taskId;

    /**
     * 任务的请求参数
     * */
    private List<TaskArg> taskArgs = Lists.newArrayList();

    /**
     * 任务状态
     * */
    private String state;
    /**
     * 执行结果
     * */
    private Result result;


    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public List<TaskArg> getTaskArgs() {
        return taskArgs;
    }

    public void setTaskArgs(List<TaskArg> taskArgs) {
        this.taskArgs = taskArgs;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
