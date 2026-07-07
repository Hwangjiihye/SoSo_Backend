package com.soso.global.util;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class RedisService {
    private final StringRedisTemplate redisTemplate;

    // 롬복 없이 직접 생성자 작성 (스프링이 자동으로 의존성 주입)
    public RedisService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void setValuesWithTimeout(String key, String value, long timeoutMillis) {
        redisTemplate.opsForValue().set(key, value, Duration.ofMillis(timeoutMillis));
    }

    public String getValues(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public void deleteValues(String key) {
        redisTemplate.delete(key);
    }
}
