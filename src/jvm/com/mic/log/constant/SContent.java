package com.mic.log.constant;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class SContent {
	public static int writeFileIndex=0;//写文件的索引id
	public static  Map<String,String> mapKey=new HashMap<String,String>();//插入数据库时候的标示map

	public static String REMOTE_ADDR="Remote_Addr";
	public static String STATUS="Status";
	public static String TIME_LOCAL="Time_Local";
	public static String REQUEST_METHOND="Request_Method";
	public static String REQUEST_URL="Request_Url";
	public static String PROTOCOL="Protocol";
	public static String BODY_BYTES="Body_Bytes";
	public static String HTTP_REFERER="Http_Referer";
	public static String HTTP_USER_AGENT="Http_User_Agent";
	public static String PID="Pid";
	public static String SE="Se";
	public static String HOST="Host";

	public static String VISIT_INFO="visitInfo";
	public static String VISIT_COUNT="visitCount";
	
	public static String SPIDER="Spider";
	public static String CODE_200="200";
	public static String CODE_403="403";
	public static String CODE_404="404";
	public static String CODE_407="407";
	public static String CODE_408="408";
	public static String CODE_414="414";
	public static String CODE_503="503";
	public static String CODE_505="505";
	public static String M_GET="GET";
	public static String M_POST="POST";
	
	public static Set<String> lsAlarmSql=new HashSet<String>();
}
