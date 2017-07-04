package org.k2.processmining.service.whitebox;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.k2.processmining.model.LogGroup;
import org.k2.processmining.model.LogShareState;
import org.k2.processmining.model.LogState;
import org.k2.processmining.model.log.EventLog;
import org.k2.processmining.model.log.NormalLog;
import org.k2.processmining.model.log.RawLog;
import org.k2.processmining.service.EventLogService;
import org.k2.processmining.service.impl.EventLogServiceImpl;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

/**
 * Created by nyq on 2017/6/26.
 */
public class EventLogServiceWhiteBoxTest {
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
        // 3.1
        EventLog eventLog = new EventLog();
        eventLog.setId(UUID.randomUUID().toString());
        eventLog.setLogName("eventLogTest.txt");
        eventLog.setUserId("1");
        eventLog.setCreateDate(new Date());
        eventLog.setFormat("xes");
        InputStream inputForRemote = EventLogServiceWhiteBoxTest.class.getClassLoader().getResourceAsStream("log/eventLogTest.xes");
        InputStream inputForSummarize = EventLogServiceWhiteBoxTest.class.getClassLoader().getResourceAsStream("log/eventLogTest.xes");
        eventLogService.save(eventLog, inputForRemote, inputForSummarize);

        // 3.2
        inputForRemote.close();
        try {
            eventLogService.save(eventLog, inputForRemote, inputForSummarize);
            throw new RuntimeException("Could not appear");
        }
        catch (IOException e) {
            // assert exception
        }
    }

    @Test
    public void verifyIsActive() throws Exception {
        EventLogServiceImpl eventLogService = new EventLogServiceImpl(null, null);
        Class<?> clazz = eventLogService.getClass();
        Method method = clazz.getDeclaredMethod("verifyLogGroupsIsActive", List.class);
        method.setAccessible(true);
        List<LogGroup> logGroups = getLogGroups();
        method.invoke(eventLogService, logGroups);

        Assert.assertEquals(2, logGroups.size());
    }

    @Test
    public void verifyIsShared() throws Exception {
        EventLogServiceImpl eventLogService = new EventLogServiceImpl(null, null);
        Class<?> clazz = eventLogService.getClass();
        Method method = clazz.getDeclaredMethod("verifyLogGroupsIsShared", List.class);
        method.setAccessible(true);
        List<LogGroup> logGroups = getLogGroups();
        method.invoke(eventLogService, logGroups);

        Assert.assertEquals(2, logGroups.size());
    }


    private List<LogGroup> getLogGroups() {
        List<LogGroup> logGroups = new LinkedList<>();

        RawLog rawLogNull = null;
        NormalLog normalLogNull = null;
        EventLog eventLogNull = null;

        // 判定情况 2:true, 5:true
        logGroups.add(getLogGroup(rawLogNull, normalLogNull, eventLogNull));

        EventLog eventLog = new EventLog();
        eventLog.setId("1");

        // 判定情况 2:false, 5:false, 3:true, 4:true, 6:true, 7:true
        logGroups.add(getLogGroup(rawLogNull, normalLogNull, eventLog));

        EventLog another = new EventLog();
        another.setId("2");
        RawLog rawLog = new RawLog();
        rawLog.setState(LogState.ACTIVE.getValue());
        rawLog.setIsShared(LogShareState.SHARED.getValue());

        NormalLog normalLog = new NormalLog();
        normalLog.setState(LogState.ACTIVE.getValue());
        normalLog.setIsShared(LogShareState.SHARED.getValue());

        logGroups.add(getLogGroup(rawLog, normalLog, another));

        return logGroups;
    }

    private LogGroup getLogGroup(RawLog rawLog, NormalLog normalLog, EventLog eventLog) {
        LogGroup logGroup = new LogGroup();
        logGroup.setRawLog(rawLog);
        logGroup.setNormalLog(normalLog);
        logGroup.setEventLog(eventLog);
        return logGroup;
    }
}
