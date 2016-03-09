package com.github.celeskyking.squirrel.tinytask.workerpool;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.github.celeskyking.squirrel.tinytask.Result;

import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by tianqing.wang
 * DATE : 16/2/24
 * TIME : 下午4:11
 * PROJECT : squirrel
 * PACKAGE : com.github.celeskyking.squirrel.tinytask.workerpool
 *
 * @author <a href="mailto:celeskyking@163.com">tianqing.wang</a>
 */
public class Scheduler<K,V> {

    private ExecutorService service;

    private CompletionService<V> completionService;

    private Function<K,V> function;

    private AtomicInteger atomicInteger = new AtomicInteger();

    public Scheduler(int pool,Function<K,V> function){
        this.function = function;
        this.service = Executors.newFixedThreadPool(pool == -1 ? Runtime.getRuntime().availableProcessors() : pool);
        this.completionService = new ExecutorCompletionService<>(service);
    }


    public void submit(final K parameter){
        this.completionService.submit(new Callable<V>() {
            @Override
            public V call() throws Exception {
                return Scheduler.this.function.apply(parameter);
            }
        });
        atomicInteger.getAndIncrement();
    }

    public List<Result<V>> finish(){
        List<Result<V>> results = Lists.newArrayList();
        while(true){
            if(results.size() == atomicInteger.get()){
                break;
            }
            Result<V> result = new Result<>();
            try{
                Future<V> future = completionService.take();
                result.setResult(future.get());
                result.setSuccess(true);
            }catch (Throwable throwable){
                result.setSuccess(false);
                result.setThrowable(throwable);
            }finally {
                results.add(result);
            }
        }
        service.shutdown();
        return results;
    }



}
