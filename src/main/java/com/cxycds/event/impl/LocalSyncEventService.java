package com.cxycds.event.impl;

import com.cxycds.event.AbstractEventService;
import com.cxycds.event.support.EventTransactionSynchronizationAdapter;
import org.springframework.stereotype.Component;

/**
 * Created by leicheng on 2020/10/10.
 */
@Component
public class LocalSyncEventService extends AbstractEventService {
    @Override
    public void publishEvent(String eventName, Object... params) {
        this.publishLocal(eventName,params);
    }
}
