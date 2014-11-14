package com.mic.log.util.storm;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.mic.log.util.LogUtils;
public class RegUtils {
	private static final Logger LOGGER = Logger.getLogger(RegUtils.class);

	/*
	 * 判断是否是爬虫
	 */
	public static boolean regBot(String matcherText) {
		Pattern p = Pattern
				.compile("(([a-z]bot[^a-z])|([a-z]Bot[^a-z])|([sS]pider[^a-z])|(Yahoo! Slurp)|(Mediapartners-Google))");
		Matcher m = p.matcher(matcherText);
		while (m.find()) {
			return true;
		}
		return false;
	}
	/**
	 * 处理flume读取日志到kafka的消息，通过正则表达式
	 * 按照formaat的格式取得相应的值
	 * 
	 * @param logContent 日志消息
	 * @return  返回各个nginx的访问键值
	 */
	public static Map<String, String> getListByReg(String logContent) {
		Map<String, String> mapRegLogContent = new HashMap<String, String>();
		Pattern p = Pattern
				.compile("([\\d.]*?)\\s*-\\s*-\\s*\\[([^\\]]*?)\\]\\s*\"([^\"]*?)\"\\s*([^\\s]*?)\\s*(\\d*)\\s*\"([^\"]*?)\"\\s*\"([^\"]*?)\"\\s*\"-\"\\s*\"pid=(.*?)\\s*se=([^\"]*?)\"\\s*\"([^\"]*?)\"");
		Matcher m = p.matcher(logContent);
		// '$remote_addr -- [$time_local]' '"request" $status $body_bytes_send
		// "$http_referer" "http_user_agent" "-" "val1=$val1 val2=$val2"
		// "$host"';
		while (m.find()) {
			mapRegLogContent.put("Remote_Addr", m.group(1));
			mapRegLogContent.put("Time_Local", m.group(2));
			mapRegLogContent.put("Request_Method",
					(m.group(3).toString().split(" "))[0]);
			mapRegLogContent.put("Request_Url",
					(m.group(3).toString().split(" "))[1]);
			mapRegLogContent.put("Status", m.group(4));
			mapRegLogContent.put("Body_Bytes_Send", m.group(5));
			mapRegLogContent.put("Http_Referer", m.group(6));
			mapRegLogContent.put("Http_User_Agent", m.group(7));
			mapRegLogContent.put("PID", m.group(8));
			mapRegLogContent.put("SE", m.group(9));
			mapRegLogContent.put("Host", m.group(10));
		}
		return mapRegLogContent;
	}
	/**
	 * 此方法已经废弃，不使用，仅作为备份
	 * @param appNameInfo
	 * @return
	 */
	public static Map<String, Integer> getTheVisitTotal(String appNameInfo) {
		Map<String, Integer> mapTotalVisitInfo = new HashMap<String, Integer>();
		// String
		// log="{vo-project|vo-app1|192.168.128.132|mainland-idc-2|11/Oct/2014=9, en-project|en-app1|192.168.128.131|mainland-idc-1|11/Oct/2014=86}";
		String[] str = appNameInfo.replace("{", "").replace("}", "").replace(" ", "").split(",");
		for (String s : str) {
			String[] nameAndTotal = s.split("\\=");
			if (nameAndTotal.length > 1)
				mapTotalVisitInfo.put(nameAndTotal[0],
						Integer.valueOf(nameAndTotal[1]));
		}
		return mapTotalVisitInfo;
	}
    /**
     * 此方法已经废弃，不作使用，仅作为备份
     * @param info
     * @return
     */
	public static Map<String, String> getOtherInfo(String info) {
		Map<String, String> mapInfo = new HashMap<String, String>();
		String[] str = info.replace("{", "").replace("}", "").split("<");
		for (String s : str) {
			String[] sStr = s.split("==");
			if (sStr.length > 1) {
				mapInfo.put(sStr[0], sStr[1]);
			}
		}
		return mapInfo;
	}
	/**
	 *  处理应用日志，返回按照log4j.properties的格式化的值
	 *  
	 * @param msg 日志内容，需要在flume配置推送到kafka的格
	 * 			     式，定制化的格式
	 * @return
	 */
	public static Map<String,Map<String,String>> dealAppLog(String msg)
	{
		Map<String,Map<String,String>> mapContent=new HashMap<String,Map<String,String>>();
		try
		{
			Map<String,String> contentMap = LogUtils.splitLog(msg);
			String[] result=msg.split("\\]\\[\\{");
			String[] appLogInfo=result[0].replace("[", "").replace("]","").split(" ");
			if(appLogInfo.length<4)//如果小于4，则代表不是按照格式化的内容，无需处理，返回空map
			{
				return mapContent;
			}
			String logDate=appLogInfo[0]+" "+appLogInfo[1];//得到日志的记录时间
			String threadName=appLogInfo[2];//得到日志的线程名称
			String infoLevel=appLogInfo[3];//得到日志的报警级别
			String classFullName=appLogInfo[4];//得到类的全称
			String exceptionInfo="";
			for(int i=5;i<appLogInfo.length;i++)//后面全部是应用里的异常信息
			{
				exceptionInfo+=appLogInfo[i]+" ";
			}
			Map<String,String> mapLogInfo=new HashMap<String,String>();
			mapLogInfo.put("logDate", logDate);//日志时间
			mapLogInfo.put("threadName", threadName);//线程名称
			mapLogInfo.put("logLevel", infoLevel);//报警级别
			mapLogInfo.put("classFullName", classFullName);//类全称
			mapLogInfo.put("logException", exceptionInfo);//异常信息
			
			mapContent=new HashMap<String,Map<String,String>>();
			mapContent.put("logInfo", mapLogInfo);//设置错误日志的信息map
			mapContent.put("locationInfo", contentMap);//返回错误日志的位置信息
		}catch(Exception ex)
		{
			LOGGER.error("DealAppLog:",ex);
		}
		return mapContent;
	}

	public static void main(String[] args) {
	}
}
