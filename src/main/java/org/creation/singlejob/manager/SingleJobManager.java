package org.creation.singlejob.manager;

import org.creation.singlejob.SinglejobAspectSupport.InvocationCallback;
import org.creation.singlejob.persistence.SingleJobDataPersistenceProvider;

public interface SingleJobManager {
    
    boolean useCache();
    
    Object proceedWithInvocation(String uniqueKey, InvocationCallback invocation) throws Throwable;

    void pizzaDeliverd(Object resp);

    void initPersistenceField(SingleJobDataPersistenceProvider persistenceProvider);

}
