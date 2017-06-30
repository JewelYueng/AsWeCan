package org.k2.processmining.security.admin;

import org.k2.processmining.utils.GsonParser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;

/**
 * Created by Aria on 2017/6/28.
 */
public class AdminAuthenticationFilter extends UsernamePasswordAuthenticationFilter{
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
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response){

        System.out.println("AdminAuthenticationFilter attemptAuthentication");

        HttpSession session = request.getSession();
        if ("application/json".equals(request.getHeader("Content-Type"))) {
            StringBuffer sb = new StringBuffer();
            String line = null;

            BufferedReader reader;
            System.out.println("json");
            try {
                /*
                 * HttpServletRequest can be read only once
                 */
                 reader = request.getReader();
                while ((line = reader.readLine()) != null){
                    sb.append(line);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
                System.out.println(sb.toString());
                System.out.println("session:"+session.getAttribute("validateCode"));
                AdminForm adminForm = GsonParser.fromJson(sb.toString(),AdminForm.class);
                this.jsonPassword = adminForm.getPassword();
                this.jsonUsername = adminForm.getWorkId();
                if (adminForm.getValidateCode() == null){
                    System.out.println("null");
                    throw new UsernameNotFoundException("validate code is null");
                }
                System.out.println("jsonUserName:"+jsonUsername+"  jsonPassword:"+jsonPassword);

        }


        return super.attemptAuthentication(request, response);
    }
}

class AdminForm {
    private String workId;
    private String password;
    private String validateCode;

    public void setValidateCode(String validateCode) {
        this.validateCode = validateCode;
    }

    public String getValidateCode() {
        return validateCode;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setWorkId(String workId) {
        this.workId = workId;
    }

    public String getWorkId() {
        return workId;
    }

    public String getPassword() {
        return password;
    }
}
