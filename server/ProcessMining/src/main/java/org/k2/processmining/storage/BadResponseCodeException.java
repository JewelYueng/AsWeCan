package org.k2.processmining.storage;

import java.io.IOException;

/**
 * Created by nyq on 2017/7/2.
 */
public class BadResponseCodeException extends IOException {
    public BadResponseCodeException() {}

    public BadResponseCodeException(String s) {
        super(s);
    }
}
