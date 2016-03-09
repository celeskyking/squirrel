package com.github.celeskyking.squirrel.discovery.consul;

import com.alibaba.fastjson.JSON;
import com.ecwid.consul.v1.ConsulClient;
import com.ecwid.consul.v1.Response;
import com.ecwid.consul.v1.kv.model.GetValue;
import com.ecwid.consul.v1.kv.model.PutParams;
import com.ecwid.consul.v1.session.model.Session;
import com.github.celeskyking.squirrel.caller.ICaller;
import com.github.celeskyking.squirrel.discovery.Watcher;
import com.github.celeskyking.squirrel.helper.ConsulHelper;
import com.github.celeskyking.squirrel.worker.WorkerInfo;
import com.google.common.collect.Lists;
import com.google.common.net.HostAndPort;
import com.github.celeskyking.squirrel.broker.IBroker;
import com.github.celeskyking.squirrel.caller.CallerInfo;
import com.github.celeskyking.squirrel.discovery.DiscoveryService;
import com.github.celeskyking.squirrel.job.ITriggerInfoCallback;
import com.github.celeskyking.squirrel.job.JobberInfo;
import com.github.celeskyking.squirrel.task.TaskDescription;
import com.github.celeskyking.squirrel.trigger.TriggerInfo;
import com.github.celeskyking.squirrel.trigger.TriggerState;
import jodd.util.Base64;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by tianqing.wang
 * DATE : 16-2-14
 * TIME : 下午2:32
 * PROJECT : squirrel
 * PACKAGE : com.qunar.squirrel.discovery
 *
 * @author <a href="mailto:celeskyking@163.com">tianqing.wang</a>
 */
public class ConsulService implements DiscoveryService {

    private final static String PREFIX ="squirrel/";

    private ConsulConfig consulConfig;
    /**
     * session的id,控制delete
     * */
    private String sessionId;

    private ConsulWatcher triggerWatcher ;

    /**
     * 该sessionId控制trigger的工作
     * */
    private String releaseSessionId;


    private Logger logger = LoggerFactory.getLogger(ConsulService.class);

    private ConsulService(final ConsulConfig config){
        this.consulConfig = config;
        this.triggerWatcher = new ConsulWatcher(config);
    }


    private boolean register(WorkerInfo worker) {
        try{
            this.sessionId = ConsulHelper.createSessionAndFixedRenew(consulConfig,worker, Session.Behavior.DELETE);
            this.releaseSessionId = ConsulHelper.createSessionAndFixedRenew(consulConfig,worker, Session.Behavior.RELEASE);
            ConsulHelper.registerWorker(PREFIX,sessionId,worker,consulConfig);
            ConsulHelper.registerTasks(PREFIX,sessionId,worker,consulConfig);
            return true;
        }catch (Exception e){
            logger.error("worker初始化失败",e);
            throw e;
        }
    }


    @Override
    public boolean deRegister(String taskName,WorkerInfo workerInfo) {
        try{
            ConsulClient consulClient = ConsulHelper.newConsulClient(consulConfig);
            String path = PREFIX+"tasks/"+workerInfo.getWorkerName()+"/"+taskName;
            Response<GetValue> response = consulClient.getKVValue(path);
            TaskDescription taskDescription = JSON.parseObject(response.getValue().getValue(),TaskDescription.class);
            taskDescription.setStatus(false);
            Response<Boolean> booleanResponse = consulClient.setKVValue(path,JSON.toJSONString(taskDescription),consulConfig.getToken(),ConsulHelper.defaultPutParams(sessionId));
            if(booleanResponse.getValue()){
                logger.info("下线成功，task:{}",JSON.toJSONString(taskDescription));
                return true;
            }else{
                logger.error("下线失败，task:{}",JSON.toJSONString(taskDescription));
                return false;
            }
        }catch (Exception e){
            logger.error("",e);
            return false;
        }
    }

    @Override
    public boolean register(String taskName, WorkerInfo workerInfo) {
        try{
            ConsulClient consulClient = ConsulHelper.newConsulClient(consulConfig);
            String path = PREFIX+"tasks/"+workerInfo.getWorkerName()+"/"+taskName;
            Response<GetValue> response = consulClient.getKVValue(path);
            TaskDescription taskDescription = JSON.parseObject(response.getValue().getValue(),TaskDescription.class);
            taskDescription.setStatus(true);
            Response<Boolean> booleanResponse = consulClient.setKVValue(path,JSON.toJSONString(taskDescription),consulConfig.getToken(),ConsulHelper.defaultPutParams(sessionId));
            if(booleanResponse.getValue()){
                logger.info("上线成功，task:{}",JSON.toJSONString(taskDescription));
                return true;
            }else{
                logger.error("上线成功，task:{}",JSON.toJSONString(taskDescription));
                return false;
            }
        }catch (Exception e){
            logger.error("",e);
            return false;
        }
    }

