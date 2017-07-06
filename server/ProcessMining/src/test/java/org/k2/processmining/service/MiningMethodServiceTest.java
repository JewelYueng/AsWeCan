package org.k2.processmining.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.k2.processmining.model.MethodState;
import org.k2.processmining.model.log.EventLog;
import org.k2.processmining.model.miningmethod.MiningMethod;
import org.k2.processmining.support.algorithm.Algorithm;
import org.k2.processmining.support.algorithm.MinerFactory;
import org.k2.processmining.support.mining.Miner;
import org.k2.processmining.support.mining.model.DiagramType;
import org.k2.processmining.util.Util;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.*;

/**
 * Created by nyq on 2017/6/28.
 */
public class MiningMethodServiceTest {

    private static MiningMethodService miningMethodService;
    private static EventLogService eventLogService;
    private static MiningMethod activeMethod;
    private static MiningMethod inActiveMethod;

    @BeforeClass
    public static void init() throws Exception {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring/spring-service-test.xml",
                "classpath:spring/applicationContext-dao.xml");
        miningMethodService = applicationContext.getBean(MiningMethodService.class);
        eventLogService = applicationContext.getBean(EventLogService.class);
        activeMethod = new MiningMethod();
        activeMethod.setId("1");
        activeMethod.setMethodName("heuristics");
        activeMethod.setState(MethodState.ACTIVE.getValue());
        inActiveMethod = new MiningMethod();
        inActiveMethod.setId("1221");
        inActiveMethod.setMethodName("inActive");
        inActiveMethod.setState(MethodState.FREEZE.getValue());
    }

    @Test
    public void isActiveTest() throws Exception {
        Assert.assertTrue(miningMethodService.isActive(activeMethod.getId()));
        Assert.assertTrue(miningMethodService.isActive(activeMethod));
        Assert.assertFalse(miningMethodService.isActive(inActiveMethod));
        Assert.assertFalse(miningMethodService.isActive(inActiveMethod.getId()));
    }

    @Test
    public void getMethodTest() throws Exception {
        MiningMethod miningMethod = miningMethodService.getMethodById(activeMethod.getId());
        Assert.assertNotNull(miningMethod);
        System.out.println(toJSON(miningMethod));

        String notExist = "notExist";
        miningMethod = miningMethodService.getMethodById(notExist);
        Assert.assertNull(miningMethod);
    }

    @Test
    public void getActiveMethodsTest() throws Exception {
        List<MiningMethod> activeMethods = miningMethodService.getActiveMethods();
        Assert.assertFalse(activeMethods.isEmpty());
        System.out.println("getActiveMethodsTest: activeMethods: " + toJSON(activeMethods));
    }

    @Test
    public void getAlgorithmTest() throws Exception {
        Algorithm algorithm = miningMethodService.getAlgorithmById(activeMethod.getId());
        Assert.assertNotNull(algorithm);
        System.out.println(toJSON(algorithm.getConfigMap()));
    }

    @Test
    public void getMethodConfig() throws Exception {
        Map<String,Object> configs = miningMethodService.getMethodConfig(activeMethod);
        Assert.assertNotNull(configs);
        System.out.println(toJSON(configs));
    }

    @Test
    public void miningTest() throws Exception {
        EventLog eventLog = eventLogService.getLogById("1");
        Algorithm<Miner> algorithm = miningMethodService.getAlgorithmById(activeMethod.getId());
        Map<String, Object> params = new HashMap<>();
        for (DiagramType diagramType : DiagramType.values()) {
            TimeResult result = miningMethodService.mining(eventLog, algorithm, params, diagramType);
            Assert.assertNotNull(result);
            System.out.println(toJSON(result));
        }
    }

    @Test
    public void addMethodTest() throws Exception {
        InputStream inputStream = MiningMethodServiceTest.class.getClassLoader().getResourceAsStream("algorithm/miner/heuristics-k2.jar");
        MockMultipartFile multipartFile = new MockMultipartFile("file", "heuristics-k2.jar", "", inputStream);
        MiningMethod miningMethod = new MiningMethod();
        miningMethod.setId(Util.getUUIDString());
        miningMethod = miningMethodService.saveMethod(miningMethod, new MultipartFile[]{ multipartFile });
        Assert.assertNotNull(miningMethod);
        Map<String,Object> configs = miningMethodService.getMethodConfig(miningMethod);
        System.out.println(toJSON(configs));
        miningMethodService.delete(Collections.singletonList(miningMethod.getId()));
        Assert.assertNull(miningMethodService.getMethodById(miningMethod.getId()));
    }

    @Test
    public void setMethodState() throws Exception {
        List<String> ids = Arrays.asList("1");
        miningMethodService.updateMethodState(ids, MethodState.FREEZE.getValue());
        for (String id : ids) {
            MiningMethod miningMethod = miningMethodService.getMethodById(id);
            Assert.assertEquals(MethodState.FREEZE.getValue(), miningMethod.getState());
        }
        miningMethodService.updateMethodState(ids, MethodState.ACTIVE.getValue());
        for (String id : ids) {
            MiningMethod miningMethod = miningMethodService.getMethodById(id);
            Assert.assertEquals(MethodState.ACTIVE.getValue(), miningMethod.getState());
        }
    }

    @Test
    public void deleteTest() {
        List<String> ids = Arrays.asList("1221");
        miningMethodService.delete(ids);

        for (String id : ids) {
            MiningMethod miningMethod = miningMethodService.getMethodById(id);
            Assert.assertNull(miningMethod);
            Assert.assertNull(MinerFactory.getInstance().getAlgorithm(id));
        }
    }

    private String toJSON(Object o) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(o);
    }
}
