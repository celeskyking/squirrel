package com.github.celeskyking.squirrel.helper;


import com.alibaba.fastjson.JSON;
import com.ecwid.consul.v1.ConsulClient;
import com.ecwid.consul.v1.QueryParams;
import com.ecwid.consul.v1.Response;
import com.ecwid.consul.v1.catalog.model.Node;
import com.ecwid.consul.v1.kv.model.GetValue;
import com.ecwid.consul.v1.kv.model.PutParams;
import com.ecwid.consul.v1.session.model.NewSession;
import com.ecwid.consul.v1.session.model.Session;
import com.github.celeskyking.squirrel.WorkInitException;
import com.github.celeskyking.squirrel.discovery.WatcherSwitch;
import com.github.celeskyking.squirrel.discovery.consul.ConsulConfig;
import com.github.celeskyking.squirrel.discovery.Watcher;
import com.github.celeskyking.squirrel.task.TaskDescription;
import com.github.celeskyking.squirrel.worker.WorkerInfo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by tianqing.wang
 * DATE : 16-2-14
 * TIME : 下午2:57
 * PROJECT : squirrel
 * PACKAGE : com.github.celeskyking.squirrel.helper
 *
 * @author <a href="mailto:celeskyking@163.com">tianqing.wang</a>
 */
public class ConsulHelper {

    private static Logger logger = LoggerFactory.getLogger(ConsulHelper.class);

    public static QueryParams defaultQueryParams(){
        return new QueryParams("dc1");
    }


    public static PutParams defaultPutParams(String sessionId){
        PutParams putParams = new PutParams();
        putParams.setAcquireSession(sessionId);
        return putParams;
    }


