package org.creation.singlejob.manager.observerpool;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.CuratorWatcher;
import org.apache.zookeeper.WatchedEvent;
import org.creation.singlejob.manager.SingleJobManager;

import com.alibaba.fastjson.JSON;

/** 
* @author 作者 LiuPeng E-mail: 
* @version 创建时间：2017年7月18日 下午4:18:03 
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
public class ZooKeeperObserverPool extends LocalConcurrentHashMapObserverPool {

    private static final String PATH_PRIFIX = "/SJObserverPool/";

    CuratorFramework zooKeeperClient;

    /**
     * @param zooKeeperClient
     */
    public ZooKeeperObserverPool(CuratorFramework zooKeeperClient) {
        this.zooKeeperClient = zooKeeperClient;
    }

    @Override
    public boolean putMeIntoObserverPool(String key, final SingleJobManager g) {
        final String path = PATH_PRIFIX.concat(key);
        try {
            zooKeeperClient.getData().usingWatcher(new CuratorWatcher() {
                @Override
                public void process(WatchedEvent event) throws Exception {
                    Object msg = JSON.parse(zooKeeperClient.getData().forPath(path));
                    g.pizzaDeliverd(msg);
                    synchronized (g) {
                        g.notify();
                    }
                }
            }).forPath(path);
            return true;
        } catch (Exception e) {
            return super.putMeIntoObserverPool(key, g);
        }
    }

    public void publish(String key, Object resp) {
        String path = PATH_PRIFIX.concat(key);
        try {
            zooKeeperClient.setData().forPath(path, JSON.toJSONBytes(resp));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
