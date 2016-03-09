package com.github.celeskyking.squirrel.job;

import com.github.celeskyking.squirrel.trigger.TriggerInfo;

import java.util.List;

/**
 * Created by tianqing.wang
 * DATE : 16/2/29
 * TIME : 下午2:57
 * PROJECT : squirrel
 * PACKAGE : com.github.celeskyking.squirrel.job
 *
 * @author <a href="mailto:celeskyking@163.com">tianqing.wang</a>
 */
public interface ITriggerInfoCallback {


    void ok(List<TriggerInfo> triggerInfos);

    void notOk(Throwable throwable);

}
