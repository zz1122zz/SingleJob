package org.creation.singlejob.manager.worker;

import org.creation.singlejob.SinglejobAspectSupport.InvocationCallback;
import org.creation.singlejob.exception.WaitingOverTimeException;
import org.creation.singlejob.manager.AbstrcatSingleJobManager;
import org.creation.singlejob.manager.SingleJobManager;

public class HoldManager extends AbstrcatSingleJobManager {
    
    public HoldManager(boolean readCacheIfExist) {
        super(readCacheIfExist);
        // TODO Auto-generated constructor stub
    } 

    protected long maxWaitMilliSecond = 10000;
    
    public HoldManager(boolean readCacheIfExist,long maxWaitMilliSecond) {
        super(readCacheIfExist);
        this.maxWaitMilliSecond = maxWaitMilliSecond;
    }

    private boolean isMyShow = false;

    @Override
    public void callObservers(String uniqueKey, Object resp) {
        SingleJobManager nextToShow = observerPool.getAndRemoveSingleJobManagerFromObserverList(uniqueKey);
        if (null != nextToShow) {
            nextToShow.pizzaDeliverd(resp);
            synchronized (nextToShow) {
                nextToShow.notify();
            }
        }
    }

    @Override
    public void pizzaDeliverd(Object resp) {
        this.isMyShow = true;
    }

    @Override
    protected Object handleConflict(String uniqueKey, InvocationCallback invocation) throws Throwable {
        putMeIntoObserverPool(uniqueKey, this);
        synchronized (this) {
            this.wait(maxWaitMilliSecond);
        }
        if (isMyShow) {
            return proceedWithInvocation(uniqueKey, invocation);
        }
        else {
            throw new WaitingOverTimeException(uniqueKey);
        }
    }

}
