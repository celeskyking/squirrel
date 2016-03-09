package com.github.celeskyking.squirrel.task;

import com.google.common.collect.Lists;

import java.io.Serializable;
import java.util.List;

/**
 * Created by tianqing.wang
 * DATE : 16/2/5
 * TIME : 下午5:59
 * PROJECT : squirrel
 * PACKAGE : com.github.celeskyking.squirrel.task
 *
 * @author <a href="mailto:celeskyking@163.com">tianqing.wang</a>
 *  对任务的描述
 *  主要用于服务发现的存储,支持参数类型的校验,任务注册的时候,可以设置参数类型
 *  方便调用方在调用的过程直接debug出任务参数的错误.
 */
public class TaskDescription implements Serializable{

    /**
     * 任务描述
     * */
    private String taskName;

    /**
     * 任务的描述
     * */
    private String description;

    /**
     * 任务类型
     * */
    private int taskType;

    /**
     * worker的名称
     * */
    private String workerName;


    /**
     * worker的ip地址
     * */
    private String remoteAddress;
    /**
     * 任务状态
     * 是否下线状态
     * */
    private boolean status;

    /**
     * 任务参数的描述
     * */
    private List<TaskArgDescription> argDescriptions = Lists.newArrayList();


    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public List<TaskArgDescription> getArgDescriptions() {
        return argDescriptions;
    }

    public void setArgDescriptions(List<TaskArgDescription> argDescriptions) {
        this.argDescriptions = argDescriptions;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getTaskType() {
        return taskType;
    }

    public void setTaskType(int taskType) {
        this.taskType = taskType;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getWorkerName() {
        return workerName;
    }

    public void setWorkerName(String workerName) {
        this.workerName = workerName;
    }

    public String getRemoteAddress() {
        return remoteAddress;
    }

    public void setRemoteAddress(String remoteAddress) {
        this.remoteAddress = remoteAddress;
    }
}
