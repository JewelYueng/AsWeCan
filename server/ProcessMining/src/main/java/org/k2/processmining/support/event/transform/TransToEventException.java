package org.k2.processmining.support.event.transform;

/**
 * Created by nyq on 2017/6/30.
 */
public class TransToEventException extends Exception {

    public TransToEventException() {}

    public TransToEventException(String message) {
        super(message);
    }

    public TransToEventException(Throwable e) {
        this(e.getMessage());
        this.initCause(e);
    }
}
