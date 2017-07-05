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
public class EventLogServiceTest extends CommonLogServiceTest<EventLog> {
    private static ApplicationContext applicationContext;
    private static EventLogService eventLogService;

    public EventLogServiceTest() {
        super(eventLogService);
    }

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
        eventLogService.save(eventLog, inputForRemote, inputForSummarize);
    }

    private String toJSON(Object o) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(o);
    }
}
