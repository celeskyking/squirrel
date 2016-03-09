package com.github.celeskyking.squirrel.caller;

import com.alibaba.fastjson.JSON;
import com.github.celeskyking.squirrel.Response;
import com.github.celeskyking.squirrel.resultbackend.IResultBackend;
import com.github.celeskyking.squirrel.resultbackend.MonitorResultEventListener;
import com.github.celeskyking.squirrel.resultbackend.ResultEventListener;
import com.github.celeskyking.squirrel.task.Task;
import com.github.celeskyking.squirrel.task.TaskArgDescription;
import com.github.celeskyking.squirrel.task.TaskDescription;
import com.github.celeskyking.squirrel.task.TaskSignature;
import com.google.common.collect.Maps;
import com.github.celeskyking.squirrel.broker.IBroker;
import com.github.celeskyking.squirrel.discovery.DiscoveryService;
import com.github.celeskyking.squirrel.exception.ArgTypeNotMatchException;
import com.github.celeskyking.squirrel.helper.NetHelper;
import com.github.celeskyking.squirrel.helper.TaskHelper;
import com.github.celeskyking.squirrel.monitor.IMonitor;
import com.github.celeskyking.squirrel.resultbackend.DefaultResultEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * Created by tianqing.wang
 * DATE : 16/1/28
 * TIME : 上午10:26
 * PROJECT : squirrel
 * PACKAGE : com.qunar.squirrel.client
 *
 * @author <a href="mailto:celeskyking@163.com">tianqing.wang</a>
 */
public class DefaultCaller implements ICaller {

    private Logger logger = LoggerFactory.getLogger(DefaultCaller.class);

    private IBroker broker;

    private IResultBackend resultBackend;

    private Map<String,ICallback> callbackMap = Maps.newHashMap();

    private DiscoveryService discoveryService;

    private String worker;

    private boolean distributeLock = false;

    private boolean needTaskExist = true;

    private CallerInfo callerInfo = new CallerInfo();

    private IMonitor monitor;


    private ICallerEventHandler eventHandler;


    private ICallback deathCallback = new ICallback() {
        @Override
        public void handle(TaskSignature taskSignature) {
            logger.info("接收到任务执行回调,该回调只打印日志,task:{}",JSON.toJSONString(taskSignature));
        }
    };

    //caller的名称,每一个caller都含有名称,作为连接worker的通道
    private String name;


    public DefaultCaller(String name,String worker,IBroker broker,
                         IResultBackend resultBackend,
                         DiscoveryService discoveryService){
        this(name,worker,broker,resultBackend,discoveryService,null);
    }

    public DefaultCaller(String name, String worker,IBroker broker,
                         IResultBackend resultBackend,
                         DiscoveryService discoveryService,
                         IMonitor monitor){
        this.name = name;
        this.worker = worker;
        this.broker = broker;
        this.resultBackend = resultBackend;
        this.discoveryService = discoveryService;
        this.monitor = monitor;
        if(monitor!=null){
            addEventListener(new MonitorResultEventListener(monitor));
        }
        addEventListener(new DefaultResultEventListener() {

            @Override
            public void fireTaskSuccess(TaskSignature taskSignature) {
                fireFinished(taskSignature);
            }
            @Override
            public void fireTaskFailure(TaskSignature taskSignature) {
                fireFinished(taskSignature);
            }
            private void fireFinished(TaskSignature taskSignature){
                String taskName = taskSignature.getTask().getTaskName();
                if(callbackMap.containsKey(taskName)){
                    callbackMap.get(taskName).handle(taskSignature);
                }else{
                    deathCallback.handle(taskSignature);
                }
            }
        });
        this.callerInfo.setName(name);
        this.callerInfo.setRemoteAddress(NetHelper.getLocalIp());
        this.callerInfo.setWorker(worker);
    }

    public boolean distributeLock() {
        return distributeLock;
    }

    public void distributeLock(boolean distributeLock) {
        this.distributeLock = distributeLock;
    }

    public Response sendTask(Task task) throws ArgTypeNotMatchException {
        logger.info("发送task:{}", JSON.toJSONString(task));
        Response response = new Response();
        if(needTaskExist){
            boolean exist = taskExist(task.getTaskName());
            if(exist){
                checkArgs(task);
            }else{
                logger.info("task不存在,可能已经被下线");
                response.setOk(false);
                return response;
            }
        }
        TaskSignature taskSignature = TaskHelper.build(this,task);
        try {
            broker.publish(taskSignature);
            response.setOk(true);
            resultBackend.setStatePending(taskSignature);
        } catch (Throwable throwable) {
            logger.error("",throwable);
            response.setOk(false);
        }
        return response;
    }

    public boolean taskExist(String taskName){
        return discoveryService.getTasks(worker,taskName).size()>0;
    }

    private void checkArgs(Task task) throws ArgTypeNotMatchException {
        TaskDescription taskDescription = discoveryService.getTasks(worker,task.getTaskName()).get(0);
        List<TaskArgDescription> args = taskDescription.getArgDescriptions();
        if(taskDescription.getArgDescriptions().size() == task.getTaskArgs().size()){
            for(int i=0;i<args.size();i++){
                if(!args.get(i).getArgType().equals(task.getTaskArgs().get(i).getType())){
                    throw new ArgTypeNotMatchException("参数类型不匹配,本地args:"+JSON.toJSONString(task.getTaskArgs())+",远程args:"+JSON.toJSONString(taskDescription));
                }
            }
        }else{
            throw new ArgTypeNotMatchException("参数个数不匹配,本地args:"+JSON.toJSONString(task.getTaskArgs())+",远程args:"+JSON.toJSONString(taskDescription));
        }
    }

    @Override
    public void start() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if(discoveryService.register(callerInfo)){
                    if(distributeLock()) {
                        discoveryService.distributeLock(DefaultCaller.this);
                    }
                }
                eventHandler.handle();
            }
        }).start();

        resultBackend.startConsumer(name);
    }

    @Override
    public void stop() {
        resultBackend.stopConsumer();
    }


    public void addResultCallback(String taskName, ICallback callback){
        this.callbackMap.put(taskName,callback);
    }

    public void withStartedCallback(final ICallerEventHandler handler){
        this.eventHandler = handler;
    }

    public List<TaskDescription> getTasks() {
        return discoveryService.getTasks(worker);
    }

    @Override
    public String getWorker() {
        return worker;
    }

    public CallerInfo callerInfo() {
        return callerInfo;
    }

    @Override
    public void monitor(IMonitor monitor) {
        this.monitor = monitor;
        if(monitor!=null){
            addEventListener(new MonitorResultEventListener(monitor));
        }
    }

    public void needTaskExist(boolean exist) {
        this.needTaskExist = exist;
    }

    @Override
    public void addEventListener(ResultEventListener listener) {
        resultBackend.register(listener);
    }

    @Override
    public IBroker getBroker() {
        return broker;
    }

    @Override
    public String getName() {
        return name;
    }

}
