package org.k2.processmining.service.impl;

import org.k2.processmining.mapper.UserMapper;
import org.k2.processmining.model.user.User;
import org.k2.processmining.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        return 3;
    }

    @Override
    public void deleteUser() {

    }

    @Override
    public void getUserById() {

    }

    @Override
    public void updateStateByUserId(List<String> ids, int state) {
        userMapper.updateStateByUserId(ids,state);
    }


    @Override
    public void setUserState() {

    }

    @Override
    public List<User> getAllUsers() {
        return userMapper.listAllUsers();
    }

    @Override
    public void changePassword() {

    }

    @Override
    public User getUserByEmail(String email) {
        return userMapper.getUserByEmail(email);
    }
}
