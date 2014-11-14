package com.mic.log.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.sshtools.j2ssh.util.Hash;

public class StrUtils {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		/*String s="pid=";
		String[] st=s.split("\\=");
		System.out.println(st[0]);
		System.out.println(st.length);
		System.out.println(st.length>1?"":"1");*/
	/*	String[] splitStatus;
		String s="{505=0, POST=10, 408=0, 503=0, Spider=0, GET=0, 404=0, 407=0, 200=10, 403=0, 414=0}";
		String[]  statusArray=s.replace("{", "").replace("}", "").replace(" ", "").split("\\,");
		Map<String,String> mapStatus=new HashMap<String,String>();
		for(String ss:statusArray)
		{
			splitStatus=ss.split("=");
			mapStatus.put(splitStatus[0], splitStatus[1]);
			System.out.println(splitStatus[0]+"------------------"+splitStatus[1]);
		}*/
		/*	String logContent=getLogContent();
			System.out.println(logContent);
		    Pattern p = Pattern.compile("([\\d.]*?)\\s*-\\s*-\\s*\\[([^\\]]*?)\\]\\s*\"([^\"]*?)\"\\s*([^\\s]*?)\\s*(\\d*)\\s*\"([^\"]*?)\"\\s*\"([^\"]*?)\"\\s*\"-\"\\s*\"pid=(.*?)\\s*se=([^\"]*?)\"\\s*\"([^\"]*?)\"");     
		   // Pattern p = Pattern.compile("([\\d.]*?)");     
		    Matcher m = p.matcher(logContent);
	        while(m.find())
	        {
	        	System.out.println(m.group(1));
	        	System.out.println(m.group(2));
	        	System.out.println(m.group(3));
	        	System.out.println(m.group(4));
	        	System.out.println(m.group(5));
	        	System.out.println(m.group(6));
	        	System.out.println(m.group(7));
	        	System.out.println(m.group(8));
	        	System.out.println(m.group(9));
	        	System.out.println(m.group(10));
	        }
	        */
/*		String log="{vo-project|vo-app1|192.168.128.132|mainland-idc-2|11/Oct/2014=9, en-project|en-app1|192.168.128.131|mainland-idc-1|11/Oct/2014=86}";
		//String log="{vo-project|vo-app1|192.168.128.132|mainland-idc-2|11/Oct/2014=3100}";
		String[] str=log.replace("{", "").replace("}","").replace(" ", "").split(",");
		Map<String,String> mapTotalVisitInfo=new HashMap<String,String>();
		for(String s:str)
		{
			String[] nameAndTotal=s.split("\\=");
			if(nameAndTotal.length>1)
				mapTotalVisitInfo.put(nameAndTotal[0],nameAndTotal[1]);
		}*/
		/*String dayRandom=new SimpleDateFormat("dd/MMM/yyyy:HH:mm:ss Z",Locale.ENGLISH).format(new Date());
		System.out.println(random(10,20)+dayRandom.substring(2));*/
		String msg="[2014-11-01 04:17:42,816 [http-bio-8081-exec-13] ERROR org.apache.jsp.index_jsp kkkk adfasdf' '][{ headers:{timestamp=1414840672778, datacenter=en-project,en-app1,192.168.128.131,mainland-idc-1} body: 32 30 31 34 2D 31 31 2D 30 31 20 30 34 3A 31 37 2014-11-01 04:17 }]";
		
		System.out.println(msg.substring(0,2));
		Map<String,String> contentMap = LogUtils.splitLog(msg);
		String[] result=msg.split("\\]\\[\\{");
		String[] appLogInfo=result[0].replace("[", "").replace("]","").split(" ");
		String logDate=appLogInfo[0]+" "+appLogInfo[1];
		String threadName=appLogInfo[2];
		String infoLevel=appLogInfo[3];
		String exceptionInfo="";
		for(int i=3;i<appLogInfo.length;i++)
		{
			exceptionInfo+=appLogInfo[i];
		}
		Map<String,String> mapLogInfo=new HashMap<String,String>();
		mapLogInfo.put("logDate", logDate);
		mapLogInfo.put("threadName", threadName);
		mapLogInfo.put("logLevel", infoLevel);
		mapLogInfo.put("logException", exceptionInfo);
		
