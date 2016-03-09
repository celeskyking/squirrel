package com.github.celeskyking.squirrel.task;

import java.io.Serializable;

/**
 * Created by tianqing.wang
 * DATE : 16/2/5
 * TIME : 下午6:02
 * PROJECT : squirrel
 * PACKAGE : com.qunar.squirrel.task
 *
 * @author <a href="mailto:celeskyking@163.com">tianqing.wang</a>
 */
public class TaskArgDescription implements Serializable {

    /**
     * 通过Context可以定义arg的命名,如果不定义的话,可以通过arg0...arg10等的方式来获取参数
     * 还有一个就是通过参数的顺序来获取,见Task的注解实现,通过类型来实现参数的注入
     * 默认会按照build的先后顺序来赋予arg0...10的值
     * */
    private String argName;

    /**
     * 参数类型,主要是对应ArgType里面注册的类型
     * */
    private String argType;

    /**
     * 对应参数的索引值,为了方便校验.
     * */
    private int index;


    public String getArgName() {
        return argName;
    }

    public void setArgName(String argName) {
        this.argName = argName;
    }

    public String getArgType() {
        return argType;
    }

    public void setArgType(String argType) {
        this.argType = argType;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
