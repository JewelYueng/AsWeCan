package org.k2.processmining.support.mining.format;

import org.k2.processmining.support.mining.model.NetElement;
import org.k2.processmining.support.mining.algorithm.heuristics.models.SimpleHeuristicsNet;
import org.k2.processmining.support.mining.model.Link;
import org.k2.processmining.support.mining.model.Node;

import java.util.List;

/**
 * 因果矩阵其他形式的数据格式的父类
 * @author admin
 *
 */
public abstract class Formatter {
	/**
	 * 挖掘算法花费的时间
	 */
	public String mineUsetime;
	/**
	 * petri net的活动列表变量
	 */
	public List<NetElement> netElementList;
	/**
	 * 流程中的活动集
	 */
	public List<Node> nodes;
	/**
	 * 流程中所有活动间的关系集
	 */
	public List<Link> links;
	/**
	 * 事件日志去重后的traces
	 */
	public List<String> traces;
	/**
	 * 事件日志所有traces
	 */
	public List<String> allTraces;
	public String getMineUsetime() {
		return mineUsetime;
	}

	public void setMineUsetime(String mineUsetime) {
		this.mineUsetime = mineUsetime;
	}

	public List<NetElement> getNetElementList() {
		return netElementList;
	}

	public void setNetElementList(List<NetElement> netElementList) {
		this.netElementList = netElementList;
	}

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

	/**
	 * 将因果矩阵转换成前端接受的数据格式
	 * @param net
	 * @return
	 */
	abstract public void transform(SimpleHeuristicsNet net);
}
