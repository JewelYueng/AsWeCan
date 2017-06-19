package org.k2.processmining.support.event.entity;

import java.util.ArrayList;
import java.util.List;

public class ConjunctiveCorrelation {
	public List<AtomicCorrelation> AC;
	public int nonNullnum;
	public int caseSize;
	public int tmpNNnum;
	public int tmpCszie;
	
	public int getTmpNNnum() {
		return tmpNNnum;
	}

	public void setTmpNNnum(int tmpNNnum) {
		this.tmpNNnum = tmpNNnum;
	}

	public int getTmpCszie() {
		return tmpCszie;
	}

	public void setTmpCszie(int tmpCszie) {
		this.tmpCszie = tmpCszie;
	}

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

	public ConjunctiveCorrelation(List<AtomicCorrelation> AC) {
		this.AC = AC;
	}

	public ConjunctiveCorrelation(AtomicCorrelation r) {
		this.AC = new ArrayList<AtomicCorrelation>();
		this.AC.add(r);
	}

	public void addRelation(AtomicCorrelation r) {
		this.AC.add(r);
	}
}
