package org.k2.processmining.exception;

import org.k2.processmining.util.Message;

/**
 * Created by nyq on 2017/6/25.
 */
public class InternalServerErrorException extends RuntimeException {

    public InternalServerErrorException() {
        this(Message.INTERNAL_SERVER_ERROR);
    }
    public InternalServerErrorException(String message) {
        super(message);
    }
}
