package org.creation.singlejob.manager.singlejobpool;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

import org.creation.singlejob.manager.SingleJobManager;

public class LocalHashMapSingleJobPool implements SingleJobPool<String, SingleJobManager> {
    private static LocalHashMapSingleJobPool instance;

    protected LocalHashMapSingleJobPool() {
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

    //做记录用
    //static private ConcurrentHashMap<String, SingleJobManager> currentJobPool = new ConcurrentHashMap<String, SingleJobManager>();
    //可重入锁对象
    static private ConcurrentHashMap<String, ReentrantLock> currentLockPool = new ConcurrentHashMap<String, ReentrantLock>();

    @Override
    public synchronized boolean removeFromJobPool(String key) {
        if (currentLockPool.containsKey(key)) {
            currentLockPool.get(key).unlock();
            if(currentLockPool.get(key).getHoldCount()==0)
            {
                currentLockPool.remove(key);
                //currentJobPool.remove(key);
            }
            return true;
        }
        else {
            return false;
        }
    }

    @Override
    public synchronized boolean putIntoExecutorPool(String key, SingleJobManager worker) {
        if (currentLockPool.containsKey(key)) {
            return currentLockPool.get(key).tryLock();
        }
        else {
            ReentrantLock lock = new ReentrantLock();
            lock.lock();
            currentLockPool.put(key, lock);
            //currentJobPool.put(key, worker);
            return true;
        }
    }

}
