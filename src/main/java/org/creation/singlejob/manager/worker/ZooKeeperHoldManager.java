package org.creation.singlejob.manager.worker;

import org.creation.singlejob.manager.observerpool.RedissonObserverPool;
import org.creation.singlejob.manager.observerpool.ZooKeeperObserverPool;
import org.redisson.client.RedisException;

public class ZooKeeperHoldManager extends HoldManager {

    public ZooKeeperHoldManager(boolean readCacheIfExist) {
        super(readCacheIfExist);
        // TODO Auto-generated constructor stub
    }
    
    @Override
    public void callObservers(String uniqueKey, Object resp) {
        if(observerPool instanceof RedissonObserverPool)
        {
            try {
                ((ZooKeeperObserverPool)observerPool).publish(uniqueKey, resp);
            } catch (RedisException e) {
                super.callObservers(uniqueKey, resp);
            }
        }
    }
}
