package com.github.celeskyking.squirrel.disruptor;

import com.lmax.disruptor.EventFactory;
import com.github.celeskyking.squirrel.task.Task;

/**
 * Created by tianqing.wang
 * DATE : 16-2-17
 * TIME : 下午4:23
 * PROJECT : squirrel
 * PACKAGE : com.github.celeskyking.squirrel.disruptor
 *
 * @author <a href="mailto:celeskyking@163.com">tianqing.wang</a>
 */
public class TaskEventFactory implements EventFactory<Task> {


    @Override
    public Task newInstance() {
        return new Task();
    }
}
