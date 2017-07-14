package org.creation.singlejob.manager.excutionresponsecache;

import java.util.concurrent.TimeUnit;

import org.redisson.api.RedissonClient;
import org.redisson.client.RedisException;

public class RedissonExcutionResponseCache extends LocalExcutionResponseCache {
    private static final String SINGLE_JOB_REDISSON_EXCUTION_RESPONSE_CACHE = "SJRespCache_";

    RedissonClient redissonClient;

    private long surviveTime;

    private TimeUnit surviveTimeUnit;

    public RedissonExcutionResponseCache(RedissonClient redissonClient, long surviveTime, TimeUnit surviveTimeUnit) {
        this.redissonClient = redissonClient;
        this.surviveTime = surviveTime;
        this.surviveTimeUnit = surviveTimeUnit;
    }

    @Override
    public Object getIfPresent(Object t) {
        try {
            return redissonClient.getMap(SINGLE_JOB_REDISSON_EXCUTION_RESPONSE_CACHE + t.toString()).get(t);
        } catch (RedisException e) {
            return super.getIfPresent(t);
        }
    }

    @Override
    public void put(Object t, Object g) {
        try {
            redissonClient.getMap(SINGLE_JOB_REDISSON_EXCUTION_RESPONSE_CACHE + t.toString()).put(t, g);
            redissonClient.getMap(SINGLE_JOB_REDISSON_EXCUTION_RESPONSE_CACHE + t.toString()).expire(surviveTime,
                    surviveTimeUnit);
        } catch (RedisException e) {
            super.put(t, g);
        }
    }

}
