package com.cxycds.event;

import com.cxycds.event.annotation.EventListener;
import com.cxycds.event.support.EventListenerRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by leicheng on 2020/10/12.
 */
public class EventServiceStrategy extends AbstractEventService{
    private static final Logger logger = LoggerFactory.getLogger(EventServiceStrategy.class);

    @Resource
    private EventListenerRegistry registry;
    @Resource
    private ApplicationContext applicationContext;

    public void publishEvent(String eventName, Object... params) {
        for (Map.Entry<String, Map<String, List<Method>>> entry : registry.getRegistry().entrySet()) {
            List<Method> methods = entry.getValue().getOrDefault(eventName, Collections.emptyList());
            if (CollectionUtils.isEmpty(methods)) {
                logger.error("不存在对应的监听器代码,eventName:{},params:{}", eventName, params);
                return;
            }
            for (Method m : methods) {
                EventListener eventListener = m.getAnnotation(EventListener.class);
                eventListener.type();
                AbstractEventService eventService = applicationContext.getBean(eventListener.type());
                eventService.publishEvent(eventName,params);
            }
        }
    }
}
