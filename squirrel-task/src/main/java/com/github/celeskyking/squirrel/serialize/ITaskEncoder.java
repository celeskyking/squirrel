package com.github.celeskyking.squirrel.serialize;

import com.github.celeskyking.squirrel.task.TaskSignature;
import com.github.celeskyking.squirrel.exception.SerializeException;

/**
 * Created by tianqing.wang
 * DATE : 16/2/5
 * TIME : 上午11:01
 * PROJECT : squirrel
 * PACKAGE : com.github.celeskyking.squirrel.serialize
 *
 * @author <a href="mailto:celeskyking@163.com">tianqing.wang</a>
 */
public interface ITaskEncoder {


    byte[] encode(TaskSignature task) throws SerializeException;


}
