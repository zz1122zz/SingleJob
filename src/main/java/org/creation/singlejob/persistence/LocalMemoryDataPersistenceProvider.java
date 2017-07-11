package org.creation.singlejob.persistence;

import org.creation.singlejob.manager.SingleJobManager;
import org.creation.singlejob.manager.excutionresponsecache.ExcutionResponseCache;
import org.creation.singlejob.manager.excutionresponsecache.LocalExcutionResponseCache;
import org.creation.singlejob.manager.observerpool.LocalConcurrentHashMapObserverPool;
import org.creation.singlejob.manager.observerpool.ObserverPool;
import org.creation.singlejob.manager.singlejobpool.LocalHashMapSingleJobPool;
import org.creation.singlejob.manager.singlejobpool.SingleJobPool;

/** 
* @author 作者 LiuPeng E-mail: 
* @version 创建时间：2017年7月11日 下午6:13:25 
* 类说明 
*/
/**
 * <p>Title: Module Information         </p>
 * <p>Description: Function Description </p>
 * <p>Copyright: Copyright (c) 2017     </p>
 * <p>Company: ND Co., Ltd.       </p>
 * <p>Create Time: 2017年7月11日           </p>
 * @author LiuPeng
 * <p>Update Time: 2017年7月11日               </p>
 * <p>Updater:                          </p>
 * <p>Update Comments:                  </p>
 */
public class LocalMemoryDataPersistenceProvider implements SingleJobDataPersistenceProvider {

    /**	
     * <p>Description:              </p>
     * <p>Create Time: 2017年7月11日   </p>
     * <p>Create author: LiuPeng   </p>
     * @return
     */
    @Override
    public SingleJobPool<String, SingleJobManager> initSingleJobPool() {
        return LocalHashMapSingleJobPool.getInstance();
    }

    /**	
     * <p>Description:              </p>
     * <p>Create Time: 2017年7月11日   </p>
     * <p>Create author: LiuPeng   </p>
     * @return
     */
    @Override
    public ObserverPool<SingleJobManager> initObserverPool() {
        return LocalConcurrentHashMapObserverPool.getInstance();
    }

    /**	
     * <p>Description:              </p>
     * <p>Create Time: 2017年7月11日   </p>
     * <p>Create author: LiuPeng   </p>
     * @return
     */
    @Override
    public ExcutionResponseCache<Object, Object> initlExcutionResponseCache() {
        return LocalExcutionResponseCache.getInstance();
    }

}
