package com.github.celeskyking.squirrel.tinytask;

import com.github.celeskyking.squirrel.tinytask.context.Context;

/**
 * Created by tianqing.wang
 * DATE : 16/2/24
 * TIME : 下午1:35
 * PROJECT : squirrel
 * PACKAGE : com.qunar.squirrel.tinytask
 *
 * @author <a href="mailto:celeskyking@163.com">tianqing.wang</a>
 */
public interface Something<T> {

    T doIt(Context context);
}
