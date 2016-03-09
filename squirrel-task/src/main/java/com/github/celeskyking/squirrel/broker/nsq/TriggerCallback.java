package com.github.celeskyking.squirrel.broker.nsq;

import com.alibaba.fastjson.JSON;
import com.github.brainlag.nsq.NSQMessage;
import com.github.brainlag.nsq.callbacks.NSQMessageCallback;
import com.github.celeskyking.squirrel.broker.IBroker;
import com.github.celeskyking.squirrel.discovery.DiscoveryService;
import com.github.celeskyking.squirrel.trigger.JobTrigger;
import com.github.celeskyking.squirrel.trigger.TriggerInfo;
import com.github.celeskyking.squirrel.trigger.handler.TriggerStatusHandlers;
import com.github.celeskyking.squirrel.worker.WorkerInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by tianqing.wang
 * DATE : 16-2-19
 * TIME : 下午1:34
 * PROJECT : squirrel
 * PACKAGE : com.github.celeskyking.squirrel.broker.nsq
 *
 * @author <a href="mailto:celeskyking@163.com">tianqing.wang</a>
 */
public class TriggerCallback implements NSQMessageCallback {


    private TriggerStatusHandlers triggerStatusHandlers;

    private Logger logger = LoggerFactory.getLogger(TriggerCallback.class);

    public TriggerCallback(DiscoveryService discoveryService,
                           IBroker broker,
                           WorkerInfo workerInfo){
        JobTrigger jobTrigger = new JobTrigger(broker, discoveryService, workerInfo);
        this.triggerStatusHandlers = new TriggerStatusHandlers(discoveryService, jobTrigger,workerInfo,broker);
    }

    @Override
    public void message(NSQMessage message) {
        try{
            TriggerInfo triggerInfo = JSON.parseObject(message.getMessage(),TriggerInfo.class);
            this.triggerStatusHandlers.handle(message,triggerInfo);
        }catch (Exception e){
            logger.error("",e);
            message.requeue(1000);
        }
    }

}
