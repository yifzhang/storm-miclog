package com.mic.log.util;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import backtype.storm.tuple.Values;

public class LogUtils implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6841394643348804259L;
	/**
	 * 得到访问日志的日期
	 * @param content 初始化内容
	 * @param reg  正则表达式
	 * @return
	 */
	public static String getDate(String content,String reg)
	{
		Pattern pattern = Pattern.compile(reg,
				Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
		String result="";
		Matcher matcher = pattern.matcher(content);
		if (matcher.find()) {
			result = matcher.group(0);
		}
		return result;
	}
	/**
	 * 得到访问日志的内容，初始化的时候通过flume添加了header的内容
	 * 所以需要分解
	 * @param content
	 * @return
	 */
	public static String getLogContent(String content)
	{
		return content.split("\\]\\[\\{")[0];
	}
	/**
	 * 得到访问日志的机器信息，初始化的时候通过flume添加了header的内容
	 * 所以需要分解
	 * @param content
	 * @return
	 */
	public static String getLogLocation(String content)
	{
		return content.split("\\]\\[\\{")[1];
	}
	/**
	 * 通过正则表达式分析访问日志，得到具体的位置机器信息
	 * 
	 * @param content
	 * @return map机器信息
	 */
	public static Map<String, String> splitLog(String content) {
		Map<String, String> mapContent = new HashMap<String, String>();
		try {
			String logContent =getLogContent(content);
			String logLocation =getLogLocation(content);
			Pattern pattern = Pattern.compile("(.*?) headers:(.*?)body:",
					Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
			Matcher matcher = pattern.matcher(logLocation);
			if (matcher.find()) {
				String result = matcher.group(2);
				Pattern pattern1 = Pattern
						.compile("timestamp=(.*?), datacenter=(.*?)[}]{1}");
				Matcher matcher1 = pattern1.matcher(result);
				if (matcher1.find()) {
					mapContent.put("timestamp", matcher1.group(1));
					String[] dataCenter=matcher1.group(2).split(",");
					mapContent.put("projectname",dataCenter[0]);
					mapContent.put("appname",dataCenter[1]);
					mapContent.put("ip",dataCenter[2]);
					mapContent.put("location",dataCenter[3]);
					mapContent.put("logcontent", logContent);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return mapContent;
	}
	public static void main(String[] args)
	{
		String str="ser'root'@'vm131'(u";
		System.out.println(str.replaceAll("'","\\\\'"));
	}
}
