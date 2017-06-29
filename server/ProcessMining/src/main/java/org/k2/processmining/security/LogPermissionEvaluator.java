package org.k2.processmining.security;

import org.k2.processmining.exception.JSONBadRequestException;
import org.k2.processmining.exception.JSONForbiddenException;
import org.k2.processmining.model.log.AbstractLog;
import org.k2.processmining.security.config.IUserDetail;
import org.k2.processmining.support.algorithm.Algorithm;
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
            throw new JSONForbiddenException("Attempted to access the protected resource!");
        }
        if (targetDomainObject instanceof AbstractLog) {
            AbstractLog log = (AbstractLog) targetDomainObject;
//            MyUserDetails userDetail = (MyUserDetails) authentication.getPrincipal();
//            return log.getUserId().equals(userDetail.getUser().getId());
            return log.getUserId().equals("1");
        }
        else if (targetDomainObject instanceof Algorithm) {
            return true;
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
