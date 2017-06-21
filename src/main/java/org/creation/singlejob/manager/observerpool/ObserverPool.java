package org.creation.singlejob.manager.observerpool;

import java.util.List;

public interface ObserverPool<G> {
    boolean putMeIntoObserverPool(String key,G g);

    List<G> getObserverList(String uniqueKey);

    G getAndRemoveSingleJobManagerFromObserverList(String uniqueKey);
    
    //Object callAll(String key,InvocationCallback callback);
}
