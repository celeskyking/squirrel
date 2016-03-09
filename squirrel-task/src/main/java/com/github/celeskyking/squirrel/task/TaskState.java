package com.github.celeskyking.squirrel.task;

import com.google.common.collect.Maps;
import org.msgpack.annotation.Message;

import java.util.Map;

/**
 * Created by tianqing.wang
 * DATE : 16/1/27
 * TIME : 下午4:07
 * PROJECT : squirrel
 * PACKAGE : com.qunar.squirrel.task
 *
 * @author <a href="mailto:celeskyking@163.com">tianqing.wang</a>
 */
@Message
public enum  TaskState {

    STARTED("started"),
    PENDING("pending"),
    RECEIVED("received"),
    SUCCESS("success"),
    FAILURE("failure");

    private static Map<String,TaskState> taskStateMap = Maps.newHashMap();

    static{
        taskStateMap.put("started",STARTED);
        taskStateMap.put("pending",PENDING);
        taskStateMap.put("received",RECEIVED);
        taskStateMap.put("success",SUCCESS);
        taskStateMap.put("failure",FAILURE);
    }

    public static TaskState get(String desc){
        return taskStateMap.get(desc);
    }

    private String desc;

    TaskState(String desc){
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
