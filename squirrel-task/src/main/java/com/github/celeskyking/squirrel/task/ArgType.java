package com.github.celeskyking.squirrel.task;

import com.github.celeskyking.squirrel.exception.ArgTypeNotMatchException;
import com.google.common.collect.Maps;
import org.msgpack.annotation.Message;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * Created by tianqing.wang
 * DATE : 16/1/29
 * TIME : 下午2:54
 * PROJECT : squirrel
 * PACKAGE : com.github.celeskyking.squirrel.task
 *
 * @author <a href="mailto:celeskyking@163.com">tianqing.wang</a>
 */
@Message
public abstract class ArgType<T> implements Serializable{

    private String desc;

    public ArgType(String desc){
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public static Map<String,ArgType> argTypeMap = Maps.newHashMap();

    private static Map<Class<?>,ArgType> classArgTypeMap = Maps.newHashMap();

    static{
        argTypeMap.put("int",withInt());
        argTypeMap.put("boolean",withBoolean());
        argTypeMap.put("float",withFloat());
        argTypeMap.put("date",withDate());
        argTypeMap.put("short",withShort());
        argTypeMap.put("string",withText());
        argTypeMap.put("double",withDouble());
        argTypeMap.put("long",withLong());
        classArgTypeMap.put(int.class,withInt());
        classArgTypeMap.put(Integer.class,withInt());
        classArgTypeMap.put(boolean.class,withBoolean());
        classArgTypeMap.put(Boolean.class,withBoolean());
        classArgTypeMap.put(float.class,withFloat());
        classArgTypeMap.put(Float.class,withFloat());
        classArgTypeMap.put(short.class,withShort());
        classArgTypeMap.put(Short.class,withShort());
        classArgTypeMap.put(String.class,withText());
        classArgTypeMap.put(double.class,withDouble());
        classArgTypeMap.put(Double.class,withDouble());
        classArgTypeMap.put(long.class,withLong());
        classArgTypeMap.put(Long.class,withLong());
        classArgTypeMap.put(Date.class,withDate());
    }

    public static ArgType with(String desc){
        return argTypeMap.get(desc);
    }


    public static ArgType with(Class<?> clazz){
        return classArgTypeMap.get(clazz);
    }

    public static void register(String desc, ArgType argType){
        argTypeMap.put(desc,argType);
    }

    public static ArgType<Integer> withInt(){
        return new IntArgType();
    }

    public static ArgType<Boolean> withBoolean(){
        return new BooleanArgType();
    }

    public static ArgType<Float> withFloat(){
        return new FloatArgType();
    }

    public static ArgType<Double> withDouble(){
        return new DoubleArgType();
    }

    public static ArgType<Short> withShort(){
        return new ShortArgType();
    }

    public static ArgType<Long> withLong(){
        return new LongArgType();
    }

    public static ArgType<Date> withDate(){
        return new DateArgType();
    }

    public static ArgType<String> withText(){
        return new StringArgType();
    }


    public abstract String encode(T instance);

    public abstract T decode(String value);

    public static class IntArgType extends ArgType<Integer> {

        public IntArgType() {
            super("int");
        }

        @Override
        public String encode(Integer instance) {
            return String.valueOf(instance);
        }

        @Override
        public Integer decode(String value) {
            return Integer.valueOf(value);
        }
    }


    public static class BooleanArgType extends ArgType<Boolean>{

        public BooleanArgType(){
            super("boolean");
        }

        @Override
        public String encode(Boolean instance) {
            return String.valueOf(instance);
        }

        @Override
        public Boolean decode(String value) {
            return Boolean.valueOf(value);
        }
    }

    public static class FloatArgType extends ArgType<Float>{

        public FloatArgType(){
            super("float");
        }

        @Override
        public String encode(Float instance) {
            return String.valueOf(instance);
        }

        @Override
        public Float decode(String value) {
            return Float.valueOf(value);
        }
    }

    public static class DoubleArgType extends ArgType<Double>{

        public DoubleArgType(){
            super("double");
        }

        @Override
        public String encode(Double instance) {
            return String.valueOf(instance);
        }

        @Override
        public Double decode(String value) {
            return Double.valueOf(value);
        }
    }

    public static class ShortArgType extends ArgType<Short>{

        public ShortArgType(){
            super("short");
        }

        @Override
        public String encode(Short instance) {
            return String.valueOf(instance);
        }

        @Override
        public Short decode(String value) {
            return Short.valueOf(value);
        }
    }

    public static class LongArgType extends ArgType<Long>{

        public LongArgType(){
            super("long");
        }

        @Override
        public String encode(Long instance) {
            return String.valueOf(instance);
        }

        @Override
        public Long decode(String value) {
            return Long.valueOf(value);
        }
    }

    public static class StringArgType extends ArgType<String>{

        public StringArgType(){
            super("string");
        }

        @Override
        public String encode(String instance) {
            return instance;
        }

        @Override
        public String decode(String value) {
            return value;
        }
    }

    public static class DateArgType extends ArgType<Date>{

        public DateArgType(){
            super("date");
        }

        @Override
        public String encode(Date instance) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
            return format.format(instance);
        }

        @Override
        public Date decode(String value) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
            try {
                return format.parse(value);
            } catch (ParseException e) {
                throw new ArgTypeNotMatchException("不能够转化为Date类型,value:"+value);
            }
        }
    }

}
