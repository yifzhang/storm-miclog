package com.mic.log.blots;

import java.util.Map;

import com.sun.mail.util.logging.CollectorFormatter;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Tuple;

public class AppSendExToDbBolt extends BaseRichBolt {
	private OutputCollector _collCollector;
	@Override
	public void prepare(Map stormConf, TopologyContext context,
			OutputCollector collector) {
		this._collCollector=collector;
	}

	@Override
	public void execute(Tuple input) {
		
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		
	}

}
