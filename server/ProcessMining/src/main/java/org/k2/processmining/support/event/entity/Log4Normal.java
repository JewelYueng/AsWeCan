package org.k2.processmining.support.event.entity;

import java.util.ArrayList;
import java.util.Map;

public class Log4Normal {
	/**
	 * 规范化日志
	 */
	private String inputFile;
	/**
	 * 事件日志
	 */
	private String outputFile;
	/**
	 * 空值填充值
	 */
	public static final String nulVal = " ";
	/**
	 * 分隔符
	 */
	public static final String seperator = "\t";
	/**
	 * 时间戳所在列
	 */
	public static final int timeIndex = 0;
	/**
	 * 时间格式
	 */
	public static final String sdfStr = "yyyy-MM-dd'T'HH:mm:ss";
	/**
	 * 记录第一行 属性名
	 */
	private String[] indexs;
	/**
	 * 将日志放入内存
	 */
	private ArrayList<String> events;
	/**
	 * 存放所有列的属性 key值代表第几列
	 */
	private Map<Integer,ArrayList<String>> attValsMap;
	public Log4Normal(String outputFile, String[] indexs,
			ArrayList<String> events, Map<Integer, ArrayList<String>> attValsMap) {
		this.outputFile = outputFile;
		this.indexs = indexs;
		this.events = events;
		this.attValsMap = attValsMap;
	}
	public String[] getIndexs() {
		return indexs;
	}
	public void setIndexs(String[] indexs) {
		this.indexs = indexs;
	}
	public ArrayList<String> getEvents() {
		return events;
	}
	public void setEvents(ArrayList<String> events) {
		this.events = events;
	}
	public Map<Integer, ArrayList<String>> getAttValsMap() {
		return attValsMap;
	}
	public void setAttValsMap(Map<Integer, ArrayList<String>> attValsMap) {
		this.attValsMap = attValsMap;
	}
	public String getInputFile() {
		return inputFile;
	}
	public void setInputFile(String inputFile) {
		this.inputFile = inputFile;
	}
	public String getOutputFile() {
		return outputFile;
	}
	public void setOutputFile(String outputFile) {
		this.outputFile = outputFile;
	}
	

}
