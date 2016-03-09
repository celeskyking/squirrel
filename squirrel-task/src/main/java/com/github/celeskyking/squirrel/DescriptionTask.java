package com.github.celeskyking.squirrel;

import com.github.celeskyking.squirrel.task.TaskDescription;
import com.github.celeskyking.squirrel.task.executor.ITaskExecutor;

/**
 * Created by tianqing.wang
 * DATE : 16-2-14
 * TIME : 上午10:53
 * PROJECT : squirrel
 * PACKAGE : com.qunar.squirrel
 *
 * @author <a href="mailto:celeskyking@163.com">tianqing.wang</a>
 */
public class DescriptionTask{


    private TaskDescription taskDescription;

    private ITaskExecutor taskExecutor;

    public DescriptionTask(TaskDescription description, ITaskExecutor executor){
        this.taskDescription = description;
        this.taskExecutor = executor;
    }


    public TaskDescription getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(TaskDescription taskDescription) {
        this.taskDescription = taskDescription;
    }

    public ITaskExecutor getTaskExecutor() {
        return taskExecutor;
    }

    public void setTaskExecutor(ITaskExecutor taskExecutor) {
        this.taskExecutor = taskExecutor;
    }
}
