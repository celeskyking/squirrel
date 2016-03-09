package com.github.celeskyking.squirrel.exception;

/**
 * Created by tianqing.wang
 * DATE : 16/2/3
 * TIME : 下午6:12
 * PROJECT : squirrel
 * PACKAGE : com.qunar.squirrel.exception
 *
 * @author <a href="mailto:celeskyking@163.com">tianqing.wang</a>
 */
public class TaskNotExistException extends TaskException {

    public TaskNotExistException() {
        super();
    }

    public TaskNotExistException(String message) {
        super(message);
    }

    public TaskNotExistException(String message, Throwable cause) {
        super(message, cause);
    }

    public TaskNotExistException(Throwable cause) {
        super(cause);
    }

    protected TaskNotExistException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
