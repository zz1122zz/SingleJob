package org.creation.singlejob.persistence;

import org.creation.singlejob.manager.SingleJobManager;
import org.creation.singlejob.manager.excutionresponsecache.ExcutionResponseCache;
import org.creation.singlejob.manager.observerpool.ObserverPool;
import org.creation.singlejob.manager.singlejobpool.SingleJobPool;

public interface SingleJobDataPersistenceProvider {

    SingleJobPool<String, SingleJobManager> initSingleJobPool();

    ObserverPool<SingleJobManager> initObserverPool();

    ExcutionResponseCache<Object, Object> initlExcutionResponseCache();

}
