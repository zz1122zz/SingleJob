package org.creation.singlejob.manager.worker;

import org.creation.singlejob.SinglejobAspectSupport.InvocationCallback;
import org.creation.singlejob.exception.WaitingOverTimeException;
import org.creation.singlejob.manager.AbstrcatSingleJobManager;
import org.creation.singlejob.manager.SingleJobManager;

public class SameReturnManager  extends AbstrcatSingleJobManager {

    private Object resp = null;
    
    private boolean getResp = false;
    
    public SameReturnManager(boolean readCacheIfExist) {
        super(readCacheIfExist);
        // TODO Auto-generated constructor stub
    }

    protected long maxWaitMilliSecond = 10000;
    
    public SameReturnManager(boolean readCacheIfExist,long maxWaitMilliSecond) {
        super(readCacheIfExist);
        this.maxWaitMilliSecond = maxWaitMilliSecond;
    }
    
    @Override
    public void callObservers(String uniqueKey, Object resp) {
        for(SingleJobManager observer:observerPool.getObserverList(uniqueKey))
        {
            observer.pizzaDeliverd(resp);
            synchronized (observer) {
                observer.notify();
            }
        }
    }

    @Override
    public void pizzaDeliverd(Object resp) {
        this.resp=resp;
        this.getResp=true;
    }

    @Override
    protected Object handleConflict(String uniqueKey, InvocationCallback invocation) throws Exception {
        putMeIntoObserverPool(uniqueKey,this);
        synchronized(this)
        {
            this.wait(maxWaitMilliSecond);
        }
        if(getResp)
        {
            return resp;
        }else
        {
            throw new WaitingOverTimeException(uniqueKey);
        }
    }
}
