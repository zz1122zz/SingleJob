package org.creation.singlejob.persistence;

import org.creation.singlejob.manager.SingleJobManager;
import org.creation.singlejob.manager.excutionresponsecache.ExcutionResponseCache;
import org.creation.singlejob.manager.excutionresponsecache.RedissonExcutionResponseCache;
import org.creation.singlejob.manager.observerpool.ObserverPool;
import org.creation.singlejob.manager.observerpool.RedissonObserverPool;
import org.creation.singlejob.manager.singlejobpool.RedissonSingleJobPool;
import org.creation.singlejob.manager.singlejobpool.SingleJobPool;
import org.redisson.api.RedissonClient;

public class RedisSingleJobDataPersistenceProvider implements SingleJobDataPersistenceProvider {

    RedissonClient redissonClient;
    
    @Override
    public SingleJobPool<String, SingleJobManager> initSingleJobPool() {
        //return new RedisSingleJobPool(jedisRankConnFactory);
        return new RedissonSingleJobPool(redissonClient);
    }

    @Override
    public ObserverPool<SingleJobManager> initObserverPool() {
        return new RedissonObserverPool(redissonClient);
    }

    @Override
    public ExcutionResponseCache<Object, Object> initlExcutionResponseCache() {
        return new RedissonExcutionResponseCache(redissonClient);
    }

    public RedissonClient getRedissonClient() {
        return redissonClient;
    }

    public void setRedissonClient(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

}
