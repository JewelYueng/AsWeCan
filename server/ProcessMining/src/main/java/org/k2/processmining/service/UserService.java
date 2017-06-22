package org.k2.processmining.service;

import org.k2.processmining.model.user.User;

import java.util.List;

/**
 * Created by Aria on 2017/6/13.
 */
public interface UserService {

    void addUser(User user);
    void deleteUser();
    void getUserById();
    void updateStateByUserId(List<String>ids,int state);
    List<User> getAllUsers();
    void setUserState();
    void changePassword();
    User getUserByEmail(String email);
}
