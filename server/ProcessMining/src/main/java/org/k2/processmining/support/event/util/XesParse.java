package org.k2.processmining.support.event.util;

import org.deckfour.xes.classification.XEventAttributeClassifier;
import org.deckfour.xes.classification.XEventClassifier;
import org.deckfour.xes.extension.XExtension;
import org.deckfour.xes.extension.std.*;
import org.deckfour.xes.model.XAttribute;
import org.deckfour.xes.model.impl.*;
import org.k2.processmining.support.event.XESEntity.XLog4EventLog;
import org.k2.processmining.support.event.export.EventLogExport;
import org.k2.processmining.support.event.export.impl.EventLogExportImpl;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class XesParse{
	public static XEventImpl formatXesObject(String[] keys, String[] values){
		XAttributeMapImpl XAttributeMap=new XAttributeMapImpl();
		String resourceStr="";
		for(int i=0;i<keys.length;i++){
			if(keys[i].equalsIgnoreCase("time")){
				XsDateTimeFormat format =new XsDateTimeFormat();
				try {
					XAttributeMap.put("2", new XAttributeTimestampImpl("time:timestamp",format.parseObject(values[i])));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else if(keys[i].equalsIgnoreCase("eventname")){
				XAttributeMap.put("1", new XAttributeLiteralImpl("concept:name",values[i]));
			}else{
//				XAttributeMap.put((""+i), new XAttributeLiteralImpl(keys[i],values[i]));
				resourceStr+=" "+values[i];
			}
		}
		if(resourceStr.length()!=0){
			resourceStr=resourceStr.substring(1);
		}
		XAttributeMap.put("3", new XAttributeLiteralImpl("org:resource",resourceStr));
		XAttributeMap.put("4", new XAttributeLiteralImpl("lifecycle:transition","complete"));
		return new XEventImpl(XAttributeMap);
	}
	public static void outputEventLog(File outputFile, int timeIndex, String seperator, String nulVal, String sdfStr,
			Map<String, List<String>> caseMap,String[] keys) throws IOException, ParseException {
//		String[] globalKeys={"lifecycle:model","source"};
//		String[] globalValues={"standard","Rapid Synthesizer"};
//		XLogImpl XLog=(XLogImpl) formatXesObject(globalKeys,globalValues,0);
		XLog4EventLog XLog=getXLog4EventLog();
		
		int index = 1;
		Iterator<String> it = caseMap.keySet().iterator();
		while (it.hasNext()) {
			String caseId = "case-" + index;
			
			XAttributeMapImpl traceXAttributeMap=new XAttributeMapImpl();
			traceXAttributeMap.put("1", new XAttributeLiteralImpl("concept:name",caseId));
			XTraceImpl XTrace=new XTraceImpl(traceXAttributeMap);
//			String[] traceKeys={"concept:name"};
//			String[] traceValues={caseId};
//			XTraceImpl XTrace=(XTraceImpl) formatXesObject(traceKeys,traceValues,1);
			
			String key = it.next().toString();
			List<String> value = caseMap.get(key);
			DateFormat sdf = new SimpleDateFormat(sdfStr);
			for (int k = value.size() - 1; k > 0; k--) {
				boolean flag = true;
				for (int m = 0; m < k; m++) {
					String timeStr1 = value.get(m).split(seperator)[timeIndex];
					String timeStr2 = value.get(m + 1).split(seperator)[timeIndex];
					Date date1 = sdf.parse(timeStr1);
					Date date2 = sdf.parse(timeStr2);
					if (date1.getTime() > date2.getTime()) {
						String temp = value.get(m + 1);
						value.set(m + 1, value.get(m));
						value.set(m, temp);
						flag = false;
					}
				}
				if (flag) {
					break;
				}
			}
			for (String e : value){
				String eventValues[]=e.split(seperator);
				XEventImpl XEvent= formatXesObject(keys,eventValues);
				XTrace.add(XEvent);
			}
			XLog.add(XTrace);
			index++;
		}
		EventLogExport export = new EventLogExportImpl();
		export.convertXLogToXesFile(XLog, outputFile);
	}
	public static XLog4EventLog getXLog4EventLog(){
		Set<XExtension> extensions = new HashSet();
		extensions.add(XLifecycleExtension.instance());
		extensions.add(XOrganizationalExtension.instance());
		extensions.add(XTimeExtension.instance());
		extensions.add(XConceptExtension.instance());
		extensions.add(XSemanticExtension.instance());
		
		List<XEventClassifier> classifiersList=new ArrayList();
		classifiersList.add(new XEventAttributeClassifier("MXML Legacy Classifier","concept:name","lifecycle:transition"));
		classifiersList.add(new XEventAttributeClassifier("Event Name","concept:name"));
		classifiersList.add(new XEventAttributeClassifier("Resource","org:resource"));
		
		List<XAttribute> globalTraceAttributes=new ArrayList();
		globalTraceAttributes.add(new XAttributeLiteralImpl("concept:name", "__INVALID__"));
		
		List<XAttribute> globalEventAttributes=new ArrayList();
		globalEventAttributes.add(new XAttributeLiteralImpl("concept:name", "__INVALID__"));
		globalEventAttributes.add(new XAttributeLiteralImpl("lifecycle:transition", "complete"));
		/*-----------------------全局变量-----------------------------*/
		XAttributeMapImpl XAttributeMap = new XAttributeMapImpl();
		XAttributeMap.put("1", new XAttributeLiteralImpl("source", "Rapid Synthesizer"));
		XAttributeMap.put("2", new XAttributeLiteralImpl("concept:name", "test.mxml"));
		XAttributeMap.put("3", new XAttributeLiteralImpl("lifecycle:model", "standard"));
		XLog4EventLog xLog = new XLog4EventLog(XAttributeMap, extensions,classifiersList,globalTraceAttributes,globalEventAttributes);
		return xLog;
	}
}
