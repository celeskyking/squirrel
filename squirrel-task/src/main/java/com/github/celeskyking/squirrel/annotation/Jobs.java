package com.github.celeskyking.squirrel.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by tianqing.wang
 * DATE : 16/2/29
 * TIME : 下午3:39
 * PROJECT : squirrel
 * PACKAGE : com.github.celeskyking.squirrel.annotation
 *
 * @author <a href="mailto:celeskyking@163.com">tianqing.wang</a>
 */

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Jobs {
}