    @Override
    public boolean register(JobberInfo jobber) {
        if(StringUtils.isEmpty(sessionId)){
            synchronized (ConsulService.this){
                this.sessionId = ConsulHelper.createSessionAndFixedRenew(consulConfig, Session.Behavior.DELETE);
            }
        }
        ConsulClient consulClient = ConsulHelper.newConsulClient(consulConfig);
        String path = PREFIX+"jobbers/"+jobber.getWorker()+"/"+jobber.getName()+"/"+jobber.getRemoteAddress();
        Response<GetValue> response = consulClient.getKVValue(path,consulConfig.getToken());
        if(response!=null&&response.getValue()!=null){
            consulClient.deleteKVValue(path,consulConfig.getToken(),ConsulHelper.defaultQueryParams());
        }
        PutParams putParams = new PutParams();
        putParams.setAcquireSession(sessionId);
        Response<Boolean> booleanResponse = consulClient.setKVValue(path,JSON.toJSONString(jobber),consulConfig.getToken(),putParams);
        if(booleanResponse.getValue()){
            return true;
        }
        return false;
    }


    @Override
    public boolean register(CallerInfo caller) {
        if(StringUtils.isEmpty(sessionId)){
            synchronized (ConsulService.this){
                if(StringUtils.isEmpty(sessionId)){
                    this.sessionId = ConsulHelper.createSessionAndFixedRenew(consulConfig,Session.Behavior.DELETE);
                }
            }
            this.sessionId = ConsulHelper.createSessionAndFixedRenew(consulConfig, Session.Behavior.DELETE);
        }
        caller.setUuid(sessionId);
        ConsulClient consulClient = ConsulHelper.newConsulClient(consulConfig);
        String path = PREFIX+"callers/"+caller.getWorker()+"/"+caller.getName()+"/"+caller.getRemoteAddress();
        Response<GetValue> getValueResponse = consulClient.getKVValue(path,consulConfig.getToken());
        if(getValueResponse!=null&&getValueResponse.getValue()!=null){
            consulClient.deleteKVValue(path,consulConfig.getToken());
        }
        PutParams putParams = new PutParams();
        putParams.setAcquireSession(sessionId);
        Response<Boolean> response = consulClient.setKVValue(path,JSON.toJSONString(caller),consulConfig.getToken(),putParams);
        if(response.getValue()){
            return true;
        }
        return false;
    }


    /**
     * 当worker接收到注册请求的时候，添加schedule成功之后，注册discovery
     *
     * */
    @Override
    public boolean register(TriggerInfo triggerInfo) {
        triggerInfo.setSessionId(releaseSessionId);
        ConsulClient consulClient = ConsulHelper.newConsulClient(consulConfig);
        String path = PREFIX+"triggers/"+triggerInfo.getWorker()+"/"+triggerInfo.getCaller()+"/"+triggerInfo.getName();
        Response<GetValue> getValueResponse = consulClient.getKVValue(path,consulConfig.getToken());
        if(getValueResponse!=null&&getValueResponse.getValue()!=null){
            consulClient.deleteKVValue(path,consulConfig.getToken());
        }
        PutParams putParams = new PutParams();
        putParams.setAcquireSession(releaseSessionId);
        Response<Boolean> response = consulClient.setKVValue(path,JSON.toJSONString(triggerInfo),consulConfig.getToken(),putParams);
        logger.info("注册trigger结果:{},trigger:{}",response.getValue(),JSON.toJSONString(triggerInfo));
        return response.getValue();
    }


    public boolean existTrigger(String worker,String jobber, String jobName) {
        return getTrigger(worker,jobber,jobName) != null;
    }

    public TriggerInfo getTrigger(String worker,String jobber,String jobName){
        ConsulClient consulClient = ConsulHelper.newConsulClient(consulConfig);
        String path = PREFIX+"triggers/"+worker+"/"+jobber+"/"+jobName;
        Response<GetValue> getValueResponse = consulClient.getKVValue(path,consulConfig.getToken());
        if(getValueResponse!=null&&getValueResponse.getValue()!=null){
            String value = getValueResponse.getValue().getValue();
            TriggerInfo triggerInfo = JSON.parseObject(Base64.decodeToString(value),TriggerInfo.class);
            if (StringUtils.isEmpty(getValueResponse.getValue().getSession())){
                triggerInfo.setSessionId(null);
            }
            return triggerInfo;
        }
        return null;
    }


