package com.github.celeskyking.squirrel.tinytask;

/**
 * Created by tianqing.wang
 * DATE : 16/2/24
 * TIME : 下午1:48
 * PROJECT : squirrel
 * PACKAGE : com.qunar.squirrel.tinytask
 *
 * @author <a href="mailto:celeskyking@163.com">tianqing.wang</a>
 */
public class Result<T> {

    private Throwable throwable;

    private boolean success;

    private T result;


    public Throwable getThrowable() {
        return throwable;
    }

    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }
}
