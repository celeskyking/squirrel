package com.github.celeskyking.squirrel.exception;

/**
 * Created by tianqing.wang
 * DATE : 16/2/5
 * TIME : 下午2:11
 * PROJECT : squirrel
 * PACKAGE : com.qunar.squirrel.exception
 *
 * @author <a href="mailto:celeskyking@163.com">tianqing.wang</a>
 * <p>
 *     该类代表了worker目前不能够执行该类,由过滤器过滤掉了.
 * </p>
 *
 */
public class TaskNotRunnableException extends Exception {

    public TaskNotRunnableException() {
        super();
    }

    public TaskNotRunnableException(String message) {
        super(message);
    }

    public TaskNotRunnableException(String message, Throwable cause) {
        super(message, cause);
    }

    public TaskNotRunnableException(Throwable cause) {
        super(cause);
    }

    protected TaskNotRunnableException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
