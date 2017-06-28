package org.k2.processmining.service;

import org.junit.BeforeClass;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by nyq on 2017/6/28.
 */
public class MiningMethodServiceTest {

    @BeforeClass
    public static void init() throws Exception {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring/spring-service-test.xml",
                "classpath:spring/applicationContext-dao.xml");

    }

    public void isActiveTest() throws Exception {

    }

    public void getMethodTest() throws Exception {

    }

    public void getAlgorithmTest() throws Exception {

    }

    public void getMethodConfig() throws Exception {

    }

    public void miningTest() throws Exception {

    }

    public void addMethodTest() throws Exception {

    }

    public void setMethodState() throws Exception {

    }

    public void deleteTest() {

    }
}
