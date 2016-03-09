package com.github.celeskyking.squirrel.serialize;

import com.github.celeskyking.squirrel.exception.SerializeException;
import com.github.celeskyking.squirrel.task.TaskSignature;

/**
 * Created by tianqing.wang
 * DATE : 16/2/5
 * TIME : 上午11:03
 * PROJECT : squirrel
 * PACKAGE : com.qunar.squirrel.serialize
 *
 * @author <a href="mailto:celeskyking@163.com">tianqing.wang</a>
 */
public interface ITaskDecoder {

    /**
     * 将字节转化为类,但是不支持List类型,需要把集合类或者Map放置在里面
     * */
    TaskSignature decode(byte[] bytes) throws SerializeException;

}
