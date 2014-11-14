package com.mic.log.blots.em;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import org.apache.log4j.Logger;

import com.mic.log.util.MysqlUtils;
import com.mic.log.util.PropertiesUtils;
import com.mic.log.util.email.MailGetList;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Tuple;

public class WarnBolt extends BaseRichBolt {
	private static final Logger LOGGER = Logger.getLogger(FatalBolt.class);
	private OutputCollector _collector;
	private static String _emailTitle = "";
	private Map<String, Map<String, String>> _alarmList;
	private String _alarmMailChargeA = "";
	private String _alarmAdministrator="";
	private static String _getAlaramList="select * from fks_alarmlist where allowalarm=1";
	private int _count = 0;
  
	@Override
	public void prepare(Map stormConf, TopologyContext context,
			OutputCollector collector) {	
		this._collector = collector;
		this._emailTitle = PropertiesUtils.getValue("log.alarm.title1");
		this._alarmMailChargeA = PropertiesUtils.getValue("mail.SMTPUsername");
		this._alarmAdministrator=PropertiesUtils.getValue("log.alarm.administrator");
		try {
			this._alarmList = MailGetList.resultSetToArrayList(MysqlUtils.select(_getAlaramList));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void execute(Tuple input) {
		try {
			String level = input.getSourceStreamId();
			String id = input.getStringByField("id");
			String projectName = input.getStringByField("projectname");
			String appName = input.getStringByField("appname");
			String location = input.getStringByField("location");
			String ip = input.getStringByField("ip");
			String happentime = input.getStringByField("happentime");
			String description = input.getStringByField("description");
			String context = input.getStringByField("context");
		} catch (Exception ex) {
			LOGGER.info("WARN_BOLT:" + ex.toString());
		}
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {

	}


}
