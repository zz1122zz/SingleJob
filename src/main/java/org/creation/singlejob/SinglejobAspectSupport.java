package org.creation.singlejob;

import java.lang.reflect.Method;

import org.aspectj.lang.ProceedingJoinPoint;
import org.creation.singlejob.key.SimpleSingleJobKeyGenerator;
import org.creation.singlejob.key.SingleJobKeyGenerator;
import org.creation.singlejob.manager.SingleJobManager;
import org.creation.singlejob.manager.worker.HoldManager;
import org.creation.singlejob.manager.worker.RedissonHoldManager;
import org.creation.singlejob.manager.worker.RedissonSameReturnManager;
import org.creation.singlejob.manager.worker.RejectManager;
import org.creation.singlejob.manager.worker.SameReturnManager;
import org.creation.singlejob.persistence.RedisSingleJobDataPersistenceProvider;
import org.creation.singlejob.persistence.SingleJobDataPersistenceProvider;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.StringUtils;

public class SinglejobAspectSupport implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    protected String methodIdentification(Method method) {
        return method.getDeclaringClass().getName() + "." + method.getName();
    }

    public Object invokeWithSinglejobControl(ProceedingJoinPoint point, Method method,
            Class<? extends Object> targetClass, InvocationCallback invocation) throws Throwable {

        SingleJob annotation = method.getAnnotation(SingleJob.class);

        String joinpointIdentification = methodIdentification(method);

        SingleJobKeyGenerator keyGenerator = (SingleJobKeyGenerator) determineBean(annotation.keyGenerator(),
                SingleJobKeyGenerator.class, SimpleSingleJobKeyGenerator.getInstance());
        String paramsIdentification = keyGenerator.generate(annotation.distinction(), method, point.getArgs());

        String uniqueKey = joinpointIdentification + paramsIdentification;

        SingleJobManager jobManager = determineSingleJobManager(annotation);

        Object result = jobManager.proceedWithInvocation(uniqueKey, invocation);
        if (result instanceof Throwable) {
            throw ((Throwable) result);
        }
        else {
            return result;
        }
    }

    private SingleJobManager determineSingleJobManager(SingleJob annotation) throws Exception {
        boolean readCacheIfExist = annotation.readCacheIfExist();

        SingleJobDataPersistenceProvider persistenceProvider = (SingleJobDataPersistenceProvider) determineBean(
                annotation.SingleJobDataPersistenceProvider(), SingleJobDataPersistenceProvider.class, null);
        
        SingleJobManager jobManager = null;
        if (annotation.singleJobPolicy().equals(SingleJobPolicy.REJECT)) {
            jobManager = new RejectManager(readCacheIfExist);
        }
        else if (annotation.singleJobPolicy().equals(SingleJobPolicy.WAIT_IN_QUENE_AND_USE_SAME_RETURN)) {
            if(null != persistenceProvider && persistenceProvider instanceof RedisSingleJobDataPersistenceProvider)
            {
                jobManager = new RedissonSameReturnManager(readCacheIfExist);
            }else
            {
                jobManager = new SameReturnManager(readCacheIfExist);
            }
        }
        else if (annotation.singleJobPolicy().equals(SingleJobPolicy.WAIT_IN_QUENE_TO_PROCEED)) {
            if(null != persistenceProvider && persistenceProvider instanceof RedisSingleJobDataPersistenceProvider)
            {
                jobManager = new RedissonHoldManager(readCacheIfExist);
            }else
            {
                jobManager = new HoldManager(readCacheIfExist);
            }
        }
        else {
            throw new Exception();
        }
        if (null != persistenceProvider) {
            jobManager.initPersistenceField(persistenceProvider);
        }
        return jobManager;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private Object determineBean(String qualifier, Class type, Object defaultReturn) {

        if (StringUtils.hasText(qualifier)) {
            // Object persistenceProvider = BeanFactoryAnnotationUtils.qualifiedBeanOfType(
            // this.applicationContext.getParentBeanFactory(), type, qualifier);
            return this.applicationContext.getBean(qualifier);
        }
        else {
            try {
                return this.applicationContext.getBean(type);
            } catch (BeansException e) {
                return defaultReturn;
            }
        }
    }

    public interface InvocationCallback {
        Object proceedWithInvocation() throws Throwable;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
