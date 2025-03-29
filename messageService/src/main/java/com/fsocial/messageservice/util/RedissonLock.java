package com.fsocial.messageservice.util;

import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
@RequiredArgsConstructor
public class RedissonLock {
    private final RedissonClient redissonClient;

    public boolean acquireLock(String key) {
        return acquireLock(key, 5, 10);
    }

    public boolean acquireLock(String key, long timeout, long ttl) {
        RLock rLock = redissonClient.getLock(key);
        try {
            return rLock.tryLock(timeout, ttl, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            return false;
        }
    }

    public void releaseLock(String key) {
        RLock rLock = redissonClient.getLock(key);
        if (rLock.isHeldByCurrentThread())
            rLock.unlock();
    }
}
