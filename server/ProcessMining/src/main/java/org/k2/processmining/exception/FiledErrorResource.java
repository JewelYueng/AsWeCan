package org.k2.processmining.exception;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Created by nyq on 2017/7/2.
 */
public class FiledErrorResource {
    @JsonIgnore
    private String resource;
    @JsonIgnore
    private String code;
    private String filed;
    private String msg;

    public FiledErrorResource(String resource, String code, String filed, String msg) {
        this.resource = resource;
        this.code = code;
        this.filed = filed;
        this.msg = msg;
    }

    public String getResource() {
        return resource;
    }

    public String getCode() {
        return code;
    }

    public String getFiled() {
        return filed;
    }

    public String getMsg() {
        return msg;
    }
}
