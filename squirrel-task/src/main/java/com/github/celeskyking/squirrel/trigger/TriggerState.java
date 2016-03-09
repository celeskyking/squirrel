package com.github.celeskyking.squirrel.trigger;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * Created by tianqing.wang
 * DATE : 16-2-22
 * TIME : 上午11:59
 * PROJECT : squirrel
 * PACKAGE : com.qunar.squirrel.trigger
 *
 * @author <a href="mailto:celeskyking@163.com">tianqing.wang</a>
 */
public enum  TriggerState {

    RUNNING("running"),
    PAUSING("pausing"),
    RESUME("unPausing"),
    STOPPING("stopping"),
    UPDATE("update"),
    PAUSING_RECOVERY("pause_recovery");



    private static Map<String,TriggerState> map = Maps.newHashMap();

    static{
        map.put("running",RUNNING);
        map.put("pausing",PAUSING);
        map.put("unPausing",RESUME);
        map.put("stopping",STOPPING);
        map.put("update",UPDATE);
        map.put("pause_recovery",PAUSING_RECOVERY);
    }

    public static TriggerState get(String stateDesc){
        return map.get(stateDesc);
    }

    private String desc;

    TriggerState(String desc){
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
