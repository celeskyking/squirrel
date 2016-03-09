package com.github.celeskyking.squirrel.trigger.handler;

import com.alibaba.fastjson.JSON;
import com.github.celeskyking.squirrel.worker.WorkerInfo;
import com.google.common.collect.Maps;
import com.qunar.nsq.client.NSQMessage;
import com.github.celeskyking.squirrel.broker.IBroker;
import com.github.celeskyking.squirrel.discovery.DiscoveryService;
import com.github.celeskyking.squirrel.trigger.CronJobNotify;
import com.github.celeskyking.squirrel.trigger.JobTrigger;
import com.github.celeskyking.squirrel.trigger.TriggerInfo;
import com.github.celeskyking.squirrel.trigger.TriggerState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Created by tianqing.wang
 * DATE : 16-2-23
 * TIME : 下午12:00
 * PROJECT : squirrel
 * PACKAGE : com.qunar.squirrel.trigger.handler
 *
 * @author <a href="mailto:celeskyking@163.com">tianqing.wang</a>
 */
public class TriggerStatusHandlers {

    private DiscoveryService discoveryService;

    private JobTrigger jobTrigger;

    private WorkerInfo workerInfo;

    private IBroker broker;

    private Logger logger = LoggerFactory.getLogger(TriggerStatusHandler.class);

    private Map<TriggerState,TriggerStatusHandler> handlerMap = Maps.newHashMap();

    {
        handlerMap.put(TriggerState.RUNNING, new TriggerStatusHandler() {
            @Override
            public void handle(NSQMessage message, TriggerInfo triggerInfo, DiscoveryService discoveryService, JobTrigger jobTrigger) {
                TriggerInfo discoveryTriggerInfo = discoveryService.getTrigger(workerInfo.getWorkerName(),triggerInfo.getCaller(),triggerInfo.getName());
                if(discoveryTriggerInfo != null && discoveryTriggerInfo.getSessionId()!=null){
                    logger.info("已经注册过的job，进行更新操作。trigger:{}", JSON.toJSONString(triggerInfo));
                    jobTrigger.removeJob(buildJob(triggerInfo));
                    jobTrigger.addJob(buildJob(triggerInfo));
                    registerTriggerInfo(message,triggerInfo);
                }else{
                    logger.info("定时任务注册,trigger:{}",JSON.toJSONString(triggerInfo));
                    jobTrigger.addJob(buildJob(triggerInfo));
                    registerTriggerInfo(message,triggerInfo);
                }
            }
        });
        handlerMap.put(TriggerState.PAUSING, new TriggerStatusHandler() {
            @Override
            public void handle(NSQMessage message, TriggerInfo triggerInfo, DiscoveryService discoveryService, JobTrigger jobTrigger) {
                CronJobNotify jobNotify = buildJob(triggerInfo);
                if(jobTrigger.containsJob(jobNotify)){
                    jobTrigger.pauseJob(jobNotify);
                    registerTriggerInfo(message,triggerInfo);
                }else{
                    message.requeue();
                }
            }
        });
        handlerMap.put(TriggerState.PAUSING_RECOVERY, new TriggerStatusHandler() {
            @Override
            public void handle(NSQMessage message, TriggerInfo triggerInfo, DiscoveryService discoveryService, JobTrigger jobTrigger) {
                CronJobNotify jobNotify = buildJob(triggerInfo);
                jobTrigger.addJob(jobNotify);
                jobTrigger.pauseJob(jobNotify);
                triggerInfo.setState(TriggerState.PAUSING.getDesc());
                registerTriggerInfo(message,triggerInfo);
            }
        });
        handlerMap.put(TriggerState.RESUME, new TriggerStatusHandler() {
            @Override
            public void handle(NSQMessage message, TriggerInfo triggerInfo, DiscoveryService discoveryService, JobTrigger jobTrigger) {
                CronJobNotify jobNotify = buildJob(triggerInfo);
                if(jobTrigger.containsJob(jobNotify)){
                    jobTrigger.resumeJob(jobNotify);
                    triggerInfo.setState(TriggerState.RUNNING.getDesc());
                    registerTriggerInfo(message,triggerInfo);
                }else{
                    message.requeue();
                }
            }
        });

        handlerMap.put(TriggerState.UPDATE, new TriggerStatusHandler() {
            @Override
            public void handle(NSQMessage message, TriggerInfo triggerInfo, DiscoveryService discoveryService, JobTrigger jobTrigger) {
                CronJobNotify jobNotify = buildJob(triggerInfo);
                if(jobTrigger.containsJob(jobNotify)){
                    jobTrigger.modifyJob(jobNotify);
                    triggerInfo.setState(TriggerState.RUNNING.getDesc());
                    registerTriggerInfo(message,triggerInfo);
                }else{
                    message.requeue();
                }
            }
        });

        handlerMap.put(TriggerState.STOPPING, new TriggerStatusHandler() {
            @Override
            public void handle(NSQMessage message, TriggerInfo triggerInfo, DiscoveryService discoveryService, JobTrigger jobTrigger) {
                CronJobNotify jobNotify = buildJob(triggerInfo);
                if(jobTrigger.containsJob(jobNotify)){
                    jobTrigger.removeJob(jobNotify);
                    registerTriggerInfo(message,triggerInfo);
                }else{
                    message.requeue();
                }
            }
        });
    }

    public TriggerStatusHandlers(DiscoveryService discoveryService,
                                 JobTrigger jobTrigger,
                                 WorkerInfo workerInfo,
                                 IBroker broker){
        this.discoveryService = discoveryService;
        this.jobTrigger = jobTrigger;
        this.workerInfo = workerInfo;
        this.broker = broker;
    }

    public void handle(NSQMessage message,TriggerInfo triggerInfo){
        TriggerState state = TriggerState.get(triggerInfo.getState());
        if(state!=null){
            handlerMap.get(state).handle(message,triggerInfo,discoveryService,jobTrigger);
        }else{
            logger.error("废弃的job信息,不存在的类型:"+JSON.toJSONString(triggerInfo));
            message.finished();
        }

    }

    private void registerTriggerInfo(NSQMessage message,TriggerInfo triggerInfo){
        boolean result = discoveryService.register(triggerInfo);
        if(!result){
            message.requeue(1000);
        }else{
            message.finished();
        }
    }

    private CronJobNotify buildJob(TriggerInfo triggerInfo){
        CronJobNotify jobNotify = new CronJobNotify();
        jobNotify.setTriggerInfo(triggerInfo);
        jobNotify.setBroker(broker);
        jobNotify.setWorkerInfo(workerInfo);
        jobNotify.setDiscoveryService(discoveryService);
        return jobNotify;
    }


}
