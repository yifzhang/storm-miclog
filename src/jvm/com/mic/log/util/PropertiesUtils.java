package com.mic.log.util;

import java.io.IOException;
import java.io.Serializable;
import java.util.Properties;

public class PropertiesUtils implements Serializable{
	 /**
	 * 
	 */
	private static final long serialVersionUID = -2609155065098635092L;
	private static Properties p = new Properties();  
	 public static String proFileNameAndPath="/config.properties";
	    /** 
	     * 读取properties配置文件信息 
	     */  
	    static{  
	        try {  
	            p.load(PropertiesUtils.class.getClass().getResourceAsStream(proFileNameAndPath));  
	        } catch (IOException e) {  
	            e.printStackTrace();   
	        }  
	    }  
	    /** 
	     * 根据key得到value的值 
	     */  
	    public static String getValue(String key)  
	    {  
	        return p.getProperty(key);  
	    }  
	    /** 
	     * 根据key得到value的值 
	     */  
	    public static int getIntValue(String key)  
	    {  
	        return Integer.valueOf(p.getProperty(key));  
	    }  
}
