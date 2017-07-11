package org.creation.singlejob.manager.singlejobpool;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

import org.creation.singlejob.manager.SingleJobManager;

public class LocalHashMapSingleJobPool implements SingleJobPool<String, SingleJobManager> {
    private static LocalHashMapSingleJobPool instance;

    private LocalHashMapSingleJobPool() {
    }

    public static LocalHashMapSingleJobPool getInstance() { // 对获取实例的方法进行同步
        if (instance == null) {
            synchronized (LocalHashMapSingleJobPool.class) {
                if (instance == null)
                    instance = new LocalHashMapSingleJobPool();
            }
        }
        return instance;
    }

    static protected ConcurrentHashMap<String, ReentrantLock> currentJobPool = new ConcurrentHashMap<String, ReentrantLock>();

    @Override
    public synchronized boolean removeFromJobPool(String key) {
        if (currentJobPool.containsKey(key)) {
            currentJobPool.remove(key);
            return true;
        }
        else {
            return false;
        }
    }

    @Override
    public synchronized boolean putIntoExecutorPool(String key, SingleJobManager worker) {
        ReentrantLock lock = new ReentrantLock();lock.lock();
        if (currentJobPool.containsKey(key)) {
            return false;
        }
        else {
            currentJobPool.put(key, lock);
            return true;
        }
    }

}
