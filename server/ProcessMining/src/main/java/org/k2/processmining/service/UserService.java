package org.k2.processmining.service;

import org.k2.processmining.model.user.User;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Created by Aria on 2017/6/13.
 */
public interface UserService {

    Map addUser(User user);
    List<User> getAllUsers();
    User getUserById(String id);
    User getUserByEmail(String email);
    void updateStateByUserId(List<String>ids,int state);
    void updateStateByUserEmail(List<String> emailList,int state);
    void updatePwdById(String userId,String password);
    int checkoutUserByEmailAndPwd(String email,String password);
    int sendActivateEmail(String email, String activateCode);
    int activateAccountByEmailAndCode(String email,String activateCode);
    @Transactional
    void deleteUserById(List<String> idList);

}
