package com.shop.common.utils;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class RedisLockUtil {
    @Resource
    private RedissonClient redissonClient;
    
    /**
     * 加锁
     * @param lockKey 锁的key
     * @param timeoutSeconds 超时时间
     * @param waitTimeSeconds 等待时间
     * @return 是否获取到锁
     */
    public boolean tryLock(String lockKey, int timeoutSeconds, int waitTimeSeconds) {
        RLock lock = redissonClient.getLock(lockKey);
        try {
            return lock.tryLock(waitTimeSeconds, timeoutSeconds, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            log.error("获取分布式锁异常", e);
            return false;
        }
    }
    
    /**
     * 解锁
     * @param lockKey 锁的key
     */
    public void unlock(String lockKey) {
        RLock lock = redissonClient.getLock(lockKey);
        if(lock.isLocked() && lock.isHeldByCurrentThread()) {
            lock.unlock();
        }
    }
}