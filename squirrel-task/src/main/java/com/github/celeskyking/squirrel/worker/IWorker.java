package com.github.celeskyking.squirrel.worker;

import com.github.celeskyking.squirrel.exception.RouteKeyNotMatchException;
import com.github.celeskyking.squirrel.exception.TaskNotExistException;
import com.github.celeskyking.squirrel.exception.TaskNotRunnableException;
import com.github.celeskyking.squirrel.task.TaskSignature;
import com.github.celeskyking.squirrel.task.executor.ITaskExecutor;
import com.github.celeskyking.squirrel.task.processor.ITaskProcessor;

/**
 * Created by tianqing.wang
 * DATE : 16/1/27
 * TIME : 下午12:07
 * PROJECT : squirrel
 * PACKAGE : com.github.celeskyking.squirrel.worker
 *
 * @author <a href="mailto:celeskyking@163.com">tianqing.wang</a>
 */
public interface IWorker extends ITaskProcessor{

    /**
     * 初始化,并且启动服务,进行监控
     * <p>
     *     初始化的过程中,通过discovery来注册当前该worker的任务,已任务为主键进行存储
     *     比如我们有一个任务名称为 update_node_status,worker的名称为worker1
     *     存储结构为${prefix}/update_node_status/worker1
     *     存储的value值为当前worker的基本信息,参考workerInfo,
     *     保证每一个worker都是无状态的,如果worker挂掉了,自动的注销掉所负责的所有任务.
     *     caller里面会实时保存当前所有的worker的状态.当我们的任务触发的时候,如果不含有该taskName,
     *     则取消发送.至于发送规则还是依据broker的publish()
     *
     * </p>
     * */
    void launch(boolean trigger) throws Throwable;


    /**
     * 下线一个task,移除taskName下面的worker.
     * 该操作会有部分延迟,用等待session超时
     * */
    void deRegister(String taskName);



    /**
     * 上线一个task,移除taskName下面的worker.
     * 该操作会有部分延迟,用等待session超时
     * */
    void register(String taskName);

    /**
     * 该操作将会把所有的任务都下线,但是该worker不会停止工作.
     * */
    void offline();



    /**
     * 该方法会重新注册所有的任务信息,前提是任务都在下线状态
     * */
    void online();


    /**
     *退出work,当server停止的时候,会触发该方法
     * */
    void quit();

    /**
     * 接收消息
     * */
    void process(TaskSignature taskSignature) throws TaskNotExistException, TaskNotRunnableException, RouteKeyNotMatchException;


    /**
     * 返回status当前的状态
     * online则为true,
     * offline则为false
     * */
    boolean status();



    void addTask(String name, ITaskExecutor executor);


    /**
     * trigger如果jobber不存在的时候是否触发调度,默认不触发
     * */
    void triggerForced(boolean forced);

}
