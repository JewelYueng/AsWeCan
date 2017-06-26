package org.k2.processmining.support.mining;


import org.deckfour.xes.model.XLog;
import org.k2.processmining.support.mining.algorithm.heuristics.models.SimpleHeuristicsNet;
import org.k2.processmining.support.mining.model.PetriNet;
import org.k2.processmining.support.mining.model.ResourceRelation;
import org.k2.processmining.support.mining.model.Sankey;
import org.k2.processmining.support.mining.model.TransitionSystem;

import java.util.Map;

public interface Miner {
    SimpleHeuristicsNet mining(XLog xLog, Map<String, Object> params);
    PetriNet toPetriNet(SimpleHeuristicsNet net, XLog xLog);
    Map<String, ResourceRelation> toResourceRelation(SimpleHeuristicsNet net, String resourceAttr, XLog xLog);
    Sankey toSankey(SimpleHeuristicsNet net, XLog xLog);
    TransitionSystem toTransitionSystem(SimpleHeuristicsNet net, XLog xLog);
}
