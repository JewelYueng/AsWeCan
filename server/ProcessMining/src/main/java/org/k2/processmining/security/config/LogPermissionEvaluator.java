package org.k2.processmining.security.config;

import org.k2.processmining.exception.JSONBadRequestException;
import org.k2.processmining.model.log.AbstractLog;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;

import java.io.Serializable;

/**
 * Created by nyq on 2017/6/24.
 */
public class LogPermissionEvaluator implements PermissionEvaluator {
    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        if (targetDomainObject == null) {
            throw new JSONBadRequestException("The log is not exist.");
        }
        if (targetDomainObject instanceof AbstractLog) {
            IUserDetail iUserDetail = (IUserDetail) authentication.getPrincipal();
            AbstractLog log = (AbstractLog) targetDomainObject;
            return log.getUserId().equals(iUserDetail.getUser().getId());
        }
        throw new UnsupportedOperationException("hasPermission not supported for object <"
                + targetDomainObject
                + "> and permission <" + permission + ">");
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        return false;
    }
}
