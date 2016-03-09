package com.github.celeskyking.squirrel.demo;


import com.github.celeskyking.squirrel.job.IJobExecutor;
import com.github.celeskyking.squirrel.job.JobContext;

/**
 * Created by tianqing.wang
 * DATE : 16/2/29
 * TIME : 下午8:22
 * PROJECT : squirrel
 * PACKAGE : com.github.celeskyking.squirrel
 *
 * @author <a href="mailto:celeskyking@163.com">tianqing.wang</a>
 */
public class DemoJobExecutorTWO implements IJobExecutor {

    @Override
    public void execute(JobContext context) throws Throwable {
        System.out.println("-->"+context.getJobDataMap().getString("name"));
    }
}
