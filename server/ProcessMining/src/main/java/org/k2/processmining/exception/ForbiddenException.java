package org.k2.processmining.exception;

/**
 * Created by nyq on 2017/6/28.
 */
public class ForbiddenException extends RuntimeException {
    private String message;

    public ForbiddenException() {
        message = "403 Forbidden";
    }
    public ForbiddenException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