    @Override
    public boolean online(WorkerInfo workerInfo) {
        return register(workerInfo);
    }

    @Override
    public boolean offline(WorkerInfo workerInfo) {
        try{
            ConsulClient consulClient = ConsulHelper.newConsulClient(consulConfig);
            consulClient.sessionDestroy(this.sessionId,ConsulHelper.defaultQueryParams());
            return true;
        }catch (Exception e){
            logger.error("",e);
            return false;
        }
    }

    @Override
    public List<WorkerInfo> getWorkers(String routeKey) {
        ConsulClient consulClient = ConsulHelper.newConsulClient(consulConfig);
        Response<List<GetValue>> response;
        if(StringUtils.isEmpty(routeKey)){
            response =  consulClient.getKVValues(PREFIX+"workers/",consulConfig.getToken());
        }else{
            response = consulClient.getKVValues(PREFIX+"workers/"+routeKey,consulConfig.getToken());
        }
        if(response!=null&&response.getValue()!=null){
            List<GetValue> values = response.getValue();
            List<WorkerInfo> workerInfos = Lists.newArrayList();
            for(GetValue value : values){
                WorkerInfo workerInfo = JSON.parseObject(Base64.decode(value.getValue()),WorkerInfo.class);
                workerInfos.add(workerInfo);
            }
            return workerInfos;
        }
        return null;
    }

    @Override
    public List<CallerInfo> getCallers(String worker) {
        return null;
    }

    @Override
    public List<JobberInfo> getJobbers(String worker, String jobber) {
        ConsulClient consulClient = ConsulHelper.newConsulClient(consulConfig);
        Response<List<GetValue>> response;
        if(StringUtils.isEmpty(jobber)){
            response =  consulClient.getKVValues(PREFIX+"jobbers/"+worker,consulConfig.getToken());
        }else{
            response = consulClient.getKVValues(PREFIX+"jobbers/"+worker+"/"+jobber,consulConfig.getToken());
        }
        if(response!=null&&response.getValue()!=null){
            List<GetValue> values = response.getValue();
            List<JobberInfo> jobberInfos = Lists.newArrayList();
            for(GetValue value : values){
                JobberInfo jobberInfo = JSON.parseObject(Base64.decode(value.getValue()),JobberInfo.class);
                jobberInfos.add(jobberInfo);
            }
            return jobberInfos;
        }
        return null;
    }


    @Override
    public List<TaskDescription> getTasks(String workerName) {
        return getTasks(workerName,null);
    }

    @Override
    public List<TaskDescription> getTasks(String workerName,String taskName) {
        ConsulClient consulClient = ConsulHelper.newConsulClient(consulConfig);
        Response<List<GetValue>> response;
        if(StringUtils.isEmpty(taskName)){
            response =  consulClient.getKVValues(PREFIX+"tasks/"+workerName,consulConfig.getToken());
        }else{
            response =  consulClient.getKVValues(PREFIX+"tasks/"+workerName+"/"+taskName,consulConfig.getToken());
        }
        List<GetValue> values = response.getValue();
        List<TaskDescription> taskDescriptions = Lists.newArrayList();
        if(values!=null){
            for(GetValue value : values){
                TaskDescription taskDescription = JSON.parseObject(Base64.decode(value.getValue()),TaskDescription.class);
                taskDescriptions.add(taskDescription);
            }
            return taskDescriptions;
        }
        return Lists.newArrayList();
    }

    @Override
    public void distributeLock(ICaller caller) {
        logger.info("启动分布式锁..");
        if(StringUtils.isEmpty(sessionId)){
            synchronized (ConsulService.this){
                if(StringUtils.isEmpty(sessionId)){
                    this.sessionId = ConsulHelper.createSessionAndFixedRenew(consulConfig, Session.Behavior.DELETE);
                }
            }
        }
        ConsulWatcher watcher = new ConsulWatcher(consulConfig);
        String keyPrefix = PREFIX+"callers/"+caller.getWorker()+"/"+caller.getName();
        watcher.util(keyPrefix,consulConfig.getDistributeWait(),new DistributeLockCondition(sessionId));
        logger.info("分布式锁解锁,唤醒caller的处理...");
    }

