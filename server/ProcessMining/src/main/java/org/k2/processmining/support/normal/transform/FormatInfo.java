package org.k2.processmining.support.normal.transform;

import java.util.HashMap;
import java.util.Map;

/**
 * 当数据项需要格式化时，将格式化的规则存储为此数据结构
 * @author admin
 *
 */
public class FormatInfo { //泛型T，当日志类型为有数据项名，T的类型为String；无数据项名时为false
	/**
	 * 数据项的序号或名称
	 */
	public String itemNameOrIndex; //数据项的序号或名称
	//public T formatTypeNameOrIndex; //用作源格式标识的数据项序号或名称
	/**
	 * 占位符数组，数组中的元素组成格式模板
	 */
	public char[] placeHolders; //占位符数组，数组中的元素组成格式模板
	/**
	 * 源格式模板与源格式标识属性的映射关系
	 */
	public String formatTypeNameOrIndex;
	
	public String getFormatTypeNameOrIndex() {
		return formatTypeNameOrIndex;
	}
	public void setFormatTypeNameOrIndex(String formatTypeNameOrIndex) {
		this.formatTypeNameOrIndex = formatTypeNameOrIndex;
	}

	public Map<String,String> sourceFormatTemplates; //源格式模板与源格式标识属性的映射关系
	/**
	 * 目标格式模板
	 */
	public String targetFormatTemplate; //目标格式模板
	
	public FormatInfo(String itemNameOrIndex, char[] placeHolders,String formatTypeNameOrIndex,
			Map<String, String> sourceFormatTemplates, String targetFormatTemplate) {
		super();
		this.itemNameOrIndex = itemNameOrIndex;
		this.placeHolders = placeHolders;
		this.formatTypeNameOrIndex = formatTypeNameOrIndex;
		this.sourceFormatTemplates = sourceFormatTemplates;
		this.targetFormatTemplate = targetFormatTemplate;
	}
	public String getItemNameOrIndex() {
		return itemNameOrIndex;
	}
	public void setItemNameOrIndex(String itemNameOrIndex) {
		this.itemNameOrIndex = itemNameOrIndex;
	}
	public char[] getPlaceHolders() {
		return placeHolders;
	}
	public void setPlaceHolders(char[] placeHolders) {
		this.placeHolders = placeHolders;
	}
	public Map<String, String> getSourceFormatTemplates() {
		return sourceFormatTemplates;
	}
	public void setSourceFormatTemplates(Map<String, String> sourceFormatTemplates) {
		this.sourceFormatTemplates = sourceFormatTemplates;
	}
	public String getTargetFormatTemplate() {
		return targetFormatTemplate;
	}
	public void setTargetFormatTemplate(String targetFormatTemplate) {
		this.targetFormatTemplate = targetFormatTemplate;
	}
	public String formatTransform(String data,String formatTypeData){//data为需要格式化的数据，formatTypeData为当前所在的格式模板标识
		
			int nums = placeHolders.length;
			
			String format = null;
			format =  sourceFormatTemplates.get(formatTypeData);
			if(format==null){
				format =  sourceFormatTemplates.get("DEFAULT");
//				System.out.println(format);
			}

		
		if(nums==0||format==null)
			return data;
		
		
		FormatItem[] formatItems=new FormatItem[nums]; 	
//		for(int i=0;i<nums;i++)formatItems[i].setPlaceHolderString("");
		
		for(int i=0;i<nums;i++)
			for(int j=0;j<format.length();j++)
				if(format.charAt(j)==placeHolders[i]){
					FormatItem temp = new FormatItem(placeHolders[i], j);
//					formatItems[i].setPlaceHolder(placeHolders[i]);
//					formatItems[i].setPosition(j);
					formatItems[i] = temp;
					break;
				}else if(j==format.length()-1)
				{
					FormatItem temp = new FormatItem(placeHolders[i],-1);
//					formatItems[i].setPlaceHolder(placeHolders[i]);
//					formatItems[i].setPosition(-1);   //格式中未出现该占位符，位置设为-1
					formatItems[i] = temp;
				}
/*//		打印数据
		System.out.println("对formatItems以位置项进行排序before");
		for(int i=0;i<nums;i++){
			System.out.println(formatItems[i].toString());
		}*/
			//对formatItems以位置项进行排序
		for(int i=0;i<nums;i++)
		{
			int min=i;
			for(int j=i;j<nums;j++)
				if(formatItems[j].getPosition()<formatItems[min].getPosition())
				min=j;
			if(min!=i)
			{
				FormatItem tempFormatItem = new FormatItem(formatItems[min].getPlaceHolder(), formatItems[min].getPosition());
				formatItems[min].setPlaceHolder(formatItems[i].getPlaceHolder());
				formatItems[min].setPosition(formatItems[i].getPosition());
				formatItems[i].setPlaceHolder(tempFormatItem.getPlaceHolder());
				formatItems[i].setPosition(tempFormatItem.getPosition());
			}
		}
/*//		打印数据
		System.out.println("对formatItems以位置项进行排序after");
		for(int i=0;i<nums;i++){
			System.out.println(formatItems[i].toString());
		}*/
		//一个判断，占位符之间必须由字符隔开
		for(int i=1;i<nums;i++){
			if(formatItems[i-1].getPosition()!=-1)
				if(formatItems[i].getPosition()==formatItems[i-1].getPosition()+1){
					System.out.println("占位符之间必须由字符隔开！");
					return data;
				}	
		}
		
		//获得实际使用的占位符个数
		int validNums = 0;
		for(int i=0;i<nums;i++){
			if(formatItems[i].getPosition()!=-1)
			{
				validNums = nums - i;
				break;
			}
		}
		if(validNums == 0)return data;
		
		
		String[] remainStrings = new String[validNums+1];//除开占位符的剩余字符，共可能有validNums+1个
		remainStrings[0]=format.substring(0, formatItems[nums-validNums].getPosition());
		for(int i=1;i<validNums;i++)
			remainStrings[i]=format.substring(formatItems[nums-validNums+i-1].getPosition()+1, formatItems[nums-validNums+i].getPosition());
		remainStrings[validNums]=format.substring(formatItems[nums-1].getPosition()+1);
		
		//获取占位符对应的字符串
		int[] indexOfString = new int[remainStrings.length];//remainString在数据中对应的位置
		for(int i=0;i<remainStrings.length;i++){
			if(i==0)indexOfString[i]=data.indexOf(remainStrings[i]);
			else if(i==remainStrings.length-1){
					if(remainStrings[i].equals(""))indexOfString[i] = data.length();
					else indexOfString[i]=data.indexOf(remainStrings[i],indexOfString[i-1]+remainStrings[i-1].length());
			}else
				indexOfString[i]=data.indexOf(remainStrings[i],indexOfString[i-1]+remainStrings[i-1].length());
//			System.out.println(indexOfString[i]);
			
			if(indexOfString[i]==-1){
				System.out.println("模板格式与数据不匹配,默认使用原格式！");
				return data;
			}
		}
		
		for(int i=0;i<validNums;i++){
			int a=indexOfString[i]+remainStrings[i].length();
			int b= indexOfString[i+1];
//			if(a>b){
//				System.out.println("模板格式与数据不匹配！");
//				return data;
//			}
			formatItems[nums-validNums+i].setPlaceHolderString(data.substring(a,b));
		}
		
		//返回格式化后的String;
		String tempTargetString = targetFormatTemplate;
		for(int i=0;i<nums;i++)
			tempTargetString=tempTargetString.replaceAll(formatItems[i].getPlaceHolder()+"", formatItems[i].getPlaceHolderString());
		
		/*检查formatItems*/
//		for(int i=0;i<remainStrings.length;i++)
//		System.out.println(remainStrings[i]+"\n");
//		for(int i=0;i<nums;i++)
//		System.out.println(formatItems[i].getPlaceHolder()+"  ");
//		System.out.println("\n");
//		for(int i=0;i<nums;i++)
//		System.out.println(formatItems[i].getPosition()+"  ");
//		System.out.println("\n");
//		for(int i=0;i<nums;i++)
//		System.out.println(formatItems[i].getPlaceHolderString()+"  ");
		/*检查formatItems*/	
		
		return tempTargetString;
	}
	
