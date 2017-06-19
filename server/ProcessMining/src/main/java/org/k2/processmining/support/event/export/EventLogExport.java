package org.k2.processmining.support.event.export;

import org.deckfour.xes.model.XLog;

import java.io.File;
import java.io.OutputStream;

/**
 * 事件日志的持久化
 * @author 琪
 *
 */
public interface EventLogExport {
	
	void convertXLogToXesFile(XLog xlog, File file);
	boolean convertXLog(XLog xLog, OutputStream outputStream);
}
