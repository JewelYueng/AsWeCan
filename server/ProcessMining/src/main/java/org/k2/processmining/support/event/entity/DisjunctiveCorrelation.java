package org.k2.processmining.support.event.entity;

import java.util.ArrayList;
import java.util.List;

public class DisjunctiveCorrelation {
	public List<ConjunctiveCorrelation> CC;
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
	
	public DisjunctiveCorrelation() {
		this.AC = new ArrayList<AtomicCorrelation>();
		this.CC = new ArrayList<ConjunctiveCorrelation>();
	}

	public DisjunctiveCorrelation(List<AtomicCorrelation> AC, List<ConjunctiveCorrelation> CC) {
		if (AC != null)
			this.AC = AC;
		else
			this.AC = new ArrayList<AtomicCorrelation>();
		if (CC != null)
			this.CC = CC;
		else
			this.CC = new ArrayList<ConjunctiveCorrelation>();
	}

	public DisjunctiveCorrelation(ConjunctiveCorrelation cr) {
		this.CC = new ArrayList<ConjunctiveCorrelation>();
		this.CC.add(cr);
		this.AC = new ArrayList<AtomicCorrelation>();
	}

	public DisjunctiveCorrelation(AtomicCorrelation ar) {
		this.AC = new ArrayList<AtomicCorrelation>();
		this.AC.add(ar);
		this.CC = new ArrayList<ConjunctiveCorrelation>();
	}

	public void addAtomicRelation(AtomicCorrelation r) {
		this.AC.add(r);
	}

	public void addConjunctiveRelation(ConjunctiveCorrelation cr) {
		this.CC.add(cr);
	}
}
