package com.github.celeskyking.squirrel.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by tianqing.wang
 * DATE : 16-2-14
 * TIME : 上午11:17
 * PROJECT : squirrel
 * PACKAGE : com.github.celeskyking.squirrel.annotation
 *
 * @author <a href="mailto:celeskyking@163.com">tianqing.wang</a>
 */
public class Arg {

    @Target({ElementType.PARAMETER})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface INT{

    }
    @Target({ElementType.PARAMETER})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface DOUBLE{

    }

    @Target({ElementType.PARAMETER})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface LONG{

    }

    @Target({ElementType.PARAMETER})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface TEXT{

    }

    @Target({ElementType.PARAMETER})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface SHORT{

    }
    @Target({ElementType.PARAMETER})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface FLOAT{

    }

    @Target({ElementType.PARAMETER})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface DATE{

    }
}
