package org.creation.singlejob.manager.observerpool;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import com.google.common.collect.Lists;
import org.creation.singlejob.manager.SingleJobManager;

public class LocalConcurrentHashMapObserverPool implements ObserverPool<SingleJobManager> {
    private static LocalConcurrentHashMapObserverPool instance;

    private static ConcurrentHashMap<String, List<SingleJobManager>> observerPool = new ConcurrentHashMap<String, List<SingleJobManager>>();
    
    public static LocalConcurrentHashMapObserverPool getInstance() { // 对获取实例的方法进行同步
        if (instance == null) {
            synchronized (LocalConcurrentHashMapObserverPool.class) {
                if (instance == null)
                    instance = new LocalConcurrentHashMapObserverPool();
            }
        }
        return instance;
    }
    
    @Override
    public boolean putMeIntoObserverPool(String uniqueKey, SingleJobManager singleJobManager) {
        synchronized(observerPool)
        {
            if(observerPool.containsKey(uniqueKey))
            {
                return observerPool.get(uniqueKey).add(singleJobManager);
            }else
            {
                List<SingleJobManager> SingleJobManagerList = new ArrayList<SingleJobManager>();
                SingleJobManagerList.add(singleJobManager);
                observerPool.put(uniqueKey, SingleJobManagerList);
                return true;
            }
        }
    }

    @Override
    public List<SingleJobManager> getObserverList(String uniqueKey) {
        synchronized(observerPool)
        {
            if(observerPool.containsKey(uniqueKey))
            {
                return observerPool.get(uniqueKey);
            }else
            {
                return Lists.newArrayList();
            }
        }
    }

    @Override
    public SingleJobManager getAndRemoveSingleJobManagerFromObserverList(String uniqueKey) {
        synchronized(observerPool)
        {
            if(observerPool.containsKey(uniqueKey)&&!observerPool.get(uniqueKey).isEmpty())
            {
                return observerPool.get(uniqueKey).remove(0);
            }else
            {
                return null;
            }
        }
    }

}
