package com.mic.log.util.sms;

import javax.xml.namespace.QName;

import org.apache.axis2.AxisFault;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.rpc.client.RPCServiceClient;

public class WebServiceClient {
	private RPCServiceClient serviceClient;
	private String wsAddress;
	public RPCServiceClient getServiceClient() {
		return serviceClient;
	}
	public void setServiceClient(RPCServiceClient serviceClient) {
		this.serviceClient = serviceClient;
	}
	public String getWsAddress() {
		return wsAddress;
	}
	public void setWsAddress(String wsAddress) {
		this.wsAddress = wsAddress;
	}
	public String getNamespaceURI() {
		return namespaceURI;
	}
	public void setNamespaceURI(String namespaceURI) {
		this.namespaceURI = namespaceURI;
	}
	private String namespaceURI;
	public void init(String wsAddress,String namespaceURI) throws Exception
	{
		this.setWsAddress(wsAddress);
		this.setNamespaceURI(namespaceURI);
		this.init();
	}
	public void init()throws Exception
	{
		try
		{
			serviceClient=new RPCServiceClient();
			Options options=serviceClient.getOptions();
			EndpointReference targetEPR=new EndpointReference(wsAddress);
			options.setTo(targetEPR);
		}catch(AxisFault e)
		{
			e.printStackTrace();
			throw e;
		}
	}
	public Object[] invoke(String methodName,Object[] args,Class[] returnTypes)throws Exception{
		Object[] o=null;
		try
		{
			QName opAddEntry=new QName(namespaceURI,methodName);
			Object[] objects=serviceClient.invokeBlocking(opAddEntry, args, returnTypes);
			return o;
		}catch(AxisFault e)
		{
			e.printStackTrace();
			throw e;
		}
	}
	public Object invokeForObject(String methodName,Object[] args,Class[] returnTypes)throws Exception{
		Object o=null;
		try
		{
			QName opAddEntry=new QName(namespaceURI,methodName);
			Object[] objects=serviceClient.invokeBlocking(opAddEntry, args, returnTypes);
			if(objects!=null&&objects.length>0)
			{
				o=objects[0];
			}
			return o;
		}catch(AxisFault e)
		{
			e.printStackTrace();
			throw e;
		}
	}
}
