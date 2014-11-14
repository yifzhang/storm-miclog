package com.mic.log.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.mic.log.util.storm.RegUtils;


public class RegTest {
	private static String logContent = "";
	private static String[] requestMethod={"GET","POST","HEAD"};
	private static String[] urlStr={"/Products1-search/find-china-products/"
									,"/Products-search/find2-china-products/"
									,"/Products-search/find3-china-products/"
									};
	private static String[] statusCode={"200","404","503","403","407","408","414","503","505"};
	private static String[] requestUrl={"http://1.1.made-in-china.com"
										,"http://1.2.made-in-china.com"
										,"http://1.3.made-in-china.com"
										,"http://1.4.made-in-china.com"
										,"http://5.made-in-china.com"
										,"http://6.made-in-china.com"
										};
	private static String[] userAgent={ "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/536.11 (KHTML, like Gecko) Chrome/20.0.1132.11 Safari/2.0 Safari/536.11"
										,"Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)"
										,"Baiduspider+(+http://www.baidu.com/search/spider.htm)"
										,"Mozilla/5.0 (compatible; Yahoo! Slurp China; http://misc.yahoo.com.cn/help.html)"
										,"Mozilla/5.0 (compatible; Yahoo! Slurp; http://help.yahoo.com/help/us/ysearch/slurp)"
										,"iaskspider/2.0(+http://iask.com/help/help_index.html)"
										,"Mozilla/5.0 (compatible; iaskspider/1.0; MSIE 6.0)"
										,"Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)"
										,"Mediapartners-Google/2.1"
										,"Mozilla/5.0 (compatible; YodaoBot/1.0; http://www.yodao.com/help/webmaster/spider/;)"
										,"msnbot/1.0 (+http://search.msn.com/msnbot.htm)"
										,"msnbot-media/1.0 (+http://search.msn.com/msnbot.htm)"
										};
	public static void main(String[] args) {
		String pid=RandomUtils.createCode(50);
		for(int i=0;i<25;i++)
		{
			logContent =
						 String.valueOf(i)+ "."+ random(0, 255)+ "."+ random(0, 255)+ "."+ random(0, 255)
						+ " - - ["
						+ new SimpleDateFormat("dd/MMM/yyyy:HH:mm:ss Z",Locale.ENGLISH).format(new Date())
						+ "] \""+requestMethod[random(0, 2)]
						+" "+urlStr[random(0, 2)]
						+"0b0nolimit/Ladies_Panty-"+requestMethod[random(0, 2)]
						+".html HTTP/1.1\" "
						+statusCode[random(0, 8)]+" 500 \""+requestUrl[random(0, 5)]
						+"/products-search/hot-china-products/Ladies_Panty.html\" \""+userAgent[random(0,11)]+"\" \"-\" \"pid="
						+pid+" se="+pid.replace("a", "d")+"\" \"www.\"";
				Map<String,String> mapContent=new HashMap<String,String>();
				Map<String,String> mapSplitLog=new HashMap<String,String>();
				String httpUserAgent="";
				System.out.println(logContent);
				String[] logSplitBySpace=logContent.replace("\"", "").replace("[", "").replace("]","").split(" ");
/*				for(String s:logSplitBySpace)
				{
					System.out.println(s);
				}*/
				if(null!=logSplitBySpace&&logSplitBySpace.length>=16)
				{
					mapContent.put("IP", logSplitBySpace[0]);
					mapContent.put("VisitTime", logSplitBySpace[3]);
					mapContent.put("TimeZone", logSplitBySpace[4]);
					mapContent.put("RequestMethod", logSplitBySpace[5]);
					mapContent.put("FirstVisitURL", logSplitBySpace[6]);
					mapContent.put("Protocol", logSplitBySpace[7]);
					mapContent.put("StatusCode", logSplitBySpace[8]);
					mapContent.put("BodyBytes", logSplitBySpace[9]);
					mapContent.put("RefererURL", logSplitBySpace[10]);
					for(int index=11;index<logSplitBySpace.length-3;index++)
					{
						httpUserAgent=httpUserAgent+" "+logSplitBySpace[index];
					}
					mapContent.put("HttpUserAgent", httpUserAgent);
					String[] pidArrayStr=logSplitBySpace[logSplitBySpace.length-3].split("\\=");
					String[] seArrayStr=logSplitBySpace[logSplitBySpace.length-2].split("\\=");
					mapContent.put("Pid", pidArrayStr.length<2?null:pidArrayStr[1]);
					mapContent.put("Se", seArrayStr.length<2?null:seArrayStr[1]);
					mapContent.put("Host", logSplitBySpace[logSplitBySpace.length-1]);
				}
				System.out.println("===============IP:"+mapContent.get("IP")+"==============================");
				System.out.println("===============VisitTime:"+mapContent.get("VisitTime")+"==============================");
				System.out.println("===============TimeZone:"+mapContent.get("TimeZone")+"==============================");
				System.out.println("===============RequestMethod:"+mapContent.get("RequestMethod")+"==============================");
				System.out.println("===============FirstVisitURL:"+mapContent.get("FirstVisitURL")+"==============================");
				System.out.println("===============Protocol:"+mapContent.get("Protocol")+"==============================");
				System.out.println("===============StatusCode:"+mapContent.get("StatusCode")+"==============================");
				System.out.println("===============BodyBytes:"+mapContent.get("BodyBytes")+"==============================");
				System.out.println("===============RefererURL:"+mapContent.get("RefererURL")+"==============================");
				System.out.println("===============HttpUserAgent:"+mapContent.get("HttpUserAgent")+"==============================");
				System.out.println("===============Pid:"+mapContent.get("Pid")+"==============================");
				System.out.println("===============Se:"+mapContent.get("Se")+"==============================");
				System.out.println("===============Host:"+mapContent.get("Host")+"==============================");
				System.out.println(RegUtils.regBot(mapContent.get("HttpUserAgent")));
		}
	}
	public static int random(int min, int max) {
		return (int) (Math.random() * (max + 1 - min) + min);
	}
	
}
