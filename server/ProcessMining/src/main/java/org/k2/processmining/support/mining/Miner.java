package org.k2.processmining.support.mining;


import org.deckfour.xes.model.XLog;

import java.util.Map;

public interface Miner {
    String mining(XLog xLog, Map<String, Object> params);
}
