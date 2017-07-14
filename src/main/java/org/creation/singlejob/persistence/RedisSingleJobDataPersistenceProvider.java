package org.creation.singlejob.persistence;

import java.util.concurrent.TimeUnit;

import org.creation.singlejob.manager.SingleJobManager;
import org.creation.singlejob.manager.excutionresponsecache.ExcutionResponseCache;
import org.creation.singlejob.manager.excutionresponsecache.RedissonExcutionResponseCache;
import org.creation.singlejob.manager.observerpool.ObserverPool;
import org.creation.singlejob.manager.observerpool.RedissonObserverPool;
import org.creation.singlejob.manager.singlejobpool.RedissonSingleJobPool;
import org.creation.singlejob.manager.singlejobpool.SingleJobPool;
import org.redisson.api.RedissonClient;

public class RedisSingleJobDataPersistenceProvider extends LocalMemoryDataPersistenceProvider {
    
    public boolean useLocalMemoryCache = false;

    RedissonClient redissonClient;
    private long leaseTime = 10;
    private TimeUnit unit = TimeUnit.SECONDS;
    private long surviveTime = 60;
    private TimeUnit surviveTimeUnit = TimeUnit.SECONDS;
    
    @Override
    public SingleJobPool<String, SingleJobManager> initSingleJobPool() {
        //return new RedisSingleJobPool(jedisRankConnFactory);
        return new RedissonSingleJobPool(redissonClient, leaseTime,unit);
    }

    @Override
    public ObserverPool<SingleJobManager> initObserverPool() {
        return new RedissonObserverPool(redissonClient);
    }

    @Override
    public ExcutionResponseCache<Object, Object> initlExcutionResponseCache() {
        if(useLocalMemoryCache)
        {
            return super.initlExcutionResponseCache();
        }
        return new RedissonExcutionResponseCache(redissonClient, surviveTime,surviveTimeUnit);
    }

    public RedissonClient getRedissonClient() {
        return redissonClient;
    }

    public void setRedissonClient(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    public void setLeaseTime(long leaseTime,TimeUnit unit ) {
        this.leaseTime = leaseTime;
        this.unit = unit;
    }

    public void setExcutionResponseCacheSurviveTime(long surviveTime,TimeUnit surviveTimeUnit ) {
        this.surviveTime = surviveTime;
        this.surviveTimeUnit = surviveTimeUnit;
    }
}
