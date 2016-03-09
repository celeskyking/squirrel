package com.github.celeskyking.squirrel.exception;

/**
 * Created by tianqing.wang
 * DATE : 16/1/31
 * TIME : 下午12:22
 * PROJECT : squirrel
 * PACKAGE : com.github.celeskyking.squirrel.exception
 *
 * @author <a href="mailto:celeskyking@163.com">tianqing.wang</a>
 */
public class ArgTypeException extends RuntimeException {

    public ArgTypeException() {
        super();
    }

    public ArgTypeException(String message) {
        super(message);
    }

    public ArgTypeException(String message, Throwable cause) {
        super(message, cause);
    }

    public ArgTypeException(Throwable cause) {
        super(cause);
    }

    protected ArgTypeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
