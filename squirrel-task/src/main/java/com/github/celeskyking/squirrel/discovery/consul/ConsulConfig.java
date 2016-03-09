package com.github.celeskyking.squirrel.discovery.consul;

import com.google.common.net.HostAndPort;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by tianqing.wang
 * DATE : 16-2-14
 * TIME : 下午2:33
 * PROJECT : squirrel
 * PACKAGE : com.github.celeskyking.squirrel.discovery.consul
 *
 * @author <a href="mailto:celeskyking@163.com">tianqing.wang</a>
 */
public class ConsulConfig {

    private ConsulConfig(HostAndPort hostAndPort,String token,String sessionTTL,long sessionLockDelay,long wait){
        this.address = hostAndPort;
        this.token = token;
        this.sessionTTL = sessionTTL;
        this.sessionLockDelay = sessionLockDelay;
        this.distributeWait = wait;
    }

    /**
     * consul的访问地址
     * */
    private HostAndPort address;

    /**
     * consul的访问token
     * */
    private String token;

    /**
     * session的存活时间
     * */
    private String sessionTTL = "15s";

    /**
     * session锁定的延迟时间
     * */
    private long sessionLockDelay;


    private long distributeWait;



    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getSessionTTL() {
        return sessionTTL;
    }

    public void setSessionTTL(String sessionTTL) {
        this.sessionTTL = sessionTTL;
    }

    public long getSessionLockDelay() {
        return sessionLockDelay;
    }

    public void setSessionLockDelay(long sessionLockDelay) {
        this.sessionLockDelay = sessionLockDelay;
    }

    public HostAndPort getAddress() {
        return address;
    }

    public void setAddress(HostAndPort address) {
        this.address = address;
    }

    public long getDistributeWait() {
        return distributeWait;
    }

    public void setDistributeWait(long distributeWait) {
        this.distributeWait = distributeWait;
    }

    public static Builder builder(){
        return new Builder();
    }


    public static class Builder{

        private HostAndPort hostAndPort;

        private String token;

        private String ttl;

        private long lockDelay = -1;

        private long wait = 10;

        public Builder withTtl(String ttl){
            this.ttl = ttl;
            return this;
        }

        public Builder withToken(String token){
            this.token = token;
            return this;
        }

        public Builder withHostAndPort(HostAndPort hostAndPort){
            this.hostAndPort = hostAndPort;
            return this;
        }

        public Builder withLockDelay(long delay){
            this.lockDelay = delay;
            return this;
        }

        public Builder withDistributeWait(long seconds){
            this.wait = seconds;
            return this;
        }


        public ConsulConfig build(){
            if(StringUtils.isEmpty(ttl)){
                this.ttl = "10s";
            }
            if(hostAndPort==null){
                hostAndPort = HostAndPort.fromParts("127.0.0.1",8500);
            }
            if(lockDelay == -1){
                lockDelay = 0;
            }
            return new ConsulConfig(hostAndPort,token,ttl,lockDelay,wait);
        }
    }
}
