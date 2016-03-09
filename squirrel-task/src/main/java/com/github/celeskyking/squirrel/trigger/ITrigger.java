package com.github.celeskyking.squirrel.trigger;

/**
 * Created by tianqing.wang
 * DATE : 16-2-19
 * TIME : 上午10:24
 * PROJECT : squirrel
 * PACKAGE : com.github.celeskyking.squirrel.tigger
 *
 * @author <a href="mailto:celeskyking@163.com">tianqing.wang</a>
 */
public interface ITrigger {

    void addJob(CronJobNotify jobNotify);

    void modifyJob(CronJobNotify jobNotify);

    void removeJob(CronJobNotify jobNotify);

}
