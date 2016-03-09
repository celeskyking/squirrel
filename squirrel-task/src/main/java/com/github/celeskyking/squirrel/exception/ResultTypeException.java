package com.github.celeskyking.squirrel.exception;

/**
 * Created by tianqing.wang
 * DATE : 16/2/3
 * TIME : 上午1:00
 * PROJECT : squirrel
 * PACKAGE : com.github.celeskyking.squirrel.exception
 *
 * @author <a href="mailto:celeskyking@163.com">tianqing.wang</a>
 *
 * 结果类型转化失败
 *
 */
public class ResultTypeException extends RuntimeException {


    public ResultTypeException() {
        super();
    }

    public ResultTypeException(String message) {
        super(message);
    }

    public ResultTypeException(String message, Throwable cause) {
        super(message, cause);
    }

    public ResultTypeException(Throwable cause) {
        super(cause);
    }

    protected ResultTypeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
