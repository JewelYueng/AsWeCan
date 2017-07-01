package org.k2.processmining.service.whitebox;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.k2.processmining.exception.BadRequestException;
import org.k2.processmining.model.MethodState;
import org.k2.processmining.model.log.EventLog;
import org.k2.processmining.model.mergemethod.MergeMethod;
import org.k2.processmining.service.EventLogService;
import org.k2.processmining.service.MergeMethodService;
import org.k2.processmining.service.TimeResult;
import org.k2.processmining.support.algorithm.MergerFactory;
import org.k2.processmining.support.merge.Merger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by nyq on 2017/6/26.
 */
public class MergeMethodServiceWhiteBoxTest {
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
    public void mergeTest() throws Exception {
        EventLog eventLog1 = eventLogService.getEventLogById("1");
        EventLog eventLog2 = eventLogService.getEventLogById("2");
        TimeResult<EventLog> res = null;
        Map<String,Object> params = new HashMap<>();

        // 判定情况 1:true
        mergeAssertException(eventLog1, eventLog2, inactiveMethod, params);

        // 判定情况 1:false, 2:true
        Merger merger = MergerFactory.getInstance().getAlgorithm(activeMethod.getId()).getAlgorithm();
        MergerFactory.getInstance().getAlgorithm(activeMethod.getId()).setAlgorithm(null);
        mergeAssertException(eventLog1, eventLog2, activeMethod, params);
        MergerFactory.getInstance().getAlgorithm(activeMethod.getId()).setAlgorithm(merger);

        // 判定情况 1:false, 2:false, 3:true
        mergeAssertException(null, eventLog2, activeMethod, params);

        // 判定情况 1:false, 2:false, 3:false, 4:true
        mergeAssertException(eventLog1, null, activeMethod, params);

        // 判定情况 1:false, 2:false, 3:false, 4:false, 5:true
        MergerFactory.getInstance().getAlgorithm(activeMethod.getId()).setAlgorithm(((xLog1, xLog2, params1) -> null));
        mergeAssertException(eventLog1, eventLog2, activeMethod, params);
        MergerFactory.getInstance().getAlgorithm(activeMethod.getId()).setAlgorithm(merger);

        // 判定情况 1:false, 2:false, 3:false, 4:false, 5:false, 6:true
//        Assert.assertNull(res);

        // 判定情况 1:false, 2:false, 3:false, 4:false, 5 false, 6:false
        res = mergeMethodService.merge(eventLog1, eventLog2, activeMethod, params);
        Assert.assertNotNull(res);
    }

    private void mergeAssertException(EventLog eventLog1, EventLog eventLog2, MergeMethod mergeMethod, Map<String, Object> params) {
        try {
            mergeMethodService.merge(eventLog1, eventLog2, mergeMethod, params);
        }
        catch (BadRequestException e) {
            return;
        }
        throw new RuntimeException("fail");
    }
}
