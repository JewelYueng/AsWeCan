package org.k2.processmining.model.log;

/**
 * Created by Aria on 2017/6/13.
 */
public class NormalLog extends AbstractLog {
    public static final String TYPE = "NormalLog";

    private String rawLogId;

    public NormalLog() {
        super(TYPE);
    }

    public String getRawLogId() {
        return rawLogId;
    }

    public void setRawLogId(String rawLogId) {
        this.rawLogId = rawLogId;
    }
}
