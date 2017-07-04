package org.k2.processmining.security.user;

import org.k2.processmining.security.admin.AdminRememberService;
import org.k2.processmining.util.Message;
import org.k2.processmining.utils.GsonParser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;

/**
 * Created by Aria on 2017/6/28.
 */
public class UserAuthenticationFilter extends UsernamePasswordAuthenticationFilter{

    private String jsonUsername;
    private String jsonPassword;

    @Override
    protected String obtainPassword(HttpServletRequest request) {
        String password = null;
        if ("application/json".equals(request.getHeader("Content-Type"))) {
            password = this.jsonPassword;
        }else{
            password = super.obtainPassword(request);
        }
        return password;
    }

    @Override
    protected String obtainUsername(HttpServletRequest request){
        String username = null;

        if ("application/json".equals(request.getHeader("Content-Type"))) {
            username = this.jsonUsername;
        }else{
            username = super.obtainUsername(request);
        }

        return username;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        HttpSession session = request.getSession();

        if ("application/json".equals(request.getHeader("Content-Type"))){
            StringBuffer buffer = new StringBuffer();
            String line = null;
            BufferedReader reader;
            try {
                reader = request.getReader();
                while (((line = reader.readLine())!=null)){
                    buffer.append(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println(buffer.toString());
            System.out.println("json:"+buffer.toString());
            UserForm userForm = GsonParser.fromJson(buffer.toString(),UserForm.class);
            this.jsonPassword = userForm.getPassword();
            this.jsonUsername = userForm.getEmail();
            if ("".equals(userForm.getValidateCode())){
                System.out.println("user validateCode is null");
                throw new UsernameNotFoundException(Message.USER_VALIDATECODE_NULL_CODE);
            }
            System.out.println("session.verCode:"+session.getAttribute("validateCode"));
            if (userForm.getValidateCode().compareToIgnoreCase((String) session.getAttribute("validateCode")) != 0){
                System.out.println("validateCode is wrong"+userForm.getValidateCode());
//                throw new UsernameNotFoundException(UserFailureHandler.USER_VALIDATECODE_WRONG);
                throw new UsernameNotFoundException(Message.USER_VALIDATECODE_WRONG_CODE);
            }
            if ("".equalsIgnoreCase(userForm.getUserRemember())){
                System.out.println("rememberMe is null");
            }else {
                System.out.println("rememberMe:"+userForm.getUserRemember());
                request.setAttribute(UserRememberMeService.REMEMBER_ME_PARAMER,userForm.getUserRemember());
            }
        }else {
            String str = request.getParameter(AdminRememberService.REMEMBER_ME_PARAMER);
            System.out.println("非JSon请求状况下:"+str);
        }
        System.out.println(jsonUsername+"   "+jsonPassword);
        return super.attemptAuthentication(request, response);
    }
}

class  UserForm{
    private String email;
    private String password;
    private String validateCode;
    private String userRemember;

    public void setValidateCode(String validateCode) {
        this.validateCode = validateCode;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getValidateCode() {
        return validateCode;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public void setUserRemember(String userRemember) {
        this.userRemember = userRemember;
    }

    public String getUserRemember() {
        return userRemember;
    }
}
