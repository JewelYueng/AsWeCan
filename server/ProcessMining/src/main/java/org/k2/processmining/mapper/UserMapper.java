package org.k2.processmining.mapper;

import org.apache.ibatis.annotations.Param;
import org.k2.processmining.model.user.User;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * Created by Aria on 2017/6/22.
 */

@Repository
public interface UserMapper {

    List<User> listAllUsers();
    void save(User user);
    void updatePwdById(@Param("id")String id,@Param("password")String password);
    User getUserByUserId(@Param("id")String userId);
    User getUserByEmail(@Param("email")String email);
    void updateStateByUserId(@Param("ids")List<String> ids,@Param("state")int state);
    void updateStateByEmail(@Param("emailList")List<String> emailList,@Param("state")int state);
    void updateUserByUserEmail(User user);
    User getUserByEmailAndPwd(@Param("email")String email,@Param("password")String password);
    void deleteLogsByUserId(@Param("idList")List<String> idList);
    void deleteRawLogsByUserId(@Param("idList")List<String> idList);
    void deleteNormalLogsByUserId(@Param("idList")List<String> idList);
    void deleteEventLogsByUserId(@Param("idList")List<String> idList);
    void updateRegisterDateByEmail(@Param("email")String email, @Param("date")Date date);
}