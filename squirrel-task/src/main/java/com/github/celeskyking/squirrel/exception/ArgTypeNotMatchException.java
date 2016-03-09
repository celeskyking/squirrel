package com.github.celeskyking.squirrel.exception;

/**
 * Created by tianqing.wang
 * DATE : 16/2/2
 * TIME : 下午1:59
 * PROJECT : squirrel
 * PACKAGE : com.qunar.squirrel.exception
 *
 * @author <a href="mailto:celeskyking@163.com">tianqing.wang</a>
 */
public class ArgTypeNotMatchException extends RuntimeException{

    public ArgTypeNotMatchException() {

    }

    public ArgTypeNotMatchException(String message) {
        super(message);
    }

    public ArgTypeNotMatchException(String message, Throwable cause) {
        super(message, cause);
    }

    public ArgTypeNotMatchException(Throwable cause) {
        super(cause);
    }

    protected ArgTypeNotMatchException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
