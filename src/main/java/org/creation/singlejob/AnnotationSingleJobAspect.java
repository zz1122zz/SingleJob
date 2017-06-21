package org.creation.singlejob;

import java.lang.reflect.Method;

import javax.annotation.Resource;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.creation.singlejob.SinglejobAspectSupport.InvocationCallback;

@Aspect
public class AnnotationSingleJobAspect {

    @Resource
    SinglejobAspectSupport SinglejobAspectSupport;

    @Pointcut("@annotation(SingleJob)")
    public void annotationSingleJobAspect() {
    }

    @Around("annotationSingleJobAspect()")
    public Object doAround(final ProceedingJoinPoint point) throws Throwable {

        Method method = ((MethodSignature) point.getSignature()).getMethod();
        return SinglejobAspectSupport.invokeWithSinglejobControl(point, method, point.getClass(),
                new InvocationCallback() {

                    @Override
                    public Object proceedWithInvocation() throws Throwable {
                        // TODO Auto-generated method stub
                        return point.proceed(point.getArgs());
                    }

                });

    }
}
