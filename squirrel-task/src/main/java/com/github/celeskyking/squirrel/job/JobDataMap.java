package com.github.celeskyking.squirrel.job;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import jodd.typeconverter.TypeConverterManager;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by tianqing.wang
 * DATE : 16/2/29
 * TIME : 下午1:59
 * PROJECT : squirrel
 * PACKAGE : com.qunar.squirrel.job
 *
 * @author <a href="mailto:celeskyking@163.com">tianqing.wang</a>
 */
public class JobDataMap implements Serializable{

    public Map<String,Object> pairs = Maps.newHashMap();

    public Integer getInteger(String key){
        if(pairs.containsKey(key)){
            return TypeConverterManager.convertType(get(key),Integer.class);
        }
        return null;
    }

    public String getString(String key){
        if(pairs.containsKey(key)){
            return TypeConverterManager.convertType(get(key),String.class);
        }
        return null;
    }

    public <T> T getBean(String key,Class<T> tClass){
        if(pairs.containsKey(key)){
            String json = getString(key);
            return JSON.parseObject(json,tClass);
        }
        return null;
    }

    public boolean getBoolean(String key){
        if(pairs.containsKey(key)){
            return TypeConverterManager.convertType(get(key),Boolean.class);
        }
        throw new RuntimeException("not found the key :"+key);
    }

    public Double getDouble(String key){
        if(pairs.containsKey(key)){
            return TypeConverterManager.convertType(get(key),Double.class);
        }
        return null;
    }

    public Short getShort(String key){
        if(pairs.containsKey(key)){
            return TypeConverterManager.convertType(get(key),Short.class);
        }
        return null;
    }

    public Float getFloat(String key){
        if(pairs.containsKey(key)){
            return TypeConverterManager.convertType(get(key),Float.class);
        }
        return null;
    }

    private Object get(String key){
        return pairs.containsKey(key)?pairs.get(key):null;
    }

    public void put(String key,Object object){
        if(TypeConverterManager.lookup(object.getClass())==null){
            putBean(key,JSON.toJSONString(object));
        }else{
            this.pairs.put(key,object);
        }

    }

    public <T> T get(String key,Class<T> clazz){
        if(pairs.containsKey(key)){
            if(TypeConverterManager.lookup(clazz)!=null){
                return TypeConverterManager.convertType(get(key),clazz);
            }else{
                return JSON.parseObject(getString(key),clazz);
            }
        }
        return null;
    }

    public void putString(String key,String value){
        this.pairs.put(key,value);
    }

    public void putInteger(String key,Integer value){
        this.pairs.put(key,value);
    }

    public void putBoolean(String key,Boolean bool){
        this.pairs.put(key,bool);
    }

    public void putFloat(String key,Float value){
        this.pairs.put(key,value);
    }

    public void putDouble(String key,Double value){
        this.pairs.put(key,value);
    }

    public void putBean(String key,Object object){
        if(object.getClass().isAssignableFrom(Serializable.class)){
            this.pairs.put(key, JSON.toJSONString(object));
        }else{
            throw new RuntimeException("bean必须继承自Serializable.class");
        }
    }

    public void putShort(String key,Short value){
        this.pairs.put(key,value);
    }

}
