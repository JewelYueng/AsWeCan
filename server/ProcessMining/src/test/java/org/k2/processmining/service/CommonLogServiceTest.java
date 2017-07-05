package org.k2.processmining.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;
import org.k2.processmining.model.LogGroup;
import org.k2.processmining.model.LogShareState;
import org.k2.processmining.model.LogState;
import org.k2.processmining.model.log.AbstractLog;
import org.k2.processmining.model.user.User;

import java.util.Arrays;
import java.util.List;

/**
 * Created by nyq on 2017/7/5.
 */
public abstract class CommonLogServiceTest<T extends AbstractLog> {

    private CommonLogService<T> logService;

    public CommonLogServiceTest(CommonLogService<T> logService) {
        this.logService = logService;
    }
    
    @Test
    public void getLogByIdTest() throws Exception {
        String id = "1";
        T log = logService.getLogById(id);
        Assert.assertEquals(id, log.getId());
        System.out.println("getLogByIdTest: rawLog: " + toJSON(log));
    }
    
    @Test
    public void updateShareStateByLogIdForUserTest() throws Exception {
        List<String> ids = Arrays.asList("7", "8");
        int sharedState = LogShareState.SHARED.getValue();
        int unSharedState = LogShareState.UNSHARED.getValue();
        String userId = "1";
        logService.updateShareStateByLogIdForUser(ids, unSharedState, userId);
        for (String id : ids) {
            Assert.assertEquals(unSharedState, logService.getLogById(id).getIsShared());
        }
        logService.updateShareStateByLogIdForUser(ids, sharedState, userId);
        for (String id : ids) {
            Assert.assertEquals(sharedState, logService.getLogById(id).getIsShared());
        }
    }
    
    @Test
    public void updateStateByLogIdForUserTest() throws Exception {
        List<String> ids = Arrays.asList("3", "4");
        int deleteState = LogState.DELETE.getValue();
        String userId = "1";
        logService.updateStateByLogIdForUser(ids, deleteState, userId);
        for (String id : ids) {
            Assert.assertEquals(deleteState, logService.getLogById(id).getState());
        }
    }
    
    @Test
    public void getLogGroupsTest() throws Exception {
        int page = 1;
        List<LogGroup> logGroups = logService.getLogGroups(page);
        System.out.println("getLogGroupsTest:" + toJSON(logGroups));
    }
    
    @Test
    public void getLogGroupsByKeyWordTest() throws Exception {
        int page = 1;
        String keyword = "t";
        List<LogGroup> logGroups = logService.getLogGroupsByKeyWord(keyword, page);
    }
    
    @Test
    public void deleteByAdminTest() throws Exception {
        List<String> ids = Arrays.asList("5", "6");
        int deleteState = LogState.DELETE.getValue();
        logService.deleteByAdmin(ids);
        for (String id : ids) {
            Assert.assertEquals(deleteState, logService.getLogById(id).getState());
        }
    }
    
    @Test
    public void getLogsByUserTest() throws Exception {
        User user = new User();
        user.setId("1");
        int page = 1;
        List<LogGroup> logGroups = logService.getLogsByUser(user, page);
        System.out.println("getLogsByUserTest: logGroups: " + toJSON(logGroups));
    }
    
    @Test
    public void getLogsByUserAndKeyWordTest() throws Exception {
        User user = new User();
        user.setId("1");
        int page = 2;
        String keyWord = "t";
        List<LogGroup> logGroups = logService.getLogsByUserAndKeyWord(user, keyWord, page);
        System.out.println("getLogsByUserAndKeyWordTest.page: " + page);
        System.out.println("getLogsByUserAndKeyWordTest.logGroupsCount: " + logGroups.size());
        System.out.println("getLogsByUserAndKeyWordTest.logGroups: " + toJSON(logGroups));
    }
    
    @Test
    public void getSharedLogsTest() throws Exception {
        int page = 1;
        List<LogGroup> logGroups = logService.getSharedLogs(page);
        System.out.println("getSharedLogsTest: logGroups: " + toJSON(logGroups));
    }
    
    @Test
    public void getSharedLogsByKeyWordTest() throws Exception {
        String keyWord = "t";
        int page = 1;
        List<LogGroup> logGroups = logService.getSharedLogsByKeyWord(keyWord, page);
        System.out.println("getSharedLogsByFuzzyNameTest: logGroups: " + toJSON(logGroups));
    }
    
    @Test
    public void getLogPageNumTest() throws Exception {
        int page = logService.getLogPageNum();
        System.out.println("getLogPageNumTest.page: " + page);
    }
    
    @Test
    public void getLogPageNumByKeyWordTest() throws Exception {
        String keyWord = "t";
        int page = logService.getLogPageNumByKeyWord(keyWord);
        System.out.println("getLogPageNumByKeyWordTest: {page: " + page + "},{keyWord: " + keyWord +"}");
    }
    
    @Test
    public void getLogPageNumByUserIdTest() throws Exception {
        String userId = "1";
        int page = logService.getLogPageNumByUserId(userId);
        System.out.println("getLogPageNumByUserIdTest: {page: " + page + "},{userId: " + userId +"}");
    }
    
    @Test
    public void getLogPageNumByUserIdAndKeyWordTest() throws Exception {
        String userId = "1";
        String keyWord = "t";
        int page = logService.getLogPageNumByUserIdAndKeyWord(userId, keyWord);
        System.out.printf("getLogPageNumByUserIdAndKeyWordTest: " +
                "{page: %d}, {userId: %s}, {keyWord: %s}\n", page, userId, keyWord);
    }
    
    @Test
    public void getSharedLogPageNumTest() throws Exception {
        int page = logService.getSharedLogPageNum();
        System.out.printf("getSharedLogPageNumTest: {page: %d}\n", page);
    }
    
    @Test
    public void getSharedLogPageNumByKeyWordTest() throws Exception {
        String keyWord = "t";
        int page = logService.getSharedLogPageNumByKeyWord(keyWord);
        System.out.printf("getSharedLogPageNumByKeyWordTest: {page: %d}, {keyWord: %s}\n", page, keyWord);
    }
    
    @Test
    public void getPageOfLogIdTest() throws Exception {
        String userId = "1";
        String logId = "1";
        int page = logService.getPageOfLogId(userId, logId);
        System.out.printf("getPageOfLogIdTest: {page: %d}, {userId: %s}, {logId: %s}\n", page, userId, logId);
    }
    
    @Test
    public void getPageOfSharedLogIdTest() throws Exception {
        String id = "1";
        int page = logService.getPageOfSharedLogId(id);
        System.out.printf("getPageOfSharedLogIdTest: {page; %d}, {id: %s}\n", page, id);
    }

    private String toJSON(Object o) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(o);
    }
}
