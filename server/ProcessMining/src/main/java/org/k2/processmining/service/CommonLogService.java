package org.k2.processmining.service;

import org.k2.processmining.model.LogGroup;
import org.k2.processmining.model.user.User;
import org.springframework.security.access.prepost.PreAuthorize;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by nyq on 2017/7/3.
 */
public interface CommonLogService<T> {

    T getLogById(String id);
    void save(T log);
    void save(T log, InputStream inputStream) throws IOException;

    void updateShareStateByLogIdForUser(List<String> ids, int isShared, String userId);
    void updateStateByLogIdForUser(List<String> ids, int state, String userId);

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    List<LogGroup> getLogGroups(int page);
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    List<LogGroup> getLogGroupsByKeyWord(String keyWord, int page);
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    void deleteByAdmin(List<String> ids);

    List<LogGroup> getLogsByUser(User user, int page);
    List<LogGroup> getLogsByUserAndKeyWord(User user, String keyWord, int page);
    List<LogGroup> getSharedLogs(int page);
    List<LogGroup> getSharedLogsByKeyWord(String keyWord, int page);

    int getLogPageNum();
    int getLogPageNumByKeyWord(String keyWord);
    int getLogPageNumByUserId(String userId);
    int getLogPageNumByUserIdAndKeyWord(String userId, String keyWord);
    int getSharedLogPageNum();
    int getSharedLogPageNumByKeyWord(String keyWord);
    int getPageOfLogId(String userId, String id);
    int getPageOfSharedLogId(String id);
}
