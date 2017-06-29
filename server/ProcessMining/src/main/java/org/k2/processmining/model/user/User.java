package org.k2.processmining.model.user;

import java.io.Serializable;
import java.util.Date;

/**
 * 补充了state状态
 * Created by Aria on 2017/6/9.
 */
public class User implements Serializable{
    private String id;
    private String name;
    private String email;
    private String password;
    private int state = 1;
    private Date registerDate;
    private String activateCode;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getState() {
        return state;
    }

    public void setActivateCode(String activateCode) {
        this.activateCode = activateCode;
    }

    public String getActivateCode() {
        return activateCode;
    }

    public Date getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(Date registerDate) {
        this.registerDate = registerDate;
    }
}
