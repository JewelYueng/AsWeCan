package org.k2.processmining.model;

/**
 * Created by nyq on 2017/6/19.
 */
public enum UserState {
    FREEZE(0), ACTIVE(1);
    int value;
    UserState(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
