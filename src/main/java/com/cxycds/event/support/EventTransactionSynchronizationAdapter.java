package com.cxycds.event.support;

import com.cxycds.event.AbstractEventService;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;

/**
 * Created by leicheng on 2020/10/10.
 */
public class EventTransactionSynchronizationAdapter extends TransactionSynchronizationAdapter {
    private AbstractEventService eventService;

    public EventTransactionSynchronizationAdapter(AbstractEventService abstractEventService) {
        this.eventService = eventService;
    }

    public AbstractEventService getAbstractEventService() {
        return eventService;
    }
}
