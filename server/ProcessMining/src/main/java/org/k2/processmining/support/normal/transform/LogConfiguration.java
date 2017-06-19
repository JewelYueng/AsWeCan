package org.k2.processmining.support.normal.transform;

//import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class LogConfiguration {
	public String[] timeItems; //时间戳属性对应数据项名称或序号
	//public T[] classifierNameOrIndexs; //分类器属性对应数据项名称或序号
//	public String[] redunceItems; //冗余数据项名称或序号
	public Map<String, String[]> renameORmergeItems; //需要合并或更名的数据项名字或序号（除时间项的所有其他项！）
	public String formatTypeNameOrIndex; //源格式模板标识
	public FormatInfo[] formatInfos; //格式信息数组，用于不同数据项的格式统一化
	public String nameValSeparator; //数据项名与数据项值之间的分隔符，只对有数据项名日志有效
	public String itemSeparator; //数据项之间的分隔符
	public String nulVal; //空值使用的字符串
	public String[] getTimeItems() {
		return timeItems;
	}
	public void setTimeItems(String[] timeItems) {
		this.timeItems = timeItems;
	}
	public Map<String, String[]> getRenameORmergeItems() {
		return renameORmergeItems;
	}
	public void setRenameORmergeItems(Map<String, String[]> renameORmergeItems) {
		this.renameORmergeItems = renameORmergeItems;
	}
	public String getFormatTypeNameOrIndex() {
		return formatTypeNameOrIndex;
	}
	public void setFormatTypeNameOrIndex(String formatTypeNameOrIndex) {
		this.formatTypeNameOrIndex = formatTypeNameOrIndex;
	}
	public FormatInfo[] getFormatInfos() {
		return formatInfos;
	}
	public void setFormatInfos(FormatInfo[] formatInfos) {
		this.formatInfos = formatInfos;
	}
	public String getNameValSeparator() {
		return nameValSeparator;
	}
	public void setNameValSeparator(String nameValSeparator) {
		this.nameValSeparator = nameValSeparator;
	}
	public String getItemSeparator() {
		return itemSeparator;
	}
	public void setItemSeparator(String itemSeparator) {
		this.itemSeparator = itemSeparator;
	}
	public String getNulVal() {
		return nulVal;
	}
	public void setNulVal(String nulVal) {
		this.nulVal = nulVal;
	}
	public LogConfiguration(String[] timeItems,
                            Map<String, String[]> renameORmergeItems,
                            String formatTypeNameOrIndex, FormatInfo[] formatInfos,
                            String nameValSeparator, String itemSeparator, String nulVal) {
		super();
		this.timeItems = timeItems;
		this.renameORmergeItems = renameORmergeItems;
		this.formatTypeNameOrIndex = formatTypeNameOrIndex;
		this.formatInfos = formatInfos;
		this.nameValSeparator = nameValSeparator;
		this.itemSeparator = itemSeparator;
		this.nulVal = nulVal;
	}
	public LogConfiguration(String formats, String timeNames, String dataNames, String itemSeparator, String nameValSeparator, String nulVal){
		
		String[] tempFormats = formats.split(";");//格式信息
		FormatInfo[] formatInfos = new FormatInfo[tempFormats.length];
		for(int i=0;i<tempFormats.length;i++){
			String[] formatItem = tempFormats[i].split(",");
			Map<String,String> myMap = new LinkedHashMap<String,String>();//格式模板
			for(int j=0;j<formatItem.length-4;j++){
				String[] identificationAndValue=formatItem[3+j].split(":");
				myMap.put(identificationAndValue[0], identificationAndValue[1]);
			}
			FormatInfo tempFormatInfo = new FormatInfo(formatItem[0], formatItem[1].toCharArray(),formatItem[2], myMap, formatItem[formatItem.length-1]);
			formatInfos[i]=tempFormatInfo;
		}
		
		String[] timeName = timeNames.split(";");//组成时间戳的项
		
		Map<String,String[]>myMergeMap = new LinkedHashMap<String,String[]>();

		String[] tempDataNames = dataNames.split(",");
		
		for(int i=0;i<tempDataNames.length;i++){
			
			String[] namePlusNew = tempDataNames[i].split(":");
			myMergeMap.put(namePlusNew[1], namePlusNew[0].split(";"));
			
		}
		
		this.timeItems = timeName;
		this.renameORmergeItems = myMergeMap;
		this.formatTypeNameOrIndex = "[Method]";
		this.formatInfos = formatInfos;
		this.nameValSeparator = nameValSeparator;
		this.itemSeparator = itemSeparator;
		this.nulVal = nulVal;

	}


	
	
}
