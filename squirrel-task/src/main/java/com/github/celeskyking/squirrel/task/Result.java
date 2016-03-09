package com.github.celeskyking.squirrel.task;

import org.msgpack.annotation.Message;

import java.util.Date;

/**
 * Created by tianqing.wang
 * DATE : 16/2/2
 * TIME : 下午4:34
 * PROJECT : squirrel
 * PACKAGE : com.github.celeskyking.squirrel
 *
 * @author <a href="mailto:celeskyking@163.com">tianqing.wang</a>
 */
@Message
public class Result {
    /**
     * 执行结果
     * */
    private boolean success;
    /**
     * 开始时间
     * */
    private Date startTime;

    /**
     * 结束时间
     * */
    private Date endTime;

    /**
     * 执行任务消耗时间
     * */
    private long duration;

    /**
     * 返回的信息,默认是String类型,不关心Task返回值类型是什么,但是统一转化为String
     * */
    private String message;


    /**
     * 如果失败了,该字段不为空,代表了任务执行失败的原因
     * */
    private String reason;


    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
