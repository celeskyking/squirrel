package com.github.celeskyking.squirrel.trigger;

import com.github.celeskyking.squirrel.job.JobDataMap;

import java.util.Date;

/**
 * Created by tianqing.wang
 * DATE : 16-2-19
 * TIME : 上午10:50
 * PROJECT : squirrel
 * PACKAGE : com.qunar.squirrel.tigger
 *
 * @author <a href="mailto:celeskyking@163.com">tianqing.wang</a>
 */
public class TriggerInfo {

    private String description;

    private String worker;

    private String name;

    private String remoteAddress;

    private String caller;

    private String cronExpress;

    private String sessionId;

    private String state;

    private String target;

    private Date receivedTime;

    private JobDataMap jobDataMap;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRemoteAddress() {
        return remoteAddress;
    }

    public void setRemoteAddress(String remoteAddress) {
        this.remoteAddress = remoteAddress;
    }

    public String getCaller() {
        return caller;
    }

    public void setCaller(String caller) {
        this.caller = caller;
    }

    public String getCronExpress() {
        return cronExpress;
    }

    public void setCronExpress(String cronExpress) {
        this.cronExpress = cronExpress;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getWorker() {
        return worker;
    }

    public void setWorker(String worker) {
        this.worker = worker;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Date getReceivedTime() {
        return receivedTime;
    }

    public void setReceivedTime(Date receivedTime) {
        this.receivedTime = receivedTime;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public JobDataMap getJobDataMap() {
        return jobDataMap;
    }

    public void setJobDataMap(JobDataMap jobDataMap) {
        this.jobDataMap = jobDataMap;
    }
}
