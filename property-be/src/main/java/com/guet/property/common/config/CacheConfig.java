package com.guet.property.common.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.guet.property.dto.session.SessionUserInfo;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.concurrent.TimeUnit;

/**
 * 缓存配置类
 *
 * @author dhxstart
 * @date 2021/12/7 23:43
 */
@Configuration
@EnableCaching
public class CacheConfig {

    /**
     * 配置默认的缓存管理器
     *
     * @return CacheManager
     */
    @Primary
    @Bean("defaultCacheManager")
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.setCaffeine(Caffeine.newBuilder()
                // 设置最后一次写入后经过固定时间过期.
                .expireAfterWrite(10, TimeUnit.SECONDS)
                // 初始的缓存空间大小
                .initialCapacity(100)
                // 缓存的最大条数
                .maximumSize(1000));
        return cacheManager;
    }

    /**
     * 此方法用于将token存放在redis中
     *
     * @return Cache<SessionUserInfo>
     */
    @Bean("tokenCacheManager")
    public Cache<String, SessionUserInfo> caffeineCache() {
        return Caffeine.newBuilder()
                // 设置最后一次访问后经过固定时间过期.
                .expireAfterAccess(30L, TimeUnit.MINUTES)
                // 初始的缓存空间大小
                .initialCapacity(100)
                // 缓存的最大条数
                .maximumSize(10000)
                .build();
    }
}