package com.github.celeskyking.squirrel.demo;

import com.github.celeskyking.squirrel.annotation.Task;
import com.github.celeskyking.squirrel.annotation.Tasks;
import com.github.celeskyking.squirrel.task.executor.IMessage;
import com.github.celeskyking.squirrel.task.handler.Messages;

/**
 * Created by tianqing.wang
 * DATE : 16-2-16
 * TIME : 下午6:23
 * PROJECT : squirrel
 * PACKAGE : com.qunar.squirrel.demo
 *
 * @author <a href="mailto:celeskyking@163.com">tianqing.wang</a>
 */
@Tasks
public class TaskDemo {

    private int times;

    @Task(description = "demo事例",name = "hello_task")
    public IMessage hello(String name) throws InterruptedException {
        //Thread.sleep(1000);
        times++;
        return Messages.text("hello,"+name+",times:"+times);
    }
}
