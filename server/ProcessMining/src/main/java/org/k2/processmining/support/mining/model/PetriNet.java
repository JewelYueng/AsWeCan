package org.k2.processmining.support.mining.model;

import java.util.List;

public class PetriNet {

    /**
     * petri net的活动列表变量
     */
    public List<NetElement> netElementList;

    public List<NetElement> getNetElementList() {
        return netElementList;
    }

    public void setNetElementList(List<NetElement> netElementList) {
        this.netElementList = netElementList;
    }
}
