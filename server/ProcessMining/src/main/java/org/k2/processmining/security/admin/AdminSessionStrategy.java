package org.k2.processmining.security.admin;

import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.authentication.session.ConcurrentSessionControlAuthenticationStrategy;

/**
 * Created by Aria on 2017/7/6.
 */
public class AdminSessionStrategy extends ConcurrentSessionControlAuthenticationStrategy{
    public AdminSessionStrategy(SessionRegistry sessionRegistry) {
        super(sessionRegistry);
    }


}
