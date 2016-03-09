package com.github.celeskyking.squirrel.demo;


import com.github.celeskyking.squirrel.tinytask.DoThis;
import com.github.celeskyking.squirrel.tinytask.Result;
import com.github.celeskyking.squirrel.tinytask.Something;
import com.github.celeskyking.squirrel.tinytask.TinyTask;
import com.github.celeskyking.squirrel.tinytask.context.Context;

/**
 * Created by tianqing.wang
 * DATE : 16/2/24
 * TIME : 下午4:09
 * PROJECT : squirrel
 * PACKAGE : com.github.celeskyking.squirrel.demo
 *
 * @author <a href="mailto:celeskyking@163.com">tianqing.wang</a>
 */
public class TinyTaskDemo {


    public static void main(String[] args) {
        TinyTask.perform(new Something<String>() {

            @Override
            public String doIt(Context context) {
                throw new RuntimeException();
            }
        }).context("name","tian").whenDone(new DoThis<String>() {
            @Override
            public void doThis(Result<String> t) {
                if(t.isSuccess()){
                    System.out.println(t.getResult());
                }else{
                    t.getThrowable().printStackTrace();
                }
            }
        }).go();
    }
}
