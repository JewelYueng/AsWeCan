package org.k2.processmining.model.mergemethod;

/**
 * Created by Aria on 2017/6/9.
 */
public class MergeMethod {
    private String id;
    private String methodName;
    private int state = 1;

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
