package org.creation.singlejob.manager.observerpool;

import java.util.List;

import org.redisson.api.RedissonClient;
import org.redisson.api.listener.MessageListener;

import org.creation.singlejob.manager.SingleJobManager;

public class RedissonObserverPool implements ObserverPool<SingleJobManager> {
    
    RedissonClient redissonClient;

    public RedissonObserverPool(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    @Override
    public boolean putMeIntoObserverPool(String key, final SingleJobManager g) {
        return redissonClient.getTopic(key).addListener(new MessageListener<Object>(){
            @Override
            public void onMessage(String channel, Object msg) {
                g.pizzaDeliverd(msg);
                synchronized (g) {
                    g.notify();
                }
            }
        })>0;
    }

    @Override
    public List<SingleJobManager> getObserverList(String uniqueKey) {
        return null;
    }

    @Override
    public SingleJobManager getAndRemoveSingleJobManagerFromObserverList(String uniqueKey) {
        return null;
    }

    public long publish(String key, Object resp) {
        return redissonClient.getTopic(key).publish(resp);
    }

}
