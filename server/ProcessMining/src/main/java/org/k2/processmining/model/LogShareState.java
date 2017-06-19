package org.k2.processmining.model;

/**
 * Created by nyq on 2017/6/19.
 */
public enum LogShareState {
    UNSHARED(0), SHARED(1);
    int value;
    LogShareState(int value) {
        this.value = value;
    }
    public int getValue() {
        return value;
    }

    public static boolean isValid(int state) {
        return state == UNSHARED.value || state == SHARED.value;
    }

    public static boolean isShared(int state) {
        return state == SHARED.value;
    }
}
