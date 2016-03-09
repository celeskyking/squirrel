package com.github.celeskyking.squirrel.caller;

import com.google.common.collect.Lists;
import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.ExceptionHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.github.celeskyking.squirrel.broker.IBroker;
import com.github.celeskyking.squirrel.discovery.DiscoveryService;
import com.github.celeskyking.squirrel.disruptor.TaskDisruptorHandler;
import com.github.celeskyking.squirrel.disruptor.TaskEventFactory;
import com.github.celeskyking.squirrel.disruptor.TaskProducer;
import com.github.celeskyking.squirrel.resultbackend.IResultBackend;
import com.github.celeskyking.squirrel.task.Task;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by tianqing.wang
 * DATE : 16-2-17
 * TIME : 下午6:56
 * PROJECT : squirrel
 * PACKAGE : com.github.celeskyking.squirrel.caller
 *
 * @author <a href="mailto:celeskyking@163.com">tianqing.wang</a>
 */
public class DisruptorCaller extends DefaultCaller {

    /**
     * 主要是用于大量数据的时候的发送
     * */
    private List<TaskProducer> producer = Lists.newArrayList();

    private ISenderListener senderListener;

    private Disruptor<Task> disruptor;


    public DisruptorCaller(String name, String worker,
                           IBroker broker,
                           IResultBackend resultBackend,
                           DiscoveryService discoveryService,
                           final ISenderListener senderListener) {
        super(name, worker, broker, resultBackend, discoveryService);
        this.senderListener = senderListener;
        ExecutorService executorService  = Executors.newCachedThreadPool();
        TaskEventFactory factory = new TaskEventFactory();
        int bufferSize = 1024 * 1024;
        this.disruptor = new Disruptor<>(factory,bufferSize, executorService,
                ProducerType.MULTI,new BlockingWaitStrategy());
        disruptor.handleEventsWith(new TaskDisruptorHandler(this));
        disruptor.setDefaultExceptionHandler(new ExceptionHandler<Task>(){

            @Override
            public void handleEventException(Throwable ex, long sequence, Task event) {
                senderListener.handleEventException(ex,DisruptorCaller.this,event);
            }

            @Override
            public void handleOnStartException(Throwable ex) {
                senderListener.handleOnStartException(ex,DisruptorCaller.this);
            }

            @Override
            public void handleOnShutdownException(Throwable ex) {
                senderListener.handleOnShutdownException(ex,DisruptorCaller.this);
            }
        });
        disruptor.start();
    }


    public TaskProducer newProducer(){
        RingBuffer<Task> ringBuffer = this.disruptor.getRingBuffer();
        return new TaskProducer(ringBuffer);
    }


    public ISenderListener getSenderListener() {
        return senderListener;
    }

    public void setSenderListener(ISenderListener senderListener) {
        this.senderListener = senderListener;
    }

    public List<TaskProducer> getProducer() {
        return producer;
    }

    public void setProducer(List<TaskProducer> producer) {
        this.producer = producer;
    }

    public Disruptor<Task> getDisruptor() {
        return disruptor;
    }

    public void setDisruptor(Disruptor<Task> disruptor) {
        this.disruptor = disruptor;
    }
}
