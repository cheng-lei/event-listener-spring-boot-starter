package com.cxycds.event.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * Created by leicheng on 2020/10/10.
 */
@Service
public class RedisService {
    @Resource
    private RedisEventConfig redisEventConfig;
    RedissonClient redissonClient;

    @PostConstruct
    public void init() {
        Config config = new Config();
        SingleServerConfig singleServerConfig = config.useSingleServer();
        singleServerConfig.setAddress(redisEventConfig.getAddress());
        singleServerConfig.setPassword(redisEventConfig.getPassword());
        if(redisEventConfig.getDatabase()!=null) {
            singleServerConfig.setDatabase(redisEventConfig.getDatabase());
        }
        if(redisEventConfig.getMaxConnection()!=null) {
            singleServerConfig.setConnectionMinimumIdleSize(redisEventConfig.getMinConnection());
        }
        if(redisEventConfig.getMinConnection()!=null) {
            singleServerConfig.setConnectionPoolSize(redisEventConfig.getMaxConnection());
        }
        redissonClient = Redisson.create(config);
    }

    public RedissonClient getRedissonClient(){
        return this.redissonClient;
    }
}
