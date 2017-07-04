package org.k2.processmining.model.miningmethod;

import org.k2.processmining.model.MethodState;

import java.io.Serializable;

/**
 * Created by Aria on 2017/6/9.
 */
public class MiningMethod implements Serializable{
    private String methodName;
    private String id;
    private int state = MethodState.ACTIVE.getValue();

    public void setState(int state) {
        this.state = state;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getState() {
        return state;
    }

    public String getId() {
        return id;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }
}
