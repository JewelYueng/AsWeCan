package org.k2.processmining.support.event.export.impl;


import org.deckfour.xes.model.XLog;
import org.deckfour.xes.out.XesXmlSerializer;
import org.k2.processmining.support.event.export.EventLogExport;
import org.springframework.stereotype.Component;

import java.io.*;
@Component
public class EventLogExportImpl implements EventLogExport {

	@Override
	public void convertXLogToXesFile(XLog xlog, File file) {
		//创建文件输出流
		OutputStream out = null;
		try {
			out = new FileOutputStream(file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("lq.......................");
			e.printStackTrace();			
		}
		//创建XesXmlSerializer
		XesXmlSerializer xesXmlSerializer = new XesXmlSerializer();
		//利用XesXmlSerializer的serialize方法进行序列化，写出文件到out
		try {
			xesXmlSerializer.serialize(xlog, out);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public boolean convertXLog(XLog xLog, OutputStream outputStream) {
		XesXmlSerializer xesXmlSerializer = new XesXmlSerializer();
		try {
			xesXmlSerializer.serialize(xLog, outputStream);
			return true;
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
}
