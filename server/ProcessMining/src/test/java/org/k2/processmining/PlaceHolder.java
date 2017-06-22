package org.k2.processmining;

import org.k2.processmining.support.algorithm.MethodManage;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by nyq on 2017/6/20.
 */
public class PlaceHolder {
    public static void main(String[] args) {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring/spring-service-test.xml",
                "classpath:spring/applicationContext-dao.xml",
                "classpath:spring/applicationContext-transaction.xml");
        MethodManage methodManage = applicationContext.getBean(MethodManage.class);
        System.out.println();
    }
}


