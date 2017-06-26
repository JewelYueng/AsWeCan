package org.k2.processmining.support.mining.model;

public class Link {
	private String source;
	private String target;
	private long value;
	public Link(String source, String target, long value) {
		super();
		this.source = source;
		this.target = target;
		this.value = value;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getTarget() {
		return target;
	}
	public void setTarget(String target) {
		this.target = target;
	}
	public long getValue() {
		return value;
	}
	public void setValue(long value) {
		this.value = value;
	}
}
