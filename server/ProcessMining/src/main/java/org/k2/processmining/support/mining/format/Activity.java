package org.k2.processmining.support.mining.format;

import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.XLog;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface Activity {
	/**
	 * 活动对应的event集 
	 * @param log
	 * @param activityName
	 * @return Map<活动的名称,活动的事件集>
	 */
	public Map<String,List<XEvent>> getEventList(XLog log, Map<Integer, String> names);
	/**
	 * event中的属性值
	 * @param log
	 * @param activityName
	 * @param attributeName
	 * @param attrType(1:int;2:string;3:float)
	 * @return Map<活动的名称,活动的某个属性的集合（以,间隔）>
	 */
	public Map<String,String> getEventAttribute(XLog log, Map<Integer, String> names, String attributeName);
    /**
     * 每个event中的所有属性项
     * @param log
     * @return
     */
	public Set<String> getEventAttrs(XLog log);
}
