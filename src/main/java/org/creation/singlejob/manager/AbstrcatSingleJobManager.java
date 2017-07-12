package org.creation.singlejob.manager;


import org.creation.singlejob.SinglejobAspectSupport.InvocationCallback;
import org.creation.singlejob.key.SingleJobKeyGenerator;
import org.creation.singlejob.key.SpELSingleJobKeyGenerator;
import org.creation.singlejob.manager.excutionresponsecache.ExcutionResponseCache;
import org.creation.singlejob.manager.excutionresponsecache.LocalExcutionResponseCache;
import org.creation.singlejob.manager.observerpool.LocalConcurrentHashMapObserverPool;
import org.creation.singlejob.manager.observerpool.ObserverPool;
import org.creation.singlejob.manager.singlejobpool.LocalHashMapSingleJobPool;
import org.creation.singlejob.manager.singlejobpool.SingleJobPool;
import org.creation.singlejob.persistence.SingleJobDataPersistenceProvider;

public abstract class AbstrcatSingleJobManager implements SingleJobManager  {

    public boolean readCacheIfExist = false;
    
    private SingleJobPool<String, SingleJobManager> executorPool;
    protected ObserverPool<SingleJobManager> observerPool ;
    protected ExcutionResponseCache<Object, Object> respCache ;
    
    private SingleJobKeyGenerator keyGenerator;
    
    public AbstrcatSingleJobManager(boolean readCacheIfExist) {
        this.readCacheIfExist = readCacheIfExist;
        if(null==executorPool)
        {
            executorPool = LocalHashMapSingleJobPool.getInstance();
        }
        if(null==observerPool)
        {
            observerPool = LocalConcurrentHashMapObserverPool.getInstance();
        }
        if(null==respCache)
        {
            respCache = LocalExcutionResponseCache.getInstance();
        }
        if(null==keyGenerator)
        {
            keyGenerator = SpELSingleJobKeyGenerator.getInstance();
        }
    }
    

    @Override
    public Object proceedWithInvocation(String uniqueKey, InvocationCallback invocation) throws Throwable
    {
        if(readCacheIfExist)
        {
            Object resp = respCache.getIfPresent(uniqueKey);
            if(null!=resp)
            {
                return resp;
            }
        }
        if(executorPool.putIntoExecutorPool(uniqueKey,this))
        {
            Object resp;
            try {
                resp = invocation.proceedWithInvocation();
                if(readCacheIfExist&&null!=resp)
                {
                    respCache.put(uniqueKey, resp);
                }
            } finally {
                executorPool.removeFromJobPool(uniqueKey);
            }
            callObservers(uniqueKey,resp);
            return resp;
        }else
        {
            return handleConflict(uniqueKey, invocation);
        }
    }
    @Override
    public boolean useCache() {
        return this.readCacheIfExist;
    }


    public abstract void callObservers(String uniqueKey, Object resp) ;

    public abstract void pizzaDeliverd(Object resp);

    protected abstract Object handleConflict(String uniqueKey, InvocationCallback invocation) throws Throwable;

    protected void putMeIntoObserverPool(String uniqueKey, SingleJobManager abstrcatSingleJobManager) {
        observerPool.putMeIntoObserverPool(uniqueKey, abstrcatSingleJobManager);
    }

    @Override
    public void initPersistenceField(SingleJobDataPersistenceProvider persistenceProvider) {
        SingleJobPool<String, SingleJobManager> executorPool = persistenceProvider.initSingleJobPool();
        ObserverPool<SingleJobManager> observerPool = persistenceProvider.initObserverPool();
        ExcutionResponseCache<Object, Object> respCache = persistenceProvider.initlExcutionResponseCache();
        if(null!=executorPool)
        {
            this.executorPool =executorPool;
        }
        if(null!=observerPool)
        {
            this.observerPool =observerPool;
        }
        if(null!=respCache)
        {
            this.respCache =respCache;
        }
    }
}
