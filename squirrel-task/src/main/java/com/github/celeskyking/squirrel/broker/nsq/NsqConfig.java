package com.github.celeskyking.squirrel.broker.nsq;

import com.google.common.collect.Sets;
import com.google.common.net.HostAndPort;

import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by tianqing.wang
 * DATE : 16/2/5
 * TIME : 下午2:50
 * PROJECT : squirrel
 * PACKAGE : com.qunar.squirrel.broker
 *
 * @author <a href="mailto:celeskyking@163.com">tianqing.wang</a>
 */
public class NsqConfig {

    /**
     * 不支持nsqd的连接模式,要保证nsq的高可用
     * */
    private Set<HostAndPort> lookupAddress;
    /**
     * 默认的主题
     * */
    public final static String TASK_TOPIC = "squirrel_task";


    public final static String RESULT_TOPIC = "squirrel_result";

    public final static String TRIGGER_TOPIC = "squirrel_trigger";

    /**
     * 通过几个线程执行任务执行
     * */
    private ExecutorService executorService = Executors.newCachedThreadPool();


    private NsqConfig(Set<HostAndPort> lookupAddresses){
        this.lookupAddress = lookupAddresses;
    }


    public Set<HostAndPort> getLookupAddress() {
        return lookupAddress;
    }

    public void setLookupAddress(Set<HostAndPort> lookupAddress) {
        this.lookupAddress = lookupAddress;
    }

    public String getTaskTopic() {
        return TASK_TOPIC;
    }



    public String getResultTopic() {
        return RESULT_TOPIC;
    }



    public ExecutorService getExecutorService() {
        return executorService;
    }


    public void setExecutorService(ExecutorService executorService) {
        this.executorService = executorService;
    }

    public String getTriggerTopic() {
        return TRIGGER_TOPIC;
    }


    public static Builder builder(){
        return new Builder();
    }

    public static class Builder{

        private Set<HostAndPort> hostAndPorts = Sets.newHashSet();



        public Builder withLookup(HostAndPort hostAndPort){
            hostAndPorts.add(hostAndPort);
            return this;
        }

        public NsqConfig build(){
            return new NsqConfig(hostAndPorts);
        }

    }

}
