package org.k2.processmining.support.mining.format;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.k2.processmining.support.mining.model.NetElement;
import org.k2.processmining.support.mining.algorithm.heuristics.models.HeuristicsNet;
import org.k2.processmining.support.mining.algorithm.heuristics.models.SimpleHeuristicsNet;
import org.k2.processmining.support.mining.algorithm.heuristics.models.impl.HNSubSet;

/**
 * 因果矩阵的petri net形式的数据结构
 * @author admin
 *
 */
public class PetriNet extends Formatter{
	//petri net数据结构的变量有mineUsetime、netElementList
	public PetriNet(HeuristicsNet net, String time){
		this.mineUsetime=time;
		this.netElementList = new ArrayList<NetElement>();
		this.transform((SimpleHeuristicsNet)net);
	}
	@Override
	public void transform(SimpleHeuristicsNet net) {
		Map<Integer,String> names = new HashMap<Integer,String>();
		int size = net.size();
		for(int i=0;i<size;i++){
			names.put(i, net.getActivitiesMappingStructures().getXEventClasses().getByIndex(i).toString());
		}
		HNSubSet starts = net.getStartActivities();
		int start = starts.get(0);
		HNSubSet ends = net.getEndActivities();
		int end = ends.get(0);
		
		int startCount = 0;
		int endCount = 0;
		
		for(int i=0;i<size;i++){
			
			String name = names.get(i);
			NetElement netElement = new NetElement();
			int inputSetSize = net.getInputSet(i).size();
			int outputSetSize = net.getOutputSet(i).size();
			List<String> outputs = new ArrayList<String>();
			List<Integer> subnetlist = new ArrayList<Integer>();
			for(int j=0;j<outputSetSize;j++){
				int subSetSize = net.getOutputSet(i).get(j).size();
				subnetlist.add(subSetSize);
				for(int k=0;k<subSetSize;k++){
					outputs.add(names.get(net.getOutputSet(i).get(j).get(k)));					
				}				
			}
			
			netElement.setElement(Integer.toString(i));
			netElement.setName(name);
			netElement.setOutput(outputs);
			netElement.setSubnetNum(outputSetSize);
			netElement.setSubnetList(subnetlist);
			netElement.setTime(mineUsetime);
			this.netElementList.add(netElement);
			
			if(starts.contains(i)){
				System.out.println("startIndex:" + i);
				System.out.println("start:" + name);
			}
			if(ends.contains(i)){
				System.out.println("end:" + name);
			}
			
			if(inputSetSize==0){
				netElement.setIsStart("true");
				startCount++;
			}
			else{
				netElement.setIsStart("false");
			}
			
			if(outputs.size()>0){
				netElement.setIsEnd("false");
			}
			else{
				netElement.setIsEnd("true");
				endCount++;
			}
			
		}
		
		if(startCount == 0){
			for(int i=0;i<starts.size();i++){
				netElementList.get(starts.get(i)).setIsStart("true");
			}
		}
		if(endCount == 0){
			for(int i=0;i<ends.size();i++){
				netElementList.get(ends.get(i)).setIsEnd("true");
			}
		}
		
		
		int currentStart = 0;
		int currentEnd = size-1;
		for(int i=0;i<size;i++){
			
			if(netElementList.get(i).getElement().equals(Integer.toString(start)))
				currentStart = i;
			if(netElementList.get(i).getElement().equals(Integer.toString(end)))
				currentEnd = i;
			
		}
		
		if(currentStart != 0)			
			Collections.swap(netElementList,0,currentStart);
		if(currentEnd != size-1)			
			Collections.swap(netElementList,size-1,currentEnd);	
		for(int i=0;i<netElementList.size();i++){
			netElementList.get(i).setElement("Activity" + i);
		}
		
	}
//	public static void main(String args[]) throws FileNotFoundException, MapperException, JsonProcessingException{
//		InputStream in =new FileInputStream(new File("C:\\Users\\admin\\Desktop\\healthcare\\test-data\\Chapter_8\\reviewing.xes"));
//		EventLogParseImpl eventLogSumariseImpl = new EventLogParseImpl();
//		XLog log = eventLogSumariseImpl.eventLogParse(in);
//		HashMap<String,Object> map = new HashMap<String,Object>();
//		HeuristicsMiner hm = new HeuristicsMiner(log,map);
//		//TODO 返回的因果矩阵
//		SimpleHeuristicsNet net = (SimpleHeuristicsNet) hm.mine();
//		Formatter formatter=new PetriNet(net,hm.usedTime);
//		try
//		   {
//		       PrintStream out = new PrintStream("D:\\testfile\\PetriNetOut.txt");
//		       System.setOut(out);
//		   }
//		   catch(FileNotFoundException e)
//		   {
//		       e.printStackTrace();
//		   }
//		System.out.println(JsonUtil.objectToJsonStr(formatter));
//	}
}
