package org.creation.singlejob.manager.singlejobpool;

import java.util.concurrent.TimeUnit;

import org.creation.singlejob.manager.SingleJobManager;
import org.redisson.api.RedissonClient;
import org.redisson.client.RedisException;

public class RedissonSingleJobPool extends LocalHashMapSingleJobPool {
    
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
        } catch (RedisException e) {
            //判定为redis服务不可用，决定降级
            return super.putIntoExecutorPool(key, worker);
        }catch (InterruptedException e) {
            //超时时间为0，即立马返回失败
            return false;
        }
    }

    @Override
    public boolean removeFromJobPool(String key) {
        try {
            redissonClient.getLock(key).unlock();
        }catch (RedisException e) {
            //判定为redis服务不可用，决定降级
            return super.removeFromJobPool(key);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

}
