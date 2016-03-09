package com.github.celeskyking.squirrel.resultbackend.nsq;

import com.github.celeskyking.squirrel.broker.nsq.NsqConfig;
import com.github.celeskyking.squirrel.resultbackend.IResultBackend;
import com.github.celeskyking.squirrel.resultbackend.ResultEventListener;
import com.github.celeskyking.squirrel.resultbackend.ResultHandler;
import com.github.celeskyking.squirrel.resultbackend.ResultListener;
import com.github.celeskyking.squirrel.serialize.ITaskDecoder;
import com.github.celeskyking.squirrel.serialize.ITaskEncoder;
import com.github.celeskyking.squirrel.task.TaskSignature;
import com.google.common.net.HostAndPort;
import com.qunar.nsq.client.NSQConsumer;
import com.qunar.nsq.client.NSQMessage;
import com.qunar.nsq.client.NSQProducer;
import com.qunar.nsq.client.ServerAddress;
import com.qunar.nsq.client.callbacks.NSQMessageCallback;
import com.qunar.nsq.client.exceptions.NSQException;
import com.qunar.nsq.client.lookup.DefaultNSQLookup;
import com.qunar.nsq.client.lookup.NSQLookup;
import com.github.celeskyking.squirrel.exception.SerializeException;
import com.github.celeskyking.squirrel.task.TaskState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeoutException;

/**
 * Created by tianqing.wang
 * DATE : 16-2-15
 * TIME : 下午3:55
 * PROJECT : squirrel
 * PACKAGE : com.qunar.squirrel.resultbackend.nsq
 *
 * @author <a href="mailto:celeskyking@163.com">tianqing.wang</a>
 */
public class NSQResultBackend implements IResultBackend {


    private static Logger logger  = LoggerFactory.getLogger(NSQResultBackend.class);

    private ResultListener resultListener = new ResultListener();

    private NsqConfig nsqConfig;

    private NSQProducer producer;

    private ITaskEncoder encoder;

    private NSQLookup nsqLookup;

    private ITaskDecoder decoder;

    private NSQConsumer nsqConsumer;

    public NSQResultBackend(NsqConfig config,ITaskEncoder encoder,ITaskDecoder decoder){
        this.nsqConfig = config;
        this.encoder = encoder;
        this.decoder = decoder;
        this.nsqLookup = buildNsqLookup();
        Set<ServerAddress> addresses = nsqLookup.nodes();
        for(ServerAddress address : addresses){
            this.producer = new NSQProducer().addAddress(address.getHost(),address.getPort());
        }
        this.producer.start();
    }

    private void setState(TaskSignature task, TaskState state, String topic){
        try {
            task.getTask().setState(state.getDesc());
            this.producer.produce(topic,encoder.encode(task));
        } catch (NSQException | TimeoutException e) {
            try {
                Thread.sleep(1000);
                setStateSuccess(task);
            } catch (InterruptedException e1) {
                //do nothing
            }
        } catch (SerializeException e) {
            logger.error("序列化失败",e);
        }
    }

    @Override
    public void setStateSuccess(TaskSignature task) {
        setState(task,TaskState.SUCCESS,nsqConfig.getResultTopic()+"_"+task.getCaller());
    }

    @Override
    public void setStatePending(TaskSignature task) {
        setState(task,TaskState.PENDING,nsqConfig.getResultTopic()+"_"+task.getCaller());
    }

    @Override
    public void setStateReceived(TaskSignature task) {
        setState(task,TaskState.RECEIVED,nsqConfig.getResultTopic()+"_"+task.getCaller());
    }

    @Override
    public void setStateStarted(TaskSignature task) {
        setState(task,TaskState.STARTED,nsqConfig.getResultTopic()+"_"+task.getCaller());
    }

    @Override
    public void setStateFailure(TaskSignature task) {
        setState(task,TaskState.SUCCESS,nsqConfig.getResultTopic()+"_"+task.getCaller());
    }


    @Override
    public void startConsumer(String caller) {
        this.nsqConsumer = new NSQConsumer(this.nsqLookup,
                this.nsqConfig.getResultTopic()+"_"+caller,caller,
                new NSQMessageCallback() {
                    @Override
                    public void message(NSQMessage message) {
                        try{
                            TaskSignature taskSignature = decoder.decode(message.getMessage());
                            ResultHandler.newInstance(resultListener).handle(taskSignature);
                            message.finished();
                        }catch (Throwable throwable){
                            logger.error("",throwable);
                            message.requeue(1000);
                        }
                    }
                });
        this.nsqConsumer.setExecutor(Executors.newCachedThreadPool());
        nsqConsumer.start();
    }

    @Override
    public void stopConsumer() {
        this.nsqConsumer.shutdown();
    }

    @Override
    public void register(ResultEventListener resultEventListener) {
        this.resultListener.register(resultEventListener);
    }


    private NSQLookup buildNsqLookup(){
        NSQLookup lookup = new DefaultNSQLookup();
        for(HostAndPort hostAndPort : nsqConfig.getLookupAddress()){
            lookup.addLookupAddress(hostAndPort.getHostText(),hostAndPort.getPortOrDefault(4161));
        }
        return lookup;
    }
}
