package org.creation.singlejob.manager.excutionresponsecache;

import java.util.concurrent.TimeUnit;

import org.redisson.api.RedissonClient;

public class RedissonExcutionResponseCache implements ExcutionResponseCache<Object, Object> {
    private static final String SINGLE_JOB_REDISSON_EXCUTION_RESPONSE_CACHE = "SJRespCache_";
    RedissonClient redissonClient;

    public RedissonExcutionResponseCache(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    @Override
    public Object getIfPresent(Object t) {
       return redissonClient.getMap(SINGLE_JOB_REDISSON_EXCUTION_RESPONSE_CACHE+t.toString()).get(t);
    }

    @Override
    public void put(Object t, Object g) {
        redissonClient.getMap(SINGLE_JOB_REDISSON_EXCUTION_RESPONSE_CACHE+t.toString()).put(t, g);
        redissonClient.getMap(SINGLE_JOB_REDISSON_EXCUTION_RESPONSE_CACHE+t.toString()).expire(100, TimeUnit.SECONDS);
    }

}
