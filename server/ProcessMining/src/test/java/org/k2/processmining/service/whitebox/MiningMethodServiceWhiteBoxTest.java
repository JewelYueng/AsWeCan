package org.k2.processmining.service.whitebox;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.k2.processmining.exception.InternalServerErrorException;
import org.k2.processmining.model.log.EventLog;
import org.k2.processmining.model.miningmethod.MiningMethod;
import org.k2.processmining.service.EventLogService;
import org.k2.processmining.service.MiningMethodService;
import org.k2.processmining.service.TimeResult;
import org.k2.processmining.support.algorithm.Algorithm;
import org.k2.processmining.support.algorithm.MinerFactory;
import org.k2.processmining.support.mining.Miner;
import org.k2.processmining.support.mining.model.DiagramType;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Collections;

/**
 * Created by nyq on 2017/6/29.
 */
public class MiningMethodServiceWhiteBoxTest {

    private static MiningMethodService miningMethodService;
    private static EventLogService eventLogService;
    private static String notExistId;
    private static String inActiveId;
    private static String activeId;

    @Rule
    public final ExpectedException expectedException = ExpectedException.none();

    @BeforeClass
    public static void init() throws Exception {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring/spring-service-test.xml",
                "classpath:spring/applicationContext-dao.xml");
        miningMethodService = applicationContext.getBean(MiningMethodService.class);
        eventLogService = applicationContext.getBean(EventLogService.class);
        notExistId = "notExistId";
        inActiveId = "1221";
        activeId = "1";
    }

    @Test
    public void getAlgorithmByIdTest() throws Exception {
        Assert.assertNull(miningMethodService.getAlgorithmById(notExistId));

        MinerFactory.getInstance().put(notExistId, new Algorithm<>());
        Assert.assertNull(miningMethodService.getAlgorithmById(notExistId));
        MinerFactory.getInstance().deleteAlgorithm(notExistId);

        MinerFactory.getInstance().put(inActiveId, new Algorithm<>());
        Assert.assertNull(miningMethodService.getAlgorithmById(inActiveId));

        Assert.assertNotNull(miningMethodService.getAlgorithmById(activeId));
    }

    @Test
    public void getMethodConfigTest() throws Exception {
        MiningMethod miningMethod = new MiningMethod();
        miningMethod.setId(inActiveId);
        Assert.assertNull(miningMethodService.getMethodConfig((MiningMethod) null));
        Assert.assertNull(miningMethodService.getMethodConfig(miningMethod));
        Assert.assertNull(miningMethodService.getMethodConfig(inActiveId));

        miningMethod.setId(activeId);
        Assert.assertNotNull(miningMethodService.getMethodConfig(miningMethod));
        Assert.assertNotNull(miningMethodService.getMethodConfig(activeId));
    }

    @Test
    public void miningTest1() throws Exception {
        Algorithm<Miner> algorithm = miningMethodService.getAlgorithmById(activeId);

        expectedException.expect(InternalServerErrorException.class);
        miningMethodService.mining((EventLog)null, algorithm, Collections.emptyMap(), DiagramType.PetriNet);
    }

    @Test
    public void miningTest2() throws Exception {
        String eventLogId = "1";
        EventLog eventLog = eventLogService.getEventLogById(eventLogId);

        expectedException.expect(InternalServerErrorException.class);
        miningMethodService.mining(eventLog, (Algorithm)null, Collections.emptyMap(), DiagramType.PetriNet);
    }

    @Test
    public void miningTest() throws Exception {
        EventLog eventLog = eventLogService.getEventLogById("1");
        Algorithm<Miner> algorithm = miningMethodService.getAlgorithmById(activeId);
        for (DiagramType diagramType : DiagramType.values()) {
            TimeResult result = miningMethodService.mining(eventLog, algorithm, Collections.emptyMap(), diagramType);
            Assert.assertNotNull(result);
            System.out.println(toJSON(result));
        }
    }

    private String toJSON(Object o) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(o);
    }
}
