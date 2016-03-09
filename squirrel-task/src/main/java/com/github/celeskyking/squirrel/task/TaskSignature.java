package com.github.celeskyking.squirrel.task;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import org.msgpack.annotation.Message;


/**
 * Created by tianqing.wang
 * DATE : 16/1/27
 * TIME : 下午7:13
 * PROJECT : squirrel
 * PACKAGE : com.qunar.squirrel.task
 *
 * @author <a href="mailto:celeskyking@163.com">tianqing.wang</a>x'z
 */
@Message
public class TaskSignature {

    /**
     * 任务的唯一id
     * */
    @JSONField(name = "id")
    public String id;

    /**
     * 任务,单个任务
     * */
    @JSONField(name = "task")
    public Task task;


    @JSONField(name = "request_address")
    public String requestAddress;

//    @JSONField(name = "cron_expression")
//    public String cronExpression;

    /**
     * 发送者的caller地址
     * */
    @JSONField(name = "caller")
    public String caller;

    @JSONField(name = "worker")
    public String worker;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRequestAddress() {
        return requestAddress;
    }

    public void setRequestAddress(String requestAddress) {
        this.requestAddress = requestAddress;
    }

//    public String getCronExpression() {
//        return cronExpression;
//    }
//
//    public void setCronExpression(String cronExpression) {
//        this.cronExpression = cronExpression;
//    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public String getCaller() {
        return caller;
    }

    public void setCaller(String caller) {
        this.caller = caller;
    }

    public String toString(){
        return JSON.toJSONString(this);
    }

    public String getWorker() {
        return worker;
    }

    public void setWorker(String worker) {
        this.worker = worker;
    }
}
