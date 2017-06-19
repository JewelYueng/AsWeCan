package org.k2.processmining.support.event.sumarise;

import org.deckfour.xes.model.XAttribute;
import org.deckfour.xes.model.XLog;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by nyq on 2017/6/17.
 */
public class Summarize {

    public static EventLogSummary summarizeXLog(XLog log) {
        int events = 0;
        Map<XAttribute,Integer> map1 = new HashMap<>();
        Map<XAttribute,Integer> map2 = new HashMap<>();
        for(int i = 0;i < log.size(); i++){
            for(int j=0;j < log.get(i).size();j++){
                events++;
                if(!map1.containsKey(log.get(i).get(j).getAttributes().get("concept:name"))){
                    map1.put(log.get(i).get(j).getAttributes().get("concept:name"), 1);
                }
                if(!map2.containsKey(log.get(i).get(j).getAttributes().get("org:resource"))){
                    map2.put(log.get(i).get(j).getAttributes().get("org:resource"), 1);
                }
            }
        }
        Set<XAttribute> keySet1 = map1.keySet();
        Iterator<XAttribute> it1 = keySet1.iterator();
        StringBuilder sb1 = new StringBuilder();
        while(it1.hasNext()){
            sb1.append(it1.next()).append(",");
        }
        String event_name = sb1.substring(0, sb1.length()-1);

        Set<XAttribute> keySet2 = map2.keySet();
        Iterator<XAttribute> it2 = keySet2.iterator();
        StringBuilder sb2 = new StringBuilder();
        while(it2.hasNext()){
            sb2.append(it2.next()).append(",");
        }
        String org_name = sb2.substring(0, sb2.length()-1);

        EventLogSummary eventLogSummary = new EventLogSummary();
        eventLogSummary.setCases(log.size());
        eventLogSummary.setEvents(events);
        eventLogSummary.setPerEventInCase((events/log.size()));
        eventLogSummary.setEventNames(event_name);
        eventLogSummary.setOperatorNames(org_name);
        return eventLogSummary;
    }
}
