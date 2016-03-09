package com.github.celeskyking.squirrel;

/**
 * Created by tianqing.wang
 * DATE : 16/1/27
 * TIME : 下午3:06
 * PROJECT : squirrel
 * PACKAGE : com.github.celeskyking.squirrel
 *
 * @author <a href="mailto:celeskyking@163.com">tianqing.wang</a>
 */
public interface IConfig {

    String get(String key);

    <T> T get(String key, Class<T> clazz);

}
