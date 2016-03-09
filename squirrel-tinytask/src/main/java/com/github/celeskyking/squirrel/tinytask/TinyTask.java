package com.github.celeskyking.squirrel.tinytask;

import com.github.celeskyking.squirrel.tinytask.context.Context;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

/**
 * Created by tianqing.wang
 * DATE : 16/2/24
 * TIME : 上午11:56
 * PROJECT : squirrel
 * PACKAGE : com.qunar.squirrel.tinytask
 *
 * @author <a href="mailto:celeskyking@163.com">tianqing.wang</a>
 */
public class TinyTask<T>{

    private Context context = new Context();

    private Callable<T> callable;

    private DoThis<T> doThis;


    private TinyTask(final Something<T> something){
        this.callable = new Callable<T>() {
            @Override
            public T call() throws Exception {
                return something.doIt(context);
            }
        };
    }

    public static <T> TinyTask<T> perform(Something<T> something){
        return new TinyTask<>(something);
    }

    public TinyTask<T> context(String key,Object value){
        this.context.addContext(key,value);
        return this;
    }

    public TinyTask<T> context(Context context){
        this.context = new Context(context);
        return this;
    }



    public TinyTask<T> whenDone(DoThis<T> doThis){
        this.doThis = doThis;
        return this;
    }


    public void go(){
        Result<T> result = new Result<>();
        try {
            FutureTask<T> futureTask = new FutureTask<>(callable);
            new Thread(futureTask).start();
            result.setResult(futureTask.get());
            result.setSuccess(true);
        } catch (Exception e) {
            result.setThrowable(e);
            result.setSuccess(false);
        }finally {
            if(doThis!=null){
                doThis.doThis(result);
            }
        }
    }




}
