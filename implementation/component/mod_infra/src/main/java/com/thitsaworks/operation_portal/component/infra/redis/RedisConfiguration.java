package com.thitsaworks.operation_portal.component.infra.redis;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;

public class RedisConfiguration {

    public static final String REDIS_SETTINGS_PATH = "redis/settings";

    @Bean
    public RedissonClient redissonClient(Settings settings) {

        Config config = new Config();
        config.useSingleServer().setAddress(settings.redisUrl());

        return Redisson.create(config);
    }

    public record Settings(String redisUrl) { }
}
