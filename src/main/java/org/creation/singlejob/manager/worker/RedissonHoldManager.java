package org.creation.singlejob.manager.worker;

import org.creation.singlejob.manager.observerpool.RedissonObserverPool;
import org.redisson.client.RedisException;

public class RedissonHoldManager extends HoldManager {

    public RedissonHoldManager(boolean readCacheIfExist) {
        super(readCacheIfExist);
        // TODO Auto-generated constructor stub
    }
    
    public RedissonHoldManager(boolean readCacheIfExist,long maxWaitMilliSecond) {
        super(readCacheIfExist,maxWaitMilliSecond);
        // TODO Auto-generated constructor stub
    }
    
    @Override
    public void callObservers(String uniqueKey, Object resp) {
        if(observerPool instanceof RedissonObserverPool)
        {
            try {
                ((RedissonObserverPool)observerPool).publish(uniqueKey, resp);
            } catch (RedisException e) {
                super.callObservers(uniqueKey, resp);
            }
        }
    }
}
