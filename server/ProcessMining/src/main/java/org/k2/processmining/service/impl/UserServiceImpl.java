package org.k2.processmining.service.impl;

import org.k2.processmining.mapper.UserMapper;
import org.k2.processmining.model.UserState;
import org.k2.processmining.model.user.User;
import org.k2.processmining.service.UserService;
import org.k2.processmining.storage.LogStorage;
import org.k2.processmining.util.Util;
import org.k2.processmining.utils.SendEmail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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

    private static String serverUrl = "http://192.168.0.100:8080";

    @Override
    public int addUser(User newUser) {

        if ("".equals(newUser.getEmail().trim())){
            return 403; //邮箱为空
        }
        User oldUser=  userService.getUserByEmail(newUser.getEmail());
        if (oldUser != null && oldUser.getState() != 2){
            return 404;//邮箱重复
        }
        if ("".equals(newUser.getName().trim())){
            return 405; //用户名为空
        }
        newUser.setId(Util.getUUIDString());
        newUser.setState(UserState.FREEZE.getValue());
        newUser.setRegisterDate(new Date());
        newUser.setActivateCode(SendEmail.getValCode(newUser.getEmail()));
        if (oldUser!=null){
            userMapper.updateUserByUserEmail(newUser);
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

        return 200;
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
    public void updatePwdById(String userId,String password) {
        userMapper.updatePwdById(userId,password);
    }

    @Override
    public List<User> getAllUsers() {
        return userMapper.listAllUsers();
    }

    @Override
    public User getUserByEmail(String email) {
        return userMapper.getUserByEmail(email);
    }

    @Override
    public int checkoutUserByEmailAndPwd(String email, String password) {

        if (userMapper.getUserByEmail(email) == null){
            return 400; //邮箱不存在
        }
        if (userMapper.getUserByEmailAndPwd(email,password) == null){
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
//        userMapper.deleteLogsByUserId(idList);
        System.out.println("id:"+idList.get(0));
        userMapper.deleteRawLogsByUserId(idList);
        userMapper.deleteNormalLogsByUserId(idList);
        userMapper.deleteEventLogsByUserId(idList);
        userMapper.updateStateByUserId(idList,UserState.DELETE.getValue());
    }

    private Date getValidateDate(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE,2);
        System.out.println(calendar.getTime());
        System.out.println(new Date());
        return calendar.getTime();
    }
}
