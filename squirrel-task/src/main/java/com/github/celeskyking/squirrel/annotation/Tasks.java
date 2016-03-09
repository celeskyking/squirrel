package com.github.celeskyking.squirrel.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by tianqing.wang
 * DATE : 16/2/3
 * TIME : 下午3:44
 * PROJECT : squirrel
 * PACKAGE : com.github.celeskyking.squirrel.annotation
 *
 * @author <a href="mailto:celeskysking@163.com">tianqing.wang</a>
 * 该注解用来标注要运行的任务类
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Tasks {

}
