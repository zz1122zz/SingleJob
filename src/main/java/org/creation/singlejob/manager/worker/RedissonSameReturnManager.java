package org.creation.singlejob.manager.worker;

import org.creation.singlejob.manager.observerpool.RedissonObserverPool;

public class RedissonSameReturnManager extends SameReturnManager {
    
    public RedissonSameReturnManager(boolean readCacheIfExist) {
        super(readCacheIfExist);
        // TODO Auto-generated constructor stub
    }
    
    @Override
    public void callObservers(String uniqueKey, Object resp) {
        if(observerPool instanceof RedissonObserverPool)
        {
            ((RedissonObserverPool)observerPool).publish(uniqueKey, resp);
        }
    }
}
