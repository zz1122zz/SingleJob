package org.creation.singlejob.manager.worker;

import org.creation.singlejob.SinglejobAspectSupport.InvocationCallback;
import org.creation.singlejob.manager.SingleJobManager;

public class RedissonHoldManager extends HoldManager {

    public RedissonHoldManager(boolean readCacheIfExist) {
        super(readCacheIfExist);
        // TODO Auto-generated constructor stub
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
            this.wait(100000);
        }
        if (isMyShow) {
            return proceedWithInvocation(uniqueKey, invocation);
        }
        else {
            throw new Exception();
        }
    }
}