    /**
     * 创建consul的session并且持续的线程更新
     * */
    public static String createSessionAndFixedRenew(final ConsulConfig config, final WorkerInfo workerInfo, Session.Behavior behavior){
        final ConsulClient client = new ConsulClient(config.getAddress().getHostText(),config.getAddress().getPortOrDefault(8500));
        final NewSession session = new NewSession();
        session.setBehavior(behavior);
        session.setLockDelay(config.getSessionLockDelay());
        session.setTtl(config.getSessionTTL());
        session.setNode(getLeaderNodeName(config));
        final Response<String> response = client.sessionCreate(session,defaultQueryParams());
        if(response!=null&& StringUtils.isNotEmpty(response.getValue())){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
                    service.scheduleWithFixedDelay(new Runnable() {
                        @Override
                        public void run() {
                            if(workerInfo.isOnline()){
                                client.renewSession(response.getValue(),defaultQueryParams());
                            }
                        }
                    },10,10, TimeUnit.SECONDS);
                }
            }).start();
            return response.getValue();
        }
        return null;
    }


    /**
     * 创建consul的session并且持续的线程更新
     * */
    public static String createSessionAndFixedRenew(final ConsulConfig config, Session.Behavior behavior){
        final ConsulClient client = new ConsulClient(config.getAddress().getHostText(),config.getAddress().getPortOrDefault(8500));
        final NewSession session = new NewSession();
        session.setBehavior(behavior);
        session.setLockDelay(config.getSessionLockDelay());
        session.setTtl(config.getSessionTTL());
        session.setNode(getLeaderNodeName(config));
        final Response<String> response = client.sessionCreate(session,defaultQueryParams());
        if(response!=null&& StringUtils.isNotEmpty(response.getValue())){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
                    service.scheduleWithFixedDelay(new Runnable() {
                        @Override
                        public void run() {
                            client.renewSession(response.getValue(),defaultQueryParams());
                        }
                    },10,10, TimeUnit.SECONDS);
                }
            }).start();
            return response.getValue();
        }
        return null;
    }


    public static ConsulClient newConsulClient(ConsulConfig config){
        return new ConsulClient(config.getAddress().getHostText(),config.getAddress().getPortOrDefault(8500));
    }

    private static String getLeaderNodeName(ConsulConfig config){
        ConsulClient consulClient = newConsulClient(config);
        String leader = consulClient.getStatusLeader().getValue();
        Response<List<Node>> response =  consulClient.getCatalogNodes(defaultQueryParams());
        if(response!=null){
            List<Node> nodes = response.getValue();
            for(Node node : nodes){
                if(leader.startsWith(node.getAddress())){
                    return node.getNode();
                }
            }
        }
        return null;
    }


    public static void registerTasks(String prefix,String sessionId,WorkerInfo workerInfo,ConsulConfig config){
        logger.info("worker注册任务信息:{}",JSON.toJSONString(workerInfo));
        ConsulClient consul = ConsulHelper.newConsulClient(config);
        PutParams putParams = new PutParams();
        putParams.setAcquireSession(sessionId);
        for(TaskDescription taskDescription : workerInfo.getTaskDescriptions()){
            String path = prefix+"tasks/"+workerInfo.getWorkerName()+"/"+taskDescription.getTaskName();
            Response<GetValue> getValueResponse = consul.getKVValue(path,config.getToken());
            if(getValueResponse!=null&&getValueResponse.getValue()!=null){
                consul.deleteKVValue(path,config.getToken());
            }
            Response<Boolean> response = consul.setKVValue(path,JSON.toJSONString(taskDescription),config.getToken(),putParams);
            if(response.getValue()){
                logger.info("注册任务:{}",JSON.toJSONString(taskDescription));
            }else{
                throw new WorkInitException("worker初始化失败，任务注册失败，task:"+JSON.toJSONString(taskDescription));
            }
        }
    }

    public static void registerWorker(String prefix,String sessionId,WorkerInfo workerInfo,ConsulConfig config){
        logger.info("worker注册信息:{}",JSON.toJSONString(workerInfo));
        ConsulClient consul = ConsulHelper.newConsulClient(config);
        PutParams putParams = new PutParams();
        putParams.setAcquireSession(sessionId);
        String key = prefix+"workers/"+workerInfo.getWorkerName()+"/"+workerInfo.getRemoteAddress();
        Response<GetValue> getValueResponse = consul.getKVValue(key,config.getToken());
        if(getValueResponse!=null&&getValueResponse.getValue()!=null){
            consul.deleteKVValue(key,config.getToken());
        }
        Response<Boolean> response = consul.setKVValue(key,
                JSON.toJSONString(workerInfo),
                config.getToken(),putParams);
        if(response.getValue()){
            logger.info("注册worker成功");
        }else{
            throw new WorkInitException("worker初始化失败..");
        }
    }


    public static void watchValues(final ConsulConfig config, WatcherSwitch watcherSwitch,long waitTimes,final String key, final Watcher.IValuesCallback<GetValue> callback) {
        long index = 0;
        ConsulClient consulClient = newConsulClient(config);
        while(watcherSwitch.isStatus()){
            try{
                QueryParams queryParams = new QueryParams(waitTimes,index);
                Response<List<GetValue>> response = consulClient.getKVValues(key,config.getToken(),queryParams);
                if(response!=null&&response.getValue()!=null&&response.getConsulIndex()>index){
                    callback.ok(response.getValue());
                    index = response.getConsulIndex();
                }
            }catch (Exception e){
                callback.notOk(e);
            }
        }
    }

    public static void watchValueOnce(final ConsulConfig config, long waitTimes,final String key, final Watcher.IValueCallback<GetValue> callback)  {
        try{
            ConsulClient consulClient = newConsulClient(config);
            QueryParams queryParams = new QueryParams(waitTimes,0);
            Response<GetValue> response = consulClient.getKVValue(key,config.getToken(),queryParams);
            if(response!=null&&response.getValue()!=null){
                callback.ok(response.getValue());
            }
        }catch (Exception e){
            callback.notOk(e);
        }
    }

    public static void watchValuesOnce(final ConsulConfig config,long waitTimes, final String key, final Watcher.IValuesCallback<GetValue> callback) {
        try{
            ConsulClient consulClient = newConsulClient(config);
            QueryParams queryParams = new QueryParams(waitTimes,0);
            Response<List<GetValue>> response = consulClient.getKVValues(key,config.getToken(),queryParams);
            if(response!=null&&response.getValue()!=null){
                callback.ok(response.getValue());
            }
        }catch (Exception e){
            callback.notOk(e);
        }
    }
    public static void watchValue(final ConsulConfig config, WatcherSwitch watcherSwitch,long waitTimes,final String key, final Watcher.IValueCallback<GetValue> callback)  {
        long index = 0;
        ConsulClient consulClient = newConsulClient(config);
        while(watcherSwitch.isStatus()){
            try{
                QueryParams queryParams = new QueryParams(waitTimes,index);
                Response<GetValue> response = consulClient.getKVValue(key,config.getToken(),queryParams);
                if(response!=null&&response.getValue()!=null&&index < response.getConsulIndex()){
                    callback.ok(response.getValue());
                    index = response.getConsulIndex();
                }
            }catch (Exception e){
                callback.notOk(e);
            }
        }
    }


    public static void watchValuesUtil(final ConsulConfig config,WatcherSwitch watcherSwitch,long waitTimes,final String key,Watcher.IValuesCondition<GetValue> condition){
        boolean status = false;
        long index = 0;
        ConsulClient consulClient = newConsulClient(config);
        while(watcherSwitch.isStatus()&&!status){
            try{
                QueryParams queryParams = new QueryParams(waitTimes,index);
                Response<List<GetValue>> response = consulClient.getKVValues(key,config.getToken(),queryParams);
                if(response!=null&&response.getValue()!=null&&response.getConsulIndex()>index){
                    status = condition.match(response.getValue());
                    index = response.getConsulIndex();
                }
            }catch (Exception e){
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    public static void watchValueUtil(final ConsulConfig config,WatcherSwitch watcherSwitch,long waitTimes,final String key,Watcher.IValueCondition<GetValue> condition){
        boolean status = false;
        long index = 0;
        ConsulClient consulClient = newConsulClient(config);
        while(watcherSwitch.isStatus()&&!status){
            try{
                QueryParams queryParams = new QueryParams(waitTimes,index);
                Response<GetValue> response = consulClient.getKVValue(key,config.getToken(),queryParams);
                if(response!=null&&response.getValue()!=null){
                    status = condition.match(response.getValue());
                    index = response.getConsulIndex();
                }
            }catch (Exception e){
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

}
