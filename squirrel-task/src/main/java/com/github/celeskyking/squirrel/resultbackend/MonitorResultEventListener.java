package com.github.celeskyking.squirrel.resultbackend;

import com.github.celeskyking.squirrel.task.TaskSignature;
import com.github.celeskyking.squirrel.monitor.IMonitor;

/**
 * Created by tianqing.wang
 * DATE : 16-2-22
 * TIME : 下午2:09
 * PROJECT : squirrel
 * PACKAGE : com.github.celeskyking.squirrel.resultbackend
 *
 * @author <a href="mailto:celeskyking@163.com">tianqing.wang</a>
 */
public class MonitorResultEventListener implements ResultEventListener{

    private IMonitor monitor;

    public MonitorResultEventListener(IMonitor monitor){
        this.monitor = monitor;
    }


    @Override
    public void fireTaskPending(TaskSignature taskSignature) {
        monitor.collect(taskSignature);
    }

    @Override
    public void fireTaskReceived(TaskSignature taskSignature) {
        monitor.collect(taskSignature);
    }

    @Override
    public void fireTaskSuccess(TaskSignature taskSignature) {
        monitor.collect(taskSignature);
    }

    @Override
    public void fireTaskFailure(TaskSignature taskSignature) {
        monitor.collect(taskSignature);
    }

    @Override
    public void fireTaskStarted(TaskSignature taskSignature) {
        monitor.collect(taskSignature);
    }
}
