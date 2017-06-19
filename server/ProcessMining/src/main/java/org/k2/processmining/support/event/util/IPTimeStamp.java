package org.k2.processmining.support.event.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * 在上传文件的操作中，如果多个用户上传的文件名称一样，则肯定会发生覆盖的 情况，为了解决这个问题，可以采用为上传文件自动命名的方式
 * 自动命名采用的的文件格式如下：IP地址+时间戳+三位随机数
 * 
 * @author Voishion
 * 
 */
public class IPTimeStamp {
	public SimpleDateFormat sdf = null;
	public String ip = null;
	public String filename = null;

	public IPTimeStamp() {
	}

	public IPTimeStamp(String ip, String filename) {
		this.ip = ip;
		this.filename = filename;
	}

	/**
	 * 得到 IP地址+时间戳+三位随机数 的新文件名
	 * 
	 * @return
	 */

	public String getNewFileName(String newName, String state, String fileExt) {
		StringBuffer buf = new StringBuffer();
		buf.append(newName + "_" + state + "." + fileExt);
		return buf.toString();
	}

	public String getNewFileName(String newName, String state) {
		StringBuffer buf = new StringBuffer();
		buf.append(newName + "-" + state + ".txt");
		return buf.toString();
	}

	public String getIPTimeRandName() {
		StringBuffer buf = new StringBuffer();
		if (this.ip != null) {
			String str[] = this.ip.split("\\.");
			for (int i = 0; i < str.length; i++) {
				buf.append(this.addZero(str[i], 3));
			}
		}// 加上IP地址
		buf.append(this.getTimeStamp());// 加上日期
		Random random = new Random();
		for (int i = 0; i < 3; i++) {
			buf.append(random.nextInt(10));// 取三个随机数追加到StringBuffer
		}
		// buf.append("."+this.getFileExt());//加上扩展名
		return buf.toString();

	}

	/**
	 * 补0操作【如果不够指定位数，则在前面补0】
	 * 
	 * @param str
	 * @param len
	 * @return
	 */
	private String addZero(String str, int len) {
		StringBuffer s = new StringBuffer();
		s.append(str);
		while (s.length() < len) {
			s.insert(0, "0");
		}
		return s.toString();
	}

	/**
	 * 取得时间戳
	 * 
	 * @return
	 */
	private String getTimeStamp() {
		this.sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		return this.sdf.format(new Date());
	}

	/**
	 * 获取文件扩展名
	 * 
	 * @return
	 */
	private String getFileExt() {
		int i = this.filename.lastIndexOf(".");// 返回最后一个点的位置
		String extension = this.filename.substring(i + 1);// 取出扩展名
		return extension;
	}
}