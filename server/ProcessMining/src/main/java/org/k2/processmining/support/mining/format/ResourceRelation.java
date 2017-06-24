package org.k2.processmining.support.mining.format;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.deckfour.xes.model.XLog;
import org.k2.processmining.support.mining.algorithm.heuristics.mining.HeuristicsMetrics;
import org.k2.processmining.support.mining.algorithm.heuristics.models.HeuristicsNet;
import org.k2.processmining.support.mining.algorithm.heuristics.models.SimpleHeuristicsNet;
import org.k2.processmining.support.mining.model.Link;
import org.k2.processmining.support.mining.model.Node;

/**
 * 因果矩阵的资源关系网形式的数据结构，不以“活动名称：concept:name”将活动分组，以“resource_attr”（event中的某个属性）分组，然后根据流程模型，建立资源关系网
 * @author admin
 *
 */
public class ResourceRelation extends Formatter{
	//属性有mineUsetime、nodes、links、resource_attr
	private XLog log;
	/**
	 * 定义用来分组的event属性
	 */
	private String resource_attr;
	public ResourceRelation(HeuristicsNet net, String time, String resource_attr, XLog log){
		this.nodes=new ArrayList<Node>();
		this.links=new ArrayList<Link>();
		this.mineUsetime=time;
		this.resource_attr=resource_attr;
		this.log=log;
		transform((SimpleHeuristicsNet)net);
	}
	@Override
	public void transform(SimpleHeuristicsNet net) {
		Map<String,Link> linksMap=new HashMap<String,Link>();
		Map<String,Node> nodesMap=new HashMap<String,Node>();
		ActivityImpl activityImpl=new ActivityImpl();
		HeuristicsMetrics metrics = net.getMetrics();
		int size = net.size();
		Map<Integer, String> names = new HashMap<Integer, String>();
		for (int i = 0; i < size; i++) {
			names.put(i, net.getActivitiesMappingStructures()
					.getXEventClasses().getByIndex(i).toString());
		}
		Map<String,String> resourceNames=activityImpl.getEventAttribute(log, names, resource_attr);
		for (int i = 0; i < size; i++) {
			String sourceResource=resourceNames.get(names.get(i));
			nodesMap.put(sourceResource, new Node(sourceResource));
			// input
			int inputSetSize = net.getInputSet(i).size();
			for (int j = 0; j < inputSetSize; j++) {
				int subSetSize = net.getInputSet(i).get(j).size();
				for (int k = 0; k < subSetSize; k++) {
					int element = net.getInputSet(i).get(j).get(k);
					String targetResource=resourceNames.get(names.get(element));
					String linksMapKey=targetResource+"-"+sourceResource;
					if(targetResource.equals(sourceResource)){
						continue;
					}
					//System.out.println("counts:"+metrics.getDirectSuccessionCount(i, element));
					if(linksMap.containsKey(linksMapKey)){
						long counts=linksMap.get(linksMapKey).getValue()+(long) metrics.getDirectSuccessionCount(element,i);
						linksMap.get(linksMapKey).setValue(counts);
					}else{
						linksMap.put(linksMapKey, new Link(targetResource,sourceResource,(long) metrics.getDirectSuccessionCount(element,i)));
					}
				}
			}
			// 组织活动的output
			int outputSetSize = net.getOutputSet(i).size();
			for (int j = 0; j < outputSetSize; j++) {
				int subSetSize = net.getOutputSet(i).get(j).size();
				for (int k = 0; k < subSetSize; k++) {
					int element = net.getOutputSet(i).get(j).get(k);
					String targetResource=resourceNames.get(names.get(element));
					String linksMapKey=sourceResource+"-"+targetResource;
					if(targetResource.equals(sourceResource)){
						continue;
					}
					if(linksMap.containsKey(linksMapKey)){
						long counts=linksMap.get(linksMapKey).getValue()+(long) metrics.getDirectSuccessionCount(i, element);
						linksMap.get(linksMapKey).setValue(counts);
					}else{
						linksMap.put(linksMapKey, new Link(sourceResource,targetResource,(long) metrics.getDirectSuccessionCount(i, element)));
					}
				}
			}
		}
		Collection<Link> linksCollection = linksMap.values();
		this.links = new ArrayList<Link>(linksCollection);
		Collection<Node> nodesCollection = nodesMap.values();
		this.nodes = new ArrayList<Node>(nodesCollection);
	}
//	public static void main(String args[]) throws FileNotFoundException, MapperException{
//		InputStream in =new FileInputStream(new File("C:\\Users\\admin\\Desktop\\healthcare\\test-data\\Chapter_8\\reviewing.xes"));
//		EventLogParseImpl eventLogSumariseImpl = new EventLogParseImpl();
//		XLog log = eventLogSumariseImpl.eventLogParse(in);
//		HashMap<String,Object> map = new HashMap<String,Object>();
//		HeuristicsMiner hm = new HeuristicsMiner(log,map);
//		//TODO 返回的因果矩阵
//		SimpleHeuristicsNet net = (SimpleHeuristicsNet) hm.mine();
//		Formatter formatter=new ResourceRelation(net,hm.usedTime,"org:resource",log,2);
//		try
//		   {
//		       PrintStream out = new PrintStream("D:\\testfile\\ResourceRelationOut.txt");
//		       System.setOut(out);
//		   }
//		   catch(FileNotFoundException e)
//		   {
//		       e.printStackTrace();
//		   }
//		System.out.println(JsonUtil.objectToJsonStr(formatter));
//	}
}
