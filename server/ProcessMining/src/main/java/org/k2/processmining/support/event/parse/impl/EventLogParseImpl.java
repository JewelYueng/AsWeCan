package org.k2.processmining.support.event.parse.impl;

import org.deckfour.xes.in.XesXmlParser;
import org.deckfour.xes.model.XLog;
import org.k2.processmining.exception.BadRequestException;
import org.k2.processmining.exception.InternalServerErrorException;
import org.k2.processmining.model.log.EventLog;
import org.k2.processmining.storage.LogStorage;
import org.k2.processmining.support.event.parse.EventLogParse;
import org.k2.processmining.support.event.parse.EventLogParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

	private static final Logger LOGGER = LoggerFactory.getLogger(EventLogParseImpl.class);

	@Autowired
	private LogStorage logStorage;

	@Override
	public XLog eventLogParse(EventLog eventLog) {
		if (eventLog != null) {
			try {
				return logStorage.download(eventLog, inputStream -> {
					try {
						return eventLogParse(inputStream);
					}
					catch (EventLogParseException e) {
						LOGGER.error("Fail to parse to XLog for eventLog:{}", eventLog, e);
						throw new BadRequestException("Fail to parse eventLog to XLog.");
					}
				});
			}
			catch (IOException e) {
				LOGGER.error("Fail to parse to XLog for eventLog:{}", eventLog, e);
				throw new InternalServerErrorException("Fail to parse eventLog to XLog.");
			}
		}
		return null;
	}

	// TODO: 2017/7/2 refactor 

	@SuppressWarnings("unchecked")
	@Override
	public XLog eventLogParse(InputStream in) throws IOException, EventLogParseException {
		//检查文件BOM
		InputStream input = checkForUtf8BOMAndDiscardIfAny(in);
		//创建XesParser对象
		XesXmlParser xxp = new XesXmlParser();
		List<XLog> listXLog = new ArrayList<XLog>();
		//使用XesParser对象解析输入的文件流
		try {
			listXLog = xxp.parse(input);
		} catch (Exception e) {
			if (e instanceof IOException) {
				throw (IOException) e;
			}
			throw new EventLogParseException(e);
		}
		if(listXLog.isEmpty()){
			throw new EventLogParseException("Parse result is empty.");
		}
		return listXLog.get(0);
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
