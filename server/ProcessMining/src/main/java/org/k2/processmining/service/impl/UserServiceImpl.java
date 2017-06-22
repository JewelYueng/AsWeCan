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
    public void addUser(User user) {

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
}
