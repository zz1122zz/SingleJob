package org.creation.singlejob.persistence;

import java.util.concurrent.TimeUnit;

import org.apache.curator.framework.CuratorFramework;
import org.creation.singlejob.manager.SingleJobManager;
import org.creation.singlejob.manager.excutionresponsecache.ExcutionResponseCache;
import org.creation.singlejob.manager.excutionresponsecache.ZooKeeperExcutionResponseCache;
import org.creation.singlejob.manager.observerpool.ObserverPool;
import org.creation.singlejob.manager.observerpool.ZooKeeperObserverPool;
import org.creation.singlejob.manager.singlejobpool.SingleJobPool;
import org.creation.singlejob.manager.singlejobpool.ZooKeeperSingleJobPool;

public class ZooKeeperSingleJobDataPersistenceProvider extends LocalMemoryDataPersistenceProvider {
    
    public boolean useLocalMemoryCache = false;

    CuratorFramework zooKeeperClient;
    
    private long leaseTime = 10;
    private TimeUnit unit = TimeUnit.SECONDS;
    private long surviveTime = 60;
    private TimeUnit surviveTimeUnit = TimeUnit.SECONDS;
    
    @Override
    public SingleJobPool<String, SingleJobManager> initSingleJobPool() {
        return new ZooKeeperSingleJobPool(zooKeeperClient, leaseTime,unit);
    }

    @Override
    public ObserverPool<SingleJobManager> initObserverPool() {
        return new ZooKeeperObserverPool(zooKeeperClient);
    }

    @Override
    public ExcutionResponseCache<Object, Object> initlExcutionResponseCache() {
        if(useLocalMemoryCache)
        {
            return super.initlExcutionResponseCache();
        }
        return new ZooKeeperExcutionResponseCache(zooKeeperClient, surviveTime,surviveTimeUnit);
    }
    

    public CuratorFramework getZooKeeperClient() {
        return zooKeeperClient;
    }

    public void setZooKeeperClient(CuratorFramework zooKeeperClient) {
        this.zooKeeperClient = zooKeeperClient;
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