		Map<String,Map<String,String>> mapContent=new HashMap<String,Map<String,String>>();
		mapContent.put("logInfo", mapLogInfo);
		mapContent.put("notifyInfo", contentMap);
		System.out.println(StrUtils.format("日志时间:{0}  线程名称:{1}日志级别:{2}异常消息:{3}一级名称:{4}二级应用:{5}IP地址:{6}机房地址:{7}", logDate,threadName,infoLevel,exceptionInfo,contentMap.get("projectname"),contentMap.get("appname"),contentMap.get("ip"),contentMap.get("location")));
	}
	public static int random(int min, int max) {
		return (int) (Math.random() * (max + 1 - min) + min);
	}
	public static String getLogContent()
	{
		//return "[1.123.123.123 - - [11/Oct/2014:20:12:04 +0800] \"GET /Products1-search/find-china-products/0b0nolimit/Ladies_Panty-2.html HTTP/1.1\" 404 500 \"http://5.made-in-china.com/products-search/hot-china-products/Ladies_Panty.html\" \"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/536.11 (KHTML, like Gecko) Chrome/20.0.1132.11 Safari/2.0 Safari/536.11\" \"-\" \"pid=121212sI9i20OeqEsKyPHWJElkxwgkTGGcABnUZmKgwtSyuQq se=sI9i20mnPd7dbOeqEsKyPHWJElkxwgkTGGcABnUZmKgwtSyuQq\" \"www.\"][header={en";
		return "[1.123.123.123 - - [11/Oct/2014:20:12:04 +0800] \"GET /Products1-search/find-china-products/0b0nolimit/Ladies_Panty-2.html HTTP/1.1\" 404 500 \"http://5.made-in-china.com/products-search/hot-china-products/Ladies_Panty.html\" \"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/536.11 (KHTML, like Gecko) Chrome/20.0.1132.11 Safari/2.0 Safari/536.11\" \"-\" \"pid= se=\" \"www.\"]";
		//return "[1.201.221.126 - - [11/Oct/2014:09:12:53 +0800] \"GET /Products1-search/find-china-products/0b0nolimit/Ladies_Panty-2.html HTTP/1.1\" 500 3136 \"http://5.made-in-china.com/products-search/hot-china-products/Ladies_Panty.html\" \"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/536.11 (KHTML, like Gecko) Chrome/20.0.1132.11 Safari/2.0 Safari/536.11\" \"-\" \"pid=1212 se=kakakkakakakakak\" \"www.\"]";
	}

    //字符串合并方法，返回一个合并后的字符串  
    public static String format(String str,Object ... args)  
    {  
  
        //这里用于验证数据有效性  
        if(str==null||"".equals(str))  
            return "";  
        if(args.length==0)  
        {  
            return str;  
        }  
  
        /* 
         *如果用于生成SQL语句，这里用于在字符串前后加单引号 
        for(int i=0;i<args.length;i++) 
        { 
            String type="java.lang.String"; 
            if(type.equals(args[i].getClass().getName())) 
                args[i]="'"+args[i]+"'"; 
        } 
        */  
  
        String result=str;  
  
        //这里的作用是只匹配{}里面是数字的子字符串  
        java.util.regex.Pattern p = java.util.regex.Pattern.compile("\\{(\\d+)\\}");  
        java.util.regex.Matcher m = p.matcher(str);  
  
        while(m.find())  
        {  
            //获取{}里面的数字作为匹配组的下标取值  
            int index=Integer.parseInt(m.group(1));  
  
            //这里得考虑数组越界问题，{1000}也能取到值么？？  
            if(index<args.length)  
            {  
  
                //替换，以{}数字为下标，在参数数组中取值  
                result=result.replace(m.group(),args[index].toString());  
            }  
        }  
        return result;  
    }  
}
