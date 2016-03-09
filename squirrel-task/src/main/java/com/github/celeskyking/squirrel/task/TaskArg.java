package com.github.celeskyking.squirrel.task;

import org.msgpack.annotation.Message;

import java.io.Serializable;


/**
 * Created by tianqing.wang
 * DATE : 16/1/29
 * TIME : 下午2:54
 * PROJECT : squirrel
 * PACKAGE : com.qunar.squirrel.task
 *
 * @author <a href="mailto:celeskyking@163.com">tianqing.wang</a>
 */
@Message
public class TaskArg implements Serializable{

    private String type;

    private String value;

    private transient ArgType argType;

    public TaskArg(){}



    @SuppressWarnings("unchecked")
    public static TaskArg of(Object value){
        TaskArg taskArg = new TaskArg();
        taskArg.setArgType(ArgType.with(value.getClass()));
        taskArg.setType(taskArg.getArgType().getDesc());
        taskArg.setValue(taskArg.getArgType().encode(value));
        return taskArg;
    }


    public String getValue(){
        return value;
    }

    public String getType(){
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public ArgType getArgType() {
        return argType;
    }

    public void setArgType(ArgType argType) {
        this.argType = argType;
    }

    //    public static void main(String[] args) {
//        TaskArg arg = TaskArg.type(ArgType.withInt()).of(1);
//        System.out.println(JSON.toJSONString(arg,true));
//    }
}
