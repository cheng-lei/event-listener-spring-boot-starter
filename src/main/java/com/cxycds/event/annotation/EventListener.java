package com.cxycds.event.annotation;

import com.cxycds.event.AbstractEventService;

import java.lang.annotation.*;

/**
 * Created by leicheng on 2020/10/10.
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EventListener {
    String name();
    int order() default 0;
    Class<? extends AbstractEventService> type();
}
