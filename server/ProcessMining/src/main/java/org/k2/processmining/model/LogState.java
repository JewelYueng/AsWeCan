package org.k2.processmining.model;

/**
 * Created by nyq on 2017/6/17.
 */
public enum LogState {
    FREEZE(0), ACTIVE(1), DELETE(2);
    private int value;
    LogState(int state) {
        this.value = state;
    }

    public int getValue() {
        return value;
    }

    public static boolean isActive(int state) {
        return state == ACTIVE.value;
    }
}
