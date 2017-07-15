package org.k2.processmining.exception;

/**
 * Created by nyq on 2017/6/24.
 */
public class BadRequestException extends RuntimeException {

    public BadRequestException() {}
    public BadRequestException(String message) {
        super(message);
    }
}
