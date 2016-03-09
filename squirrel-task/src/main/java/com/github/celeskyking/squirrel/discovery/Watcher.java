package com.github.celeskyking.squirrel.discovery;

import java.util.List;

/**
 * Created by tianqing.wang
 * DATE : 16-2-18
 * TIME : 下午6:05
 * PROJECT : squirrel
 * PACKAGE : com.qunar.squirrel.discovery.consul
 *
 * @author <a href="mailto:celeskyking@163.com">tianqing.wang</a>
 */
public interface Watcher<T> {

    void once(String keyPrefix,long waitTimes,IValueCallback<T> callback) throws Throwable;

    void once(String keyPrefix,long waitTimes,IValuesCallback<T> callback) throws Throwable;

    void always(String keyPrefix,long waitTimes,IValueCallback<T> callback) throws Throwable;

    void always(String keyPrefix,long waitTimes,IValuesCallback<T> callback) throws Throwable;

    void util(String keyPrefix,long waitTimes,IValueCondition<T> condition);

    void util(String keyPrefix,long waitTimes,IValuesCondition<T> condition);

    void stop();


    interface IValueCondition<T>{
        boolean match(T t);
    }

    interface IValuesCondition<T>{

        boolean match(List<T> t);
    }


    interface IValueCallback<T>{

        void ok(T t);


        void notOk(Throwable throwable);
    }

    interface IValuesCallback<T>{

        void ok(List<T> t);


        void notOk(Throwable throwable);
    }


}
