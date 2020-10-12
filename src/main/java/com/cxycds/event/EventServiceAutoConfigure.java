package com.cxycds.event;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Created by leicheng on 2020/10/12.
 */
@Configuration
@EnableConfigurationProperties
@ComponentScan(basePackages = {"com.cxycds.event"})
public class EventServiceAutoConfigure {

    @Bean
    EventServiceStrategy getEventServiceStrategy() {
        return new EventServiceStrategy();
    }
}
