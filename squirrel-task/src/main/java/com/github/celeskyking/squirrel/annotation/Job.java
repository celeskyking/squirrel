package com.github.celeskyking.squirrel.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by tianqing.wang
 * DATE : 16-2-19
 * TIME : 下午4:33
 * PROJECT : squirrel
 * PACKAGE : com.github.celeskyking.squirrel.annotation
 *
 * @author <a href="mailto:celeskyking@163.com">tianqing.wang</a>
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Job {

    /**
     * task的描述
     * */
    String description();

    /***
     * task的名称
     * */
    String name();


    String cronExpress();
}
