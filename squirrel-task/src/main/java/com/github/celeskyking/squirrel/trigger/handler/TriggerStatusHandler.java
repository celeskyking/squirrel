package com.github.celeskyking.squirrel.trigger.handler;

import com.github.brainlag.nsq.NSQMessage;
import com.github.celeskyking.squirrel.discovery.DiscoveryService;
import com.github.celeskyking.squirrel.trigger.JobTrigger;
import com.github.celeskyking.squirrel.trigger.TriggerInfo;

/**
 * Created by tianqing.wang
 * DATE : 16-2-23
 * TIME : 上午11:59
 * PROJECT : squirrel
 * PACKAGE : com.github.celeskyking.squirrel.trigger.handler
 *
 * @author <a href="mailto:celeskyking@163.com">tianqing.wang</a>
 */
public interface TriggerStatusHandler {

    void handle(NSQMessage message, TriggerInfo triggerInfo, DiscoveryService discoveryService, JobTrigger jobTrigger);

}
