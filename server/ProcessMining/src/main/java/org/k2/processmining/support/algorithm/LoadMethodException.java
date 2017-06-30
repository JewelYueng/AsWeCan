package org.k2.processmining.support.algorithm;

/**
 * Created by nyq on 2017/6/22.
 */
public class LoadMethodException extends Exception {
    private String message;

    public LoadMethodException() {}

    public LoadMethodException(String message) {
        this.message = message;
    }

    public LoadMethodException(Throwable e) {
        this(e.getMessage());
        this.initCause(e);
    }

    public LoadMethodException(Throwable e, String message) {
        this(message);
        this.initCause(e);
    }

    @Override
    public String getMessage() {
        return message;
    }
}
