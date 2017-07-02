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
public class NormalLogServiceTest {
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
    public void updateStateByLogIdForUserTest() {
        List<String> ids = Arrays.asList("1", "2");
        int state = LogState.DELETE.getValue();
        String userId = "1";
        normalLogService.updateStateByLogIdForUser(ids, state, userId);
        for (String id : ids) {
            Assert.assertEquals(state, normalLogService.getNormalLogById(id).getState());
        }
    }

    @Test
    public void updateShareStateByLogIdForUserTest() {
        List<String> ids = Arrays.asList("3", "4");
        int state = LogShareState.SHARED.getValue();
        String userId = "1";
        normalLogService.updateShareStateByLogIdForUser(ids, state, userId);
        for (String id : ids) {
            Assert.assertEquals(state, normalLogService.getNormalLogById(id).getIsShared());
        }
    }

    @Test
    public void getNormalLogByIdTest() throws Exception {
        String id = "1";
        NormalLog normalLog = normalLogService.getNormalLogById(id);
        Assert.assertEquals(id, normalLog.getId());
        System.out.println("getNormalLogByIdTest: normalLog: " + toJSON(normalLog));
    }

    @Test
    public void getLogGroupsByUserIdTest() throws Exception {
        String userId = "1";
        List<LogGroup> logGroups = normalLogService.getLogGroupsByUserId(userId);
        System.out.println("getLogGroupsByUserIdTest: logGroups: " + toJSON(logGroups));
    }

    @Test
    public void getSharedLogGroupsTest() throws Exception {
        List<LogGroup> logGroups = normalLogService.getSharedLogGroups();
        System.out.println("getSharedLogsTest: logGroups: " + toJSON(logGroups));
    }

    @Test
    public void getLogByFuzzyNameTest() throws Exception {
        String keyWord = "t";
        User user = new User();
        user.setId("1");
        List<LogGroup> logGroups = normalLogService.getLogByFuzzyName(keyWord, user);
        System.out.println("getLogByFuzzyNameTest: logGroups: " + toJSON(logGroups));
    }

    @Test
    public void getSharedLogsByFuzzyNameTest() throws Exception {
        String keyWord = "t";
        List<LogGroup> logGroups = normalLogService.getSharedLogsByFuzzyName(keyWord);
        System.out.println("getSharedLogsByFuzzyNameTest: logGroups: " + toJSON(logGroups));
    }

    @Test
    public void transToEventLogTest() throws Exception {
        String normalLogId = "1";
        NormalLog normalLog = normalLogService.getNormalLogById(normalLogId);
        EventLog eventLog = normalLogService.transToEventLog(normalLog);
        Assert.assertNotNull(eventLog);
        System.out.println("transToEventLogTest: eventLog: " + toJSON(eventLog));
    }

    private String toJSON(Object o) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(o);
    }
}
