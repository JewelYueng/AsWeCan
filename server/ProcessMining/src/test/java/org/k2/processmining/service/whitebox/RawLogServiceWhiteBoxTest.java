package org.k2.processmining.service.whitebox;

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
import org.k2.processmining.model.log.RawLog;
import org.k2.processmining.service.RawLogService;
import org.k2.processmining.service.RawLogServiceTest;
import org.k2.processmining.service.impl.RawLogServiceImpl;
import org.k2.processmining.support.normal.transform.LogConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

/**
 * Created by nyq on 2017/6/26.
 */
public class RawLogServiceWhiteBoxTest {
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
        // 1.1
        RawLog rawLog = new RawLog();
        rawLog.setId(UUID.randomUUID().toString());
        rawLog.setLogName("rawLogTest.txt");
        rawLog.setUserId("1");
        rawLog.setCreateDate(new Date());
        rawLog.setFormat("txt");
        InputStream inputStream = RawLogServiceWhiteBoxTest.class.getClassLoader().getResourceAsStream("log/rawLogTest.txt");
        Assert.assertTrue(rawLogService.save(rawLog, inputStream));


        // 1.2 inputStream exception
        inputStream.close();
        Assert.assertFalse(rawLogService.save(rawLog, inputStream));
    }

    @Test
    public void normalizeTest() throws Exception {
        // 1.3
        RawLog rawLog = rawLogService.getRawLogById("1");
        String formats = "[QC],ABCD,A-B-CTD,A-B-CTD";
        String timeNames = "[QC]";
        String dataNames = "[Method];[Status]:EventName,[FKPlanID]:FKPlanID,[PkIncidentID]:PkIncidentID,[PkTaskID]:PkTaskID,[PKPlanID]:PKPlanID;[FKIncidentID]:FKIncidentID";
        String oriItemSeparator = "\t";
        String oriNameValSeparator = " ";
        String oriNulVal = "";
        LogConfiguration lc = new LogConfiguration(formats, timeNames, dataNames, oriItemSeparator, oriNameValSeparator, oriNulVal);
        NormalLog normalLog = rawLogService.normalize(rawLog, lc);
        Assert.assertNotNull(normalLog);
        System.out.println("normalizeTest: normalLog: " + toJSON(normalLog));


        // 1.4
        rawLog.setId("123");
        try {
            normalLog = rawLogService.normalize(rawLog, lc);
            System.out.println("Could not appear");
        }
        catch (Exception e) {
            // assert exception
        }
    }

    @Test
    public void verifyIsActive() throws Exception {
        RawLogServiceImpl rawLogService = new RawLogServiceImpl();
        Class<?> clazz = rawLogService.getClass();
        Method verifyLogGroupsIsActive  = clazz.getDeclaredMethod("verifyLogGroupsIsActive", List.class);
        verifyLogGroupsIsActive.setAccessible(true);

        List<LogGroup> logGroups = getLogGroups();
        verifyLogGroupsIsActive.invoke(rawLogService, logGroups);

        Assert.assertEquals(2, logGroups.size());
    }

    @Test
    public void verifyIsShared() throws Exception {
        RawLogServiceImpl rawLogService = new RawLogServiceImpl();
        Class<?> clazz = rawLogService.getClass();
        Method verifyLogGroupsIsShared = clazz.getDeclaredMethod("verifyLogGroupsIsShared", List.class);
        verifyLogGroupsIsShared.setAccessible(true);

        List<LogGroup> logGroups = getLogGroups();

        verifyLogGroupsIsShared.invoke(rawLogService, logGroups);

        Assert.assertEquals(2, logGroups.size());
    }

    public List<LogGroup> getLogGroups() {
        List<LogGroup> logGroups = new LinkedList<>();

        // 判定情况 3:true, 7:true
        RawLog rawLogNull = null;
        NormalLog normalLogNull = null;
        EventLog eventLogNull = null;
        logGroups.add(getLogGroup(rawLogNull, normalLogNull, eventLogNull));

        // 判定情况 3:false, 4:false, 5:false, 6:false, 7:false, 8:false, 9:false, 10:false;
        RawLog rawLog = new RawLog();
        rawLog.setId("1");
        NormalLog normalLog = new NormalLog();
        normalLog.setState(LogState.ACTIVE.getValue());
        normalLog.setIsShared(LogShareState.SHARED.getValue());
        EventLog eventLog = new EventLog();
        eventLog.setState(LogState.ACTIVE.getValue());
        eventLog.setIsShared(LogShareState.SHARED.getValue());
        logGroups.add(getLogGroup(rawLog, normalLog, eventLog));

        // 判定情况 3:false, 4:true, 7:false, 8:true
        logGroups.add(getLogGroup(rawLog, normalLog, eventLog));

        // 判定情况 5:true, 6:true, 9:true, 10:true
        RawLog rawLog1 = new RawLog();
        rawLog1.setId("2");
        logGroups.add(getLogGroup(rawLog1, null, null));


        return logGroups;
    }

    private String toJSON(Object o) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(o);
    }

    private LogGroup getLogGroup(RawLog rawLog, NormalLog normalLog, EventLog eventLog) {
        LogGroup logGroup = new LogGroup();
        logGroup.setRawLog(rawLog);
        logGroup.setNormalLog(normalLog);
        logGroup.setEventLog(eventLog);
        return logGroup;
    }
}
