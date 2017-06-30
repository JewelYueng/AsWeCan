package org.k2.processmining.exception;

import org.k2.processmining.util.Message;

/**
 * Created by nyq on 2017/6/25.
 */
public class InternalServerErrorException extends RuntimeException {
    private String message;

    public InternalServerErrorException() {
        message = Message.INTERNAL_SERVER_ERROR;
    }
    public InternalServerErrorException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
