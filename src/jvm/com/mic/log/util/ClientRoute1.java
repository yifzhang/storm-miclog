package com.mic.log.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ClientRoute1 {
	private static Map<String,String> mapPercent=new HashMap<String,String>();
	/**
	 * 1:假设分流比例为30%：70%
	 * 2:按照权重来控制随机数的产生。
	 * 3：example:getRandomNum(new int[]{3,7}, new int[]{30,70});
	 * 4：产生随机数的误差概率控制在0.001，保证分流用户的数据标本值接近准确。                       
	 * @param args
	 */
	public static void main(String[] args) {
		int val;
		int aVersionPercent=3;//A方案占比为30%,请输入3
		int bVersionPercent=10-aVersionPercent;//B方案的占比为70%，请输入7
		int aVersionCount=0;//进入A方案的总数
		int bVersionCount=0;//进入B方案的总数
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
			val=getRandomNum(new int[]{aVersionPercent,bVersionPercent}, new int[]{50,50});
			if(val==aVersionPercent)
			{
				mapPercent.put(String.valueOf(aVersionPercent),  String.valueOf(++aVersionCount));
			}
			else
			{
				mapPercent.put(String.valueOf(bVersionPercent), String.valueOf(++bVersionCount));
			}
		}
		 System.out.println("占比"+(aVersionPercent*10)+"%产生的个数为:"+mapPercent.get(String.valueOf(aVersionPercent)));
		 System.out.println("占比"+(bVersionPercent*10)+"%产生的个数为:"+mapPercent.get(String.valueOf(bVersionPercent)));
		 System.out.println("总产生随机数的个数为:"+visitTotal);
		 System.out.println("执行时间:"+String.valueOf(executeTime)+"毫秒");
		 
	}
	//probability 与 arr 一一对应的表示 arr 中各个数的概率，且满足 probability 各元素和不能超过 100；
    public static int getRandomNum(int[] arr, int[] probability){
    	//3,7  30,70
        if(arr.length != probability.length) return Integer.MIN_VALUE;
        Random ran = new Random();
        int ran_num = ran.nextInt(100);
        int temp = 0;
        for (int i = 0; i < arr.length; i++) {
            temp += probability[i];
            if(ran_num < temp)
                return arr[i];
        }
        return Integer.MIN_VALUE;
    }
}


