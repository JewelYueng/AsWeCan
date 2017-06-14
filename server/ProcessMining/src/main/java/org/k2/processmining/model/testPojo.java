package org.k2.processmining.model;

/**
 * Created by Aria on 2017/6/14.
 */
public class testPojo {
    private int id;
    private String username;

    public testPojo(){}

    public testPojo(int id,String username){
        this.id = id;
        this.username = username;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