    @Override
    public void distributeLock(final IBroker broker, final WorkerInfo workerInfo) {
        logger.info("启动分布式锁..");
        ConsulWatcher watcher = new ConsulWatcher(consulConfig);
        String keyPrefix = PREFIX+"triggers/"+workerInfo.getWorkerName();
        watcher.always(keyPrefix,consulConfig.getDistributeWait(),new Watcher.IValuesCallback<GetValue>(){

            private boolean unlocked(){
                ConsulClient consulClient = ConsulHelper.newConsulClient(consulConfig);
                Response<List<GetValue>> response = consulClient.getKVValues(PREFIX+"workers/"+workerInfo.getWorkerName(),consulConfig.getToken());
                if(response!=null&&response.getValue()!=null){
                    return new DistributeLockCondition(sessionId).match(response.getValue());
                }
                return false;
            }

            @Override
            public void ok(List<GetValue> values) {
                if(unlocked()){
                    logger.info("获取到了master控制权,workers:{}",JSON.toJSONString(values));
                    for(GetValue value : values){
                        if(StringUtils.isEmpty(value.getSession())){
                            TriggerInfo triggerInfo;
                            try{
                                triggerInfo = JSON.parseObject(Base64.decode(value.getValue()),TriggerInfo.class);
                                if(TriggerState.RUNNING.getDesc().equals(triggerInfo.getState())){
                                    triggerInfo.setSessionId(null);
                                    broker.publish(triggerInfo);
                                    logger.info("重新注册定时任务:{}",JSON.toJSONString(triggerInfo));
                                }else if(TriggerState.PAUSING.getDesc().equals(triggerInfo.getState())){
                                    triggerInfo.setSessionId(null);
                                    triggerInfo.setState(TriggerState.PAUSING_RECOVERY.getDesc());
                                    broker.publish(triggerInfo);
                                    logger.info("重新注册定时任务:{}",JSON.toJSONString(triggerInfo));
                                }
                            }catch (Throwable e){
                                logger.error("",e);
                            }
                        }
                    }
                }
            }

            @Override
            public void notOk(Throwable throwable) {
                logger.error("",throwable);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    logger.error("",e);
                }
            }
        });
    }

    @Override
    public void watchingTriggers(String worker, String jobber,long waitTimes, final ITriggerInfoCallback callback) throws Throwable {
        triggerWatcher.always(PREFIX + "triggers/" + worker + "/" + jobber + "/", waitTimes,new Watcher.IValuesCallback<GetValue>() {
            @Override
            public void ok(List<GetValue> t) {
                List<TriggerInfo> triggerInfos = Lists.newArrayList();
                for(GetValue value : t){
                    TriggerInfo triggerInfo = JSON.parseObject(Base64.decodeToString(value.getValue()),TriggerInfo.class);
                    triggerInfo.setSessionId(value.getSession());
                    triggerInfos.add(triggerInfo);
                }
                callback.ok(triggerInfos);
            }

            @Override
            public void notOk(Throwable throwable) {
                callback.notOk(throwable);
            }
        });
    }

    @Override
    public void stopWatchingTriggers() {
        triggerWatcher.stop();
    }


    private static class DistributeLockCondition implements Watcher.IValuesCondition<GetValue>{

        private String sessionId;

        public DistributeLockCondition(String sessionId){
            this.sessionId = sessionId;
        }
        long currentIndex = -1;

        @Override
        public boolean match(List<GetValue> t) {
            long min = 0;
            boolean status = false;
            for(GetValue getValue :t){
                long modifyIndex = getValue.getModifyIndex();
                if(min == 0){
                    min = modifyIndex;
                }else{
                    if(min > modifyIndex){
                        min = modifyIndex;
                    }
                }
                if(StringUtils.equals(sessionId,getValue.getSession())){
                    currentIndex = getValue.getModifyIndex();
                }
                if(StringUtils.isNotEmpty(getValue.getSession())){
                    status = true;
                }
            }
            if(!status){
                return true;
            }
            if(currentIndex == min){
                return true;
            }
            return false;
        }
    }



    public static Builder builder(){
        return new Builder();
    }

    public static class Builder{

        private HostAndPort address;

        private String sessionTTL;

        private String token;

        public Builder withHostAndPort(HostAndPort address){
            this.address =address;
            return this;
        }

        public Builder withToken(String token){
            this.token = token;
            return this;
        }

        public Builder withSessionTTL(String ttl){
            this.sessionTTL = ttl;
            return this;
        }

        public ConsulService build(){
            ConsulConfig consulConfig = ConsulConfig.builder()
                    .withHostAndPort(address)
                    .withToken(token)
                    .withTtl(sessionTTL)
                    .build();
            return new ConsulService(consulConfig);
        }
    }

    public ConsulConfig getConsulConfig() {
        return consulConfig;
    }

    public void setConsulConfig(ConsulConfig consulConfig) {
        this.consulConfig = consulConfig;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }


}
