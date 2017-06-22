package org.k2.processmining.service;

import org.k2.processmining.model.user.User;

import java.util.List;

/**
 * Created by Aria on 2017/6/13.
 */
public interface UserService {

    int addUser(User user);
    User getUserById(String id);
    void updateStateByUserId(List<String>ids,int state);
    List<User> getAllUsers();
    void updatePwdById(String userId,String password);
    User getUserByEmail(String email);
    int checkoutUserByEmailAndPwd(String email,String password);
    int activeAccount(String email);
}
