package com.github.celeskyking.squirrel;

import com.github.celeskyking.squirrel.serialize.ITaskDecoder;
import com.github.celeskyking.squirrel.serialize.ITaskEncoder;

/**
 * Created by tianqing.wang
 * DATE : 16-2-16
 * TIME : 下午5:46
 * PROJECT : squirrel
 * PACKAGE : com.github.celeskyking.squirrel
 *
 * @author <a href="mailto:celeskyking@163.com">tianqing.wang</a>
 */
public class Config {

    private ITaskEncoder encoder;

    private ITaskDecoder decoder;

    public ITaskEncoder getEncoder() {
        return encoder;
    }

    public void setEncoder(ITaskEncoder encoder) {
        this.encoder = encoder;
    }

    public ITaskDecoder getDecoder() {
        return decoder;
    }

    public void setDecoder(ITaskDecoder decoder) {
        this.decoder = decoder;
    }
}
