package org.k2.processmining.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.k2.processmining.controller.UserController;
import org.k2.processmining.model.UserState;
import org.k2.processmining.model.user.User;
import org.k2.processmining.util.Util;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.*;

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
        user.setName("testName");
        user.setEmail("574335078@qq.com");
        user.setPassword("123456");
        Map map = userService.addUser(user);
        Assert.assertEquals(200,Integer.parseInt(map.get("code").toString()));
    }

    @Test
    public void getAllUsersTest()throws Exception{
        List<User> users = userService.getAllUsers();
        System.out.println(toJSON(users));
    }

    @Test
    public void getUserByIdTest() throws Exception{
        String id = "1";
        User user = userService.getUserById(id);
        System.out.println("getUserByIdTest:user:"+toJSON(user));
    }

    @Test
    public void getUserByEmailTest() throws Exception{
        String email = "574335078@qq.com";
        User user = userService.getUserByEmail(email);
        System.out.println(toJSON(user));
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
    public void updateStateByUserEmailTest() throws Exception{
        List<String> emailList = Arrays.asList("y2k@y2k.com","574335078@qq.com");
        int state = UserState.ACTIVE.getValue();
        userService.updateStateByUserEmail(emailList,state);
        for (String email:emailList){
            Assert.assertEquals(state,userService.getUserByEmail(email).getState());
        }
    }

    @Test
    public void updatePwdByIdTest()throws Exception {

        String userId = "2ab753d8-bc87-481e-bccb-fc3fd44324f3";
        UserController.PwdForm pwdForm = new UserController.PwdForm();
        pwdForm.setOldPassword("123456");
        pwdForm.setNewPassword("1");
        pwdForm.setRePassword("1");
        userService.updatePwdById(userId,pwdForm);
        Assert.assertEquals(Util.encryptStr(pwdForm.getNewPassword()), userService.getUserById(userId).getPassword());
    }

    @Test
    public void checkoutUserByEmailAndPwd()throws Exception {
        String email = "574335078@qq.com";
        String password = "1";
        Assert.assertEquals(200,userService.checkoutUserByEmailAndPwd(email,password));
    }

    @Test
    public void activateAccountByEmailAndCodeTest() throws Exception{
        String email = "642977047@qq.com";
        String activateCode = "M46afaxoU8X8zsTiYHqrkA==";
        userService.activateAccountByEmailAndCode(email,activateCode);
        Assert.assertEquals(UserState.ACTIVE.getValue(),userService.getUserByEmail(email).getState());
    }

    @Test
    public void deleteUserByIdTest()throws Exception{
        List<String> list = Arrays.asList("2ab753d8-bc87-481e-bccb-fc3fd44324f3");
        userService.deleteUserById(list);
        Assert.assertEquals(UserState.DELETE.getValue(),userService.getUserById(list.get(0)).getState());

    }

    private String toJSON(Object o) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(o);
    }
}
