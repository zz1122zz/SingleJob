package org.creation.singlejob.manager.observerpool;

import java.util.ArrayList;
import java.util.List;

import org.creation.singlejob.manager.SingleJobManager;

public class EmptyObserverPool  implements ObserverPool<SingleJobManager> {

    @Override
    public boolean putMeIntoObserverPool(String a, SingleJobManager g) {
        return false;
    }

    @Override
    public List<SingleJobManager> getObserverList(String uniqueKey) {
        return new ArrayList<SingleJobManager>();
    }

    @Override
    public SingleJobManager getAndRemoveSingleJobManagerFromObserverList(String uniqueKey) {
        return null;
    }

}
