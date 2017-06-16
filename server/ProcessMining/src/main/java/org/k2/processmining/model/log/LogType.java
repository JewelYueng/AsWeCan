package org.k2.processmining.model.log;

/**
 * Created by nyq on 2017/6/16.
 */
public enum  LogType {
    RAWLOG("RawLog"), NormalLog("NormalLog"), EVENTLOG("EventLog");

    public String value;
    LogType(String type) {
        this.value = type;
    }
}
