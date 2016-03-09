package com.github.celeskyking.squirrel.trigger;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * Created by tianqing.wang
 * DATE : 16-2-19
 * TIME : 下午4:07
 * PROJECT : squirrel
 * PACKAGE : com.qunar.squirrel.tigger
 *
 * @author <a href="mailto:celeskyking@163.com">tianqing.wang</a>
 */
public class TriggerListener {

    private List<TriggerEventListener> listeners = Lists.newArrayList();

    public void addListener(TriggerEventListener triggerEventListener){
        this.listeners.add(triggerEventListener);
    }

    public void fireTriggerSuccess(TriggerInfo triggerInfo){
        for(TriggerEventListener eventListener : listeners){
            eventListener.fireTriggerSuccess(triggerInfo);
        }
    }


    public void fireTriggerFailure(Throwable throwable,TriggerInfo triggerInfo){
        for(TriggerEventListener eventListener : listeners){
            eventListener.fireTriggerFailure(throwable,triggerInfo);
        }
    }

}
