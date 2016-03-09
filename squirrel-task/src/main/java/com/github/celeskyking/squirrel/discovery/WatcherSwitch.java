package com.github.celeskyking.squirrel.discovery;

/**
 * Created by tianqing.wang
 * DATE : 16-2-19
 * TIME : 下午1:20
 * PROJECT : squirrel
 * PACKAGE : com.github.celeskyking.squirrel.discovery
 *
 * @author <a href="mailto:celeskyking@163.com">tianqing.wang</a>
 */
public class WatcherSwitch {

    boolean status = true;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
