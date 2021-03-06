package org.k2.processmining.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.k2.processmining.model.MethodState;
import org.k2.processmining.model.log.EventLog;
import org.k2.processmining.model.mergemethod.MergeMethod;
import org.k2.processmining.support.algorithm.MergerFactory;
import org.k2.processmining.util.Util;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.*;

/**
 * Created by nyq on 2017/6/20.
 */
public class MergeMethodServiceTest {

    private static ApplicationContext applicationContext;
    private static MergeMethodService mergeMethodService;
    private static EventLogService eventLogService;
    private static MergeMethod activeMethod;
    private static MergeMethod inactiveMethod;

    @BeforeClass
    public static void init() {
        applicationContext = new ClassPathXmlApplicationContext("classpath:spring/spring-service-test.xml",
                "classpath:spring/applicationContext-dao.xml");
        mergeMethodService = applicationContext.getBean(MergeMethodService.class);
        eventLogService = applicationContext.getBean(EventLogService.class);

        String activeId = "1";
        activeMethod = new MergeMethod();
        activeMethod.setId(activeId);
        activeMethod.setState(MethodState.ACTIVE.getValue());

        String inactiveId = "1221";
        inactiveMethod = new MergeMethod();
        inactiveMethod.setId(inactiveId);
        inactiveMethod.setState(MethodState.FREEZE.getValue());
    }

    @Test
    public void isActiveTest() {
        Assert.assertTrue(mergeMethodService.isActive(activeMethod.getId()));
        Assert.assertFalse(mergeMethodService.isActive(inactiveMethod.getId()));

        Assert.assertTrue(mergeMethodService.isActive(activeMethod));
        Assert.assertFalse(mergeMethodService.isActive(inactiveMethod));
    }

    @Test
    public void getMethodByIdTest() throws Exception {
        MergeMethod mergeMethod = mergeMethodService.getMethodById(activeMethod.getId());
        Assert.assertNotNull(mergeMethod);
        System.out.println("getMethodByIdTest: mergeMethod: " + toJSON(mergeMethod));

        MergeMethod m = mergeMethodService.getMethodById(inactiveMethod.getId());
        Assert.assertTrue( m == null || !MethodState.isActive(m.getState()) );
    }

    @Test
    public void getMethodConfigTest() throws Exception {
        Map<String, Object> activeMethodConfig = mergeMethodService.getMethodConfig(activeMethod);
        Assert.assertNotNull(activeMethodConfig);
        System.out.println("getMethodConfigTest: activeMethodConfig: " + toJSON(activeMethodConfig));

        Map<String, Object> inactiveMethodConfig = mergeMethodService.getMethodConfig(inactiveMethod);
        Assert.assertEquals(0, inactiveMethodConfig.size());
    }

    @Test
    public void getActiveMethodsTest() throws Exception {
        List<MergeMethod> activeMethods = mergeMethodService.getActiveMethods();
        Assert.assertFalse(activeMethods.isEmpty());
        System.out.println("getActiveMethodsTest: activeMethods: " + toJSON(activeMethods));
    }

    @Test
    public void mergeTest() throws Exception {
        EventLog eventLog1 = eventLogService.getLogById("1");
        EventLog eventLog2 = eventLogService.getLogById("2");
        String methodId = activeMethod.getId();
        Map<String,Object> params = new HashMap<>();
        TimeResult<EventLog> result= mergeMethodService.merge(eventLog1, eventLog2, activeMethod, params);
        System.out.println("mergeTest: result: " + toJSON(result));
    }

    @Test
    public void addMethodTest() throws Exception {
        InputStream inputStream = MergeMethodServiceTest.class.getClassLoader().getResourceAsStream("algorithm/merger/aia-k2.jar");
        MockMultipartFile multipartFile = new MockMultipartFile("file", "aia-k2.jar", "", inputStream);
        MergeMethod mergeMethod = new MergeMethod();
        mergeMethod.setId(Util.getUUIDString());
        mergeMethodService.saveMethod(mergeMethod, new MultipartFile[]{ multipartFile });
        Assert.assertNotNull(mergeMethod);
        Map<String,Object> configs = mergeMethodService.getMethodConfig(mergeMethod);
        System.out.println(toJSON(configs));
        mergeMethodService.delete(Collections.singletonList(mergeMethod.getId()));
        Assert.assertNull(mergeMethodService.getMethodById(mergeMethod.getId()));
    }

    @Test
    public void setMethodStateTest() throws Exception {
        List<String> ids = Arrays.asList("1", "2");
        mergeMethodService.updateMethodState(ids, MethodState.FREEZE.getValue());
        for (String id : ids) {
            MergeMethod mergeMethod = mergeMethodService.getMethodById(id);
            Assert.assertEquals(MethodState.FREEZE.getValue(), mergeMethod.getState());
        }
        mergeMethodService.updateMethodState(ids, MethodState.ACTIVE.getValue());
        for (String id : ids) {
            MergeMethod mergeMethod = mergeMethodService.getMethodById(id);
            Assert.assertEquals(MethodState.ACTIVE.getValue(), mergeMethod.getState());
        }
    }

    @Test
    public void deleteTest() throws Exception {
        List<String> ids = Arrays.asList("1221");
        mergeMethodService.delete(ids);
        for (String id : ids) {
            MergeMethod mergeMethod = mergeMethodService.getMethodById(id);
            Assert.assertNull(mergeMethod);
            Assert.assertNull(MergerFactory.getInstance().getAlgorithm(id));
        }
    }

    private String toJSON(Object o) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(o);
    }
}
