package com.github.celeskyking.squirrel.serialize;

import com.github.celeskyking.squirrel.task.TaskSignature;
import com.github.celeskyking.squirrel.exception.SerializeException;
import org.msgpack.MessagePack;

import java.io.IOException;

/**
 * Created by tianqing.wang
 * DATE : 16/2/5
 * TIME : 下午2:25
 * PROJECT : squirrel
 * PACKAGE : com.github.celeskyking.squirrel.serialize
 *
 * @author <a href="mailto:celeskyking@163.com">tianqing.wang</a>
 */
public class MsgPackEncoder implements ITaskEncoder {


    @Override
    public byte[] encode(TaskSignature task) throws SerializeException {
        MessagePack messagePack = new MessagePack();
        try {
            return messagePack.write(task);
        } catch (IOException e) {
            throw new SerializeException(e);
        }
    }
}
