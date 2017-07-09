package org.k2.processmining.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.k2.processmining.model.user.Administrator;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by Aria on 2017/7/5.
 */
public class AdminServiceTest {
    private static ApplicationContext applicationContext;
    private static AdminService adminService;

    @BeforeClass
    public static void init() {
        applicationContext = new ClassPathXmlApplicationContext("classpath:spring/spring-service-test.xml",
                "classpath:spring/applicationContext-dao.xml");
        adminService = applicationContext.getBean(AdminService.class);
    }

    @Test
    public void getAdminByWorkIdTest() throws Exception {
        String workId = "1";
        Administrator administrator = adminService.getAdminByWorkId(workId);
        System.out.println(toJSON(administrator));
    }

    @Test
    public void checkoutAdminByWorkIdAndPwdTest()throws Exception{
        String workId = "1";
        String password = "1";
        Assert.assertEquals(200,adminService.checkoutAdminByWorkIdAndPwd(workId,password));
    }


    private String toJSON(Object o) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(o);
    }
}
