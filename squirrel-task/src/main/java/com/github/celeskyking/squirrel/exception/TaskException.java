package com.github.celeskyking.squirrel.exception;

/**
 * Created by tianqing.wang
 * DATE : 16/2/3
 * TIME : 上午1:10
 * PROJECT : squirrel
 * PACKAGE : com.github.celeskyking.squirrel.exception
 *
 * @author <a href="mailto:celeskyking@163.com">tianqing.wang</a>
 */
public class TaskException extends Exception {

    public TaskException() {
        super();
    }

    public TaskException(String message) {
        super(message);
    }

    public TaskException(String message, Throwable cause) {
        super(message, cause);
    }

    public TaskException(Throwable cause) {
        super(cause);
    }

    protected TaskException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
