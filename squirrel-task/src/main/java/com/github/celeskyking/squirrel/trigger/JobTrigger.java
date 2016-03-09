package com.github.celeskyking.squirrel.trigger;

import com.github.celeskyking.squirrel.broker.IBroker;
import com.github.celeskyking.squirrel.exception.TriggerException;
import com.github.celeskyking.squirrel.worker.WorkerInfo;
import com.google.common.collect.ImmutableMap;
import com.github.celeskyking.squirrel.discovery.DiscoveryService;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.triggers.CronTriggerImpl;

/**
 * Created by tianqing.wang
 * DATE : 16-2-19
 * TIME : 上午10:24
 * PROJECT : squirrel
 * PACKAGE : com.github.celeskyking.squirrel.tigger
 *
 * @author <a href="mailto:celeskyking@163.com">tianqing.wang</a>
 */
public class JobTrigger implements ITrigger {

    private Scheduler  scheduler;

    private IBroker broker;

    private WorkerInfo workerInfo;

    private DiscoveryService discoveryService;

    public JobTrigger(IBroker broker, DiscoveryService discoveryService,WorkerInfo workerInfo){
        try {
            this.broker = broker;
            this.workerInfo = workerInfo;
            this.discoveryService = discoveryService;
            this.scheduler = StdSchedulerFactory.getDefaultScheduler();
            scheduler.start();
        } catch (SchedulerException e) {
            throw new TriggerException(e);
        }
    }

    public synchronized void addJob(CronJobNotify jobNotify){
        try {
            boolean exist = this.scheduler.checkExists(JobKey.jobKey(jobNotify.getJobName(),jobNotify.getJobGroup()));
            if(exist){
                modifyJob(jobNotify);
            }else{
                JobDetail detail = buildJobDetail(jobNotify);
                CronExpression expression = new CronExpression(jobNotify.getCronExpress());
                Trigger trigger = TriggerBuilder.newTrigger()
                        .withIdentity(jobNotify.getJobName(),jobNotify.getJobGroup())
                        .withSchedule(CronScheduleBuilder.cronSchedule(expression))
                        .build();
                this.scheduler.scheduleJob(detail,trigger);
            }
        } catch (Throwable e) {
            throw new TriggerException(e);
        }
    }

    public synchronized void modifyJob(CronJobNotify jobNotify) {
        try{
            CronExpression cronExpression = new CronExpression(jobNotify.getCronExpress());
            TriggerKey triggerKey = TriggerKey.triggerKey(jobNotify.getCaller(),jobNotify.getWorker());
            Trigger trigger = scheduler.getTrigger(triggerKey);
            if(trigger!=null){
                CronTriggerImpl cronTrigger = (CronTriggerImpl) trigger;
                cronTrigger.setCronExpression(cronExpression);
                //cronTrigger.setJobDataMap(buildJobDataMap(jobNotify));
                scheduler.resumeTrigger(triggerKey);
            }
        }catch (Throwable e){
            throw new TriggerException(e);
        }
    }

    public synchronized void pauseJob(CronJobNotify jobNotify){
        try{
            JobKey jobKey = JobKey.jobKey(jobNotify.getJobName(),jobNotify.getJobGroup());
            this.scheduler.pauseJob(jobKey);
        }catch (Throwable throwable){
            throw new TriggerException(throwable);
        }
    }

    public synchronized void resumeJob(CronJobNotify jobNotify){
        try{
            JobKey jobKey = JobKey.jobKey(jobNotify.getJobName(),jobNotify.getJobGroup());
            this.scheduler.resumeJob(jobKey);
        }catch (Throwable throwable){
            throw new TriggerException(throwable);
        }
    }

    public synchronized void removeJob(CronJobNotify jobNotify) {
        try{
            TriggerKey triggerKey = TriggerKey.triggerKey(jobNotify.getCaller(),jobNotify.getWorker());
            this.scheduler.pauseTrigger(triggerKey);
            this.scheduler.unscheduleJob(triggerKey);
            this.scheduler.deleteJob(JobKey.jobKey(jobNotify.getJobName(),jobNotify.getCaller()));
        }catch (Throwable throwable){
            throw new TriggerException(throwable);
        }

    }

    public boolean containsJob(CronJobNotify cronJobNotify){
        try {
            JobKey jobKey = JobKey.jobKey(cronJobNotify.getJobName(),cronJobNotify.getJobGroup());
            if(scheduler.checkExists(jobKey)){
                return true;
            }else{
                return false;
            }
        } catch (SchedulerException e) {
            throw new TriggerException(e);
        }
    }


    private JobDetail buildJobDetail(final CronJobNotify jobNotify){
        return JobBuilder.newJob().withIdentity(jobNotify.getJobName(),jobNotify.getJobGroup())
                .setJobData(new JobDataMap(ImmutableMap.of("$broker", broker,
                        "$worker", workerInfo,
                        "$trigger",jobNotify.getTriggerInfo(),
                        "$discovery",discoveryService)))
                .withDescription(jobNotify.getTriggerInfo().getDescription())
                .ofType(jobNotify.getClass()).build();
    }


    private JobDataMap buildJobDataMap(CronJobNotify cronJobNotify){
        return new JobDataMap(ImmutableMap.of("$broker", broker,
                "$worker", workerInfo,
                "$trigger",cronJobNotify.getTriggerInfo(),
                "$discovery",discoveryService));
    }

}
