package org.k2.processmining.controller;

import org.k2.processmining.model.LogGroup;
import org.k2.processmining.model.UserState;
import org.k2.processmining.model.user.User;
import org.k2.processmining.security.user.MyUserDetails;
import org.k2.processmining.service.UserService;
import org.k2.processmining.util.Message;
import org.omg.CORBA.MARSHAL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * Created by Aria on 2017/6/13.
 */
@Controller
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    UserService userService;

    @RequestMapping(value = "",method = RequestMethod.GET)
    public @ResponseBody
    Object getAllUsers(){
        Map map = new HashMap();
        map.put("users",userService.getAllUsers());
        return map;
    }

//    @RequestMapping(value = "/active",method = RequestMethod.POST)
//    public @ResponseBody
//    Object activeUser(@Valid @RequestBody IdListForm idListForm){
//        Map map = new HashMap();
//        userService.updateStateByUserId(idListForm.getIdList(), UserState.ACTIVE.getValue());
//        map.put("code",200);
//        return map;
//    }

//    @RequestMapping(value = "/freeze",method = RequestMethod.POST)
//    public @ResponseBody
//    Object freezeUser(@Valid @RequestBody IdListForm idListForm){
//        Map map = new HashMap();
//        userService.updateStateByUserId(idListForm.getIdList(),UserState.FREEZE.getValue());
//        map.put("code",200);
//        return map;
//    }

    @RequestMapping(value = "/register",method = RequestMethod.POST)
    public @ResponseBody
    Object register(@RequestBody RegisterForm registerForm,HttpServletRequest request,HttpServletResponse response){

        response.setHeader("Content-type", "application/json;charset=UTF-8");
        Map map = new HashMap();
        HttpSession session = request.getSession();
        String code = null,message = null;
        System.out.println("session:"+session.getAttribute("validateCode"));
        if ("".equals(registerForm.getValidateCode())){
            code = Message.REGISTER_VALIDATECODE_NULL_CODE;
            message = Message.REGISTER_VALIDATECODE_NULL;
        }
        else if (!registerForm.getValidateCode().equalsIgnoreCase((String) session.getAttribute("validateCode"))){
            System.out.println("session:"+session.getAttribute("validateCode"));
            code = Message.REGISTER_VALIDATECODE_WRONG_CODE;
            message = Message.REGISTER_VALIDATECODE_WRONG;
        }
        else if ("".equals(registerForm.getPassword()) || "".equals(registerForm.getRePassword()) || !registerForm.getPassword().equals(registerForm.getRePassword())){
            code = Message.REGISTER_PASSWORD_INCONSISTENT_CODE;
            message = Message.REGISTER_PASSWORD_INCONSISTENT;
            // 两次输入的密码不一致
        }
        else {
            User user = new User();
            user.setEmail(registerForm.getEmail());
            user.setName(registerForm.getName());
            user.setPassword(registerForm.getPassword());
            Map temp  = userService.addUser(user);
            code = (String) temp.get("code");
            message = (String) temp.get("message");
        }
        map.put("code",code);
        map.put("message",message);
        return map;

    }

    @RequestMapping(value = "/register/activate",method = RequestMethod.GET)
    public void activateAccount(@RequestParam(value = "email")String email,@RequestParam(value = "activateCode")String activateCode,
                                  HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
        System.out.println("######################activateAccount#####################");
        int code = userService.activateAccountByEmailAndCode(email,activateCode);
        System.out.println("code:"+code);
        request.getRequestDispatcher("/html/activateSuccess.html").forward(request,response);
//        return "index";
    }

//    @RequestMapping(value = "/delete",method = RequestMethod.DELETE)
//    public @ResponseBody
//    Object delete(@RequestBody IdListForm ids){
//        Map map = new HashMap();
//        userService.deleteUserById(ids.getIdList());
//        map.put("code",200);
//        return map;
//    }

    @RequestMapping(value = "/password",method = RequestMethod.POST)
    public @ResponseBody
    Object updatePassword(@RequestBody PwdForm pwdForm){
        Map map = new HashMap();
        System.out.println("updatePassword");
        userService.updatePwdById(getLoginUser().getId(),pwdForm.getPassword());
        map.put("code",200);
        return map;
    }

    @RequestMapping(value = "/checkout",method = RequestMethod.POST)
    public @ResponseBody
    Object checkoutUser(@RequestBody User user){
        Map map = new HashMap();
        map.put("code",userService.checkoutUserByEmailAndPwd(user.getEmail(),user.getPassword()));
        return map;
    }

    @RequestMapping(value = "/getUser",method = RequestMethod.GET)
    public @ResponseBody
    Object getUser(){
        return new HashMap<Object,User>(){{put("user",getLoginUser());}};
    }

    public User getUser(int temp){
        User user = new User();
        user.setId("1");
        user.setEmail("1@1.com");
        return user;
    }

    private User getLoginUser(){
        System.out.println("getLoginUser");
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof MyUserDetails){
            String email = ((MyUserDetails)principal).getUsername();
            System.out.println(email);
            User user = userService.getUserByEmail(email);
            return user;
        }else if (principal instanceof UserDetails){
            System.out.println("userDetails");
        }
        return null;
    }

    public static class PwdForm{
        private String password;

        public void setPassword(String password) {
            this.password = password;
        }

        public String getPassword() {
            return password;
        }
    }

    public static class EmailForm{
        String email;

        public void setEmail(String email) {
            this.email = email;
        }

        public String getEmail() {
            return email;
        }
    }

    public static class RegisterForm {
        String email;
        String name;
        String password;
        String rePassword;
        String validateCode;

        public RegisterForm(){}

        public void setEmail(String email) {
            this.email = email;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public void setValidateCode(String validateCode) {
            this.validateCode = validateCode;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setRePassword(String rePassword) {
            this.rePassword = rePassword;
        }

        public String getEmail() {
            return email;
        }

        public String getPassword() {
            return password;
        }

        public String getValidateCode() {
            return validateCode;
        }

        public String getName() {
            return name;
        }

        public String getRePassword() {
            return rePassword;
        }

        @Override
        public String toString() {
            return "email:"+email+" name:"+name+" password:"+password+" rePassword:"+rePassword+" validateCode:"+validateCode;
//            return super.toString();
        }
    }
}
