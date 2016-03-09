package com.github.celeskyking.squirrel.job;

import com.alibaba.fastjson.JSON;
import com.github.celeskyking.squirrel.trigger.TriggerInfo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by tianqing.wang
 * DATE : 16/2/29
 * TIME : 下午1:33
 * PROJECT : squirrel
 * PACKAGE : com.github.celeskyking.squirrel.job
 *
 * @author <a href="mailto:celeskyking@163.com">tianqing.wang</a>
 */
public class DefaultJobExecutorBuilder implements IJobExecutorBuilder{


    private Logger logger = LoggerFactory.getLogger(DefaultJobExecutorBuilder.class);


    @Override
    @SuppressWarnings("unchecked")
    public IJobExecutor build(TriggerInfo triggerInfo) {
        try{
            String target = triggerInfo.getTarget();
            if(StringUtils.isNotEmpty(target)){
                if(StringUtils.contains(target,"$")){
                    logger.info("不支持内部类的形式:{}",target);
                    return null;
                }
                Class<? extends IJobExecutor> clazz = (Class<? extends IJobExecutor>) Class.forName(triggerInfo.getTarget());
                if(clazz == null){
                    logger.warn("找不到处理类,该定时任务可能已经被删除,暂停该job的注册");
                    return null;
                }
                return clazz.newInstance();
            }else{
                logger.info("丢弃消息:{},找不到定时任务.下线定时任务:{}", JSON.toJSONString(triggerInfo),triggerInfo.getName());
                return null;
            }
        }catch (Exception exception){
            logger.error("丢弃消息",exception);
            return null;
        }
    }
}
