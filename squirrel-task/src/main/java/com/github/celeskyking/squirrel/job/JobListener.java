package com.github.celeskyking.squirrel.job;

import com.google.common.collect.Lists;
import com.github.celeskyking.squirrel.trigger.TriggerInfo;

import java.util.List;

/**
 * Created by tianqing.wang
 * DATE : 16-2-22
 * TIME : 下午2:19
 * PROJECT : squirrel
 * PACKAGE : com.qunar.squirrel.job
 *
 * @author <a href="mailto:celeskyking@163.com">tianqing.wang</a>
 */
public class JobListener {

    private List<JobNotifyListener> listeners = Lists.newArrayList();

    public void addListener(JobNotifyListener listener){
        this.listeners.add(listener);
    }

    public void onReceive(TriggerInfo trigger){
        for(JobNotifyListener listener : listeners){
            listener.fireNotify(trigger);
        }
    }

}
