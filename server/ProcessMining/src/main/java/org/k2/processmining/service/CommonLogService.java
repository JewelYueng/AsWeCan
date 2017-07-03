package org.k2.processmining.service;

import org.k2.processmining.config.AppConfig;
import org.k2.processmining.exception.BadRequestException;
import org.k2.processmining.mapper.CommonLogMapper;
import org.k2.processmining.model.LogGroup;
import org.k2.processmining.model.LogShareState;
import org.k2.processmining.model.LogState;
import org.k2.processmining.model.user.User;

import java.util.List;

/**
 * Created by nyq on 2017/7/3.
 */
public abstract class CommonLogService {
    private CommonLogMapper mapper;

    public CommonLogService(CommonLogMapper mapper) {
        this.mapper = mapper;
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
                -1,
                null,
                (page-1) * AppConfig.PAGE_SIZE, AppConfig.PAGE_SIZE);
    }

    public List<LogGroup> getSharedLogsByKeyWord(String keyWord, int page) {
        return mapper.listLogGroupsPage(
                null,
                LogState.ACTIVE.getValue(),
                -1,
                keyWord,
                (page-1) * AppConfig.PAGE_SIZE, AppConfig.PAGE_SIZE);
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
