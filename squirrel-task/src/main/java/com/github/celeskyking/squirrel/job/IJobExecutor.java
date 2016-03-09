package com.github.celeskyking.squirrel.job;

/**
 * Created by tianqing.wang
 * DATE : 16/2/29
 * TIME : 上午11:45
 * PROJECT : squirrel
 * PACKAGE : com.qunar.squirrel.job
 *
 * @author <a href="mailto:celeskyking@163.com">tianqing.wang</a>
 */
public interface IJobExecutor {

    void execute(JobContext context) throws Throwable;

}
