package com.github.celeskyking.squirrel.job;

import com.alibaba.fastjson.JSON;
import com.github.celeskyking.squirrel.trigger.TriggerInfo;
import com.github.celeskyking.squirrel.trigger.TriggerState;
import com.google.common.collect.Maps;
import com.github.celeskyking.squirrel.annotation.Job;
import com.github.celeskyking.squirrel.broker.IBroker;
import com.github.celeskyking.squirrel.discovery.DiscoveryService;
import com.github.celeskyking.squirrel.helper.NetHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * Created by tianqing.wang
 * DATE : 16-2-19
 * TIME : 下午4:52
 * PROJECT : squirrel
 * PACKAGE : com.qunar.squirrel.job
 *
 * @author <a href="mailto:celeskyking@163.com">tianqing.wang</a>
 */
public class Jobber implements IJobber{

    private Logger logger = LoggerFactory.getLogger(Jobber.class);

    private String name;

    private DiscoveryService discoveryService;

    private IBroker broker;

    private String worker;

    private JobListener listener = new JobListener();

    private JobberInfo jobberInfo = new JobberInfo();

    private Map<String,TriggerInfo> triggerInfoMap = Maps.newConcurrentMap();

    private JobContext jobContext = new JobContext();

    private IJobExecutorBuilder jobExecutorBuilder ;

    private Map<String,IJobExecutor> jobExecutorMap = Maps.newHashMap();

    public Jobber(String name,
                 String worker,
                 DiscoveryService discoveryService,
                 IBroker broker,IJobExecutorBuilder jobExecutorBuilder) throws Throwable {
        this.name = name;
        this.worker = worker;
        this.discoveryService = discoveryService;
        this.broker = broker;
        this.jobberInfo.setName(name);
        this.jobberInfo.setRemoteAddress(NetHelper.getLocalIp());
        this.jobberInfo.setWorker(worker);
        this.jobExecutorBuilder = jobExecutorBuilder;
    }


    @Override
    public synchronized boolean exist(String jobName) {
        return triggerInfoMap.containsKey(jobName);
    }

    @Override
    public synchronized void execute(String jobName,JobContext jobContext) throws Throwable {
        TriggerInfo triggerInfo  = triggerInfoMap.get(jobName);
        if(triggerInfo!=null){
            if(jobExecutorMap.containsKey(triggerInfo.getTarget())){
                jobExecutorMap.get(triggerInfo.getTarget()).execute(jobContext);
            }else{
                IJobExecutor executor = jobExecutorBuilder.build(triggerInfo);
                if(executor!=null){
                    this.jobExecutorMap.put(triggerInfo.getTarget(),executor);
                    executor.execute(jobContext);
                }else{
                    this.remove(triggerInfo.getName());
                }
            }
        }
    }

    //启动的时候默认去加载@Job的任务
    //发布Annotation的任务
    public void publish(Object object) throws Throwable {
        Class<?> clazz = object.getClass();
        Method[] methods = clazz.getMethods();
        for(Method method : methods){
            if(method.isAnnotationPresent(Job.class)){
                Job job = method.getAnnotation(Job.class);
                String jobName = job.name();
                String description = job.description();
                String cronExpress = job.cronExpress();
                AnnotationJobExecutor jobExecutor = new AnnotationJobExecutor(method,clazz.newInstance());
                JobInfo jobInfo = new JobInfo();
                jobInfo.setName(jobName);
                jobInfo.setDescription(description);
                jobInfo.setCronExpress(cronExpress);
                this.publish(jobInfo);
                jobExecutorMap.put(jobName,jobExecutor);
            }
        }
    }


