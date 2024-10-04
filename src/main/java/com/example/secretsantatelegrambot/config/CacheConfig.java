package com.example.secretsantatelegrambot.config;


import com.example.secretsantatelegrambot.entity.User;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
@RequiredArgsConstructor
public class CacheConfig {
    @Value("${cache.users.max_size}")
    private long maxSize;

    @Value("${cache.users.refresh_after_write_duration}")
    private long refreshAfterWriteDuration;

    @Value("${cache.users.expire_after_write_duration}")
    private long expireAfterWriteDuration;

    @Bean("secretSantaCacheManager")
    public CacheManager cacheManager(Cache cache) {
        CaffeineCacheManager caffeineCacheManager = new CaffeineCacheManager();
        caffeineCacheManager.registerCustomCache("users", cache);
        return caffeineCacheManager;
    }

    @Bean
    public Cache<Long, User> userCustomCache() {
        return Caffeine.newBuilder()
                .maximumSize(maxSize)
                .expireAfterWrite(expireAfterWriteDuration, TimeUnit.SECONDS)
                .build();
    }
}
