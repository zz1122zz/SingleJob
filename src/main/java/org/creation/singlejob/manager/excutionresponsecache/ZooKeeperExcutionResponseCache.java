package org.creation.singlejob.manager.excutionresponsecache;

import java.util.concurrent.TimeUnit;

import org.apache.curator.framework.CuratorFramework;

import com.alibaba.fastjson.JSON;

/** 
* @author 作者 LiuPeng E-mail: 
* @version 创建时间：2017年7月18日 下午4:18:23 
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
public class ZooKeeperExcutionResponseCache extends LocalExcutionResponseCache {
    
    private static final String PATH_PRIFIX = "/SJRespCache/";

    CuratorFramework zooKeeperClient;

    /**
     * @param zooKeeperClient
     * @param surviveTime
     * @param surviveTimeUnit
     */
    public ZooKeeperExcutionResponseCache(CuratorFramework zooKeeperClient, long surviveTime, TimeUnit surviveTimeUnit) {
        this.zooKeeperClient = zooKeeperClient;
        //第一期先不考虑缓存失效时间，实现方式还要思考
    }

    /**	
     * <p>Description:              </p>
     * <p>Create Time: 2017年7月18日   </p>
     * <p>Create author: LiuPeng   </p>
     * @param t
     * @return
     */
    @Override
    public Object getIfPresent(Object t) {
        String path = PATH_PRIFIX.concat(t.toString());
        try {
            return JSON.parse(zooKeeperClient.getData().forPath(path));
        } catch (Exception e) {
            return super.getIfPresent(t);
        }
    }

    /**	
     * <p>Description:              </p>
     * <p>Create Time: 2017年7月18日   </p>
     * <p>Create author: LiuPeng   </p>
     * @param t
     * @param g
     */
    @Override
    public void put(Object t, Object g) {
        String path = PATH_PRIFIX.concat(t.toString());
        try {
            zooKeeperClient.create().creatingParentsIfNeeded().forPath(path, JSON.toJSONBytes(g));
        } catch (Exception e) {
            super.put(t, g);
        }
    }

}
