package org.creation.singlejob.manager.worker;

import org.creation.singlejob.SinglejobAspectSupport.InvocationCallback;
import org.creation.singlejob.exception.WaitingOverTimeException;
import org.creation.singlejob.manager.AbstrcatSingleJobManager;
import org.creation.singlejob.manager.SingleJobManager;

public class SameReturnManager  extends AbstrcatSingleJobManager {

    private Object resp = null;
    
    public SameReturnManager(boolean readCacheIfExist) {
        super(readCacheIfExist);
        // TODO Auto-generated constructor stub
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
    }

    @Override
    protected Object handleConflict(String uniqueKey, InvocationCallback invocation) throws Exception {
        putMeIntoObserverPool(uniqueKey,this);
        synchronized(this)
        {
            this.wait(1000000);
        }
        if(null!=resp)
        {
            return resp;
        }else
        {
            throw new WaitingOverTimeException(uniqueKey);
        }
    }
}
