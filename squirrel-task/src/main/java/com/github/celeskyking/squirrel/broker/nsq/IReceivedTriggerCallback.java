package com.github.celeskyking.squirrel.broker.nsq;

import com.alibaba.fastjson.JSON;
import com.github.brainlag.nsq.NSQMessage;
import com.github.brainlag.nsq.callbacks.NSQMessageCallback;
import com.github.celeskyking.squirrel.job.IJobber;
import com.github.celeskyking.squirrel.job.JobContext;
import com.github.celeskyking.squirrel.job.JobListener;
import com.github.celeskyking.squirrel.trigger.TriggerInfo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * Created by tianqing.wang
 * DATE : 16-2-19
 * TIME : 下午4:28
 * PROJECT : squirrel
 * PACKAGE : com.github.celeskyking.squirrel.tigger
 *
 * @author <a href="mailto:celeskyking@163.com">tianqing.wang</a>
 */
public class IReceivedTriggerCallback implements NSQMessageCallback {

    private Logger logger = LoggerFactory.getLogger(IReceivedTriggerCallback.class);

    private IJobber jobber;

    private JobListener jobListener;

    public IReceivedTriggerCallback(IJobber jobber, JobListener jobListener){
        this.jobber = jobber;
        this.jobListener = jobListener;
    }

    @Override
    public void message(NSQMessage message) {
        try{
            TriggerInfo triggerInfo = JSON.parseObject(message.getMessage(),TriggerInfo.class);
            triggerInfo.setReceivedTime(new Date());
            jobListener.onReceive(triggerInfo);
            logger.info("接收到定时任务的触发,trigger:{}",JSON.toJSONString(triggerInfo));
            if(jobber.exist(triggerInfo.getName())&& StringUtils.equals(triggerInfo.getCaller(),jobber.getName())){
                JobContext jobContext = new JobContext(jobber.getJobContext());
                jobContext.setJobDataMap(triggerInfo.getJobDataMap());
                jobber.execute(triggerInfo.getName(),jobContext);
            }
            message.finished();
        }catch (Throwable throwable){
            logger.error("",throwable);
            message.requeue(1000);
        }
    }


}
