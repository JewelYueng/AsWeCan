package org.k2.processmining.exception;

/**
 * Created by nyq on 2017/6/28.
 */
public class ForbiddenException extends RuntimeException {

    public ForbiddenException() {
        this("403 Forbidden");
    }
    public ForbiddenException(String message) {
        super(message);
    }
}
