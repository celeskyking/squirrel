package com.github.celeskyking.squirrel.exception;

/**
 * Created by tianqing.wang
 * DATE : 16-2-19
 * TIME : 下午5:39
 * PROJECT : squirrel
 * PACKAGE : com.github.celeskyking.squirrel.exception
 *
 * @author <a href="mailto:celeskyking@163.com">tianqing.wang</a>
 */
public class CronExpressException extends Exception {

    public CronExpressException() {
        super();
    }

    public CronExpressException(String message) {
        super(message);
    }

    public CronExpressException(String message, Throwable cause) {
        super(message, cause);
    }

    public CronExpressException(Throwable cause) {
        super(cause);
    }

    protected CronExpressException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
