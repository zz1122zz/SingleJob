package org.creation.singlejob.manager.observerpool;

import org.creation.singlejob.manager.SingleJobManager;
import org.redisson.api.RedissonClient;
import org.redisson.api.listener.MessageListener;
import org.redisson.client.RedisException;

public class RedissonObserverPool extends LocalConcurrentHashMapObserverPool {
    
    RedissonClient redissonClient;

    public RedissonObserverPool(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    @Override
    public boolean putMeIntoObserverPool(String key, final SingleJobManager g) {
        try {
            return redissonClient.getTopic(key).addListener(new MessageListener<Object>() {
                @Override
                public void onMessage(String channel, Object msg) {
                    g.pizzaDeliverd(msg);
                    synchronized (g) {
                        g.notify();
                    }
                }
            }) > 0;
        } catch (RedisException e) {
            return super.putMeIntoObserverPool(key, g);
        }
    }

    public long publish(String key, Object resp) {
        return redissonClient.getTopic(key).publish(resp);
    }

}
