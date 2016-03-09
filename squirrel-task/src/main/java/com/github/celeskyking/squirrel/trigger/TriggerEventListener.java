package com.github.celeskyking.squirrel.trigger;

/**
 * Created by tianqing.wang
 * DATE : 16-2-19
 * TIME : 下午4:04
 * PROJECT : squirrel
 * PACKAGE : com.qunar.squirrel.tigger
 *
 * @author <a href="mailto:celeskyking@163.com">tianqing.wang</a>
 */
public interface TriggerEventListener {


    /**
     * 将trigger的job发送到broker里面的时候触发
     * */
    void fireTriggerSuccess(TriggerInfo triggerInfo);


    void fireTriggerFailure(Throwable throwable,TriggerInfo triggerInfo);

}
