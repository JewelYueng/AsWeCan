package org.k2.processmining.support.event.entity;

public class AtomicCorrelation {
	public int attIndex1;
	public int attIndex2;
	public int nonNullnum;
	public int caseSize;

	public int getNonNullnum() {
		return nonNullnum;
	}

	public void setNonNullnum(int nonNullnum) {
		this.nonNullnum = nonNullnum;
	}

	public int getCaseSize() {
		return caseSize;
	}

	public void setCaseSize(int caseSize) {
		this.caseSize = caseSize;
	}

	public AtomicCorrelation(int attIndex1, int attIndex2) {
		this.attIndex1 = attIndex1;
		this.attIndex2 = attIndex2;
	}
	
}
