package com.chanlee.shortlink.project.config;

import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 布隆过滤器配置
 */
@Configuration
public class RBloomFilterConfiguration {
    /**
     * 防止短链接注册查询数据库的布隆过滤器
     */
    @Bean
    public RBloomFilter<String> shortlinkCreateCachePenetrationBloomFilter (RedissonClient redissonClient) {
        RBloomFilter<String> cachePenetrationBloomFilter = redissonClient.getBloomFilter("shortlinkCreateCachePenetrationBloomFilter");
        cachePenetrationBloomFilter.tryInit(1000000, 0.01);
        return cachePenetrationBloomFilter;
    }
}
