package org.k2.processmining.service;

import org.k2.processmining.config.AppConfig;
import org.k2.processmining.exception.BadRequestException;
import org.k2.processmining.mapper.CommonLogMapper;
import org.k2.processmining.model.LogGroup;
import org.k2.processmining.model.LogShareState;
import org.k2.processmining.model.LogState;
import org.k2.processmining.model.log.AbstractLog;
import org.k2.processmining.model.user.User;
import org.k2.processmining.storage.LogStorage;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by nyq on 2017/7/3.
 */
public abstract class CommonLogServiceImpl<T extends AbstractLog> implements CommonLogService<T> {
    private CommonLogMapper<T> mapper;
    private LogStorage logStorage;

    public CommonLogServiceImpl(CommonLogMapper<T> mapper, LogStorage logStorage) {
        this.mapper = mapper;
        this.logStorage = logStorage;
    }

    @Override
    public T getLogById(String id) {
        return mapper.getLogById(id);
    }

    @Override
    public void save(T log) {
        mapper.save(log);
    }

    @Override
    public void save(T log, InputStream inputStream) throws IOException {
        logStorage.upload(log, inputStream);
        mapper.save(log);
    }

    @Override
    public void updateShareStateByLogIdForUser(List<String> ids, int isShared, String userId) {
        mapper.updateIsShared(ids, isShared, userId);
    }

    @Override
    public void updateStateByLogIdForUser(List<String> ids, int state, String userId) {
        mapper.updateLogState(ids, state, userId);
    }

    public List<LogGroup> getLogGroups(int page) {
        return mapper.listLogGroupsPage(null,
                LogState.ACTIVE.getValue(),
                -1,
                null,
                (page-1)*AppConfig.PAGE_SIZE, AppConfig.PAGE_SIZE);
    }

    public List<LogGroup> getLogGroupsByKeyWord(String keyWord, int page) {
        return mapper.listLogGroupsPage(null,
                LogState.ACTIVE.getValue(),
                -1,
                keyWord,
                (page-1)*AppConfig.PAGE_SIZE, AppConfig.PAGE_SIZE);
    }

    public void deleteByAdmin(List<String> ids) {
        mapper.updateLogState(ids, LogState.DELETE.getValue(), null);
    }

    public List<LogGroup> getLogsByUser(User user, int page) {
        return mapper.listLogGroupsPage(user.getId(),
                LogState.ACTIVE.getValue(),
                -1,
                null,
                (page-1) * AppConfig.PAGE_SIZE, AppConfig.PAGE_SIZE);
    }

    public List<LogGroup> getLogsByUserAndKeyWord(User user, String keyWord, int page) {
        return  mapper.listLogGroupsPage(user.getId(),
                LogState.ACTIVE.getValue(),
                -1,
                keyWord,
                (page-1) * AppConfig.PAGE_SIZE, AppConfig.PAGE_SIZE);
    }

    public List<LogGroup> getSharedLogs(int page) {
        return mapper.listLogGroupsPage(
                null,
                LogState.ACTIVE.getValue(),
                LogShareState.SHARED.getValue(),
                null,
                (page-1) * AppConfig.PAGE_SIZE, AppConfig.PAGE_SIZE);
    }

    public List<LogGroup> getSharedLogsByKeyWord(String keyWord, int page) {
        return mapper.listLogGroupsPage(
                null,
                LogState.ACTIVE.getValue(),
                LogShareState.SHARED.getValue(),
                keyWord,
                (page-1) * AppConfig.PAGE_SIZE, AppConfig.PAGE_SIZE);
    }

    public int getLogPageNum() {
        Integer count = mapper.countLogs(null, LogState.ACTIVE.getValue(), -1, null);
        return AppConfig.pageNum(count);
    }

    public int getLogPageNumByKeyWord(String keyWord) {
        Integer count = mapper.countLogs(null, LogState.ACTIVE.getValue(), -1, keyWord);
        return AppConfig.pageNum(count);
    }

    public int getLogPageNumByUserId(String userId) {
        Integer count = mapper.countLogs(userId, LogState.ACTIVE.getValue(), -1, null);
        return AppConfig.pageNum(count);
    }

    public int getLogPageNumByUserIdAndKeyWord(String userId, String keyWord) {
        Integer count = mapper.countLogs(userId, LogState.ACTIVE.getValue(), -1, keyWord);
        return AppConfig.pageNum(count);
    }

    public int getSharedLogPageNum() {
        Integer count = mapper.countLogs(null, LogState.ACTIVE.getValue(),
                LogShareState.SHARED.getValue(), null);
        return AppConfig.pageNum(count);
    }

    public int getSharedLogPageNumByKeyWord(String keyWord) {
        Integer count = mapper.countLogs(null, LogState.ACTIVE.getValue(),
                LogShareState.SHARED.getValue(), keyWord);
        return AppConfig.pageNum(count);
    }

    public int getPageOfLogId(String userId, String id) {
        Integer line = mapper.lineOfLogId(id, userId,
                LogState.ACTIVE.getValue(), -1);
        if (line == null) {
            throw new BadRequestException("The log is not exist");
        }
        return AppConfig.pageNum(line);
    }

    public int getPageOfSharedLogId(String id) {
        Integer line = mapper.lineOfLogId(id, null,
                LogState.ACTIVE.getValue(), LogShareState.SHARED.getValue());
        if (line == null) {
            throw new BadRequestException("The log is not exist");
        }
        return AppConfig.pageNum(line);
    }
}
