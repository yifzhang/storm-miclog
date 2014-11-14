package com.mic.log;

import com.mic.log.blots.em.VisitErrorStatusBolt;
import com.mic.log.spouts.AppVisitAlarmSpout;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.InvalidTopologyException;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.tuple.Fields;
/**
 * 访问日志的报警fks_visitalarm
 * @author Chen
 */
public class AppVisitAlarmMain {

	/**
	 * Get data from db and send message and email for someone or group.
	 * @param args
	 * @throws InvalidTopologyException 
	 * @throws AlreadyAliveException 
	 */
	public static void main(String[] args) throws AlreadyAliveException, InvalidTopologyException {
		TopologyBuilder builder=new TopologyBuilder();
		builder.setSpout("Log-VisitAlarm-Spout", new AppVisitAlarmSpout(),10);
		//发送邮件
		builder.setBolt("Log-Alarm-VisitErrorStatusCode-Bolt",new VisitErrorStatusBolt(), 10)
						.shuffleGrouping("Log-VisitAlarm-Spout");
		Config conf=new Config();
		conf.setDebug(false);
		conf.setNumWorkers(4);
		if (args != null && args.length > 0) {
			StormSubmitter.submitTopology(args[0], conf,builder.createTopology());
		} else {
			conf.setMaxTaskParallelism(1);
			LocalCluster cluster = new LocalCluster();
			cluster.submitTopology("AlarmErrorStatusCode", conf,builder.createTopology());
		}
	}

}
