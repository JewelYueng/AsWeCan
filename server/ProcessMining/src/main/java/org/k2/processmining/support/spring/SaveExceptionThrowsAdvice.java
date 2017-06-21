package org.k2.processmining.support.spring;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.k2.processmining.model.log.AbstractLog;
import org.k2.processmining.storage.LogStorage;
import org.springframework.aop.ThrowsAdvice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by nyq on 2017/6/20.
 */
@Aspect
@Component
public class SaveExceptionThrowsAdvice{

    @Autowired
    private LogStorage logStorage;

    @Around("execution(public * org.k2.processmining.service.*.saveInDB(..))")
    public Object deleteFromLogStorageIfFail(ProceedingJoinPoint joinPoint) throws Throwable{
        Object[] objects = joinPoint.getArgs();
        Object o = null;
        if (objects.length > 0 && objects[0] instanceof AbstractLog) {
            try {
                o = joinPoint.proceed();
            }
            catch (Throwable throwable) {
                logStorage.delete((AbstractLog) (objects[0]));
                throw throwable;
            }
        }
        return o;
    }
}
