package org.k2.processmining.security;

import org.k2.processmining.model.user.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
public class UserLoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler{

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    protected void handle(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        //成功之后可以给request设置用户信息
        System.out.println("用户登陆成功！");
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof MyUserDetails){
            User user = ((MyUserDetails)principal).getUser();
            System.out.println("userEmail:"+user.getEmail());
            //TODO 将需要的用户信息装入reques中返回给前端
        }
        redirectStrategy.sendRedirect(request,response,"/home");

        super.handle(request,response,authentication);

    }

}
