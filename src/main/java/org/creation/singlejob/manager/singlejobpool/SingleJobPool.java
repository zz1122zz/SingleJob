package org.creation.singlejob.manager.singlejobpool;

public interface SingleJobPool<T,G> {
    
     public abstract boolean putIntoExecutorPool(T key , G worker);
    
     public abstract boolean removeFromJobPool(String key);

}
