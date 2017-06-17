package org.k2.processmining.model;

/**
 * Created by nyq on 2017/6/17.
 */
public enum State {
    FREEZE(0), ACTIVE(1), DELETE(2);
    private int value;
    State(int state) {
        this.value = state;
    }

    public int getValue() {
        return value;
    }
}
