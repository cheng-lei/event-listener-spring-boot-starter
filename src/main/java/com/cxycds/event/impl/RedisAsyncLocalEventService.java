package com.cxycds.event.impl;

import com.cxycds.event.AbstractEventService;
import com.cxycds.event.annotation.EventListener;
import com.cxycds.event.config.RedisService;
import org.redisson.api.RTopic;
import org.redisson.api.listener.MessageListener;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by leicheng on 2020/10/10.
 */
@Component
public class RedisAsyncLocalEventService extends AbstractEventService {
    @Resource
    private RedisService redisService;

    @PostConstruct
    public void init() {
        Map<String, List<Method>> map = this.registry.getListeners(RedisAsyncLocalEventService.class);
        for (Map.Entry<String, List<Method>> entry : map.entrySet()) {
            RTopic topic = redisService.getRedissonClient().getTopic(entry.getKey());
            List<Method> methods = entry.getValue();
            for (Method m : methods) {
                // 接受订阅的消息
                topic.addListener(Object[].class, (MessageListener<Object[]>) (charSequence, params) -> {
                    System.out.println("char:"+charSequence+",params:"+params);
                    this.publishLocal(m.getAnnotation(EventListener.class).name(), params);
                });
            }
        }
    }

    @Override
    public void publishEvent(String eventName, Object... params) {
        RTopic rTopic = redisService.getRedissonClient().getTopic(eventName);
        rTopic.publish(params);
    }
}
