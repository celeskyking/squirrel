package com.github.celeskyking.squirrel.resultbackend;

import com.github.celeskyking.squirrel.task.TaskSignature;

/**
 * Created by tianqing.wang
 * DATE : 16-2-16
 * TIME : 下午12:14
 * PROJECT : squirrel
 * PACKAGE : com.qunar.squirrel.resultbackend
 *
 * @author <a href="mailto:celeskyking@163.com">tianqing.wang</a>
 */
public interface ResultBackendHandler {

    /**
     * 处理task
     * */
    void handle(TaskSignature taskSignature);


}
