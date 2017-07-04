package org.k2.processmining.service.whitebox;

import org.junit.BeforeClass;
import org.junit.Test;
import org.k2.processmining.model.user.User;
import org.k2.processmining.service.UserService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

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
        User user = null;
        //1 true
        System.out.println("addUserTest:"+userService.addUser(user));

        user = new User();
        user.setName("1");
        user.setPassword("pwd");
        user.setEmail(null);
        //1 false 2 true
        System.out.println("addUserTest:"+userService.addUser(user));

        user.setEmail("1@1.com");
        //1 false 2 false 3,4 true
        System.out.println("addUserTest:"+userService.addUser(user));

        user.setEmail("8@8.com");
        user.setName(null);
        //1 false 2 false 3or4 false 5 true
        System.out.println("addUserTest:"+userService.addUser(user));
    }

    @Test
    public void activateAccountByEmailAndCodeTest() throws Exception{

        String email = "642977047@qq.com";
        String validateCode = "M46afaxoU8X8zsTiYHqrkA==";

        // 6 true
        System.out.println(userService.activateAccountByEmailAndCode("wrong email","kr91LREm82ZI8UWC/2lQjA=="));

        // 6 false 7 true
        System.out.println(userService.activateAccountByEmailAndCode("14608192@qq.com","kr91LREm82ZI8UWC/2lQjA=="));

        //6 false 7 false 8 true
        System.out.println(userService.activateAccountByEmailAndCode(email,"wrong code"));

        //6 false 7 false 8 false 9 true
        System.out.println(userService.activateAccountByEmailAndCode("574335078@qq.com","N+N7OtUqhB42haFS5lO/vQ=="));
    }
}
