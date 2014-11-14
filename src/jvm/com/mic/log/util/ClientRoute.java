package com.mic.log.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ClientRoute {
	private static Map<String,String> mapPercent=new HashMap<String,String>();
	/**
	 * 1:假设分流比例为50%：50%
	 * 2:按照权重来控制随机数的产生。
	 * 3：example:getRandomNum(new String[]{"A","B"}, new int[]{50,50});
	 * 4：产生随机数的误差概率控制在0.001，保证分流用户的数据标本值接近准确。                       
	 * @param args
	 */
	public static void main(String[] args) {
		String val;
		String aVersionDesc="A";//方案代号
		String bVersionDesc="B";//方案代号
		int aVersionCount=0;//进入A方案的总数
		int bVersionCount=0;//进入B方案的总数
		int aWeight=50;//权重设置
		int bWeight=50;//权重设置
		int visitTotal=0;//访问总数
		int executeTime=5000;//毫秒
		long currentTime=System.currentTimeMillis();
		while(true)
		{
			if(System.currentTimeMillis()-currentTime>executeTime)
			{
				break;
			}
			visitTotal++;
			val=getRandomNum(new String[]{aVersionDesc,bVersionDesc}, new int[]{aWeight,bWeight});
			if(val=="A")
			{
				mapPercent.put(String.valueOf(aVersionDesc),  String.valueOf(++aVersionCount));
			}
			else
			{
				mapPercent.put(String.valueOf(bVersionDesc), String.valueOf(++bVersionCount));
			}
		 //System.out.println("产生的随机数为:"+val);
		}
		 System.out.println(aVersionDesc+"权重为："+(aWeight)+"%；总计产生的个数为:"+mapPercent.get(String.valueOf(aVersionDesc)));
		 System.out.println(bVersionDesc+"权重为："+(bWeight)+"%；总计产生的个数为:"+mapPercent.get(String.valueOf(bVersionDesc)));
		 System.out.println("总产生随机数的个数为:"+visitTotal);
		 System.out.println("执行时间:"+String.valueOf(executeTime)+"毫秒");
		 
	}
    public static String getRandomNum(String[] arr, int[] probability){
        if(arr.length != probability.length) return arr[0];
        Random ran = new Random();
        int ran_num = ran.nextInt(100);
        int temp = 0;
        for (int i = 0; i < arr.length; i++) {
            temp += probability[i];
            if(ran_num < temp)
                return arr[i];
        }
        return arr[1];
    }
}
