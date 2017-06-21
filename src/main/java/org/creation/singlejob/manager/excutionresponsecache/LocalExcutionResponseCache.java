package org.creation.singlejob.manager.excutionresponsecache;

import org.creation.singlejob.manager.observerpool.LocalConcurrentHashMapObserverPool;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

public class LocalExcutionResponseCache implements ExcutionResponseCache<Object, Object> {
    private static LocalExcutionResponseCache instance;

    private static Cache<Object, Object> respCache = CacheBuilder.newBuilder()
            .maximumSize(1000)
            .build();
    
    public static LocalExcutionResponseCache getInstance() { // 对获取实例的方法进行同步
        if (instance == null) {
            synchronized (LocalConcurrentHashMapObserverPool.class) {
                if (instance == null)
                    instance = new LocalExcutionResponseCache();
            }
        }
        return instance;
    }
    @Override
    public Object getIfPresent(Object t) {
        // TODO Auto-generated method stub
        return respCache.getIfPresent(t);
    }

    @Override
    public void put(Object t, Object g) {
        // TODO Auto-generated method stub
        respCache.put(t, g);;
    }

}
