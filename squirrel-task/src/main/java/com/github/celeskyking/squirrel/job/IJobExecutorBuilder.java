package com.github.celeskyking.squirrel.job;

import com.github.celeskyking.squirrel.trigger.TriggerInfo;

/**
 * Created by tianqing.wang
 * DATE : 16/2/29
 * TIME : 下午1:26
 * PROJECT : squirrel
 * PACKAGE : com.qunar.squirrel.job
 *
 * @author <a href="mailto:celeskyking@163.com">tianqing.wang</a>
 */
public interface IJobExecutorBuilder {

    IJobExecutor build(TriggerInfo triggerInfo);

}
