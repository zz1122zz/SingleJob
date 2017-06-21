package org.creation.singlejob.manager.singlejobpool;

import org.creation.singlejob.manager.SingleJobManager;
import org.redisson.api.RedissonClient;

public class RedissonSingleJobPool implements SingleJobPool<String , SingleJobManager> {
    
    RedissonClient redissonClient;
    
    public RedissonSingleJobPool(RedissonClient redissonClient)
    {
        this.redissonClient = redissonClient;
    }

    @Override
    public boolean putIntoExecutorPool(String key, SingleJobManager worker) {
        return redissonClient.getLock(key).tryLock();
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
