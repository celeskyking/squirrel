package com.github.celeskyking.squirrel.resultbackend;

import com.google.common.collect.Maps;
import com.github.celeskyking.squirrel.task.TaskSignature;
import com.github.celeskyking.squirrel.task.TaskState;

import java.util.Map;

/**
 * Created by tianqing.wang
 * DATE : 16-2-16
 * TIME : 下午2:35
 * PROJECT : squirrel
 * PACKAGE : com.qunar.squirrel.resultbackend
 *
 * @author <a href="mailto:celeskyking@163.com">tianqing.wang</a>
 */
public class ResultBackendStatusHandler implements ResultBackendHandler {

    private ResultListener resultListener;

    public ResultBackendStatusHandler(ResultListener resultListener){
        this.resultListener = resultListener;
    }

    private Map<TaskState,ResultBackendHandler> map = Maps.newHashMap();
    {
        map.put(TaskState.PENDING, new ResultBackendHandler() {
            @Override
            public void handle(TaskSignature taskSignature) {
                resultListener.fireTaskPending(taskSignature);
            }
        });
        map.put(TaskState.STARTED, new ResultBackendHandler() {
            @Override
            public void handle(TaskSignature taskSignature) {
                resultListener.fireTaskStarted(taskSignature);
            }
        });
        map.put(TaskState.RECEIVED, new ResultBackendHandler() {
            @Override
            public void handle(TaskSignature taskSignature) {
                resultListener.fireTaskReceived(taskSignature);
            }
        });
        map.put(TaskState.FAILURE, new ResultBackendHandler() {
            @Override
            public void handle(TaskSignature taskSignature) {
                resultListener.fireTaskFailure(taskSignature);
            }
        });
        map.put(TaskState.SUCCESS, new ResultBackendHandler() {
            @Override
            public void handle(TaskSignature taskSignature) {
                resultListener.fireTaskSuccess(taskSignature);
            }
        });
    }

    @Override
    public void handle(TaskSignature taskSignature) {
        TaskState state = TaskState.get(taskSignature.getTask().getState());
        map.get(state).handle(taskSignature);
    }

}
