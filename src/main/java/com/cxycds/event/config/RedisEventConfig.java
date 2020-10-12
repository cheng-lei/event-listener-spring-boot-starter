package com.cxycds.event.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Created by leicheng on 2020/10/10.
 */
@Component
@ConfigurationProperties("event.listener.redis")
@ConditionalOnProperty(value = "event.listener.redis.address")
public class RedisEventConfig {
    private String address;
    private String password;
    private Integer maxConnection;
    private Integer minConnection;
    private Integer database;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getMaxConnection() {
        return maxConnection;
    }

    public void setMaxConnection(Integer maxConnection) {
        this.maxConnection = maxConnection;
    }

    public Integer getMinConnection() {
        return minConnection;
    }

    public void setMinConnection(Integer minConnection) {
        this.minConnection = minConnection;
    }

    public Integer getDatabase() {
        return database;
    }

    public void setDatabase(Integer database) {
        this.database = database;
    }
}
