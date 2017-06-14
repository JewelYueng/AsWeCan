package org.k2.processmining.model.log;

/**
 * Created by Aria on 2017/6/13.
 */
public class EventLog extends AbstractLog {
    public static final String TYPE = "EventLog";

    private String normalLogId;
    private int caseNumber;
    private int eventNumber;
    private int perEventInCase;
    private String eventNames;
    private String operatorNames;

    public EventLog() {
        super(TYPE);
    }

    public String getNormalLogId() {
        return normalLogId;
    }

    public void setNormalLogId(String normalLogId) {
        this.normalLogId = normalLogId;
    }

    public int getCaseNumber() {
        return caseNumber;
    }

    public void setCaseNumber(int caseNumber) {
        this.caseNumber = caseNumber;
    }

    public int getEventNumber() {
        return eventNumber;
    }

    public void setEventNumber(int eventNumber) {
        this.eventNumber = eventNumber;
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
