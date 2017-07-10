package org.k2.processmining.service.whitebox;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.k2.processmining.service.AdminService;
import org.k2.processmining.service.UserService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by Aria on 2017/7/9.
 */
public class AdminServiceWhiteBoxTest {

    private static AdminService adminService;
    private static ApplicationContext applicationContext;
    @BeforeClass
    public static void init() {
        applicationContext = new ClassPathXmlApplicationContext("classpath:spring/spring-service-test.xml",
                "classpath:spring/applicationContext-dao.xml");
         adminService = applicationContext.getBean(AdminService.class);
    }


    @Test
    public void addUserTest()throws Exception{
        String userId = "errorId";
        String password = "errorPwd";
        Assert.assertEquals(400,adminService.checkoutAdminByWorkIdAndPwd(userId,password));
        userId = "1";
        Assert.assertEquals(401,adminService.checkoutAdminByWorkIdAndPwd(userId,password));
    }

}
