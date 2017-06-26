package org.k2.processmining.exception;

/**
 * Created by nyq on 2017/6/25.
 */
public class JSONInternalServerErrorException extends RuntimeException {
    private String message;

    public JSONInternalServerErrorException() {}
    public JSONInternalServerErrorException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
