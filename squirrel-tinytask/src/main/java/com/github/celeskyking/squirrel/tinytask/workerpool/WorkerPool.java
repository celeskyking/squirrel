package com.github.celeskyking.squirrel.tinytask.workerpool;

import com.github.celeskyking.squirrel.tinytask.Result;
import com.google.common.base.Function;
import com.google.common.collect.Lists;

import java.util.Collection;
import java.util.List;

/**
 * Created by tianqing.wang
 * DATE : 16/2/24
 * TIME : 下午1:56
 * PROJECT : squirrel
 * PACKAGE : com.qunar.squirrel.tinytask
 *
 * @author <a href="mailto:celeskyking@163.com">tianqing.wang</a>
 */
public class WorkerPool<K,V> {

    private DoThese<V> doThese;

    private IFilter<K> filter;

    private Function<K,V> function;

    private Collection<K> params;

    private int pool = -1;

    private WorkerPool(Collection<K> params){
        this.params = params;
    }

    public static <K,V> WorkerPool<K,V> load(Collection<K> params){
        return new WorkerPool<>(params);
    }

    public WorkerPool<K,V> pool(int pool){
        if(pool>0){
            this.pool = pool;
        }
        return this;
    }


    public WorkerPool<K,V> perform(Function<K,V> function){
        this.function = function;
        return this;
    }

    public <T> WorkerPool<T,V> map(IMapper<K,T> mapper){
        List<T> newParams = Lists.newArrayList();
        if(params!=null){
            for(K param : params){
                newParams.add(mapper.map(param));

            }
            return new WorkerPool<>(newParams);
        }
        return null;
    }


    public WorkerPool<K,V> filter(IFilter<K> filter){
        this.filter = filter;
        return this;
    }

    public WorkerPool<K,V> collect(DoThese<V> doThese){
        this.doThese = doThese;
        return this;
    }

    public void go(){
        if(function==null){
            throw new RuntimeException("没有指定可运行的Function");
        }
        try {
            Scheduler<K,V> scheduler = new Scheduler<>(pool,function);
            for(final K request : params){
                if(filter==null||filter.filter(request)){
                    scheduler.submit(request);
                }
            }
            List<Result<V>> results = scheduler.finish();
            if(doThese!=null){
                doThese.doThese(results);
            }
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }



}