    @Override
    public void start() throws Throwable {
        logger.info("启动jobber中...");
        boolean result = discoveryService.register(jobberInfo);
        if(result){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        discoveryService.watchingTriggers(worker, name, 10,new ITriggerInfoCallback() {
                            @Override
                            public void ok(List<TriggerInfo> triggerInfos) {
                                logger.info("接收到trigger的变化:{}",JSON.toJSONString(triggerInfos));
                                synchronized (Jobber.this){
                                    triggerInfoMap.clear();
                                    for(TriggerInfo triggerInfo: triggerInfos){
                                        if(!TriggerState.STOPPING.getDesc().equals(triggerInfo.getState())){
                                            logger.info("接收到了:{}",JSON.toJSONString(triggerInfo));
                                            triggerInfoMap.put(triggerInfo.getName(),triggerInfo);
                                        }
                                    }
                                }
                            }
                            @Override
                            public void notOk(Throwable throwable) {
                                logger.error("",throwable);
                            }
                        });
                    } catch (Throwable throwable) {
                        logger.error("",throwable);
                    }
                }
            }).start();
            broker.startConsumeTriggering(this,this.listener);
        }else{
            throw new Exception("jobber注册失败:"+ JSON.toJSONString(jobberInfo));
        }
    }

    @Override
    public void stop() throws Throwable{
        discoveryService.stopWatchingTriggers();
        broker.stopConsumeTriggering();
    }


    @Override
    public synchronized void updateJob(String jobName, String cronExpress) throws Throwable {
        if(exist(jobName)){
            TriggerInfo triggerInfo = triggerInfoMap.get(jobName);
            triggerInfo.setCronExpress(cronExpress);
            triggerInfo.setState(TriggerState.UPDATE.getDesc());
            broker.publish(triggerInfo);
        }

    }

    @Override
    public void publish(JobInfo jobInfo) throws Throwable {
        logger.info("注册job:{},desc:{},cron:{}",jobInfo.getName(),jobInfo.getDescription(),jobInfo.getCronExpress());
        this.broker.publish(buildTriggerInfo(jobInfo,TriggerState.RUNNING));
    }


    @Override
    public synchronized void remove(String jobName) throws Throwable {
        if(exist(jobName)){
            TriggerInfo triggerInfo = triggerInfoMap.get(jobName);
            triggerInfo.setState(TriggerState.STOPPING.getDesc());
            this.broker.publish(triggerInfoMap.get(jobName));
        }

    }

    @Override
    public synchronized void pause(String jobName) throws Throwable{
        if(exist(jobName)){
            TriggerInfo triggerInfo = triggerInfoMap.get(jobName);
            if(TriggerState.RUNNING.getDesc().equals(triggerInfo.getState())){
                triggerInfo.setState(TriggerState.PAUSING.getDesc());
                this.broker.publish(triggerInfoMap.get(jobName));
            }

        }
    }

    @Override
    public synchronized void resume(String jobName) throws Throwable {
        if(exist(jobName)){
            TriggerInfo triggerInfo = triggerInfoMap.get(jobName);
            if(TriggerState.PAUSING.getDesc().equals(triggerInfo.getState())){
                triggerInfo.setState(TriggerState.RESUME.getDesc());
                this.broker.publish(triggerInfoMap.get(jobName));
            }
        }
    }

    @Override
    public void addListener(JobNotifyListener listener) {
        this.listener.addListener(listener);
    }

    @Override
    public JobContext getJobContext() {
        return jobContext;
    }


    private TriggerInfo buildTriggerInfo(JobInfo job, TriggerState state){
        TriggerInfo triggerInfo = new TriggerInfo();
        triggerInfo.setCaller(getName());
        triggerInfo.setWorker(getWorker());
        triggerInfo.setRemoteAddress(NetHelper.getLocalIp());
        triggerInfo.setCronExpress(job.getCronExpress());
        triggerInfo.setName(job.getName());
        triggerInfo.setDescription(job.getDescription());
        triggerInfo.setState(state.getDesc());
        triggerInfo.setTarget(job.getTarget());
        triggerInfo.setJobDataMap(job.getJobDataMap());
        return triggerInfo;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DiscoveryService getDiscoveryService() {
        return discoveryService;
    }

    public void setDiscoveryService(DiscoveryService discoveryService) {
        this.discoveryService = discoveryService;
    }

    public IBroker getBroker() {
        return broker;
    }

    public void setBroker(IBroker broker) {
        this.broker = broker;
    }

    public String getWorker() {
        return worker;
    }

    public void setWorker(String worker) {
        this.worker = worker;
    }


    public void addJobContext(String key,Object value){
        this.jobContext.addContext(key,value);
    }
}
