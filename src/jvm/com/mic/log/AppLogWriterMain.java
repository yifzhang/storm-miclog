package com.mic.log;

/**
 * 模拟写访问日志
 */
import com.mic.log.blots.AppVisitLogWriterBolt;
import com.mic.log.spouts.AppLogWriterSpout;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.InvalidTopologyException;
import backtype.storm.topology.TopologyBuilder;
/**
 * 
 * @author Chen
 *
 */
public class AppLogWriterMain{
	/**
	 * @param args
	 * @throws InvalidTopologyException 
	 * @throws AlreadyAliveException 
	 */
	public static void main(String[] args) throws AlreadyAliveException, InvalidTopologyException {
		TopologyBuilder builder=new TopologyBuilder();
		builder.setSpout("log-writer", new AppLogWriterSpout(),20);
		builder.setBolt("log-writer-file", new AppVisitLogWriterBolt(),20)
						.shuffleGrouping("log-writer");
		Config conf=new Config();
		conf.setNumWorkers(3);
		conf.setDebug(false);
		// 集群模式
		if (args != null && args.length > 0) {
			StormSubmitter.submitTopology(args[0], conf,
					builder.createTopology());
		} else {
			// 本地模式
			conf.setMaxTaskParallelism(1);
			LocalCluster cluster = new LocalCluster();
			cluster.submitTopology("logWriter", conf,builder.createTopology());
		}
	}

}
