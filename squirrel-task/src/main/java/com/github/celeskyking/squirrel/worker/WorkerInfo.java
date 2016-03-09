package com.github.celeskyking.squirrel.worker;

import com.google.common.collect.Lists;
import com.github.celeskyking.squirrel.task.TaskDescription;

import java.io.Serializable;
import java.util.List;

/**
 * Created by tianqing.wang
 * DATE : 16/2/5
 * TIME : 下午5:34
 * PROJECT : squirrel
 * PACKAGE : com.qunar.squirrel.worker
 *
 * @author <a href="mailto:celeskyking@163.com">tianqing.wang</a>
 * <p>
 *     通过服务发现来管理所有的worker状态,并且管理worker的任务情况,以及随时下线worker的任务信息
 *     下线任务操作通过直接discovery来保证强一致性.不用单台机器来保证任务可用.
 *     存储形式通过taskName的形式
 * </p>
 *
 *
 */
public class WorkerInfo implements Serializable{

    /**
     * worker的名称
     * */
    private String workerName;

    /**
     * 机器名
     * */
    private String host;


    /**
     * worker的ip地址
     * */
    private String remoteAddress;

    /**
     * 状态
     * */
    private transient boolean online;



    private boolean triggerForced;

    /**
     * worker包含的任务列表,主要是为了方便discovery的注册,
     * worker的工作状态都是通过discovery来实现的,
     * 任务的上下线也是通过discovery来控制caller的
     * */
    private List<TaskDescription> taskDescriptions = Lists.newArrayList();

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


    public List<TaskDescription> getTaskDescriptions() {
        return taskDescriptions;
    }

    public void setTaskDescriptions(List<TaskDescription> taskDescriptions) {
        this.taskDescriptions = taskDescriptions;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }


    public boolean isTriggerForced() {
        return triggerForced;
    }

    public void setTriggerForced(boolean triggerForced) {
        this.triggerForced = triggerForced;
    }
}
