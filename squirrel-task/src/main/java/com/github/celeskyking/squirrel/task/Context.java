package com.github.celeskyking.squirrel.task;

import com.google.common.collect.Maps;
import jodd.typeconverter.TypeConverterManager;

import java.util.Map;

/**
 * Created by tianqing.wang
 * DATE : 16/2/3
 * TIME : 上午11:04
 * PROJECT : squirrel
 * PACKAGE : com.qunar.squirrel
 *
 * @author <a href="mailto:celeskyking@163.com">tianqing.wang</a>
 * <p>
 *     任务请求的上下文环境
 * </p>
 *
 */
public class Context {

    public final static String REQUEST_ADDRESS = "$request_address";

    public final static String ARGS = "$args";

    public final static String UUID = "$uuid";



    /**
     * 父亲context
     * */
    private Context parent;

    /**
     * 存储
     * */
    private Map<String,Object> keyValueStore = Maps.newHashMap();


    public Context(){
    }

    public Context(Context parent){
        this.parent = parent;
    }

    public void add(String name,Object object){
        this.keyValueStore.put(name,object);
    }


    public void remove(String name){
        this.keyValueStore.remove(name);
    }

    public Object get(String key){
        Context context = this;
        while(context!=null){
            if(context.getKeyValueStore().containsKey(key)){
                return context.getKeyValueStore().get(key);
            }else{
                context = context.getParent();
            }
        }
        return null;
    }

    public Context getParent() {
        return parent;
    }

    public void setParent(Context parent) {
        this.parent = parent;
    }

    public Map<String, Object> getKeyValueStore() {
        return keyValueStore;
    }

    public void setKeyValueStore(Map<String, Object> keyValueStore) {
        this.keyValueStore = keyValueStore;
    }


    public void setResult(Object result){
        this.keyValueStore.put("$result",result);
    }

    public Map<String,Object> asMap(){
        Map<String,Object> pairs = Maps.newHashMap();
        Context context = this;
        while(context!=null){
            for(Map.Entry<String,Object> entry:context.getKeyValueStore().entrySet()){
                if(pairs.containsKey(entry.getKey())){
                    pairs.put(entry.getKey(),entry.getValue());
                }
            }
            context = context.getParent();
        }
        return pairs;
    }

    public <T> T get(String key, Class<T> clazz){
        return TypeConverterManager.convertType(get(key),clazz);
    }

    public Object getArg(int index){
        return get("$arg"+index);
    }

    public Object[] getTaskArgs(){
        return get(ARGS, Object[].class);
    }

    public String getRequestAddress(){
        return get(REQUEST_ADDRESS,String.class);
    }

}
