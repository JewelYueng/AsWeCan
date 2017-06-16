package org.k2.processmining.support.event.util;


import org.k2.processmining.support.event.entity.AtomicCorrelation;
import org.k2.processmining.support.event.entity.ConjunctiveCorrelation;
import org.k2.processmining.support.event.entity.DisjunctiveCorrelation;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class CorRatio {

	// 计算指定属性相关的alpha
	public static double alpha_Att(String nulVal, List<String> attVals, int eventNum) {
		Set<String> valSet = new HashSet<String>();
		for (String e : attVals) {
			if (e.equals(nulVal))
				continue;
			if (!valSet.contains(e))
				valSet.add(e);
		}
		return (double) valSet.size() / (double) eventNum;
	}

	// 计算distinctRatio
	public static double distinctRatio(String nulVal, List<String> attVals, Set<String> valSet) {
		int nonNullCount = 0;
		if (valSet == null)
			valSet = new HashSet<String>();
		for (String v : attVals) {
			//TODO nonNullCount应该为不为空的数量
			if (v.equals(nulVal))
				continue;
			if (valSet.contains(v))
				nonNullCount++;
			else {
				valSet.add(v);
			}
		}
		//System.out.println(valSet.size()+" "+nonNullCount);
		return (double) valSet.size() / (double) nonNullCount;
	}
    public static void main(String args[]){
    	String nulVal=" ";
    	List<String> attVals=new ArrayList<String>();
    	attVals.add(" ");
    	attVals.add("a");
    	attVals.add("a");
    	attVals.add("b");
    	attVals.add("c");
    	attVals.add("d");
    	attVals.add("e");
    	attVals.add("f");
    	distinctRatio(nulVal,attVals,null);
    } 
	// 计算sharedRatio
	public static double sharedRatio(String nulVal, List<String> attVals1, List<String> attVals2) {
		Set<String> valSet1 = new HashSet<String>();
		Set<String> valSet2 = new HashSet<String>();
		int equalCount = 0;
		double dr1 = distinctRatio(nulVal, attVals1, valSet1);
		double dr2 = distinctRatio(nulVal, attVals2, valSet2);
		for (String v : valSet1) {
			if (valSet2.contains(v))
				equalCount++;
		}
		return (double) equalCount / (double) (dr1 > dr2 ? dr1 : dr2);
	}

	// 计算piRatio 针对原子关联
	public static double piRatio_ac(String seperator, String nulVal, AtomicCorrelation r, List<String> events) {
		Set<String> attCases = new HashSet<String>();
		int nonNullCount = 0;
		for (String e : events) {
			String attVal = "";
			String[] atts = e.split(seperator);
			if (atts[r.attIndex1].equals(nulVal) && atts[r.attIndex2].equals(nulVal))
				continue;
			attVal = attVal + (atts[r.attIndex1].equals(nulVal) ? atts[r.attIndex2] : atts[r.attIndex1]);
			attCases.add(attVal);
			nonNullCount++;
		}
		r.setCaseSize(attCases.size());
		r.setNonNullnum(nonNullCount);
		return nonNullCount == 0 ? 0 : (double) attCases.size() / (double) nonNullCount;
	}

	// 使用连接关联划分日志并返回nonNullCount(相关事件数) nonGroupEvents(未分组事件集合)
	// attCases(已分组的相关属性值集合)
	public static int countCC(String seperator, String nulVal, ConjunctiveCorrelation cr, AtomicCorrelation r,
							  List<String> events, Set<String> attCases, List<String> nonGroupEvents) {
		if (nonGroupEvents == null) {
			nonGroupEvents = new LinkedList<String>();
		}
		int nonNullCount = 0;
		for (String e : events) {
			String attVal = "";
			String[] atts = e.split(seperator);
			boolean nonFlag = false;
			for (AtomicCorrelation ar : cr.AC) {
				if (atts[ar.attIndex1].equals(nulVal) && atts[ar.attIndex2].equals(nulVal)) {
					nonFlag = true;
					break;
				}
				attVal = attVal + (atts[ar.attIndex1].equals(nulVal) ? atts[ar.attIndex2] : atts[ar.attIndex1]);
			}
			if (nonFlag) {
				nonGroupEvents.add(e);
				continue;
			}
			if (r != null) {
				if (atts[r.attIndex1].equals(nulVal) && atts[r.attIndex2].equals(nulVal))
					continue;
				attVal = attVal + (atts[r.attIndex1].equals(nulVal) ? atts[r.attIndex2] : atts[r.attIndex1]);
			}
			attCases.add(attVal);
			nonNullCount++;
		}
		return nonNullCount;
	}

	// 计算piRatio 针对基于原子关联的连接关联
	public static double piRatio_cc(String seperator, String nulVal, ConjunctiveCorrelation cr, AtomicCorrelation r,
			List<String> events) {
		Set<String> attCases = new HashSet<String>();
		int nonNullCount = 0;
		nonNullCount = CorRatio.countCC(seperator, nulVal, cr, r, events, attCases, null);
		cr.setTmpNNnum(nonNullCount);
		cr.setTmpCszie(attCases.size());
		return nonNullCount == 0 ? 0 : ((double) attCases.size() / (double) nonNullCount);
	}

	// 辅助计算分离关联:使用分离关联中的第一个关联划分日志形成attEventMap,并返回nonNullCount,
	// 以便下一步的countByNonGroupEvent方法将nonGroupEvents合并入attEventMap中
	// acs1代表分离关联中的第一个关联(连接关联或原子关联),acs2代表与之组成分离关联的原子关联或连接关联.
	// nonGroupEvents未分组事件数组,attEventMap为已分组的事件
	// attEventMap key为各个属性的值相合并,分离关联之间用","隔开,原子关联与连接关联之间合并字符串,value为对应的事件集合
	// acs2:[PKIncID FKIncID]把nonGroupEvent中plan相关的记录加入attEventMap中
	public static int count_ccdc(String seperator, String nulVal, List<AtomicCorrelation> acs1,
			List<AtomicCorrelation> acs2, List<String> events, Map<String, List<String>> attEventMap,
			List<String> nonGroupEvents) {
		int nonNullCount = 0;
		for (String e : events) {
			String attVal = "";
			String[] atts = e.split(seperator);
			boolean nonFlag = false;
			for (AtomicCorrelation ar : acs1) {
				if (atts[ar.attIndex1].equals(nulVal) && atts[ar.attIndex2].equals(nulVal)) {
					nonFlag = true;
					break;
				}
				attVal = attVal + (atts[ar.attIndex1].equals(nulVal) ? atts[ar.attIndex2] : atts[ar.attIndex1]);
			}
			if (nonFlag) {
				nonGroupEvents.add(e);
				continue;
			}
			String addAttVal = "";
			for (AtomicCorrelation r : acs2) {
				if (!atts[r.attIndex1].equals(nulVal))
					addAttVal = addAttVal + atts[r.attIndex1];
				else if (!atts[r.attIndex2].equals(nulVal))
					addAttVal = addAttVal + atts[r.attIndex2];
				else {
					addAttVal = "";
					break;
				}
			}
			if (addAttVal == "") {
				boolean isAdd = false;
				Iterator<String> it = attEventMap.keySet().iterator();
				while(it.hasNext()){
					String key = it.next();
					if(key.split(",")[0].equals(attVal)){
						attEventMap.get(key).add(e);
						isAdd = true;
						break;
					}
				}
				if (!isAdd) {
					List<String> list = new ArrayList<String>();
					list.add(e);
					attEventMap.put(attVal, list);
				}
			} else {
				addAttVal = attVal + "," + addAttVal;
				if (attEventMap.containsKey(addAttVal)) {
					attEventMap.get(addAttVal).add(e);
				} else {
					List<String> list = new ArrayList<String>();
					list.add(e);
					attEventMap.put(addAttVal, list);
					if(attEventMap.containsKey(attVal)) {
						attEventMap.get(addAttVal).addAll(attEventMap.get(attVal));
						attEventMap.remove(attVal);
					}
				}
			}
			nonNullCount++;
		}
//		File log = new File("D:\\processmining\\log.txt");
//		FileWriter logw;
//		try {
//			logw = new FileWriter(log);
//			for(String k:attEventMap.keySet()){
//				logw.write(k+"\n");
//			}
//			logw.close();
//		} catch (IOException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}

		return nonNullCount;
	}

	// 辅助计算分离关联:使用一个原子关联或连接关联划分nonGroupEvents,
	// 将对应事件加入attEventMap中并返回在nonGroupEvents过程中新增的nonNullCount,
	// int i 表示分离关联中，从第二个关联算起的第几个关联 ,(若是第二个,i=1)
	// acs为划分nonGoupEvent的连接关联或原子关联 即上一个方法count_ccdc的例子中的第二个关联acs2:[PKIncID
	// FKIncID]
	// 该方法不改变attEventMap的key,但改变value,value中的事件会增加.以及减少nonGroupEvents中的事件数量.
	public static int countByNonGroupEvents(String seperator, String nulVal, List<AtomicCorrelation> acs,
			Map<String, List<String>> attEventMap, List<String> nonGroupEvents, int i) {
		if (nonGroupEvents.isEmpty() || acs.isEmpty())
			return 0;
		int nonNullCount = 0;
		Iterator<String> iter = nonGroupEvents.iterator();
		while (iter.hasNext()) {
			String e = iter.next();
			String[] atts = e.split(seperator);
			boolean nonFlag = false;
			String val = "";
			for (AtomicCorrelation r : acs) {
				if (atts[r.attIndex1].equals(nulVal) && atts[r.attIndex2].equals(nulVal)) {
					nonFlag = true;
					break;
				}
				val = val + (atts[r.attIndex1].equals(nulVal) ? atts[r.attIndex2] : atts[r.attIndex1]);
			}
			if (nonFlag)
				continue;
			for (String att : attEventMap.keySet()) {
				String[] ats = att.split(",");
				if (ats.length < i + 1) {
					continue;
				}
				if (ats[i].equals(val)) {
					attEventMap.get(att).add(e);
					iter.remove();
					nonNullCount++;
					continue;
				}
			}
		}
		return nonNullCount;

	}

	// 辅助计算分离关联:使用新的关联对attEventMap重整, acs为重整需要的原子关联集合
	// count_ccdc+countByNonGroupEvents只针对两个关联（连接关联或原子关联）组成的分离关联,多个关联要用到这个方法
	// 例如 attEventMap为
	// key="([PKIncID,PKIncID]∧[TenantID,TenantID])∨[PKIncID,FKIncID]"组成的实例分组
	// 并且 此时attEventMap已经有incident、plan相关的记录 (通过countByNonGroupEvents)
	// 利用 acs:[PKPlanID FKPlanID]对其重整,使attEventMap变为
	// key="([PKIncID,PKIncID]∧[TenantID,TenantID])∨[PKIncID,FKIncID]∨[PKPlanID,FKPlanID]
	// 如此,可以在下一步使用countByNonGroupEvents方法将nonGroupEvents中的task相关记录通过acs:[PKPlanID
	// FKPlanID]并入attEventMap中
	public static void updateAttCases(String seperator, String nulVal, List<AtomicCorrelation> acs,
			Map<String, List<String>> attEventMap) {
		for (String key : attEventMap.keySet()) {
			List<String> eventlist = attEventMap.get(key);
			Iterator<String> eventIter = eventlist.iterator();
			String attVal = key;
			while (eventIter.hasNext()) {
				String e = eventIter.next();
				String[] atts = e.split(seperator);
				String addAttVal = "";
				for (AtomicCorrelation r : acs) {
					if (!atts[r.attIndex1].equals(nulVal))
						addAttVal = addAttVal + atts[r.attIndex1];
					else if (!atts[r.attIndex2].equals(nulVal))
						addAttVal = addAttVal + atts[r.attIndex2];
					else {
						addAttVal = "";
						break;
					}
				}
				if (addAttVal == "") {
					continue;
				} else {
					addAttVal = attVal + "," + addAttVal;
					if (attEventMap.containsKey(addAttVal)) {
						attEventMap.get(addAttVal).add(e);
					} else {
						List<String> list = new ArrayList<String>();
						list.add(e);
						attEventMap.put(addAttVal, list);
					}
					eventIter.remove();
					if (attEventMap.get(attVal).isEmpty()) {
						attEventMap.remove(attVal);
					}
				}
			}
		}
	}

	// 计算piRatio 针对基于原子关联关联与原子关联的分离关联(两个关联)
	public static double piRatio_dc(String seperator, String nulVal, AtomicCorrelation ar1, AtomicCorrelation ar2,
			List<String> events, DisjunctiveCorrelation dr) {
		ConjunctiveCorrelation cr1 = new ConjunctiveCorrelation(ar1);
		ConjunctiveCorrelation cr2 = new ConjunctiveCorrelation(ar2);
		return CorRatio.piRatio_dc(seperator, nulVal, cr1, cr2, events, dr);
	}

	// 计算piRatio 针对基于连接关联关联与原子关联的分离关联(两个关联)
	public static double piRatio_dc(String seperator, String nulVal, ConjunctiveCorrelation cr, AtomicCorrelation r,
			List<String> events, DisjunctiveCorrelation dr) {
		ConjunctiveCorrelation cr2 = new ConjunctiveCorrelation(r);
		return CorRatio.piRatio_dc(seperator, nulVal, cr, cr2, events, dr);
	}

	// 计算piRatio 针对基于连接关联关联与连接关联的分离关联(两个关联)
	public static double piRatio_dc(String seperator, String nulVal, ConjunctiveCorrelation cr1,
			ConjunctiveCorrelation cr2, List<String> events, DisjunctiveCorrelation dr) {
		Map<String, List<String>> attEventMap = new ConcurrentHashMap<String, List<String>>();
		int nonNullCount = 0;
		List<String> nonGroupEvents = new LinkedList<String>();
		List<AtomicCorrelation> acs1 = new ArrayList<AtomicCorrelation>();
		List<AtomicCorrelation> acs2 = new ArrayList<AtomicCorrelation>();
		if (cr1 != null)
			acs1 = cr1.AC;
		if (cr2 != null)
			acs2 = cr2.AC;
		nonNullCount = CorRatio.count_ccdc(seperator, nulVal, acs1, acs2, events, attEventMap, nonGroupEvents);
		nonNullCount = nonNullCount
				+ CorRatio.countByNonGroupEvents(seperator, nulVal, acs2, attEventMap, nonGroupEvents, 1);
		dr.setTmpCszie(attEventMap.size());
		dr.setTmpNNnum(nonNullCount);
		return nonNullCount == 0 ? 0 : ((double) attEventMap.size() / (double) nonNullCount);
	}

	// 计算piRatio 针对基于分离关联与连接关联的分离关联(超过两个关联)
	public static double piRatio_ddc(String seperator, String nulVal, DisjunctiveCorrelation dr1,
			ConjunctiveCorrelation cr2, List<String> events) {
		Map<String, List<String>> attEventMap = new ConcurrentHashMap<String, List<String>>();
		int nonNullCount = 0;
		List<String> nonGroupEvents = new LinkedList<String>();
		List<AtomicCorrelation> acs1 = new ArrayList<AtomicCorrelation>();
		List<AtomicCorrelation> acs2 = new ArrayList<AtomicCorrelation>();
		int dr1ccIndex = 0;
		int dr1acIndex = 0;
		boolean cr2Use = false;
		if (!dr1.CC.isEmpty()) {
			acs1 = dr1.CC.get(0).AC;
			dr1ccIndex++;
			if (dr1.CC.size() > 1) {
				acs2 = dr1.CC.get(1).AC;
				dr1ccIndex++;
			} else if (!dr1.AC.isEmpty()) {
				acs2.add(dr1.AC.get(0));
				dr1acIndex++;
			} else if (!cr2.AC.isEmpty()) {
				acs2 = cr2.AC;
				cr2Use = true;
			} else
				return 0;
		} else if (!dr1.AC.isEmpty()) {
			acs1.add(dr1.AC.get(0));
			dr1acIndex++;
			if (dr1.AC.size() > 1) {
				acs2.add(dr1.AC.get(1));
				dr1acIndex++;
			} else if (!cr2.AC.isEmpty()) {
				acs2 = cr2.AC;
				cr2Use = true;
			} else
				return 0;
		}
		int i = 1;
		nonNullCount = CorRatio.count_ccdc(seperator, nulVal, acs1, acs2, events, attEventMap, nonGroupEvents);
		nonNullCount = nonNullCount
				+ CorRatio.countByNonGroupEvents(seperator, nulVal, acs2, attEventMap, nonGroupEvents, i);
		i++;
		if (cr2Use)
			return nonNullCount == 0 ? 0 : ((double) attEventMap.size() / (double) nonNullCount);
		
		for (int index1 = dr1ccIndex; index1 < dr1.CC.size(); index1++) {
			CorRatio.updateAttCases(seperator, nulVal, dr1.CC.get(index1).AC, attEventMap);
			nonNullCount = nonNullCount + CorRatio.countByNonGroupEvents(seperator, nulVal, dr1.CC.get(index1).AC,
					attEventMap, nonGroupEvents, i);
			i++;
		}
		for (int index2 = dr1acIndex; index2 < dr1.AC.size(); index2++) {
			ArrayList<AtomicCorrelation> acs = new ArrayList<AtomicCorrelation>();
			acs.add(dr1.AC.get(index2));
			CorRatio.updateAttCases(seperator, nulVal, acs, attEventMap);
			nonNullCount = nonNullCount
					+ CorRatio.countByNonGroupEvents(seperator, nulVal, acs, attEventMap, nonGroupEvents, i);
			i++;
		}
		// 与cr2合并
		if (!cr2.AC.isEmpty()) {
			CorRatio.updateAttCases(seperator, nulVal, cr2.AC, attEventMap);
			nonNullCount = nonNullCount
					+ CorRatio.countByNonGroupEvents(seperator, nulVal, cr2.AC, attEventMap, nonGroupEvents, i);
			i++;
		}
		dr1.setTmpNNnum(nonNullCount);
		dr1.setTmpCszie(attEventMap.size());
		return nonNullCount == 0 ? 0 : ((double) attEventMap.size() / (double) nonNullCount);
	}

	// 计算piRatio 针对基于分离关联与连接关联的分离关联(超过两个关联)
	public static double piRatio_ddc(String seperator, String nulVal, DisjunctiveCorrelation dr1, AtomicCorrelation ar2,
			List<String> events) {
		ConjunctiveCorrelation cr2 = new ConjunctiveCorrelation(ar2);
		return CorRatio.piRatio_ddc(seperator, nulVal, dr1, cr2, events);
	}

	// 计算piRatio 针对单独的分离关联(超过两个关联)
	public static double piRatio_ddc(String seperator, String nulVal, DisjunctiveCorrelation dr1, List<String> events) {
		ConjunctiveCorrelation cr2 = new ConjunctiveCorrelation(new ArrayList<AtomicCorrelation>());
		return CorRatio.piRatio_ddc(seperator, nulVal, dr1, cr2, events);
	}

	// 生成事件日志
	public static void generate_EventLog(String seperator, String nulVal, List<String> events,
			DisjunctiveCorrelation dr, File outputFile, int timeIndex, String sdfStr,String[] keys) throws IOException, ParseException {
		int ccCount = dr.CC.size();
		int acCount = dr.AC.size();
		int drccIndex = 0;
		int dracIndex = 0;
		List<AtomicCorrelation> acs1 = new ArrayList<AtomicCorrelation>();
		List<AtomicCorrelation> acs2 = new ArrayList<AtomicCorrelation>();
		if (ccCount > 0) {
			acs1 = dr.CC.get(0).AC;
			drccIndex++;
			if (ccCount > 1) {
				acs2 = dr.CC.get(1).AC;
				drccIndex++;
			} else if (acCount > 0) {
				acs2.add(dr.AC.get(0));
				dracIndex++;
			}
		} else if (acCount > 0) {
			acs1.add(dr.AC.get(0));
			dracIndex++;
			if (acCount > 1) {
				acs2.add(dr.AC.get(1));
				dracIndex++;
			} else if (ccCount > 0) {
				acs2 = dr.CC.get(0).AC;
				drccIndex++;
			}
		}
		Map<String, List<String>> caseMap = new ConcurrentHashMap<String, List<String>>();
		List<String> nonGroupEvents = new LinkedList<String>();
		if (acs2.isEmpty()) {// 只有acs1有值,即只有连接关联或原子关联
			CorRatio.computeCasesOfCC(seperator, nulVal, acs1, events, caseMap);
//			CorRatio.outputEventLog(outputFilePath, timeIndex, seperator, nulVal, sdfStr, caseMap);
			XesParse.outputEventLog(outputFile, timeIndex, seperator, nulVal, sdfStr, caseMap,keys);
		} else {
			int i = 1;
			CorRatio.count_ccdc(seperator, nulVal, acs1, acs2, events, caseMap, nonGroupEvents);
			CorRatio.countByNonGroupEvents(seperator, nulVal, acs2, caseMap, nonGroupEvents, i);
			for (int index1 = drccIndex; index1 < dr.CC.size(); index1++) {
				CorRatio.updateAttCases(seperator, nulVal, dr.CC.get(index1).AC, caseMap);
				CorRatio.countByNonGroupEvents(seperator, nulVal, dr.CC.get(index1).AC,
						caseMap, nonGroupEvents, i);
				i++;
			}
			for (int index2 = dracIndex; index2 < dr.AC.size(); index2++) {
				ArrayList<AtomicCorrelation> acs = new ArrayList<AtomicCorrelation>();
				acs.add(dr.AC.get(index2));
				CorRatio.updateAttCases(seperator, nulVal, acs, caseMap);
				CorRatio.countByNonGroupEvents(seperator, nulVal, acs, caseMap, nonGroupEvents, i);
				i++;
			}
//			CorRatio.outputEventLog(outputFilePath, timeIndex, seperator, nulVal, sdfStr, caseMap);
			XesParse.outputEventLog(outputFile, timeIndex, seperator, nulVal, sdfStr, caseMap, keys);
		}
	}

	// 当只有连接关联或原子关联时,用连接关联或原子关联计算事件日志
	public static void computeCasesOfCC(String seperator, String nulVal, List<AtomicCorrelation> ars,
			List<String> events, Map<String, List<String>> caseMap) {
		for (String e : events) {
			String attVal = "";
			String[] atts = e.split(seperator);
			boolean nonFlag = false;
			for (AtomicCorrelation ar : ars) {
				if (atts[ar.attIndex1].equals(nulVal) && atts[ar.attIndex2].equals(nulVal)) {
					nonFlag = true;
					break;
				}
				attVal = attVal + (atts[ar.attIndex1].equals(nulVal) ? atts[ar.attIndex2] : atts[ar.attIndex1]);
			}
			if (nonFlag) {
				continue;
			}
			if (!caseMap.containsKey(attVal)) {
				List<String> list = new ArrayList<String>();
				list.add(e);
				caseMap.put(attVal, list);
			} else {
				caseMap.get(attVal).add(e);
			}
		}
	}

	// 排序并输出事件日志
	public static void outputEventLog(String outputPath, int timeIndex, String seperator, String nulVal, String sdfStr,
			Map<String, List<String>> caseMap) throws IOException, ParseException {
		File writeFile = new File(outputPath);
		FileWriter fw = new FileWriter(writeFile, true);
		int index = 1;
		Iterator<String> it = caseMap.keySet().iterator();
		while (it.hasNext()) {
			String caseId = "case-" + index;
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
				fw.write(caseId+seperator+e+"\n");
				String values[]=e.split(seperator);
				System.out.println(values.length+"--"+values.toString());
			}
			index++;
		}
		fw.close();
	}
}
