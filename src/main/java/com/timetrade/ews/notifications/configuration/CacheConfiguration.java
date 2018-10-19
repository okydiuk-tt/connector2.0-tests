package com.timetrade.ews.notifications.configuration;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.guava.GuavaCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import com.google.common.cache.CacheBuilder;

@Configuration
@EnableCaching
public class CacheConfiguration {

    @Bean
    public CacheManager cacheManager(EwsProperties ewsProperties) {
        SimpleCacheManager cacheManager = new SimpleCacheManager();

        Set<Cache> cacheSet = new HashSet<>();
        for (Map.Entry<String, String> entry : ewsProperties.getCacheSpecs().entrySet()) {
            if (!StringUtils.isEmpty(entry.getValue())) {
                cacheSet.add(new GuavaCache(entry.getKey(), CacheBuilder.from(entry.getValue()).build()));
            }
        }
        cacheManager.setCaches(cacheSet);

        return cacheManager;
    }

}
