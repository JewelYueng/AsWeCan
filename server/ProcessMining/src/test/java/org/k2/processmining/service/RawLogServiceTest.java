package org.k2.processmining.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.k2.processmining.model.LogGroup;
import org.k2.processmining.model.LogShareState;
import org.k2.processmining.model.LogState;
import org.k2.processmining.model.log.NormalLog;
import org.k2.processmining.model.log.RawLog;
import org.k2.processmining.model.user.User;
import org.k2.processmining.support.normal.transform.LogConfiguration;
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
public class RawLogServiceTest {
    private static ApplicationContext applicationContext;
    private static RawLogService rawLogService;

    @BeforeClass
    public static void init() {
        applicationContext = new ClassPathXmlApplicationContext("classpath:spring/spring-service-test.xml",
                "classpath:spring/applicationContext-dao.xml");
        rawLogService = applicationContext.getBean(RawLogService.class);
    }

    @Test
    public void saveTest() throws Exception {
        RawLog rawLog = new RawLog();
        rawLog.setId(UUID.randomUUID().toString());
        rawLog.setLogName("rawLogTest.txt");
        rawLog.setUserId("1");
        rawLog.setCreateDate(new Date());
        rawLog.setFormat("txt");
        InputStream inputStream = RawLogServiceTest.class.getClassLoader().getResourceAsStream("log/rawLogTest.txt");
        rawLogService.save(rawLog, inputStream);
    }

    @Test
    public void updateStateByLogIdForUserTest() {
        List<String> ids = Arrays.asList("1", "2");
        int state = LogState.DELETE.getValue();
        String userId = "1";
        rawLogService.updateStateByLogIdForUser(ids, state, userId);
        for (String id : ids) {
            Assert.assertEquals(state, rawLogService.getLogById(id).getState());
        }
    }

    @Test
    public void updateShareStateByLogIdForUserTest() {
        List<String> ids = Arrays.asList("3", "4");
        int state = LogShareState.SHARED.getValue();
        String userId = "1";
        rawLogService.updateShareStateByLogIdForUser(ids, state, userId);
        for (String id : ids) {
            Assert.assertEquals(state, rawLogService.getLogById(id).getIsShared());
        }
    }

    @Test
    public void getRawLogByIdTest() throws Exception {
        String id = "1";
        RawLog rawLog = rawLogService.getLogById(id);
        Assert.assertEquals(id, rawLog.getId());
        System.out.println("getRawLogByIdTest: rawLog: " + toJSON(rawLog));
    }

    @Test
    public void getLogsByUserTest() throws Exception {
        User user = new User();
        user.setId("1");
        int page = 1;
        List<LogGroup> logGroups = rawLogService.getLogsByUser(user, page);
        System.out.println("getLogsByUserTest: logGroups: " + toJSON(logGroups));
    }

    @Test
    public void getSharedLogsTest() throws Exception {
        int page = 1;
        List<LogGroup> logGroups = rawLogService.getSharedLogs(page);
        System.out.println("getSharedLogsTest: logGroups: " + toJSON(logGroups));
    }

    @Test
    public void getLogByFuzzyNameTest() throws Exception {
        String keyWord = "t";
        User user = new User();
        user.setId("1");
        int page = 1;
        List<LogGroup> logGroups = rawLogService.getLogsByUserAndKeyWord(user, keyWord, page);
        System.out.println("getLogByFuzzyNameTest: logGroups: " + toJSON(logGroups));
    }

    @Test
    public void getSharedLogsByFuzzyNameTest() throws Exception {
        String keyWord = "t";
        int page = 1;
        List<LogGroup> logGroups = rawLogService.getSharedLogsByKeyWord(keyWord, page);
        System.out.println("getSharedLogsByFuzzyNameTest: logGroups: " + toJSON(logGroups));
    }

    @Test
    public void normalizeTest() throws Exception {
        RawLog rawLog = rawLogService.getLogById("1");
        String formats = "[QC],ABCD,[Method],Incident:A-B-C-D,Plan:C/B/ATD,Task:A/B/CTD,DEFAULT:A-B-CTD,A-B-CTD";
        String timeNames = "[QC]";
        String dataNames = "[Method];[Status]:EventName,[FKPlanID]:FKPlanID,[PKIncidentID]:PKIncidentID,[PKTaskID]:PKTaskID,[PKPlanID]:PKPlanID,[FKIncidentID]:FKIncidentID";
        String oriItemSeparator = "\t";
        String oriNameValSeparator = " ";
        String oriNulVal = "";
        LogConfiguration lc = new LogConfiguration(formats, timeNames, dataNames, oriItemSeparator, oriNameValSeparator, oriNulVal);
        NormalLog normalLog = rawLogService.normalize(rawLog, lc);
        Assert.assertNotNull(normalLog);
        System.out.println("normalizeTest: normalLog: " + toJSON(normalLog));
    }

    @Test
    public void getLogPageNumByUserIdTest() throws Exception {
        System.out.println(rawLogService.getLogPageNumByUserId("1"));
    }

    @Test
    public void getLogPageNumByUserIdAndKeyWordTest() throws Exception {
        String userId = "1";
        String keyWord = "t";
        System.out.println(rawLogService.getLogPageNumByUserIdAndKeyWord(userId, keyWord));
    }

    @Test
    public void getLogsByUserAndPageTest() throws Exception {
        User user = new User();
        user.setId("1");
        int page = 2;
        List<LogGroup> logGroups = rawLogService.getLogsByUser(user, page);
        System.out.println("getLogsByUserAndPageTest.page: " + page);
        System.out.println("getLogsByUserAndPageTest.logGroupsCount: " + logGroups.size());
        System.out.println("getLogsByUserAndPageTest.logGroups: " + toJSON(logGroups));
    }

    @Test
    public void getLogsByUserAndKeyWordAndPageTest() throws Exception {
        User user = new User();
        user.setId("1");
        int page = 2;
        String keyWord = "t";
        List<LogGroup> logGroups = rawLogService.getLogsByUserAndKeyWord(user, keyWord, page);
        System.out.println("getLogsByUserAndKeyWordAndPageTest.page: " + page);
        System.out.println("getLogsByUserAndKeyWordAndPageTest.logGroupsCount: " + logGroups.size());
        System.out.println("getLogsByUserAndKeyWordAndPageTest.logGroups: " + toJSON(logGroups));
    }

    @Test
    public void getPageOfLogIdTest() throws Exception {
        int page = rawLogService.getPageOfLogId("1", "1");
        System.out.println("getPageOfLogIdTest.page: " + page);
    }

    private String toJSON(Object o) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(o);
    }

}
