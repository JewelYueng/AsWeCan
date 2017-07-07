package org.k2.processmining.security.admin;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.authentication.session.ConcurrentSessionControlAuthenticationStrategy;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by Aria on 2017/7/6.
 */
public class AdminSessionRegistryImpl extends SessionRegistryImpl{

    @Override
    public List<SessionInformation> getAllSessions(Object principal, boolean includeExpiredSessions) {

        List<SessionInformation> list = super.getAllSessions(principal, includeExpiredSessions);

        System.out.println("AdminSessionRegistryImpl.getAllSessions:"+list);

        return list;
    }
}
