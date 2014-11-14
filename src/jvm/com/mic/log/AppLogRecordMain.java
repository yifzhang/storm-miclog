package com.mic.log;

import com.mic.log.blots.em.ErrorOrFatalToDbBolt;
import com.mic.log.spouts.AppEnReaderSpout;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.InvalidTopologyException;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.tuple.Fields;
/**
 * 处理应用日志,ERROR/FATAL/WARN
 * @author Chen
 *
 */
public class AppLogRecordMain {

	/**读取EN应用日志主控制台
	 * @param args
	 * @throws InvalidTopologyException 
	 * @throws AlreadyAliveException 
	 */
	public static void main(String[] args) throws AlreadyAliveException, InvalidTopologyException {
		TopologyBuilder builder=new TopologyBuilder();
		//定义读取kafka消息的spout
		builder.setSpout("Log-EN-AppMainSpout", new AppEnReaderSpout(),10);
		//如果符合报警的条件，则把数据和位置信息保存到数据库
		builder.setBolt("Log-SendEx-ToDb-Bolt",new ErrorOrFatalToDbBolt(), 10)
						.shuffleGrouping("Log-EN-AppMainSpout");
		
		Config conf=new Config();
		conf.setDebug(false);
		conf.setNumWorkers(2);
		// 集群模式
		if (args != null && args.length > 0) {
			StormSubmitter.submitTopology(args[0], conf,
					builder.createTopology());
		} else {
			// 本地模式
			conf.setMaxTaskParallelism(1);
			LocalCluster cluster = new LocalCluster();
			cluster.submitTopology("logAppEnRecord", conf,
					builder.createTopology());
		}
	}

}
