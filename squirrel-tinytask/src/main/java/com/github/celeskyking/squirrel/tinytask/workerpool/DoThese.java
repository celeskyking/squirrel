package com.github.celeskyking.squirrel.tinytask.workerpool;

import com.github.celeskyking.squirrel.tinytask.Result;

import java.util.List;

/**
 * Created by tianqing.wang
 * DATE : 16/2/24
 * TIME : 下午2:26
 * PROJECT : squirrel
 * PACKAGE : com.github.celeskyking.squirrel.tinytask.workerpool
 *
 * @author <a href="mailto:celeskyking@163.com">tianqing.wang</a>
 */
public interface DoThese<T> {

    void doThese(List<Result<T>> results);
}
