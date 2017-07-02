package org.k2.processmining.exception;

import java.util.List;

/**
 * Created by nyq on 2017/7/2.
 */
public class ErrorResource {
    private int code;
    private String msg;
    private List<FiledErrorResource> fieldErrors;

    public ErrorResource(int code, String msg, List<FiledErrorResource> fieldErrors) {
        this.code = code;
        this.msg = msg;
        this.fieldErrors = fieldErrors;
    }

    public ErrorResource(String msg, List<FiledErrorResource> fieldErrors) {
        this(0, msg, fieldErrors);
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public List<FiledErrorResource> getFieldErrors() {
        return fieldErrors;
    }
}
