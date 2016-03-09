package com.github.celeskyking.squirrel.demo;


import com.github.celeskyking.squirrel.annotation.Job;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by tianqing.wang
 * DATE : 16-2-19
 * TIME : 下午5:29
 * PROJECT : squirrel
 * PACKAGE : com.github.celeskyking.squirrel.demo
 *
 * @author <a href="mailto:celeskyking@163.com">tianqing.wang</a>
 */

//@Jobs
public class DemoJob{

    AtomicInteger atomicInteger = new AtomicInteger();

    @Job(name = "hello_job",cronExpress = "*/5 * * * * ?",description = "测试一下")
    public void helloJob(){
        System.out.println("开始工作"+new SimpleDateFormat().format(new Date())+",times"+atomicInteger.getAndIncrement());
    }

}
