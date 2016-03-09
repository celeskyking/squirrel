package com.github.celeskyking.squirrel.job;

import com.github.celeskyking.squirrel.trigger.TriggerInfo;
import org.springframework.context.ApplicationContext;

/**
 * Created by tianqing.wang
 * DATE : 16/3/3
 * TIME : 下午5:15
 * PROJECT : squirrel
 * PACKAGE : com.github.celeskyking.squirrel.job
 *
 * @author <a href="mailto:celeskyking@163.com">tianqing.wang</a>
 */
public class SpringJobExecutorBuilder implements IJobExecutorBuilder {

    private ApplicationContext applicationContext;


    public SpringJobExecutorBuilder(ApplicationContext applicationContext) throws Throwable {
        this.applicationContext = applicationContext;
    }

    /**
     * 只支持名称注入,不支持类型注入
     * */
    @Override
    public IJobExecutor build(TriggerInfo triggerInfo) {
        return (IJobExecutor) applicationContext.getBean(triggerInfo.getTarget());
    }
}
