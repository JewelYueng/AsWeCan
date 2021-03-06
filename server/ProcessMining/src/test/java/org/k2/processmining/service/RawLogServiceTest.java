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
public class RawLogServiceTest extends CommonLogServiceTest<RawLog> {

    private static ApplicationContext applicationContext;
    private static RawLogService rawLogService;

    public RawLogServiceTest() {
        super(rawLogService);
    }

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

    private String toJSON(Object o) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(o);
    }

}
