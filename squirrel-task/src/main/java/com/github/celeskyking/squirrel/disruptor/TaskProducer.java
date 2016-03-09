package com.github.celeskyking.squirrel.disruptor;

import com.github.celeskyking.squirrel.task.Task;
import com.lmax.disruptor.EventTranslatorOneArg;
import com.lmax.disruptor.RingBuffer;

/**
 * Created by tianqing.wang
 * DATE : 16-2-17
 * TIME : 下午4:28
 * PROJECT : squirrel
 * PACKAGE : com.qunar.squirrel.disruptor
 *
 * @author <a href="mailto:celeskyking@163.com">tianqing.wang</a>
 */
public class TaskProducer {

    private final RingBuffer ringBuffer;

    private static final EventTranslatorOneArg<Task,Task> translator = new EventTranslatorOneArg<Task, Task>() {
        @Override
        public void translateTo(Task task, long l, Task newTask) {
            task.setTaskArgs(newTask.getTaskArgs());
            task.setTaskId(newTask.getTaskId());
            task.setTaskName(newTask.getTaskName());
        }
    };

    public TaskProducer(RingBuffer<Task> ringBuffer){
        this.ringBuffer = ringBuffer;
    }

    @SuppressWarnings("unchecked")
    public void onData(Task task){
        ringBuffer.publishEvent(translator,task);
    }

}
