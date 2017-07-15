package org.k2.processmining.support.event.parse;

import org.deckfour.xes.model.XLog;
import org.k2.processmining.model.log.EventLog;

import java.io.IOException;
import java.io.InputStream;

/**
 * 事件日志流解析出XLog对象
 */
public interface EventLogParse {
	XLog eventLogParse(EventLog eventLog);
	XLog eventLogParse(InputStream in) throws IOException, EventLogParseException;
	
}
