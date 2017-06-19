package org.k2.processmining.support.event.parse;

import org.deckfour.xes.model.XLog;

import java.io.InputStream;

/**
 * 事件日志流解析出XLog对象
 */
public interface EventLogParse {
	
	public XLog eventLogParse(InputStream in);
	
}
