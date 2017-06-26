package org.k2.processmining.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Aria on 2017/6/25.
 */
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler{

    @Override
    protected void handle(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        System.out.println("CustomSuccessHandler.handle:"+request.getAttribute("type"));
        super.handle(request, response, authentication);
        String targetUrl = "/html/activateSuccess.html";
        RedirectStrategy strategy = new DefaultRedirectStrategy();
        strategy.sendRedirect(request,response,targetUrl);

    }

    @Override
    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response) {
        return super.determineTargetUrl(request, response);
    }
}
