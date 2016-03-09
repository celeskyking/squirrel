package com.github.celeskyking.squirrel.demo;


import com.github.celeskyking.squirrel.job.IJobExecutor;
import com.github.celeskyking.squirrel.job.JobContext;

/**
 * Created by tianqing.wang
 * DATE : 16/2/29
 * TIME : 下午4:19
 * PROJECT : squirrel
 * PACKAGE : com.qunar.squirrel.demo
 *
 * @author <a href="mailto:celeskyking@163.com">tianqing.wang</a>
 */
public class DemoJobExecutor implements IJobExecutor {
    @Override
    public void execute(JobContext context) throws Throwable {
        System.out.println(context.getJobDataMap().getString("name"));
    }
}
