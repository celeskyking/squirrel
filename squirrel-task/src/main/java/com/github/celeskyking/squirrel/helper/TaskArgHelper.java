package com.github.celeskyking.squirrel.helper;

import com.github.celeskyking.squirrel.task.Context;
import com.github.celeskyking.squirrel.task.handler.TaskHandler;
import com.github.celeskyking.squirrel.exception.ArgTypeException;
import com.github.celeskyking.squirrel.exception.ArgTypeNotMatchException;
import com.github.celeskyking.squirrel.task.ArgType;
import com.github.celeskyking.squirrel.task.TaskArg;

import java.lang.reflect.Method;
import java.util.List;

/**
 * Created by tianqing.wang
 * DATE : 16/2/2
 * TIME : 下午5:24
 * PROJECT : squirrel
 * PACKAGE : com.qunar.squirrel.helper
 *
 * @author <a href="mailto:celeskyking@163.com">tianqing.wang</a>
 */
public class TaskArgHelper {

    public static Object[] values(TaskHandler taskHandler, Context context){
        checkType(taskHandler,context);
        return context.getTaskArgs();
    }


    private static void checkType(TaskHandler task, Context context){
        Method method = task.getMethod();
        Class<?>[] classes = method.getParameterTypes();
        Object[] params = context.getTaskArgs();
        if(classes.length!=params.length){
            throw new ArgTypeException("参数个数不匹配,task:"+task.toString());
        }
        for(int i=0; i< classes.length; i++){
            if(params[i].getClass()!=classes[i]){
                throw new ArgTypeNotMatchException("参数类型不匹配,task:"+task.toString());
            }
        }
    }

    public static Context asContext(List<TaskArg> taskArgs){
        Context context = new Context();
        Object[] objects = new Object[taskArgs.size()];
        for(int i=0;i<taskArgs.size();i++){
            Object value = ArgType.with(taskArgs.get(i).getType()).decode(taskArgs.get(i).getValue());
            context.add("arg"+i,value);
            objects[i] = value;
        }
        context.add(Context.ARGS,objects);
        return context;
    }

    public static Context asContext(List<TaskArg> taskArgs,Context context){
        Context newContext = asContext(taskArgs);
        newContext.setParent(context);
        return newContext;
    }
}
