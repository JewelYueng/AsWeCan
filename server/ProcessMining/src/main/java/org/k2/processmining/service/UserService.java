package org.k2.processmining.service;

import org.k2.processmining.model.user.User;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Aria on 2017/6/13.
 */
public interface UserService {

    int addUser(User user);
    User getUserById(String id);
    void updateStateByUserId(List<String>ids,int state);
    void updateStateByUserEmail(List<String> emailList,int state);
    List<User> getAllUsers();
    void updatePwdById(String userId,String password);
    User getUserByEmail(String email);
    int checkoutUserByEmailAndPwd(String email,String password);
    int sendActivateEmail(String email, String activateCode);
    int activateAccountByEmailAndCode(String email,String activateCode);
    @Transactional
    void deleteUserById(List<String> idList);

}
