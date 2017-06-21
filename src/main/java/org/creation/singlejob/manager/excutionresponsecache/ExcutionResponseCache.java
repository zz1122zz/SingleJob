package org.creation.singlejob.manager.excutionresponsecache;

public interface ExcutionResponseCache<T, G> {
    public G getIfPresent(T t);

    public void put(T t, G g);
}
