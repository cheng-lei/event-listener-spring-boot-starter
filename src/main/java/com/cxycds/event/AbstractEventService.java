package com.cxycds.event;

import com.cxycds.event.support.EventListenerRegistry;
import com.cxycds.event.support.EventTransactionSynchronizationAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.List;

/**
 * Created by leicheng on 2020/10/10.
 */
public abstract class AbstractEventService{
    private final static Logger logger = LoggerFactory.getLogger(AbstractEventService.class);

    @Resource
    protected EventListenerRegistry registry;
    @Resource
    protected ApplicationContext applicationContext;

    public void publishLocal(String eventName, Object... params) {
        List<Method> methodSet = this.registry.getListeners(this.getClass(),eventName);
        if (CollectionUtils.isEmpty(methodSet)) {
            logger.warn("没有找到对应的监听器,eventName:{}", eventName);
            return;
        }
        for (Method eventMethod : methodSet) {
            eventMethod.setAccessible(true);
            try {
                Class clazz = eventMethod.getDeclaringClass();
                eventMethod.invoke(applicationContext.getBean(clazz), params);
            } catch (Exception e) {
                logger.error("执行监听器方法失败,eventName:{},params:{}", eventName, params, e);
            }
        }
    }

    protected EventTransactionSynchronizationAdapter getEventTransaction(){
        return new EventTransactionSynchronizationAdapter(this);
    }

    abstract public void publishEvent(String eventName, Object... params);

    protected void publishEventAfterTransactionSuccess(String eventName, Object... params) {
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
            public void afterCommit() {
                getEventTransaction().getAbstractEventService().publishEvent(eventName, params);
            }
        });
    }

    protected void publishEventAfterTransactionCompletion(String eventName, Object... params) {
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
            @Override
            public void afterCompletion(int status) {
                getEventTransaction().getAbstractEventService().publishEvent(eventName, params);
            }
        });
    }
}
