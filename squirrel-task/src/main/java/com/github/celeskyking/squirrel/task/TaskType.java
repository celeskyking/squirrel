package com.github.celeskyking.squirrel.task;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * Created by tianqing.wang
 * DATE : 16/1/29
 * TIME : 下午12:20
 * PROJECT : squirrel
 * PACKAGE : com.qunar.squirrel.task
 *
 * @author <a href="mailto:celeskyking@163.com">tianqing.wang</a>
 */
public enum  TaskType {

    /**
     * 普通的单个任务
     * */
    ASYNC(0,"async"),
    /**
     * 一组任务,但是结果是通过taskId来进行回调,不是原子操作
     * */
    SYNC(1,"sync"),
    /**
     * 一组任务,原子操作,统一返回执行的任务结果,成功和失败等.
     *
     * */
    STREAM(2,"stream");


    /**
     * 任务code标识
     * */
    private int code;

    /**
     * 任务类型描述
     * */
    private String desc;

    private static Map<Integer,TaskType> typeMap = Maps.newHashMap();
    static{
        typeMap.put(0,ASYNC);
        typeMap.put(1,SYNC);
        typeMap.put(2,STREAM);
    }

    public static TaskType get(int code){
        return typeMap.get(code);
    }

    TaskType(int code,String desc){
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }


    public void setCode(int code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
