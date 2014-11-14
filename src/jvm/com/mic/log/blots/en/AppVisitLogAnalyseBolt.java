package com.mic.log.blots.en;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.mortbay.log.Log;

import com.mic.log.util.MysqlUtils;
import com.mic.log.util.PropertiesUtils;
import com.mic.log.util.TupleHelpers;

import backtype.storm.Config;
import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
/**
 * 访问日志的分析
 * @author Chen
 *
 */
public class AppVisitLogAnalyseBolt extends BaseRichBolt {
	private int _emitFrequencyInSeconds;
	private OutputCollector _collector;
	private int _iCount=0;//统计
	private String _isAllowCountTotal;
	public AppVisitLogAnalyseBolt(int emitFrequencyInSeconds )
	{
		this._emitFrequencyInSeconds=emitFrequencyInSeconds/1000;
	}
	@Override
	public void prepare(Map stormConf, TopologyContext context,OutputCollector collector) {
		this._collector=collector;
		this._isAllowCountTotal=PropertiesUtils.getValue("isAllowCountTotal");
	}
	/**
	 * 分析日志
	 * 定时发送到下个bolt
	 */
	@Override
	public void execute(Tuple input) {
		try {
			_iCount++;
			if (TupleHelpers.isTickTuple(input)) {
				_collector.emit("stop", new Values("flag"));//发送统计指令
				if(_isAllowCountTotal.equals("1"))
				{
					String Id2Time=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());//更新当前时间
					MysqlUtils.update("update fks_tuple set count=count+"+_iCount+",endtime='"+Id2Time+"' where id=2");
					_iCount=0;
				}
			}
			String streamId = input.getSourceStreamId();
			if (streamId.equals("log")) {
				  
				  String appname=input.getStringByField("appname");
				  String topicname=input.getStringByField("topicname");
				  String logcontent=input.getStringByField("logcontent");
				  String projectName=input.getStringByField("projectname");
				  String appName=input.getStringByField("appname");
				  String ip=input.getStringByField("ip");
				  String location=input.getStringByField("location");
				  String spoutTime=input.getStringByField("spouttime");
				  String remark="";
		          this._collector.emit("log",new Values(appName,topicname,logcontent,projectName,ip,location,spoutTime,remark));
		          //this._collector.ack(input);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declareStream("log",new Fields("appname","topicname","logcontent","projectname","ip","location","spouttime","remark"));
		declarer.declareStream("stop", new Fields("flag"));		
	}
	@Override
	public Map<String, Object> getComponentConfiguration() {
		//上线后在storm.yaml里面配置
		Map<String, Object> conf = new HashMap<String, Object>();
		conf.put(Config.TOPOLOGY_TICK_TUPLE_FREQ_SECS, _emitFrequencyInSeconds);
		return conf;
	}

}
