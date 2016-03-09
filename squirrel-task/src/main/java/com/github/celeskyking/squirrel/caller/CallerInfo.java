package com.github.celeskyking.squirrel.caller;

import java.io.Serializable;

/**
 * Created by tianqing.wang
 * DATE : 16-2-18
 * TIME : 下午5:10
 * PROJECT : squirrel
 * PACKAGE : com.github.celeskyking.squirrel.caller
 *
 * @author <a href="mailto:celeskyking@163.com">tianqing.wang</a>
 */
public class CallerInfo implements Serializable{

    private String name;

    private String uuid;

    private String remoteAddress;

    private String worker;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getRemoteAddress() {
        return remoteAddress;
    }

    public void setRemoteAddress(String remoteAddress) {
        this.remoteAddress = remoteAddress;
    }

    public String getWorker() {
        return worker;
    }

    public void setWorker(String worker) {
        this.worker = worker;
    }
}
