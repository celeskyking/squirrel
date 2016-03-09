package com.github.celeskyking.squirrel.job;

import com.google.common.collect.Maps;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by tianqing.wang
 * DATE : 16/2/29
 * TIME : 下午1:34
 * PROJECT : squirrel
 * PACKAGE : com.qunar.squirrel.job
 *
 * @author <a href="mailto:celeskyking@163.com">tianqing.wang</a>
 */
public class JobContext implements Serializable{

    private JobContext parent;

    private JobDataMap jobDataMap = new JobDataMap();

    private Map<String,Object> keyValues = Maps.newHashMap();

    public JobContext(){

    }

    public JobContext(JobContext parent){
        this.parent = parent;
    }

    public Object get(String key){
        JobContext jobContext = this;
        while(jobContext!=null){
            if(jobContext.getKeyValues().containsKey(key)){
                return jobContext.getKeyValues().get(key);
            }else{
                jobContext = jobContext.getParent();
            }
        }
        return null;
    }


    public void addContext(String key,Object value){
        this.keyValues.put(key,value);
    }

    public Map<String, Object> getKeyValues() {
        return keyValues;
    }

    public void setKeyValues(Map<String, Object> keyValues) {
        this.keyValues = keyValues;
    }

    public JobContext getParent() {
        return parent;
    }

    public void setParent(JobContext parent) {
        this.parent = parent;
    }

    public JobDataMap getJobDataMap() {
        return jobDataMap;
    }

    public void setJobDataMap(JobDataMap jobDataMap) {
        this.jobDataMap = jobDataMap;
    }
}
