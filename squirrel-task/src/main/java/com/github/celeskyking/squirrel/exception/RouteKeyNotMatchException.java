package com.github.celeskyking.squirrel.exception;

/**
 * Created by tianqing.wang
 * DATE : 16/2/5
 * TIME : 下午12:00
 * PROJECT : squirrel
 * PACKAGE : com.github.celeskyking.squirrel.exception
 *
 * @author <a href="mailto:celeskyking@163.com">tianqing.wang</a>
 */
public class RouteKeyNotMatchException extends Exception {

    public RouteKeyNotMatchException() {
        super();
    }

    public RouteKeyNotMatchException(String message) {
        super(message);
    }

    public RouteKeyNotMatchException(String message, Throwable cause) {
        super(message, cause);
    }

    public RouteKeyNotMatchException(Throwable cause) {
        super(cause);
    }

    protected RouteKeyNotMatchException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
