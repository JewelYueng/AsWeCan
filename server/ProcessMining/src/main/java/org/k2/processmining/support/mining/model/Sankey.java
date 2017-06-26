package org.k2.processmining.support.mining.model;

import java.util.List;

public class Sankey {
    /**
     * 流程中的活动集
     */
    public List<Node> nodes;
    /**
     * 流程中所有活动间的关系集
     */
    public List<Link> links;
}
