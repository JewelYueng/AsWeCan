package org.k2.processmining.service.impl;

import org.k2.processmining.controller.UserController;
import org.k2.processmining.mapper.UserMapper;
import org.k2.processmining.model.UserState;
import org.k2.processmining.model.user.User;
import org.k2.processmining.service.UserService;
import org.k2.processmining.storage.LogStorage;
import org.k2.processmining.util.Message;
import org.k2.processmining.util.Util;
import org.k2.processmining.utils.SendEmail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.*;

/**
 * Created by Aria on 2017/6/14.
 */

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    UserMapper userMapper;

    @Autowired
    UserService userService;

    @Autowired
    LogStorage logStorage;

    private static String serverUrl = "http://116.56.129.93:8088/AssWeCan";

    @Override
    public Map addUser(User newUser) {

        Map map = new HashMap();

        if (newUser.getEmail() == null || "".equals(newUser.getEmail().trim())){
            map.put("code",Message.REGISTER_EMAIL_NULL_CODE);
            map.put("message",Message.REGISTER_EMAIL_NULL);
            return map; //邮箱为空
        }
        if (!checkEmail(newUser.getEmail())){
            map.put("code",Message.REGISTER_EMAIL_WRONG_CODE);
            map.put("message",Message.REGISTER_EMAIL_WRONG);
            return map;
        }
        User oldUser=  userService.getUserByEmail(newUser.getEmail());
        if (oldUser != null && oldUser.getState() != 2){
            map.put("code",Message.REGISTER_EMAIL_REPEAT_CODE);
            map.put("message",Message.REGISTER_EMAIL_REPEAT);
            return map;//邮箱重复
        }
        if (newUser.getName() == null || "".equals(newUser.getName().trim())){
            map.put("code",Message.REGISTER_NAME_NULL_CODE);
            map.put("message",Message.REGISTER_NAME_NULL);
            return map; //用户名为空
        }
        if (newUser.getName().length() > 32){
            map.put("code",Message.REGISTER_NAME_ILLEGAL_CODE);
            map.put("message",Message.REGISTER_NAME_ILLEGAL);
            return map;
        }
        newUser.setId(Util.getUUIDString());
        newUser.setState(UserState.FREEZE.getValue());
        newUser.setRegisterDate(new Date());
        newUser.setActivateCode(SendEmail.getValCode(newUser.getEmail()));
        newUser.setPassword(Util.encryptStr(newUser.getPassword()));
        if (oldUser!=null){
            userMapper.updateUserByUserEmail(newUser.getEmail(),
                    newUser.getName(),
                    newUser.getPassword(),
                    newUser.getState(),
                    newUser.getRegisterDate(),
                    newUser.getActivateCode());
        }else {
            userMapper.save(newUser);
        }

        new Thread(){
            @Override
            public void run() {
                super.run();
                userService.sendActivateEmail(newUser.getEmail(), newUser.getActivateCode());
            }
        }.start();
        map.put("code",Message.REGISTER_SUCCESS_CODE);
        map.put("message",Message.REGISTER_SUCCESS);
        return map;
    }

    @Override
    public User getUserById(String id) {
        return userMapper.getUserByUserId(id);
    }

    @Override
    public void updateStateByUserId(List<String> ids, int state) {
        userMapper.updateStateByUserId(ids,state);
    }

    @Override
    public void updateStateByUserEmail(List<String> emailList, int state) {
        userMapper.updateStateByEmail(emailList,state);
    }

    @Override
    public int updatePwdById(String userId, UserController.PwdForm pwdForm) {

        if (!userService.getUserById(userId).getPassword().equals(Util.encryptStr(pwdForm.getOldPassword()))){
            return 400; //密码错误
        }
        if ("".equals(pwdForm.getNewPassword()) || !pwdForm.getNewPassword().equals(pwdForm.getRePassword())){
            return 401; //两次输入的密码不一致
        }
        userMapper.updatePwdById(userId,Util.encryptStr(pwdForm.getNewPassword()));
        return 200;
    }

    @Override
    public List<User> getAllUsers() {

        List<User> users = userMapper.listAllUsers();
        verifyUserIsActiveOrForbidden(users);
        return users;
    }

    @Override
    public User getUserByEmail(String email) {

        User user = userMapper.getUserByEmailAndPwd(email,null);
        return user;
    }

    @Override
    public int checkoutUserByEmailAndPwd(String email, String password) {

        if (userMapper.getUserByEmailAndPwd(email,null) == null){
            return 400; //邮箱不存在
        }
        if (userMapper.getUserByEmailAndPwd(email,Util.encryptStr(password)) == null){
            return 401; //密码错误
        }
        return 200; //邮箱密码正确
    }

    @Override
    public int sendActivateEmail(String email, String activateCode) {
        String urlCode = null;
        try {
            urlCode = URLEncoder.encode(activateCode,"utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        userMapper.updateRegisterDateByEmail(email,new Date());

        StringBuffer buffer = new StringBuffer("点击下面链接激活账号，48小时生效，链接只能使用一次，请尽快激活！</br>");
        buffer.append("<a href=\""+serverUrl+"/user/register/activate?email="+email+"&activateCode="+urlCode);
//        buffer.append("<a href=\"http://localhost:8080/user/");
        buffer.append("\">"+serverUrl+"/user/register/activate?email="+email+"&activateCode="+activateCode);
        buffer.append("</a>");
        SendEmail.send(email,buffer.toString());
        return 0;
    }

    @Override
    public int activateAccountByEmailAndCode(String email, String activateCode) {
        User user = userService.getUserByEmail(email);
        String decodeStr = null;
        try {
            decodeStr = URLDecoder.decode(activateCode,"utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (user == null){
            return 400; //查无此用户
        }
        if (new Date().after(getValidateDate(user.getRegisterDate()))){
            return 399; //激活邮件过期
        }
        if (!activateCode.equals(user.getActivateCode())){
            System.out.println("newUser.getActivateCode:"+user.getActivateCode());
            return 401; //验证码错误
        }
        if (user.getState() == UserState.ACTIVE.getValue()){
            return 402;//用户已激活
        }
        List<String> emailList = new ArrayList<>();
        emailList.add(email);
        userService.updateStateByUserEmail(emailList,UserState.ACTIVE.getValue());
        try {
            logStorage.makeDirectoryForUser(user);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 200;
    }

    @Override
    public void deleteUserById(List<String> idList) {
        //TODO 将与用户相关的所有日志全部置为删除状态
        userMapper.deleteRawLogsByUserId(idList);
        userMapper.deleteNormalLogsByUserId(idList);
        userMapper.deleteEventLogsByUserId(idList);
        userMapper.updateStateByUserId(idList,UserState.DELETE.getValue());
    }

    public static Date getValidateDate(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE,2);
        return calendar.getTime();
    }

    private void verifyUserIsActiveOrForbidden(List<User> userList){
        Iterator<User> iterator = userList.iterator();
        while (iterator.hasNext()){
            User user = iterator.next();
            if (user.getState() == UserState.FREEZE.getValue() || user.getState() == UserState.DELETE.getValue()){
                iterator.remove();
            }
        }
    }

    public boolean checkEmail(String email){
        String regex = "^([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)*@([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)+[\\\\.][A-Za-z]{2,3}([\\\\.][A-Za-z]{2})?$";
        return email.matches(regex);
    }

    public static class UserForm{
        private String id;
        private String name;
        private String email;
        private int state;

        public void setName(String name) {
            this.name = name;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public void setId(String id) {
            this.id = id;
        }

        public void setState(int state) {
            this.state = state;
        }

        public String getName() {
            return name;
        }

        public String getEmail() {
            return email;
        }

        public String getId() {
            return id;
        }

        public int getState() {
            return state;
        }
    }
}
