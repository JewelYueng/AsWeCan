package org.k2.processmining.model.mergemethod;

/**
 * Created by Aria on 2017/6/9.
 */
public class LogMergeMethod {
    private int id;
    private String name;
    private int state;

    public void setId(int id) {
        this.id = id;
    }

    public void setState(int state) {
        this.state = state;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public int getState() {
        return state;
    }

    public String getName() {
        return name;
    }
}