	public static void main(String args[]){
		String itemNameOrIndex="QC";
		char[] placeHolders={'A','B','C','D'};
		Map<String, String> sourceFormatTemplates=new HashMap<String, String>();
/*		sourceFormatTemplates.put("updateIncident","A-B-C-D");
		sourceFormatTemplates.put("updatePlan","D/B/C/A");
		sourceFormatTemplates.put("updateTask","A-B-CTD");
		String targetFormatTemplate="A-B-CTD";
		FormatInfo formatInfo=new FormatInfo(itemNameOrIndex, placeHolders,sourceFormatTemplates,targetFormatTemplate);
        String result=formatInfo.formatTransform("2015/01/01/21:00:00","updatePlan");		
		System.out.println(result);*/
		
		
		sourceFormatTemplates.put("updateIncident","A-B-C-D");
		sourceFormatTemplates.put("updatePlan","C/B/ATD");
		sourceFormatTemplates.put("updateTask","A/B/CTD");
		String targetFormatTemplate="A-B-CTD";
		FormatInfo formatInfo=new FormatInfo(itemNameOrIndex, placeHolders,"[Method]",sourceFormatTemplates,targetFormatTemplate);
        String result1=formatInfo.formatTransform("01/01/2015T21:00:00","updatePlan");		
		System.out.println(result1);
		System.out.println("-----------------------------------------------------------------");
		String result2=formatInfo.formatTransform("2015/01/01T21:00:00","updateTask");		
		System.out.println(result2);
		System.out.println("-----------------------------------------------------------------");
	}

}
