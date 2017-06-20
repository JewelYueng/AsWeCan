package org.k2.processmining.service;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.k2.processmining.mapper.RawLogMapper;
import org.k2.processmining.model.LogState;
import org.k2.processmining.model.MethodState;
import org.k2.processmining.model.mergemethod.MergeMethod;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Arrays;
import java.util.Map;

/**
 * Created by nyq on 2017/6/20.
 */
public class MergeMethodServiceTest {

    private static ApplicationContext applicationContext;
    private static MergeMethodService mergeMethodService;
    private static MergeMethod activeMethod;
    private static MergeMethod inactiveMethod;

    @BeforeClass
    public static void init() {
        applicationContext = new ClassPathXmlApplicationContext("classpath:spring/spring-service-test.xml",
                "classpath:spring/applicationContext-dao.xml");
        mergeMethodService = applicationContext.getBean(MergeMethodService.class);

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
    public void d() {
        System.out.println(applicationContext.getBean(RawLogMapper.class).updateIsShared(Arrays.asList("2","3"), 0, null));
    }

    @Test
    public void isActiveTest() {
        Assert.assertTrue(mergeMethodService.isActive(activeMethod.getId()));
        Assert.assertFalse(mergeMethodService.isActive(inactiveMethod.getId()));

        Assert.assertTrue(mergeMethodService.isActive(activeMethod));
        Assert.assertFalse(mergeMethodService.isActive(inactiveMethod));
    }

    @Test
    public void getMethodByIdTest() {
        Assert.assertNotNull(mergeMethodService.getMethodById(activeMethod.getId()));
        Assert.assertNull(mergeMethodService.getMethodById(inactiveMethod.getId()));
    }

    @Test
    public void getMethodConfig() {
        Map<String, Object> activeMethodConfig = mergeMethodService.getMethodConfig(activeMethod);
        Assert.assertNotNull(activeMethodConfig);
        System.out.println(activeMethodConfig);

        Map<String, Object> inactiveMethodConfig = mergeMethodService.getMethodConfig(inactiveMethod);
        Assert.assertEquals(0, inactiveMethodConfig.size());
        System.out.println(inactiveMethodConfig);
    }

    @Test
    public void mergeTest() {

    }
}
