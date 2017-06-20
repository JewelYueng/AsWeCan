package org.k2.processmining.controller;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * Created by nyq on 2017/6/20.
 */
public class IdListForm {
    @NotNull
    @Size(min = 1)
    private List<String> idList;

    public List<String> getIdList() {
        return idList;
    }

    public void setIdList(List<String> idList) {
        this.idList = idList;
    }
}
