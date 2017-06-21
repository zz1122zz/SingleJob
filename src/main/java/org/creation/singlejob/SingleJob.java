package org.creation.singlejob;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SingleJob {
    
    String distinction() default "";
    
    String keyGenerator() default "";
    
    String SingleJobDataPersistenceProvider() default "";
    
    SingleJobPolicy singleJobPolicy() default SingleJobPolicy.WAIT_IN_QUENE_AND_USE_SAME_RETURN;
    
    boolean readCacheIfExist() default false;

}
