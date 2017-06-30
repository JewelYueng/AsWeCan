package org.k2.processmining.support.event.transform;

/**
 * Created by nyq on 2017/6/30.
 */
public class TransToEventException extends Exception {
    private String message;

    public TransToEventException() {}

    public TransToEventException(String message) {
        this.message = message;
    }

    public TransToEventException(Throwable e) {
        this.message = e.getMessage();
        this.initCause(e);
    }

    @Override
    public String getMessage() {
        return message;
    }
}
