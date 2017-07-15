package org.k2.processmining.service;

import java.io.Serializable;

/**
 * Created by nyq on 2017/6/24.
 */
public class TimeResult<T> implements Serializable {
    private T result;
    private long time;

    public void start() {
        time = System.currentTimeMillis();
    }

    public void stop() {
        time = System.currentTimeMillis() - time;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
