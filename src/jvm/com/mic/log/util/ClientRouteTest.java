package com.mic.log.util;

public class ClientRouteTest {
	public static void main(String[] args) {
		//如果用户是第一次访问，则执行下面产生随机数的代码;如果不是第一次访问不需要产生随机数
		String randomAB=ClientRoute.getRandomNum(new String[]{"A","B"}, new int[]{50,50});
		//第一步：判断客户端是否存在AB测试方案标识为inquery的值
		if(randomAB=="A")
		{
			System.out.println("进入A方案;");//de=inquery,0;设置生存周期;0代表A方案
		}
		else
		{
			System.out.println("进入B方案;");//de=inquery,1;设置生存周期;1代表B方案
		}
	}

}
