package com.github.celeskyking.squirrel.broker.nsq;

import com.alibaba.fastjson.JSON;
import com.github.brainlag.nsq.NSQMessage;
import com.github.brainlag.nsq.callbacks.NSQMessageCallback;
import com.github.celeskyking.squirrel.exception.RouteKeyNotMatchException;
import com.github.celeskyking.squirrel.exception.TaskNotRunnableException;
import com.github.celeskyking.squirrel.task.TaskSignature;
import com.github.celeskyking.squirrel.task.processor.ITaskProcessor;
import com.github.celeskyking.squirrel.exception.SerializeException;
import com.github.celeskyking.squirrel.exception.TaskNotExistException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by tianqing.wang
 * DATE : 16/2/5
 * TIME : 下午3:44
 * PROJECT : squirrel
 * PACKAGE : com.github.celeskyking.squirrel.broker.nsq
 *
 * @author <a href="mailto:celeskyking@163.com">tianqing.wang</a>
 */
public class TaskMessageCallBack implements NSQMessageCallback {

    private Logger logger = LoggerFactory.getLogger(TaskMessageCallBack.class);

    private NsqBroker broker;

    private ITaskProcessor taskProcessor;

    public TaskMessageCallBack(NsqBroker broker,ITaskProcessor processor){
        this.broker = broker;
        this.taskProcessor = processor;
    }

    @Override
    public void message(NSQMessage message) {
        TaskSignature signature = null;
        try {
            signature = broker.getDecoder().decode(message.getMessage());
            taskProcessor.process(signature);
            message.finished();
        } catch (SerializeException e) {
            logger.error("序列化失败",e);
            message.requeue();
        } catch (TaskNotExistException e) {
            logger.error(e.getMessage(),e);
            message.requeue();
        } catch (TaskNotRunnableException e) {
            logger.error("任务被忽略执行,"+e.getMessage());
        } catch (RouteKeyNotMatchException e){
            logger.info("非本worker的job,info:{}"+ JSON.toJSONString(signature));
            message.requeue(1000);
        }
    }
}
