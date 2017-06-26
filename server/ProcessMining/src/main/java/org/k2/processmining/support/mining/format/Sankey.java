package org.k2.processmining.support.mining.format;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.k2.processmining.support.mining.algorithm.heuristics.mining.HeuristicsMetrics;
import org.k2.processmining.support.mining.algorithm.heuristics.models.HeuristicsNet;
import org.k2.processmining.support.mining.algorithm.heuristics.models.SimpleHeuristicsNet;
import org.k2.processmining.support.mining.model.Link;
import org.k2.processmining.support.mining.model.Node;

/**
 * 因果矩阵的桑基图形式的数据结构
 * @author admin
 *
 */
public class Sankey extends Formatter{
	//变量有mineUsetime、nodes、links
	public Sankey(HeuristicsNet net, String time){
		this.mineUsetime = time;
		this.nodes=new ArrayList<Node>();
		this.links=new ArrayList<Link>();
		transform((SimpleHeuristicsNet)net);
	}
	@Override
	public void transform(SimpleHeuristicsNet net) {
		HeuristicsMetrics metrics = net.getMetrics();
		int size = net.size();
		System.out.println("EventClasses " + size);
		Map<Integer, String> names = new HashMap<Integer, String>();
		for (int i = 0; i < size; i++) {
			names.put(i, net.getActivitiesMappingStructures()
					.getXEventClasses().getByIndex(i).toString());
		}
		Set repeatSet = new HashSet<String>();// 去掉短循环的辅助变量
		for (int i = 0; i < size; i++) {
			String source = names.get(i);
			this.nodes.add(new Node(source));
			// 组织活动的output
			int outputSetSize = net.getOutputSet(i).size();
			for (int j = 0; j < outputSetSize; j++) {
				int subSetSize = net.getOutputSet(i).get(j).size();
				for (int k = 0; k < subSetSize; k++) {
					int element = net.getOutputSet(i).get(j).get(k);
					String target = names.get(element);
					if (source.equalsIgnoreCase(target))// 去掉自循环的路径
						continue;
					if (repeatSet.contains(source + target)
							|| repeatSet.contains(target + source)) {
						// 去掉短循环的路径
						System.out.println("repeat:" + source + target);
						continue;
					}
					repeatSet.add(source + target);
					this.links.add(new Link(source, target, (long) metrics
							.getDirectSuccessionCount(i, element)));
				}
			}
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
//		Formatter formatter=new Sankey(net,hm.usedTime);
//		try
//		   {
//		       PrintStream out = new PrintStream("D:\\testfile\\SankeyOut.txt");
//		       System.setOut(out);
//		   }
//		   catch(FileNotFoundException e)
//		   {
//		       e.printStackTrace();
//		   }
//		System.out.println(JsonUtil.objectToJsonStr(formatter));
//	}
}
