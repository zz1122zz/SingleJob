package org.creation.singlejob.manager.worker;

import org.apache.zookeeper.KeeperException;
import org.creation.singlejob.manager.observerpool.ZooKeeperObserverPool;

public class ZooKeeperSameReturnManager extends SameReturnManager {
    
    public ZooKeeperSameReturnManager(boolean readCacheIfExist) {
        super(readCacheIfExist);
        // TODO Auto-generated constructor stub
    }
    
    public ZooKeeperSameReturnManager(boolean readCacheIfExist,long maxWaitMilliSecond) {
        super(readCacheIfExist,maxWaitMilliSecond);
        // TODO Auto-generated constructor stub
    }
    
    @Override
    public void callObservers(String uniqueKey, Object resp) {
        if(observerPool instanceof ZooKeeperObserverPool)
        {
            try {
                ((ZooKeeperObserverPool)observerPool).publish(uniqueKey, resp);
            } catch (KeeperException e) {
                super.callObservers(uniqueKey, resp);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}
