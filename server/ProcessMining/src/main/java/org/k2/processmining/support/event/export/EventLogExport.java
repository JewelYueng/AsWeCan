package org.k2.processmining.support.event.export;

import org.deckfour.xes.model.XLog;

import java.io.File;

/**
 * 事件日志的持久化
 * @author 琪
 *
 */
public interface EventLogExport {
	
	public void convertXLogToXesFile(XLog xlog, File file);

}
