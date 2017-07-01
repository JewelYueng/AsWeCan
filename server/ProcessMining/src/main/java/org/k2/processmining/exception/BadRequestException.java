package org.k2.processmining.exception;

/**
 * Created by nyq on 2017/6/24.
 */
public class BadRequestException extends RuntimeException {
    private String message;

    public BadRequestException() {}
    public BadRequestException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
