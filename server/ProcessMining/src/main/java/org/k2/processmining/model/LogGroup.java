package org.k2.processmining.model;

import org.k2.processmining.model.log.EventLog;
import org.k2.processmining.model.log.NormalLog;
import org.k2.processmining.model.log.RawLog;
import org.k2.processmining.model.user.User;

import java.io.Serializable;

/**
 * Created by nyq on 2017/6/18.
 */
public class LogGroup implements Serializable{
    private User user;
    private RawLog rawLog;
    private NormalLog normalLog;
    private EventLog eventLog;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public RawLog getRawLog() {
        return rawLog;
    }

    public void setRawLog(RawLog rawLog) {
        this.rawLog = rawLog;
    }

    public NormalLog getNormalLog() {
        return normalLog;
    }

    public void setNormalLog(NormalLog normalLog) {
        this.normalLog = normalLog;
    }

    public EventLog getEventLog() {
        return eventLog;
    }

    public void setEventLog(EventLog eventLog) {
        this.eventLog = eventLog;
    }
}
