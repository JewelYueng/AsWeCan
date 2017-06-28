package org.k2.processmining.security.user;

import com.google.gson.Gson;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Aria on 2017/6/28.
 */
public class UserLoginEntryPoint extends LoginUrlAuthenticationEntryPoint{
    public UserLoginEntryPoint(String loginFormUrl) {
        super(loginFormUrl);
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {

        System.out.println("UserLoginEntryPoint commence");
        if ("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))){
            System.out.println("UserLoginEntryPoint json");
            Map<String,Object> error = new HashMap<>();
            error.put("code","101");
            error.put("message","user access denied");
            response.setContentType("json");
            String json = new Gson().toJson(error);
            response.getWriter().write(json);
            response.getWriter().flush();
        }else {
            super.commence(request, response, authException);
        }

    }
}
