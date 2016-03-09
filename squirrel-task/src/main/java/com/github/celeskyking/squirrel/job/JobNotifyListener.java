package com.github.celeskyking.squirrel.job;

import com.github.celeskyking.squirrel.trigger.TriggerInfo;

/**
 * Created by tianqing.wang
 * DATE : 16-2-22
 * TIME : 下午2:18
 * PROJECT : squirrel
 * PACKAGE : com.qunar.squirrel.job
 *
 * @author <a href="mailto:celeskyking@163.com">tianqing.wang</a>
 *
 *  jobber得到通知的时候可以触发该事件
 */
public interface JobNotifyListener {

    void fireNotify(TriggerInfo triggerInfo);
}
