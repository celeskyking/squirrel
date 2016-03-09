package com.github.celeskyking.squirrel.task.handler;

import com.alibaba.fastjson.JSON;
import com.github.celeskyking.squirrel.task.executor.IMessage;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by tianqing.wang
 * DATE : 16/2/3
 * TIME : 下午2:59
 * PROJECT : squirrel
 * PACKAGE : com.qunar.squirrel.task.handler
 *
 * @author <a href="mailto:celeskyking@163.com">tianqing.wang</a>
 */
public class Messages {



    public static IMessage json(Object object){
        return new JsonMessage(object);
    }

    /**
     * 支持java的基本类型,并且支持Date类型的转化
     * */
    public static IMessage text(Object object){
        return new TextMessage(object);
    }



    public static class JsonMessage implements IMessage{

        private Object object;

        public JsonMessage(Object object){
            this.object = object;
        }

        @Override
        public String message() {
            return JSON.toJSONString(object);
        }
    }

    public static class TextMessage implements IMessage{

        private Object object;

        public TextMessage(Object object){
            this.object = object;
        }

        @Override
        public String message() {
            if(object instanceof Date){
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                return format.format(object);
            }else{
                return String.valueOf(object);
            }
        }
    }

}
