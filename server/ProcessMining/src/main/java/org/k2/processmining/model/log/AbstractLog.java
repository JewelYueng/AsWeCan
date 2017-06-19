package org.k2.processmining.model.log;

import java.util.Date;

/**
 * Created by Aria on 2017/6/13.
 */
public abstract class AbstractLog {
    private String type;  //日志类型
    private String id;
    private String logName;
    private Date createDate;
    private String format; //日志文件格式
    private int state = 1;
    private int isShared = 0;
    private String userId;

    public AbstractLog(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLogName() {
        return logName;
    }

    public void setLogName(String logName) {
        this.logName = logName;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getIsShared() {
        return isShared;
    }

    public void setIsShared(int isShared) {
        this.isShared = isShared;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
