package org.k2.processmining.support.normal.transform;

/**
 * Created by nyq on 2017/6/30.
 */
public class NormalizeException extends Exception {
    private String message;

    public NormalizeException(String message) {
        this.message = message;
    }

    public NormalizeException(Throwable e) {
        this(e.getMessage());
        initCause(e);
    }

    @Override
    public String getMessage() {
        return message;
    }
}
