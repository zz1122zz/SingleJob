package org.creation.singlejob.manager.singlejobpool;

import java.util.concurrent.TimeUnit;

import org.creation.singlejob.manager.SingleJobManager;
import org.redisson.api.RedissonClient;

public class RedissonSingleJobPool implements SingleJobPool<String , SingleJobManager> {
    
    RedissonClient redissonClient;
    private long leaseTime;
    private TimeUnit unit = TimeUnit.SECONDS;
    
    public RedissonSingleJobPool(RedissonClient redissonClient)
    {
        this.redissonClient = redissonClient;
    }
    public RedissonSingleJobPool(RedissonClient redissonClient,long leaseTime,TimeUnit unit )
    {
        this.redissonClient = redissonClient;
        this.leaseTime = leaseTime;
        this.unit = unit;
    }

    @Override
    public boolean putIntoExecutorPool(String key, SingleJobManager worker) {
        try {
            return redissonClient.getLock(key).tryLock(0, leaseTime, unit);
        } catch (InterruptedException e) {
            //超时时间为0，即立马返回失败
            return false;
        }
    }

    @Override
    public boolean removeFromJobPool(String key) {
        try {
            redissonClient.getLock(key).unlock();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

}
