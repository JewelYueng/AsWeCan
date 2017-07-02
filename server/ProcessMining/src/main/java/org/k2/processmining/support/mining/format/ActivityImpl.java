package org.k2.processmining.support.mining.format;

import org.deckfour.xes.model.XAttribute;
import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;
import org.deckfour.xes.model.impl.XAttributeContinuousImpl;
import org.deckfour.xes.model.impl.XAttributeDiscreteImpl;
import org.deckfour.xes.model.impl.XAttributeLiteralImpl;
import org.k2.processmining.support.event.parse.impl.EventLogParseImpl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;

public class ActivityImpl implements Activity {

	@Override
	public Map<String,List<XEvent>> getEventList(XLog log, Map<Integer, String> names) {
		Map<String,List<XEvent>> map=null;
		if(log.size()<=0){
			return map;
		}else{
			map=new HashMap<String,List<XEvent>>();
			for (int i = 0; i < names.size(); i++) {
				map.put(names.get(i), new ArrayList<XEvent>());
			}
			for (XTrace trace : log) {
				if (trace.size() <= 0) {
					continue;
				}
				for (XEvent event : trace) {
					map.get(((XAttributeLiteralImpl)event.getAttributes().get("concept:name")).getValue()).add(event);
					//list.add(event);
					
				}
			}
		}
		return map;
	}

	@Override
	public Map<String,String> getEventAttribute(XLog log, Map<Integer, String> names,
                                                String attributeName) {
		Map<String,String> returnMap=new HashMap<String,String>();
		Map<String,List<XEvent>> map=getEventList(log,names);
		for (int i = 0; i < names.size(); i++) {
			List<XEvent> list=(List<XEvent>) map.get(names.get(i));
			Map<String,XEvent> attrMap=new HashMap<String,XEvent>();
			for(XEvent event:list){
				String attrValue=null;
				XAttribute xAttribute =event.getAttributes().get(attributeName);
				if (xAttribute instanceof XAttributeDiscreteImpl) {
					attrValue = Long.toString(((XAttributeDiscreteImpl) xAttribute).getValue());
				}
				else if (xAttribute instanceof XAttributeLiteralImpl) {
					attrValue = ((XAttributeLiteralImpl) xAttribute).getValue();
				}
				else if (xAttribute instanceof XAttributeContinuousImpl) {
					attrValue = Double.toString(((XAttributeContinuousImpl) xAttribute).getValue());
				}
				if(attrValue!=null&&!attrMap.containsKey(attrValue)){
					attrMap.put(attrValue, event);
				}
				//System.out.println("attr size:"+attrValue);
			}
			Set keys = attrMap.keySet();
			returnMap.put(names.get(i), keys.toString());
			//System.out.println(names.get(i)+" " +keys.toString());
		}
		
		return returnMap;
	}

	@Override
	public Set<String> getEventAttrs(XLog log) {
		if(log.size()>0){
			if(log.get(0).size()>0){
				XEvent event=log.get(0).get(0);
				Set<String> attrs=event.getAttributes().keySet();
				attrs.remove("time:timestamp");
				attrs.remove("concept:name");
				attrs.remove("lifecycle:transition");
				return attrs;
			}else{
				return null;
			}
		}else{
			return null;
		}
	}
//	public static void main(String args[]) throws FileNotFoundException{
//		InputStream in =new FileInputStream(new File("d:/testfile/XESfile.xes"));
//		EventLogParseImpl eventLogSumariseImpl = new EventLogParseImpl();
//		XLog log = eventLogSumariseImpl.eventLogParse(in);
//		ActivityImpl impl=new ActivityImpl();
//		System.out.println(impl.getEventAttrs(log).toString());
//	}

}
