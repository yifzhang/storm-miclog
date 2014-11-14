package com.mic.log.service;


/*一个中间结果An是否能够合并，决定于下一个task在对An的处理过程中，
无论An是否是合并过的结果，都不影响此task处理An过程的正确性。
在“荧光”项目的开发中，我们设计出了一种CombiningBolt可以对开
发者透明地进行中间结果合并，只要开发者自己的中间结果类继承了如下的接口：*/
public interface Combinable {
	public String ident();

	public Combinable combine(Combinable another);

	public int getCombinedCount();
}
