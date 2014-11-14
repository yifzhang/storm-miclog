package com.mic.log.util.sms;

import com.mic.log.util.PropertiesUtils;

public class Call {
	private final static String wsAddress;
	private final static WebServiceClient wsc;
	static
	{
		wsAddress=PropertiesUtils.getValue("smsUrl");
		wsc=new WebServiceClient();
		try {
			wsc.init(wsAddress, "MessageTransferWebService");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static String SendSms(String[] paraArray) throws Exception
	{
		/*paraArray[0]="mictest";
		paraArray[1]="focustech1+11";
		paraArray[2]="18913984951";
		paraArray[3]="sms/mt";
		paraArray[4]="项目警报";
		paraArray[5]="项目EN-发生Error";
		paraArray[6]=null;
		paraArray[7]="414";
		paraArray[8]="";
		paraArray[9]="";*/
		Integer iRet=(Integer)wsc.invokeForObject("sendMessageX", paraArray, new Class[]{Integer.class});
		return SmsManager.getSmsStatusMsg(iRet);
	}
}
