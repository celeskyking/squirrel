package com.github.celeskyking.squirrel.resultbackend;

import com.github.celeskyking.squirrel.task.TaskSignature;

/**
 * Created by tianqing.wang
 * DATE : 16-2-16
 * TIME : 下午12:17
 * PROJECT : squirrel
 * PACKAGE : com.qunar.squirrel.resultbackend
 *
 * @author <a href="mailto:celeskyking@163.com">tianqing.wang</a>
 */
public class ResultHandler implements ResultBackendHandler{

    private ResultListener resultListener;

    private ResultHandler(ResultListener resultListener){
        this.resultListener = resultListener;
    }

    public static ResultHandler newInstance(ResultListener resultListener){
        return new ResultHandler(resultListener);
    }

    public void handle(TaskSignature signature){
        new ResultBackendStatusHandler(resultListener).handle(signature);
    }

}
