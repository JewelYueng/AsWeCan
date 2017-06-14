package org.k2.processmining.model.miningmethod;

/**
 * Created by Aria on 2017/6/9.
 */
public class MiningMethod {
    private String name;
    private String id;
    private int state;

    public void setName(String name) {
        this.name = name;
    }

    public void setState(int state) {
        this.state = state;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public int getState() {
        return state;
    }

    public String getId() {
        return id;
    }

}
