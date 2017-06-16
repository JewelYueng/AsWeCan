package org.k2.processmining.support.merge;

import org.deckfour.xes.model.XLog;

import java.util.Map;

public interface Merger {
    XLog merge(XLog xLog1, XLog xLog2, Map<String, Object> params);
}
