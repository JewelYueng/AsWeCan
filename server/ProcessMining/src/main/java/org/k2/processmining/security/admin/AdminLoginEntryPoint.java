package org.k2.processmining.security.admin;

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
public class AdminLoginEntryPoint extends LoginUrlAuthenticationEntryPoint{


    public AdminLoginEntryPoint(String loginFormUrl) {
        super(loginFormUrl);
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {


        HttpServletRequest httpRequest = request;
        System.out.println("AdminLoginEntryPoint commence");
        if ("XMLHttpRequest".equals(httpRequest.getHeader("X-Requested-With"))) {
            /**
             * ajax请求无权限的链接时返回的参数
             */
            System.out.println("json");
            Map<String, Object> error = new HashMap<String, Object>();
            error.put("code","403");
            error.put("message","your access denied");
//            error.put("result", "loginfailure");
//            error.put("data", "loginfailure"); // 兼容extjs form loading
            //RenderUtils.renderJSON((HttpServletResponse) response, error);

            response.setContentType("json");
            String json = new Gson().toJson(error);
            response.getWriter().write(json);
            response.getWriter().flush();

        } else {
            super.commence(request, response, authException);
        }
    }
}
