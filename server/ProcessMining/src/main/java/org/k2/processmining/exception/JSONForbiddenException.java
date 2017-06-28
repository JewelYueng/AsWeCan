package org.k2.processmining.exception;

/**
 * Created by nyq on 2017/6/28.
 */
public class JSONForbiddenException extends RuntimeException {
    private String message;

    public JSONForbiddenException() {
        message = "403 Forbidden";
    }
    public JSONForbiddenException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
