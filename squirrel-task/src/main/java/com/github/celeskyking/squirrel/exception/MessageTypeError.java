package com.github.celeskyking.squirrel.exception;

/**
 * Created by tianqing.wang
 * DATE : 16/2/3
 * TIME : 下午3:32
 * PROJECT : squirrel
 * PACKAGE : com.qunar.squirrel.exception
 *
 * @author <a href="mailto:celeskyking@163.com">tianqing.wang</a>
 */
public class MessageTypeError extends Exception{

    public MessageTypeError() {
        super();
    }

    public MessageTypeError(String message) {
        super(message);
    }

    public MessageTypeError(String message, Throwable cause) {
        super(message, cause);
    }

    public MessageTypeError(Throwable cause) {
        super(cause);
    }

    protected MessageTypeError(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
