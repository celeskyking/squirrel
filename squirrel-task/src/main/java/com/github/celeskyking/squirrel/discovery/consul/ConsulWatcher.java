package com.github.celeskyking.squirrel.discovery.consul;

import com.ecwid.consul.v1.kv.model.GetValue;
import com.github.celeskyking.squirrel.discovery.Watcher;
import com.github.celeskyking.squirrel.discovery.WatcherSwitch;
import com.github.celeskyking.squirrel.helper.ConsulHelper;

/**
 * Created by tianqing.wang
 * DATE : 16-2-18
 * TIME : 下午7:04
 * PROJECT : squirrel
 * PACKAGE : com.qunar.squirrel.discovery.consul
 *
 * @author <a href="mailto:celeskyking@163.com">tianqing.wang</a>
 */
public class ConsulWatcher implements Watcher<GetValue> {

    private ConsulConfig config;

    private WatcherSwitch watcherSwitch;

    public ConsulWatcher(ConsulConfig config){
        this.config = config;
        this.watcherSwitch = new WatcherSwitch();
    }

    @Override
    public void once(String keyPrefix, long waitTimes,IValuesCallback<GetValue> callback)  {
        ConsulHelper.watchValuesOnce(config,waitTimes,keyPrefix,callback);
    }

    @Override
    public void always(String keyPrefix, long waitTimes,IValueCallback<GetValue> callback)  {
        ConsulHelper.watchValue(config,watcherSwitch,waitTimes,keyPrefix,callback);
    }

    @Override
    public void always(String keyPrefix,long waitTimes, IValuesCallback<GetValue> callback)  {
        ConsulHelper.watchValues(config,watcherSwitch,waitTimes,keyPrefix,callback);
    }

    @Override
    public void util(String keyPrefix, long waitTimes,IValueCondition<GetValue> condition) {
        ConsulHelper.watchValueUtil(config,watcherSwitch,waitTimes,keyPrefix,condition);
    }

    @Override
    public void util(String keyPrefix, long waitTimes,IValuesCondition<GetValue> condition) {
        ConsulHelper.watchValuesUtil(config,watcherSwitch,waitTimes,keyPrefix,condition);
    }

    @Override
    public void stop() {
        this.watcherSwitch.setStatus(false);
    }

    @Override
    public void once(String keyPrefix, long waitTimes,IValueCallback<GetValue> callback) throws Throwable {
        ConsulHelper.watchValueOnce(config,waitTimes,keyPrefix,callback);
    }
}
