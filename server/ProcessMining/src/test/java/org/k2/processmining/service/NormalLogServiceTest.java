package org.k2.processmining.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.k2.processmining.model.LogGroup;
import org.k2.processmining.model.LogShareState;
import org.k2.processmining.model.LogState;
import org.k2.processmining.model.log.EventLog;
import org.k2.processmining.model.log.NormalLog;
import org.k2.processmining.model.user.User;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by nyq on 2017/6/23.
 */
public class NormalLogServiceTest extends CommonLogServiceTest<NormalLog> {
    private static ApplicationContext applicationContext;
    private static NormalLogService normalLogService;

    public NormalLogServiceTest() {
        super(normalLogService);
    }

    @BeforeClass
    public static void init() {
        applicationContext = new ClassPathXmlApplicationContext("classpath:spring/spring-service-test.xml",
                "classpath:spring/applicationContext-dao.xml");
        normalLogService = applicationContext.getBean(NormalLogService.class);
    }

    @Test
    public void saveTest() throws Exception {
        NormalLog normalLog = new NormalLog();
        normalLog.setId(UUID.randomUUID().toString());
        normalLog.setLogName("normalLogTest.txt");
        normalLog.setUserId("1");
        normalLog.setCreateDate(new Date());
        normalLog.setFormat("txt");
        InputStream inputStream = NormalLogServiceTest.class.getClassLoader().getResourceAsStream("log/normalLogTest.txt");
        normalLogService.save(normalLog, inputStream);
    }

    @Test
    public void transToEventLogTest() throws Exception {
        String normalLogId = "1";
        NormalLog normalLog = normalLogService.getLogById(normalLogId);
        EventLog eventLog = normalLogService.transToEventLog(normalLog);
        Assert.assertNotNull(eventLog);
        System.out.println("transToEventLogTest: eventLog: " + toJSON(eventLog));
    }

    private String toJSON(Object o) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(o);
    }
}
