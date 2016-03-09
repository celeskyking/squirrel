package com.github.celeskyking.squirrel.caller;

import com.github.celeskyking.squirrel.task.TaskSignature;

/**
 * Created by tianqing.wang
 * DATE : 16-2-16
 * TIME : 下午3:43
 * PROJECT : squirrel
 * PACKAGE : com.qunar.squirrel.caller
 *
 * @author <a href="mailto:celeskyking@163.com">tianqing.wang</a>
 */
public interface ICallback {

    void handle(TaskSignature taskSignature);

}
