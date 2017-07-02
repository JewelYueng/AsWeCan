package org.k2.processmining.support.normal.transform;

/**
 * Created by nyq on 2017/6/30.
 */
public class NormalizeException extends Exception {

    public NormalizeException(String message) {
        super(message);
    }

    public NormalizeException(Throwable e) {
        this(e.getMessage());
        initCause(e);
    }
}
