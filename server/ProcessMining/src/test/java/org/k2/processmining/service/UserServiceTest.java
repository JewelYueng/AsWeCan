package org.k2.processmining.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.k2.processmining.model.UserState;
import org.k2.processmining.model.user.User;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * Created by Aria on 2017/7/3.
 */
public class UserServiceTest {
    private static ApplicationContext applicationContext;
    private static UserService userService;

    @BeforeClass
    public static void init() {
        applicationContext = new ClassPathXmlApplicationContext("classpath:spring/spring-service-test.xml",
                "classpath:spring/applicationContext-dao.xml");
        userService = applicationContext.getBean(UserService.class);
    }

    @Test
    public void addUserTest() throws Exception{
        User user = new User();
        user.setId(UUID.randomUUID().toString());
        user.setName("testName");
        user.setEmail("574335078@qq.com");
        user.setPassword("testPwd");
        userService.addUser(user);
    }

    @Test
    public void updateStateByUserIdTest() throws Exception{
        List<String> ids = Arrays.asList("1","2");
        int state = UserState.FREEZE.getValue();
        userService.updateStateByUserId(ids,state);
        for (String id:ids){
            Assert.assertEquals(state,userService.getUserById(id).getState());
        }
    }

    @Test
    public void getUserByIdTest() throws Exception{
        String id = "1";
        User user = userService.getUserById(id);
        System.out.println("getUserByIdTest:user:"+toJSON(user));
    }

    @Test
    public void updateStateByUserEmailTest() throws Exception{
        List<String> emailList = Arrays.asList("1@1.com","2@2.com");
        int state = UserState.DELETE.getValue();
        userService.updateStateByUserEmail(emailList,state);
        for (String email:emailList){
            Assert.assertEquals(state,userService.getUserByEmail(email).getState());
        }
    }

    @Test
    public void getAllUsersTest()throws Exception{
        List<User> users = userService.getAllUsers();
        for (User user:users){
            System.out.println("getAllUsersTest:user:"+toJSON(user  ));
        }
    }

    @Test
    public void updatePwdByIdTest()throws Exception{
        String userId = "1";
        String password = "newPwd";
        userService.updatePwdById(userId,password);
        Assert.assertEquals(password,userService.getUserById(userId).getPassword());
    }

    @Test
    public void getUserByEmailTest() throws Exception{
        String email = "1@1.com";
        User user = userService.getUserByEmail(email);
        System.out.println(toJSON(user));
    }

    @Test
    public void activateAccountByEmailAndCodeTest() throws Exception{
        String email = "642977047@qq.com";
        String activateCode = "M46afaxoU8X8zsTiYHqrkA==";
        userService.activateAccountByEmailAndCode(email,activateCode);
        Assert.assertEquals(UserState.ACTIVE.getValue(),userService.getUserByEmail(email).getState());
    }

    private String toJSON(Object o) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(o);
    }
}
