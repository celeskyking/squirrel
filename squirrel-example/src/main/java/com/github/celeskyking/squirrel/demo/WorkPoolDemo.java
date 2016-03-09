package com.github.celeskyking.squirrel.demo;

import com.github.celeskyking.squirrel.tinytask.Result;
import com.github.celeskyking.squirrel.tinytask.workerpool.DoThese;
import com.github.celeskyking.squirrel.tinytask.workerpool.IMapper;
import com.github.celeskyking.squirrel.tinytask.workerpool.WorkerPool;
import com.google.common.base.Function;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * Created by tianqing.wang
 * DATE : 16/2/24
 * TIME : 下午4:22
 * PROJECT : squirrel
 * PACKAGE : com.qunar.squirrel.demo
 *
 * @author <a href="mailto:celeskyking@163.com">tianqing.wang</a>
 */
public class WorkPoolDemo {

    public static void main(String[] args) {
        WorkerPool.<Integer,String>load(Lists.newArrayList(1,2,3)).map(new IMapper<Integer, String>() {
            @Override
            public String map(Integer task) {
                return String.valueOf(task);
            }
        }).perform(new Function<String, String>() {
            @Override
            public String apply(String task) {
                return "$"+task;
            }
        }).collect(new DoThese<String>() {
            @Override
            public void doThese(List<Result<String>> results) {
                System.out.println(results.size());
            }
        }).go();
    }
}
