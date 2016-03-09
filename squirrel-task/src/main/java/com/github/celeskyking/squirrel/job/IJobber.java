package com.github.celeskyking.squirrel.job;


/**
 * Created by tianqing.wang
 * DATE : 16-2-19
 * TIME : 下午4:53
 * PROJECT : squirrel
 * PACKAGE : com.github.celeskyking.squirrel.job
 *
 * @author <a href="mailto:celeskyking@163.com">tianqing.wang</a>
 */
public interface IJobber {
    /**
     * 获取该jobber的名称
     * */
    String getName();


    /**
     * 获取worker的名称
     * */
    String getWorker();


    /**
     * job是否存在
     * */
    boolean exist(String jobName);


    /**
     * 执行触发
     * */
    void execute(String jobName,JobContext jobContext) throws Throwable;


    /**
     * 启动工作线程
     * */
    void start() throws Throwable;


    /**
     * 停止工作线程
     * */
    void stop() throws Throwable;

    /**
     * 更新定时任务的cron表达式
     * */
    void updateJob(String jobName,String cronExpress) throws Throwable;


    void publish(JobInfo job) throws Throwable;

    /**
     * 发布注解的job
     * */
    void publish(Object object) throws Throwable;


    void remove(String jobName) throws Throwable;


    void pause(String jobName) throws Throwable;

    void resume(String jobName) throws Throwable;

    /**
     * 增加监听
     * */
    void addListener(JobNotifyListener listener);

    JobContext getJobContext();

}
