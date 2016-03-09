package com.github.celeskyking.squirrel.task.executor;

/**
 * Created by tianqing.wang
 * DATE : 16/2/3
 * TIME : 上午11:31
 * PROJECT : squirrel
 * PACKAGE : com.qunar.squirrel
 *
 * @author <a href="mailto:celeskyking@163.com">tianqing.wang</a>
 */
public interface IMessage {

    /**
     * 将结果的信息转化为字符串
     * 的序列化和result的序列化不是统一的
     * */
    String message() throws Throwable;
}
