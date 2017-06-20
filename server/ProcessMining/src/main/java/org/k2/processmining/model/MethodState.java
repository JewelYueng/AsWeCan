package org.k2.processmining.model;

/**
 * Created by nyq on 2017/6/20.
 */
public enum MethodState {
    FREEZE(0), ACTIVE(1);
    int value;

    MethodState(int value) {
        this.value = value;
    }
    public int getValue() {
        return value;
    }

    public static boolean isActive(int state) {
        return state == ACTIVE.value;
    }
}
