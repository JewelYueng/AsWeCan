package org.k2.processmining.service.whitebox;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.k2.processmining.model.log.EventLog;
import org.k2.processmining.model.log.NormalLog;
import org.k2.processmining.service.NormalLogService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.UUID;

/**
 * Created by nyq on 2017/6/26.
 */
public class NormalLogServiceWhiteBoxTest {

    private static ApplicationContext applicationContext;
    private static NormalLogService normalLogService;

    @BeforeClass
    public static void init() {
        applicationContext = new ClassPathXmlApplicationContext("classpath:spring/spring-service-test.xml",
                "classpath:spring/applicationContext-dao.xml");
        normalLogService = applicationContext.getBean(NormalLogService.class);
    }

    @Test
    public void saveTest() throws Exception {
        // 2.1
        NormalLog normalLog = new NormalLog();
        normalLog.setId(UUID.randomUUID().toString());
        normalLog.setLogName("normalLogTest.txt");
        normalLog.setUserId("1");
        normalLog.setCreateDate(new Date());
        normalLog.setFormat("txt");
        InputStream inputStream = NormalLogServiceWhiteBoxTest.class.getClassLoader().getResourceAsStream("log/normalLogTest.txt");
        normalLogService.save(normalLog, inputStream);


        // 2.2 inputStream exception
        inputStream.close();
        try {
            normalLogService.save(normalLog, inputStream);
            throw new RuntimeException("Could not appear");
        }
        catch (IOException e) {
            // assert exception
        }
    }

    @Test
    public void transToEventTest() throws Exception {
        // 2.3
        NormalLog normalLog = normalLogService.getNormalLogById("1");
        EventLog eventLog = normalLogService.transToEventLog(normalLog);
        Assert.assertNotNull(eventLog);

        // 2.4
        normalLog.setId("123");
        try {
            normalLogService.transToEventLog(normalLog);
            throw new RuntimeException("Could not appear");
        }
        catch (Exception e) {
            // assert exception
        }
    }
}
