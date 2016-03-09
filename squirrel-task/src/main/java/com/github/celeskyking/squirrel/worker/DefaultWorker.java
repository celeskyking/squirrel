package com.github.celeskyking.squirrel.worker;

import com.alibaba.fastjson.JSON;
import com.github.celeskyking.squirrel.exception.RouteKeyNotMatchException;
import com.github.celeskyking.squirrel.exception.TaskNotExistException;
import com.github.celeskyking.squirrel.exception.TaskNotRunnableException;
import com.github.celeskyking.squirrel.helper.NetHelper;
import com.github.celeskyking.squirrel.resultbackend.IResultBackend;
import com.github.celeskyking.squirrel.task.TaskSignature;
import com.github.celeskyking.squirrel.task.executor.ITaskExecutor;
import com.github.celeskyking.squirrel.task.processor.ITaskProcessor;
import com.github.celeskyking.squirrel.broker.IBroker;
import com.github.celeskyking.squirrel.discovery.DiscoveryService;
import com.github.celeskyking.squirrel.task.TaskContext;
import com.github.celeskyking.squirrel.task.processor.TaskRouter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by tianqing.wang
 * DATE : 16/2/2
 * TIME : 下午4:30
 * PROJECT : squirrel
 * PACKAGE : com.qunar.squirrel.worker
 *
 * @author <a href="mailto:celeskyking@163.com">tianqing.wang</a>
 */
public class DefaultWorker implements IWorker {

    private Logger logger = LoggerFactory.getLogger(DefaultWorker.class);

    private String name;

    private IBroker broker;

    private IResultBackend resultBackend;

    private TaskContext taskContext;

    private ITaskProcessor taskProcessor;

    private DiscoveryService discoveryService;

    private WorkerInfo workerInfo = new WorkerInfo();


    /**
     * 是否在线上
     * */
    private boolean online = false;


    public DefaultWorker(String name,IBroker broker,
                         IResultBackend resultBackend,
                         TaskContext taskContext,
                         DiscoveryService discoveryService){
        this.name = name;
        this.discoveryService = discoveryService;
        this.taskContext = taskContext;
        this.broker = broker;
        this.resultBackend = resultBackend;
        this.taskProcessor = new TaskRouter(broker,resultBackend,taskContext);
        workerInfo.setRemoteAddress(NetHelper.getLocalIp());
        workerInfo.setWorkerName(name);
        workerInfo.setTaskDescriptions(this.taskContext.getTaskDescriptions());
        workerInfo.setHost(NetHelper.getHost());
        workerInfo.setTriggerForced(false);
    }


    public void launch(boolean trigger) throws Throwable {
        logger.error("启动worker... info:{}", JSON.toJSONString(workerInfo));
        online();
        if(trigger){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    logger.info("worker启动成功,启动trigger系统...");
                    broker.onReceiveTriggerRegistry(workerInfo,discoveryService);
                }
            }).start();
        }
        this.broker.startConsuming(workerInfo,this);
    }


    @Override
    public void deRegister(String taskName) {
        this.discoveryService.deRegister(taskName,workerInfo);
    }

    @Override
    public void register(String taskName) {
        this.discoveryService.register(taskName,workerInfo);
    }

    @Override
    public void offline() {
        if(online){
            this.online = !this.discoveryService.offline(this.workerInfo);
            this.workerInfo.setOnline(online);
        }else{
            logger.error("不在上线状态,不能够下线worker");
        }
    }

    @Override
    public void online() {
        if(!online){
            this.online = this.discoveryService.online(workerInfo);
            this.workerInfo.setOnline(online);
        }
    }

    public void quit() {
        this.discoveryService.offline(workerInfo);
        this.broker.stopConsuming();
    }

    public void process(TaskSignature taskSignature) throws TaskNotExistException, TaskNotRunnableException, RouteKeyNotMatchException {
        if(!StringUtils.equals(taskSignature.getWorker(),getName())){
            throw new RouteKeyNotMatchException("不匹配的worker");
        }
        if(!taskContext.contains(taskSignature.getTask().getTaskName())){
            throw new TaskNotExistException("任务不存在,task:"+taskSignature.getTask().getTaskName());
        }
        if(filter(taskSignature)){
            this.taskProcessor.process(taskSignature);
        }else{
            throw new TaskNotRunnableException("不符合执行条件,task:"+taskSignature.toString());
        }

    }

    @Override
    public boolean status() {
        return online;
    }

    @Override
    public void addTask(String name, ITaskExecutor executor) {
        this.taskContext.addTask(name,executor);
    }

    @Override
    public void triggerForced(boolean forced) {
        this.workerInfo.setTriggerForced(forced);
    }


    public boolean filter(TaskSignature taskSignature){
        //用来过滤请求的任务.比如下线所有的任务执行,或者停掉一些不需要被执行的任务.
        return true;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public IBroker getBroker() {
        return broker;
    }

    public void setBroker(IBroker broker) {
        this.broker = broker;
    }

    public IResultBackend getResultBackend() {
        return resultBackend;
    }

    public void setResultBackend(IResultBackend resultBackend) {
        this.resultBackend = resultBackend;
    }

    public TaskContext getTaskContext() {
        return taskContext;
    }

    public void setTaskContext(TaskContext taskContext) {
        this.taskContext = taskContext;
    }

}
