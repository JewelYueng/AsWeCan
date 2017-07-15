package org.k2.processmining.model.mergemethod;

import org.k2.processmining.model.MethodState;

import java.io.Serializable;

/**
 * Created by Aria on 2017/6/9.
 */
public class MergeMethod implements Serializable{
    private String id;
    private String methodName;
    private int state = MethodState.ACTIVE.getValue();

    public void setId(String id) {
        this.id = id;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getId() {
        return id;
    }

    public int getState() {
        return state;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }
}
