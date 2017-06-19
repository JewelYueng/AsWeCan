package org.k2.processmining.support.event.sumarise;

/**
 * Created by nyq on 2017/6/17.
 */
public class EventLogSummary {
    private int cases; //case数量
    private int events; //event数量
    private int perEventInCase; //平均每个case中event数量
    private String eventNames; //所有活动名称
    private String operatorNames; //活动执行者

    public int getCases() {
        return cases;
    }

    public void setCases(int cases) {
        this.cases = cases;
    }

    public int getEvents() {
        return events;
    }

    public void setEvents(int events) {
        this.events = events;
    }

    public int getPerEventInCase() {
        return perEventInCase;
    }

    public void setPerEventInCase(int perEventInCase) {
        this.perEventInCase = perEventInCase;
    }

    public String getEventNames() {
        return eventNames;
    }

    public void setEventNames(String eventNames) {
        this.eventNames = eventNames;
    }

    public String getOperatorNames() {
        return operatorNames;
    }

    public void setOperatorNames(String operatorNames) {
        this.operatorNames = operatorNames;
    }
}
