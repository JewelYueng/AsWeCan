package org.k2.processmining.exception;

/**
 * Created by nyq on 2017/6/24.
 */
public class JSONBadRequestException extends RuntimeException {
    private String message;

    public JSONBadRequestException() {}
    public JSONBadRequestException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
