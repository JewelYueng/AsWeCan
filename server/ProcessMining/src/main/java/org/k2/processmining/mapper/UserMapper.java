package org.k2.processmining.mapper;

import org.apache.ibatis.annotations.Param;
import org.k2.processmining.model.user.User;
import org.springframework.stereotype.Repository;

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
    User getUserByEmailAndPwd(@Param("email")String email,@Param("password")String password);
    User getUser(@Param("id")String id,@Param("email")String email,@Param("password")String password);
}
