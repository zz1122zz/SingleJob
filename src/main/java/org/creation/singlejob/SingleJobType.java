package org.creation.singlejob;
/** 
* @author 作者 LiuPeng E-mail: 
* @version 创建时间：2017年7月12日 下午12:14:21 
* 类说明 
*/
/**
 * <p>Title: Module Information         </p>
 * <p>Description: Function Description </p>
 * <p>Copyright: Copyright (c) 2017     </p>
 * <p>Company: ND Co., Ltd.       </p>
 * <p>Create Time: 2017年7月12日           </p>
 * @author LiuPeng
 * <p>Update Time: 2017年7月12日               </p>
 * <p>Updater:                          </p>
 * <p>Update Comments:                  </p>
 */
public enum SingleJobType {
    
    LOCK_BY_METHOD(0),

    LOCK_BY_CUSTOM_LOCKNAME(1);

    private final int value;

    public static String METHOD_STR= "METHOD:";
    
    public static String LOCKNAME_STR= "LOCKNAME:";
    
    SingleJobType(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }
}
