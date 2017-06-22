package org.k2.processmining.service.impl;

import org.k2.processmining.mapper.UserMapper;
import org.k2.processmining.model.UserState;
import org.k2.processmining.model.user.User;
import org.k2.processmining.service.UserService;
import org.k2.processmining.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * Created by Aria on 2017/6/14.
 */

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    UserMapper userMapper;

    @Autowired
    UserService userService;

    @Override
    public int addUser(User user) {
        if (user == null){
            return 0;//用户为空
        }
        if (user.getEmail() == null){
            return 1; //邮箱为空
        }
        if (userService.getUserByEmail(user.getEmail()) != null){
            return 2;//邮箱重复
        }
        user.setId(Util.getUUIDString());
        user.setState(UserState.FREEZE.getValue());
        userMapper.save(user);
        return 3;
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
            return 0;
        }
        System.out.println("111111");
        if (userMapper.getUserByEmailAndPwd(email,password) == null){
            return 1;
        }
        return 2;
    }

    @Override
    public int activeAccount(String email) {
        StringBuffer buffer = new StringBuffer("点击下面链接激活账号，48小时生效，否则重新注册账号，链接只能使用一次，请尽快激活！</br>");
        buffer.append("<a href=\"http://localhost:8080/springmvc/user/register?action=activate&email=");
        buffer.append(email);

        return 0;
    }
}
