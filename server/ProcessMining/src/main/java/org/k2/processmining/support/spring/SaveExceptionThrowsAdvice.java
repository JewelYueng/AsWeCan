package org.k2.processmining.support.spring;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.k2.processmining.model.log.AbstractLog;
import org.k2.processmining.model.mergemethod.MergeMethod;
import org.k2.processmining.model.miningmethod.MiningMethod;
import org.k2.processmining.storage.LogStorage;
import org.k2.processmining.support.algorithm.MergerFactory;
import org.k2.processmining.support.algorithm.MethodManage;
import org.k2.processmining.support.algorithm.MinerFactory;
import org.k2.processmining.support.reflect.ReflectUtil;
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

    @Autowired
    private MethodManage methodManage;

    @Around("execution(public * org.k2.processmining.service.*.save(..))")
    public Object deleteLog(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            return joinPoint.proceed();
        } catch (Throwable throwable) {
            Object[] objects = joinPoint.getArgs();
            if (objects.length > 0 && objects[0] instanceof AbstractLog) {
                logStorage.delete((AbstractLog) objects[0]);
            }
            throw throwable;
        }
    }

    @Around("execution(public * org.k2.processmining.service.MergeMethodService.addMethod(..))")
    public Object deleteMergeMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] objects = joinPoint.getArgs();
        Object o = null;
        if (objects.length > 0 && objects[0] instanceof MergeMethod) {
            MergeMethod mergeMethod = (MergeMethod) objects[0];
            try {
                o = joinPoint.proceed();
            }
            catch (Throwable throwable) {
                LOGGER.error("fail to add MergeMethod");
                ReflectUtil.getInstance().closeClassLoader(mergeMethod.getId());
                System.gc();
                MergerFactory.getInstance().deleteAlgorithm(mergeMethod.getId());
                methodManage.deleteMerger(mergeMethod.getId());
                throw throwable;
            }
        }
        return o;
    }

    @Around("execution(public * org.k2.processmining.service.MiningMethodService.addMethod(..))")
    public Object deleteMinerMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] objects = joinPoint.getArgs();
        Object o = null;
        if (objects.length > 0 && objects[0] instanceof MiningMethod) {
            MiningMethod miningMethod = (MiningMethod) objects[0];
            try {
                o = joinPoint.proceed();
            }
            catch (Throwable throwable) {
                LOGGER.error("fail to create MiningMethod");
                ReflectUtil.getInstance().closeClassLoader(miningMethod.getId());
                System.gc();
                MinerFactory.getInstance().deleteAlgorithm(miningMethod.getId());
                methodManage.deleteMiner(miningMethod.getId());
                throw throwable;
            }
        }
        return o;
    }
}
