package org.k2.processmining.model.user;

import java.io.Serializable;

/**
 * Created by Aria on 2017/6/9.
 */
public class Administrator implements Serializable{
    private String id;
    private String workId;
    private String password;

    public void setId(String id) {
        this.id = id;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setWorkId(String workId) {
        this.workId = workId;
    }

    public String getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public String getWorkId() {
        return workId;
    }
}
