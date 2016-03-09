package com.github.celeskyking.squirrel.serialize;

import com.alibaba.fastjson.JSON;
import com.github.celeskyking.squirrel.task.TaskSignature;
import com.github.celeskyking.squirrel.exception.SerializeException;

/**
 * Created by tianqing.wang
 * DATE : 16/2/5
 * TIME : 上午11:27
 * PROJECT : squirrel
 * PACKAGE : com.github.celeskyking.squirrel.serialize
 *
 * @author <a href="mailto:celeskyking@163.com">tianqing.wang</a>
 */
public class JsonTaskDecoder implements ITaskDecoder {


    @Override
    public TaskSignature decode(byte[] bytes) throws SerializeException {
        return JSON.parseObject(bytes,TaskSignature.class);
    }
}
