package com.cxycds.event;

import com.cxycds.event.annotation.EventListener;
import com.cxycds.event.impl.LocalAsyncEventService;
import com.cxycds.event.impl.LocalSyncEventService;
import com.cxycds.event.impl.RedisAsyncLocalEventService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * Created by leicheng on 2020/10/12.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {EventServiceAutoConfigure.class, EventServiceStrategyTest.class})
@ActiveProfiles("dev")
public class EventServiceStrategyTest {
    private static final String TEST_EVENT_SERVICE = "TEST_EVENT_SERVICE";
    @Resource
    private EventServiceStrategy eventServiceStrategy;

    @Test
    public void test() {
        eventServiceStrategy.publishEvent(TEST_EVENT_SERVICE, "【test-test-发送消息】");
    }

    @EventListener(name = TEST_EVENT_SERVICE, type = LocalSyncEventService.class)
    public void listLocalSync(String message) {
        System.out.println("收到Sync：" + message);
    }

    @EventListener(name = TEST_EVENT_SERVICE, type = LocalAsyncEventService.class)
    public void listLocalAsync(String message) {
        System.out.println("收到Async：" + message);
    }

    @EventListener(name = TEST_EVENT_SERVICE, type = RedisAsyncLocalEventService.class)
    public void listRedis(String message) {
        System.out.println("收到Redis：" + message);
    }
}
