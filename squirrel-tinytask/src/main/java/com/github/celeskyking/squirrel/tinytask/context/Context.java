package com.github.celeskyking.squirrel.tinytask.context;

import com.google.common.collect.Maps;
import jodd.typeconverter.TypeConverterManager;

import java.util.Map;

/**
 * Created by tianqing.wang
 * DATE : 16/2/24
 * TIME : 上午11:45
 * PROJECT : squirrel
 * PACKAGE : com.qunar.squirrel.tinytask.context
 *
 * @author <a href="mailto:celeskyking@163.com">tianqing.wang</a>
 */
public class Context {

    private Map<String,Object> keyValues = Maps.newHashMap();

    private Context parent;

    public Context(){

    }

    public Context(Context parent){
        this.parent = parent;
    }


    public Object get(String key){
        Context context = this;
        while(context!=null){
            if(context.getKeyValues().containsKey(key)){
                return context.getKeyValues().get(key);
            }else{
                context = context.getParent();
            }
        }
        return null;
    }

    public void addContext(String key,Object values){
        this.keyValues.put(key,values);
    }

    public Context getParent() {
        return parent;
    }

    public void setParent(Context parent) {
        this.parent = parent;
    }

    public Map<String, Object> getKeyValues() {
        return keyValues;
    }

    public void setKeyValues(Map<String, Object> keyValues) {
        this.keyValues = keyValues;
    }

    public <T> T get(String key,Class<T> clazz){
        if(TypeConverterManager.lookup(clazz)!=null){
            return TypeConverterManager.convertType(key,clazz);
        }
        return null;
    }
}
