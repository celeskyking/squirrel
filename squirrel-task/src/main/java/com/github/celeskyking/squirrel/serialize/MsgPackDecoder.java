package com.github.celeskyking.squirrel.serialize;

import com.github.celeskyking.squirrel.exception.SerializeException;
import com.github.celeskyking.squirrel.task.TaskSignature;
import org.msgpack.MessagePack;

import java.io.IOException;

/**
 * Created by tianqing.wang
 * DATE : 16/2/5
 * TIME : 下午2:31
 * PROJECT : squirrel
 * PACKAGE : com.github.celeskyking.squirrel.serialize
 *
 * @author <a href="mailto:celeskyking@163.com">tianqing.wang</a>
 */
public class MsgPackDecoder implements ITaskDecoder {


    @Override
    public TaskSignature decode(byte[] bytes) throws SerializeException {
        MessagePack messagePack = new MessagePack();
        try {
            return messagePack.read(bytes,TaskSignature.class);
        } catch (IOException e) {
            throw new SerializeException(e);
        }
    }

}
