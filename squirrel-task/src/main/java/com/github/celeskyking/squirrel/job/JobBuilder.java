package com.github.celeskyking.squirrel.job;

import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * Created by tianqing.wang
 * DATE : 16/2/29
 * TIME : 上午11:43
 * PROJECT : squirrel
 * PACKAGE : com.github.celeskyking.squirrel.job
 *
 * @author <a href="mailto:celeskyking@163.com">tianqing.wang</a>
 */
public class JobBuilder {

    private JobBuilder(){}

    private String jobName;

    private String description;

    private String cronExpress;

    private String target;

    private JobDataMap jobDataMap = new JobDataMap();

    public static JobBuilder builder(){
        return new JobBuilder();
    }

    public JobBuilder withJobName(String name){
        this.jobName = name;
        return this;
    }

    public JobBuilder withDescription(String description){
        this.description = description;
        return this;
    }

    public JobBuilder withCronExpress(String cronExpress){
        this.cronExpress = cronExpress;
        return this;
    }

    public JobBuilder withTarget(String target){
        this.target = target;
        return this;
    }

    public JobBuilder withData(String key,Object value){
        this.jobDataMap.put(key,value);
        return this;
    }

    public JobBuilder withData(Map<String,Object> map){
        for(Map.Entry<String,Object> entry:map.entrySet()){
            this.jobDataMap.put(entry.getKey(),entry.getValue());
        }
        return this;
    }


    public JobInfo build(){
        if(StringUtils.isEmpty(jobName)||StringUtils.isEmpty(cronExpress)){
            throw new RuntimeException("job名称和表达式不能够为空");
        }
        if(StringUtils.isEmpty(description)){
            this.description = jobName;
        }
        if(StringUtils.isEmpty(target)){
            throw new RuntimeException("job必须含有可执行的操作,请指定executor");
        }
        JobInfo jobInfo = new JobInfo();
        jobInfo.setCronExpress(cronExpress);
        jobInfo.setDescription(description);
        jobInfo.setName(jobName);
        jobInfo.setTarget(target);
        jobInfo.setJobDataMap(jobDataMap);
        return jobInfo;
    }
}
