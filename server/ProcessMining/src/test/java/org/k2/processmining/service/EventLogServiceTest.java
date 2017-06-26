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
public class EventLogServiceTest {
    private static ApplicationContext applicationContext;
    private static EventLogService eventLogService;

    @BeforeClass
    public static void init() {
        applicationContext = new ClassPathXmlApplicationContext("classpath:spring/spring-service-test.xml",
                "classpath:spring/applicationContext-dao.xml");
        eventLogService = applicationContext.getBean(EventLogService.class);
    }

    @Test
    public void saveTest() throws Exception {
        EventLog eventLog = new EventLog();
        eventLog.setId(UUID.randomUUID().toString());
        eventLog.setLogName("eventLogTest.txt");
        eventLog.setUserId("1");
        eventLog.setCreateDate(new Date());
        eventLog.setFormat("xes");
        InputStream inputForRemote = EventLogServiceTest.class.getClassLoader().getResourceAsStream("log/eventLogTest.xes");
        InputStream inputForSummarize = EventLogServiceTest.class.getClassLoader().getResourceAsStream("log/eventLogTest.xes");
        Assert.assertTrue(eventLogService.save(eventLog, inputForRemote, inputForSummarize));
    }

    @Test
    public void updateStateByLogIdForUserTest() {
        List<String> ids = Arrays.asList("1", "2");
        int state = LogState.DELETE.getValue();
        String userId = "1";
        eventLogService.updateStateByLogIdForUser(ids, state, userId);
        for (String id : ids) {
            Assert.assertEquals(state, eventLogService.getEventLogById(id).getState());
        }
    }

    @Test
    public void updateShareStateByLogIdForUserTest() {
        List<String> ids = Arrays.asList("3", "4");
        int state = LogShareState.SHARED.getValue();
        String userId = "1";
        eventLogService.updateShareStateByLogIdForUser(ids, state, userId);
        for (String id : ids) {
            Assert.assertEquals(state, eventLogService.getEventLogById(id).getIsShared());
        }
    }

    @Test
    public void getEventLogByIdTest() throws Exception {
        String id = "1";
        EventLog eventLog = eventLogService.getEventLogById(id);
        Assert.assertEquals(id, eventLog.getId());
        System.out.println("getEventLogByIdTest: eventLog: " + toJSON(eventLog));
    }

    @Test
    public void getLogsByUserIdTest() throws Exception {
        String userId = "1";
        List<LogGroup> logGroups = eventLogService.getLogsByUserId(userId);
        System.out.println("getLogsByUserTest: logGroups: " + toJSON(logGroups));
    }

    @Test
    public void getSharedLogsTest() throws Exception {
        List<LogGroup> logGroups = eventLogService.getSharedLogs();
        System.out.println("getSharedLogsTest: logGroups: " + toJSON(logGroups));
    }

    @Test
    public void getLogByFuzzyNameTest() throws Exception {
        String keyWord = "t";
        User user = new User();
        user.setId("1");
        List<LogGroup> logGroups = eventLogService.getLogByFuzzyName(keyWord, user);
        System.out.println("getLogByFuzzyNameTest: logGroups: " + toJSON(logGroups));
    }

    @Test
    public void getSharedLogsByFuzzyNameTest() throws Exception {
        String keyWord = "t";
        List<LogGroup> logGroups = eventLogService.getSharedLogsByFuzzyName(keyWord);
        System.out.println("getSharedLogsByFuzzyNameTest: logGroups: " + toJSON(logGroups));
    }

    private String toJSON(Object o) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(o);
    }
}
