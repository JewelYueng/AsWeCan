package org.k2.processmining.support.spring;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.k2.processmining.model.log.AbstractLog;
import org.k2.processmining.storage.LogStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by nyq on 2017/6/20.
 */
@Aspect
@Component
public class SaveExceptionThrowsAdvice{

    private static final Logger LOGGER = LoggerFactory.getLogger(SaveExceptionThrowsAdvice.class);

    @Autowired
    private LogStorage logStorage;

    @Around("execution(public * org.k2.processmining.service.*.afterSaveInLogStorage*(..)) && args(..)")
    public Object deleteFromLogStorageIfFail(ProceedingJoinPoint joinPoint) throws Throwable{
        Object[] objects = joinPoint.getArgs();
        Object o = null;
        if (objects.length > 0 && objects[0] instanceof AbstractLog) {
            try {
                o = joinPoint.proceed();
            }
            catch (Throwable throwable) {
                LOGGER.error("fail to save in db");
                logStorage.delete((AbstractLog) (objects[0]));
                throw throwable;
            }
        }
        return o;
    }
}
