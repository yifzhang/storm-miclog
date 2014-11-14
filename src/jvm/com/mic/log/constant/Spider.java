package com.mic.log.constant;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Spider {
	public static Set<String> getSpider()
	{
		Set<String> mapContent=new HashSet<String>();
		 //~[a-z]bot[^a-z] 1;
		 // ~[sS]pider[^a-z] 1;
		 //'Yahoo! Slurp China' 1;
		 //'Mediapartners-Google' 1;
		mapContent.add("qihoobot");
		mapContent.add("Baiduspider");
		mapContent.add("Googlebot");
		mapContent.add("Googlebot-Mobile");
		mapContent.add("Googlebot-Image");
		mapContent.add("Mediapartners-Google");
		mapContent.add("Adsbot-Google");
		mapContent.add("Feedfetcher-Google");
		mapContent.add("Yahoo! Slurp");
		mapContent.add("Yahoo! Slurp China");
		mapContent.add("YoudaoBot");
		mapContent.add("Sosospider");
		mapContent.add("Sogou spider");
		mapContent.add("Sogou web spider");
		mapContent.add("MSNBot");
		mapContent.add("ia_archiver");
		mapContent.add("Tomato Bot");
		mapContent.add("360Spider");
		mapContent.add("360Spider-Image");
		mapContent.add("360Spider-Video");
		return mapContent;
	}
	public static void main(String[] args)
	{
		
		System.out.println(getSpider().contains("Tomato Bot"));
		System.out.println(getSpider().hashCode());
	}
}
