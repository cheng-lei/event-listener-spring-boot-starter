package com.cxycds.event.impl;

import com.cxycds.event.AbstractEventService;
import com.cxycds.event.support.EventTransactionSynchronizationAdapter;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;

/**
 * Created by leicheng on 2020/10/10.
 */
@Component
public class LocalAsyncEventService extends AbstractEventService {
    private TaskExecutor asyncEventListenerExecutor = new SimpleAsyncTaskExecutor("LocalAsyncEventService-");

    @Override
    public void publishEvent(String eventName, Object... params) {
        this.asyncEventListenerExecutor.execute(() -> {
            this.publishLocal(eventName, params);
        });
    }
}
