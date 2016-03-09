package com.github.celeskyking.squirrel.helper;

import com.github.celeskyking.squirrel.caller.ICaller;
import com.github.celeskyking.squirrel.task.Task;
import com.github.celeskyking.squirrel.task.TaskSignature;

/**
 * Created by tianqing.wang
 * DATE : 16-2-16
 * TIME : 下午2:58
 * PROJECT : squirrel
 * PACKAGE : com.qunar.squirrel.helper
 *
 * @author <a href="mailto:celeskyking@163.com">tianqing.wang</a>
 */
public class TaskHelper {

    public static TaskSignature build(ICaller caller, Task task){
        TaskSignature signature = new TaskSignature();
        signature.setCaller(caller.getName());
        signature.setId(task.getTaskId());
        signature.setRequestAddress(NetHelper.getLocalIp());
        signature.setTask(task);
        signature.setWorker(caller.getWorker());
        return signature;
    }

    public static TaskSignature build(String caller,String worker,Task task){
        TaskSignature signature = new TaskSignature();
        signature.setCaller(caller);
        signature.setId(task.getTaskId());
        signature.setRequestAddress(NetHelper.getLocalIp());
        signature.setTask(task);
        signature.setWorker(worker);
        return signature;
    }


}
