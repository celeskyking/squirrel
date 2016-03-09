package com.github.celeskyking.squirrel.serialize;

import com.alibaba.fastjson.JSON;
import com.github.celeskyking.squirrel.task.TaskSignature;
import com.github.celeskyking.squirrel.exception.SerializeException;

/**
 * Created by tianqing.wang
 * DATE : 16/2/5
 * TIME : 上午11:28
 * PROJECT : squirrel
 * PACKAGE : com.qunar.squirrel.serialize
 *
 * @author <a href="mailto:celeskyking@163.com">tianqing.wang</a>
 * 默认的传输的序列化方式
 *
 */
public class JsonTaskEncoder implements ITaskEncoder {


    @Override
    public byte[] encode(TaskSignature task) throws SerializeException {
        return JSON.toJSONBytes(task);
    }
}
