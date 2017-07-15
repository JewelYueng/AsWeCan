package org.k2.processmining.service.whitebox;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.k2.processmining.controller.UserController;
import org.k2.processmining.model.user.User;
import org.k2.processmining.service.UserService;
import org.k2.processmining.service.impl.UserServiceImpl;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Date;

/**
 * Created by Aria on 2017/7/3.
 */
public class UserServiceWhiteBoxTest {
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
        User user;
        user = new User();
        user.setName("1");
        user.setPassword("pwd");
        //1 true
        System.out.println("addUserTest:"+toJSON(userService.addUser(user)));


        user.setEmail("fajgjalgjol");
        //1 false 2 true
        System.out.println("addUserTest:"+toJSON(userService.addUser(user)));

        user.setEmail("y2k@y2k.com");
        //1 false 2 false 3,4 true
        System.out.println("addUserTest:"+toJSON(userService.addUser(user)));

        user.setEmail("8@8.com");
        user.setName(null);
        //1 false 2 false 3or4 false 5 true
        System.out.println("addUserTest:"+toJSON(userService.addUser(user)));
    }

    @Test
    public void updatePwdByIdTest()throws Exception{
        String userId = "2ab753d8-bc87-481e-bccb-fc3fd44324f3";
        UserController.PwdForm pwdForm = new UserController.PwdForm();
        pwdForm.setOldPassword("errorPwd");
        pwdForm.setNewPassword("123");
        pwdForm.setRePassword("errorPwd");
        Assert.assertEquals(400,userService.updatePwdById(userId,pwdForm));
        pwdForm.setOldPassword("1");
        Assert.assertEquals(401,userService.updatePwdById(userId,pwdForm));

    }

    @Test
    public void checkoutUserByEmailAndPwdTest()throws Exception{
        String email = "errorEmail@qq.com";
        String password = "errorPwd";
        Assert.assertEquals(400,userService.checkoutUserByEmailAndPwd(email,password));
        email = "574335078@qq.com";
        Assert.assertEquals(401,userService.checkoutUserByEmailAndPwd(email,password));
    }

    @Test
    public void activateAccountByEmailAndCodeTest() throws Exception{

        String email = "errorEmail@qq.com";
        String validateCode = "N+N7OtUqhB42haFS5lO/vQ==";
        Assert.assertEquals(400,userService.activateAccountByEmailAndCode(email,validateCode));

        email = "14608192@qq.com";
        Assert.assertEquals(399,userService.activateAccountByEmailAndCode(email,validateCode));

        email = "574335078@qq.com";
        validateCode = "errorCode";
        Assert.assertEquals(401,userService.activateAccountByEmailAndCode(email,validateCode));

        email = "574335078@qq.com";
        validateCode = "N+N7OtUqhB42haFS5lO/vQ==";
        Assert.assertEquals(402,userService.activateAccountByEmailAndCode(email,validateCode));

    }



    private String toJSON(Object o) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(o);
    }
}
