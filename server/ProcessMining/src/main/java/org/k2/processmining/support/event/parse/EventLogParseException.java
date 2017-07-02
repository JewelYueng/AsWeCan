package org.k2.processmining.support.event.parse;

/**
 * Created by nyq on 2017/7/2.
 */
public class EventLogParseException extends Exception {
    public EventLogParseException() {}

    public EventLogParseException(String msg) {
        super(msg);
    }

    public EventLogParseException(Throwable e) {
        this(e.getMessage());
        initCause(e);
    }
}
