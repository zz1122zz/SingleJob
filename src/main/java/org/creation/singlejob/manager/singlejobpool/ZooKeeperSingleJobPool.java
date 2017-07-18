package org.creation.singlejob.manager.singlejobpool;

import java.util.concurrent.TimeUnit;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.creation.singlejob.manager.SingleJobManager;
import org.redisson.client.RedisException;

/** 
* @author 作者 LiuPeng E-mail: 
* @version 创建时间：2017年7月18日 下午4:17:38 
* 类说明 
*/
/**
 * <p>Title: Module Information         </p>
 * <p>Description: Function Description </p>
 * <p>Copyright: Copyright (c) 2017     </p>
 * <p>Company: ND Co., Ltd.       </p>
 * <p>Create Time: 2017年7月18日           </p>
 * @author LiuPeng
 * <p>Update Time: 2017年7月18日               </p>
 * <p>Updater:                          </p>
 * <p>Update Comments:                  </p>
 */
public class ZooKeeperSingleJobPool extends LocalHashMapSingleJobPool{

    private static final String PATH_PRIFIX = "/SJLockPool/";

    CuratorFramework zooKeeperClient;
    
    /**
     * @param zooKeeperClient
     * @param leaseTime
     * @param unit
     */
    public ZooKeeperSingleJobPool(CuratorFramework zooKeeperClient, long leaseTime, TimeUnit unit) {
       this.zooKeeperClient = zooKeeperClient;
    }

    @Override
    public boolean putIntoExecutorPool(String key, SingleJobManager worker) {
        final String path = PATH_PRIFIX.concat(key);
        try {
            return new InterProcessMutex(zooKeeperClient, path).acquire(0, TimeUnit.MILLISECONDS);
        }catch (InterruptedException e) {
            //超时时间为0，即立马返回失败
            return false;
        }catch (Exception e) {
            //判定为redis服务不可用，决定降级
            return super.putIntoExecutorPool(key, worker);
        }
    }

    @Override
    public boolean removeFromJobPool(String key) {
        final String path = PATH_PRIFIX.concat(key);
        try {
            new InterProcessMutex(zooKeeperClient, path).release();
        }catch (RedisException e) {
            //判定为redis服务不可用，决定降级
            return super.removeFromJobPool(key);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

}
