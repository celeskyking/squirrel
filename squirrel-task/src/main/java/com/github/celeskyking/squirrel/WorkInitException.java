package com.github.celeskyking.squirrel;

/**
 * Created by tianqing.wang
 * DATE : 16-2-15
 * TIME : 下午1:57
 * PROJECT : squirrel
 * PACKAGE : com.github.celeskyking.squirrel
 *
 * @author <a href="mailto:celeskyking@163.com">tianqing.wang</a>
 */
public class WorkInitException extends RuntimeException {

    public WorkInitException() {
        super();
    }

    public WorkInitException(String message) {
        super(message);
    }

    public WorkInitException(String message, Throwable cause) {
        super(message, cause);
    }

    public WorkInitException(Throwable cause) {
        super(cause);
    }

    protected WorkInitException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
