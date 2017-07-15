package org.k2.processmining.support.spring;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.k2.processmining.exception.BadRequestException;
import org.k2.processmining.model.LogShareState;
import org.k2.processmining.model.LogState;
import org.k2.processmining.model.MethodState;
import org.k2.processmining.model.log.AbstractLog;
import org.k2.processmining.model.log.EventLog;
import org.k2.processmining.model.mergemethod.MergeMethod;
import org.k2.processmining.model.user.User;
import org.k2.processmining.security.user.MyUserDetails;
import org.k2.processmining.support.algorithm.Algorithm;
import org.k2.processmining.util.Message;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * Created by nyq on 2017/7/4.
 */
@Aspect
@Component
public class PermissionAdvice {

    @Around("execution(public * org.k2.processmining.service.RawLogService.normalize(..)) " +
            " || execution(public * org.k2.processmining.service.NormalLogService.transToEventLog(..))")
    public Object processLog(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] objects = joinPoint.getArgs();
        AbstractLog log = (AbstractLog)objects[0];
        Object o  = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (o instanceof MyUserDetails) {
            User user = ((MyUserDetails) o).getUser();
            if (user != null && log != null
                    && LogState.isActive(log.getState()) && log.getUserId().equals(user.getId())) {
                return joinPoint.proceed();
            }
        }
        throw new BadRequestException(Message.LOG_IS_NOT_EXIST);
    }

    @Around("execution(public * org.k2.processmining.storage.LogStorage.download(..))")
    public Object logStorageDownload(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] objects = joinPoint.getArgs();
        AbstractLog log = (AbstractLog)objects[0];
        Object o  = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (o instanceof MyUserDetails) {
            User user = ((MyUserDetails) o).getUser();
            if (user != null && log != null
                    && LogState.isActive(log.getState())
                    && (log.getUserId().equals(user.getId()) || LogShareState.isShared(log.getIsShared()))) {
                return joinPoint.proceed();
            }
        }
        throw new BadRequestException(Message.LOG_IS_NOT_EXIST);
    }

    @Around("execution(public * org.k2.processmining.service.MergeMethodService.merge(..))")
    public Object merge(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] objects = joinPoint.getArgs();
        EventLog eventLog1 = (EventLog)objects[0];
        EventLog eventLog2 = (EventLog)objects[1];
        MergeMethod mergeMethod = (MergeMethod)objects[2];
        if (mergeMethod == null || !MethodState.isActive(mergeMethod.getState()))  throw new BadRequestException(Message.METHOD_IS_NOT_EXIST);
        if (eventLog1 == null || !LogState.isActive(eventLog1.getState())) throw new BadRequestException(Message.LOG_IS_NOT_EXIST);
        if (eventLog2 == null || !LogState.isActive(eventLog2.getState())) throw new BadRequestException(Message.LOG_IS_NOT_EXIST);
        if (eventLog1.getId().equals(eventLog2.getId())) throw new BadRequestException(Message.USE_DIFF_LOG);
        return joinPoint.proceed();
    }

    @Around("execution(public * org.k2.processmining.service.MiningMethodService.mining(..))")
    public Object mining(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] objects = joinPoint.getArgs();
        if (objects[0] == null) {
            throw new BadRequestException(Message.LOG_IS_NOT_EXIST);
        }
        if (! (objects[0] instanceof EventLog)) {
            return joinPoint.proceed();
        }
        EventLog eventLog = (EventLog)objects[0];
        Algorithm algorithm = (Algorithm)objects[1];
        if (!LogState.isActive(eventLog.getState())) throw new BadRequestException(Message.LOG_IS_NOT_EXIST);
        if (algorithm == null) throw new BadRequestException(Message.METHOD_IS_NOT_EXIST);
        return joinPoint.proceed();
    }
}
