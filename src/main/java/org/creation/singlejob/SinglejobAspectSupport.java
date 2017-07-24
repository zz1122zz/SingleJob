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
import org.creation.singlejob.manager.worker.ZooKeeperHoldManager;
import org.creation.singlejob.manager.worker.ZooKeeperSameReturnManager;
import org.creation.singlejob.persistence.RedisSingleJobDataPersistenceProvider;
import org.creation.singlejob.persistence.SingleJobDataPersistenceProvider;
import org.creation.singlejob.persistence.ZooKeeperSingleJobDataPersistenceProvider;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.StringUtils;

public class SinglejobAspectSupport implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    protected String methodIdentification(Method method) {
        return new StringBuffer().append(SingleJobType.METHOD_STR).append(method.getDeclaringClass().getName())
                .append(".").append(method.getName()).append(".").toString();
    }

    protected String locknameIdentification(String lockName) {
        return new StringBuffer().append(SingleJobType.LOCKNAME_STR).append(lockName).append(".").toString();
    }

    public Object invokeWithSinglejobControl(ProceedingJoinPoint point, Method method,
            Class<? extends Object> targetClass, InvocationCallback invocation) throws Throwable {

        SingleJob annotation = method.getAnnotation(SingleJob.class);

        String joinpointIdentification = determineIdentification(annotation, method);

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

    private String determineIdentification(SingleJob annotation, Method method) {
        if (annotation.type().equals(SingleJobType.LOCK_BY_METHOD)) {
            return methodIdentification(method);
        }
        else if (annotation.type().equals(SingleJobType.LOCK_BY_CUSTOM_LOCKNAME)) {
            return locknameIdentification(annotation.lockName());
        }
        return methodIdentification(method);
    }

    private SingleJobManager determineSingleJobManager(SingleJob annotation) throws Exception {
        boolean readCacheIfExist = annotation.readCacheIfExist();
        long maxWaitMilliSecond = annotation.maxWaitMilliSecond();

        SingleJobDataPersistenceProvider persistenceProvider = (SingleJobDataPersistenceProvider) determineBean(
                annotation.singleJobDataPersistenceProvider(), SingleJobDataPersistenceProvider.class, null);

        SingleJobManager jobManager = null;
        if (annotation.singleJobPolicy().equals(SingleJobPolicy.REJECT)) {
            jobManager = new RejectManager(readCacheIfExist);
        }
        else if (annotation.singleJobPolicy().equals(SingleJobPolicy.WAIT_IN_QUENE_AND_USE_SAME_RETURN)) {
            if (null != persistenceProvider && persistenceProvider instanceof RedisSingleJobDataPersistenceProvider) {
                jobManager = new RedissonSameReturnManager(readCacheIfExist, maxWaitMilliSecond);
            }
            else if (null != persistenceProvider
                    && persistenceProvider instanceof ZooKeeperSingleJobDataPersistenceProvider) {
                jobManager = new ZooKeeperSameReturnManager(readCacheIfExist, maxWaitMilliSecond);
            }
            else {
                jobManager = new SameReturnManager(readCacheIfExist, maxWaitMilliSecond);
            }
        }
        else if (annotation.singleJobPolicy().equals(SingleJobPolicy.WAIT_IN_QUENE_TO_PROCEED)) {
            if (null != persistenceProvider && persistenceProvider instanceof RedisSingleJobDataPersistenceProvider) {
                jobManager = new RedissonHoldManager(readCacheIfExist, maxWaitMilliSecond);
            }
            else if (null != persistenceProvider
                    && persistenceProvider instanceof ZooKeeperSingleJobDataPersistenceProvider) {
                jobManager = new ZooKeeperHoldManager(readCacheIfExist, maxWaitMilliSecond);
            }
            else {
                jobManager = new HoldManager(readCacheIfExist, maxWaitMilliSecond);
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
