package org.creation.singlejob.manager.worker;

import org.creation.singlejob.SinglejobAspectSupport.InvocationCallback;
import org.creation.singlejob.manager.AbstrcatSingleJobManager;
import org.creation.singlejob.manager.SingleJobManager;

public class RejectManager extends AbstrcatSingleJobManager {

    public RejectManager(boolean readCacheIfExist){
        super(false);
    }

    @Override
    public void pizzaDeliverd(Object resp) {
        // TODO Auto-generated method stub

    }

    @Override
    protected Object handleConflict(String uniqueKey, InvocationCallback invocation) throws Exception {
        // TODO Auto-generated method stub
        throw new Exception();
    }

    @Override
    public void callObservers(String uniqueKey, Object resp) {
        {
            for(SingleJobManager observer:observerPool.getObserverList(uniqueKey))
            {
                observer.pizzaDeliverd(resp);
            }
        }
        
    }

}
