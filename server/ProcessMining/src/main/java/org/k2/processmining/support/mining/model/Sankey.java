package org.k2.processmining.support.mining.model;

import java.util.List;

public class Sankey {
    /**
     * 流程中的活动集
     */
    private List<Node> nodes;
    /**
     * 流程中所有活动间的关系集
     */
    private List<Link> links;

    public List<Node> getNodes() {
        return nodes;
    }

    public void setNodes(List<Node> nodes) {
        this.nodes = nodes;
    }

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }
}
