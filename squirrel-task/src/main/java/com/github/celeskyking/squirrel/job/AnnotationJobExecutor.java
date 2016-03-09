package com.github.celeskyking.squirrel.job;

import java.lang.reflect.Method;

/**
 * Created by tianqing.wang
 * DATE : 16-2-19
 * TIME : 下午5:35
 * PROJECT : squirrel
 * PACKAGE : com.github.celeskyking.squirrel.job
 *
 * @author <a href="mailto:celeskyking@163.com">tianqing.wang</a>
 */
public class AnnotationJobExecutor implements IJobExecutor {

    private Method method;

    private Object instance;


    public AnnotationJobExecutor(Method method, Object instance){
        this.method = method;
        this.instance = instance;
    }

    @Override
    public void execute(JobContext context) throws Throwable {
        if(method.getParameterTypes().length == 1 && method.getParameterTypes()[0].isAssignableFrom(JobContext.class)){
            method.invoke(instance,context);
        }else{
            method.invoke(instance);
        }
    }
}
