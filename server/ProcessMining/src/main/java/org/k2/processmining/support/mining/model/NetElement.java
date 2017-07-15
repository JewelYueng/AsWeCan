package org.k2.processmining.support.mining.model;

import java.util.List;

public class NetElement {
	
	public String element;
	
	public String name;
		
	public List<String> output;
	
	public int subnetNum;
	
	public List<Integer> subnetList;
	
	public String time;
	
	public String isStart;
	
	public String isEnd;
		
	public String getIsStart() {
		return isStart;
	}

	public void setIsStart(String isStart) {
		this.isStart = isStart;
	}

	public String getIsEnd() {
		return isEnd;
	}

	public void setIsEnd(String isEnd) {
		this.isEnd = isEnd;
	}

	public String getElement() {
		return element;
	}

	public void setElement(String element) {
		this.element = element;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getOutput() {
		return output;
	}

	public void setOutput(List<String> output) {
		this.output = output;
	}

	public int getSubnetNum() {
		return subnetNum;
	}

	public void setSubnetNum(int subnetNum) {
		this.subnetNum = subnetNum;
	}

	public List<Integer> getSubnetList() {
		return subnetList;
	}

	public void setSubnetList(List<Integer> subnetList) {
		this.subnetList = subnetList;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	
	
	
}
