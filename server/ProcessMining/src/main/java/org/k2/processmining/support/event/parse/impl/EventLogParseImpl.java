package org.k2.processmining.support.event.parse.impl;

import org.deckfour.xes.in.XesXmlParser;
import org.deckfour.xes.model.XLog;
import org.k2.processmining.model.log.EventLog;
import org.k2.processmining.storage.LogStorage;
import org.k2.processmining.support.event.parse.EventLogParse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 事件日志流解析出XLog对象
 */
@Component
public class EventLogParseImpl implements EventLogParse {

	@Autowired
	private LogStorage logStorage;

	@Override
	public XLog eventLogParse(EventLog eventLog) {
		return logStorage.download(eventLog, this::eventLogParse);
	}

	@SuppressWarnings("unchecked")
	@Override
	public XLog eventLogParse(InputStream in) {
		XLog xlog = null;
		InputStream input = null;
		//检查文件BOM
		try {
			input = checkForUtf8BOMAndDiscardIfAny(in);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//创建XesParser对象
		XesXmlParser xxp = new XesXmlParser();
		List<XLog> listXLog = new ArrayList<XLog>();
		//使用XesParser对象解析输入的文件流
		try {
			listXLog = xxp.parse(input);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(!listXLog.isEmpty()){
			xlog = listXLog.get(0);
		}
		return xlog;
	}
	
	private static InputStream checkForUtf8BOMAndDiscardIfAny(InputStream inputStream) throws IOException {
		PushbackInputStream pushbackInputStream = new PushbackInputStream(
				new BufferedInputStream(inputStream), 3);
		byte[] bom = new byte[3];
		if (pushbackInputStream.read(bom) != -1) {
			if (!(bom[0] == (byte) 0xEF && bom[1] == (byte) 0xBB && bom[2] == (byte) 0xBF)) {
				pushbackInputStream.unread(bom);
			}
		}
		return pushbackInputStream;
	}

}
