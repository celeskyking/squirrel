package com.github.celeskyking.squirrel.tinytask.workerpool;

/**
 * Created by tianqing.wang
 * DATE : 16/2/24
 * TIME : 下午2:10
 * PROJECT : squirrel
 * PACKAGE : com.github.celeskyking.squirrel.tinytask.workerpool
 *
 * @author <a href="mailto:celeskyking@163.com">tianqing.wang</a>
 */
public interface IMapper<K,V> {

    V map(K task);

}
