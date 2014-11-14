package com.mic.log.util.sms;

import com.mic.log.util.PropertiesUtils;



public class SmsManager {
	public static void main(String[] args)
	{
		String wsAddress=PropertiesUtils.getValue("smsUrl");
		try
		{
			WebServiceClient wsc=new WebServiceClient();
			wsc.init(wsAddress, PropertiesUtils.getValue("smsNamespaceURI"));
			Integer iRet=new Integer(0);
			String[] paraArray=new String[10];
			paraArray[0]="mictest";
			paraArray[1]="focustech1+11";
			paraArray[2]="18913984951";
			paraArray[3]="sms/mt";
			paraArray[4]="项目警报";
			paraArray[5]="项目EN-发生Error";
			paraArray[6]=null;
			paraArray[7]="414";
			paraArray[8]="";
			paraArray[9]="";
			iRet=(Integer)wsc.invokeForObject("sendMessageX", paraArray, new Class[]{Integer.class});
			System.out.println(getSmsStatusMsg(iRet));
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	public static String getSmsStatusMsg(int iRet)
	{
		if(iRet==-1)
		{
			return "用户名或口令错误";
		}
		else if(iRet==-2)
		{
			return "IP验证错误";
			
		}
		else if(iRet==-3)
		{
			return "定时日期错误";
			
		}
		else if(iRet==-101)
		{
			return "参数错误userId为空";
		}
		else if(iRet==-102)
		{
			return "参数错误目标号码为空";
		}
		else if(iRet==-103)
		{
			return "参数错误内容为空";
		}
		else if(iRet==200)
		{
			return "目标号码错误";
		}
		else if(iRet==201)
		{
			return "目标号码在黑名单中";
			
		}
		else if(iRet==202)
		{
			return "内容包含敏感词";
		}
		else if(iRet==203)
		{
			return "特服号码未分配";
		}
		else if(iRet==204)
		{
			return "分配通道错误";
		}
		else if(iRet==999)
		{
			return "发送三次都超时";
		}
		else if(iRet==-10)
		{
			return "余额不足";
		}
		else 
		{
			return "未知错误";
		}
	}
}
