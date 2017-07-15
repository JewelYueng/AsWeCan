package org.k2.processmining.support.mining.model;

import java.util.List;

public class TransitionSystem {
    /**
     * 流程中的活动集
     */
    private List<Node> nodes;
    /**
     * 流程中所有活动间的关系集
     */
    private List<Link> links;
    /**
     * 事件日志去重后的traces
     */
    private List<String> traces;
    /**
     * 事件日志所有traces
     */
    private List<String> allTraces;

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

    public List<String> getTraces() {
        return traces;
    }

    public void setTraces(List<String> traces) {
        this.traces = traces;
    }

    public List<String> getAllTraces() {
        return allTraces;
    }

    public void setAllTraces(List<String> allTraces) {
        this.allTraces = allTraces;
    }
}
