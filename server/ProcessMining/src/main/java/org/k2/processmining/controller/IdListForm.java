package org.k2.processmining.controller;

import org.k2.processmining.controller.form.NotEmptyFields;

import java.util.List;

/**
 * Created by nyq on 2017/6/20.
 */
public class IdListForm {
    @NotEmptyFields
    private List<String> idList;

    public List<String> getIdList() {
        return idList;
    }

    public void setIdList(List<String> idList) {
        this.idList = idList;
    }
}
