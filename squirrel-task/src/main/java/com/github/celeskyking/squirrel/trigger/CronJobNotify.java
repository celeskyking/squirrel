package com.github.celeskyking.squirrel.trigger;

import com.github.celeskyking.squirrel.job.JobberInfo;
import com.github.celeskyking.squirrel.worker.WorkerInfo;
import com.github.celeskyking.squirrel.broker.IBroker;
import com.github.celeskyking.squirrel.discovery.DiscoveryService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.List;

/**
 * Created by tianqing.wang
 * DATE : 16-2-19
 * TIME : 下午1:39
 * PROJECT : squirrel
 * PACKAGE : com.github.celeskyking.squirrel.tigger
 *
 * @author <a href="mailto:celeskyking@163.com">tianqing.wang</a>
 */
public class CronJobNotify implements Job {

    private IBroker broker;

    private WorkerInfo workerInfo;

    private TriggerInfo triggerInfo;

    private DiscoveryService discoveryService;


    public CronJobNotify(){

    }


    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        try {
            this.workerInfo = (WorkerInfo) context.getMergedJobDataMap().get("$worker");
            this.broker = (IBroker) context.getMergedJobDataMap().get("$broker");
            this.discoveryService = (DiscoveryService) context.getMergedJobDataMap().get("$discovery");
            this.triggerInfo = (TriggerInfo) context.getMergedJobDataMap().get("$trigger");
            if(!workerInfo.isTriggerForced()){
                List<JobberInfo> jobberInfos = discoveryService.getJobbers(workerInfo.getWorkerName(),triggerInfo.getCaller());
                if(jobberInfos!=null&&jobberInfos.size()>0){
                    broker.trigger(workerInfo,triggerInfo);
                }
            }else{
                this.broker.trigger(workerInfo,triggerInfo);
            }
        } catch (Throwable throwable) {
            throw new JobExecutionException(throwable);
        }
    }

    public String getJobName(){
        return this.triggerInfo.getName();
    }

    public String getJobGroup(){
        return this.triggerInfo.getCaller();
    }

    public String getCronExpress(){
        return this.triggerInfo.getCronExpress();
    }

    public String getWorker(){
        return workerInfo.getWorkerName();
    }

    public String getCaller(){
        return this.triggerInfo.getCaller();
    }

    public TriggerInfo getTriggerInfo() {
        return triggerInfo;
    }

    public void setTriggerInfo(TriggerInfo triggerInfo) {
        this.triggerInfo = triggerInfo;
    }

    public IBroker getBroker() {
        return broker;
    }

    public void setBroker(IBroker broker) {
        this.broker = broker;
    }

    public WorkerInfo getWorkerInfo() {
        return workerInfo;
    }

    public void setWorkerInfo(WorkerInfo workerInfo) {
        this.workerInfo = workerInfo;
    }

    public DiscoveryService getDiscoveryService() {
        return discoveryService;
    }

    public void setDiscoveryService(DiscoveryService discoveryService) {
        this.discoveryService = discoveryService;
    }
}
