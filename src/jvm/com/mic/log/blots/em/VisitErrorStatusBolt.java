package com.mic.log.blots.em;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import org.apache.log4j.Logger;

import com.mic.log.util.MysqlUtils;
import com.mic.log.util.PropertiesUtils;
import com.mic.log.util.StrUtils;
import com.mic.log.util.email.EmailSendTask;
import com.mic.log.util.email.MailGetList;
import com.mic.log.util.email.MailMessage;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Tuple;

/**
 * 发送邮件的通知者，通知访问日志的异常情况
 * @author Chen
 *
 */
public class VisitErrorStatusBolt extends BaseRichBolt {
	
	private static final Logger LOGGER = Logger.getLogger(VisitErrorStatusBolt.class);
	private OutputCollector _collector;
	private static String _emailTitle = "";
	private Map<String, String> _alarmList;
	//邮件通知人
	private String _alarmMailChargeA = "";
	//管理员--一般是开发者需要监控的
	private String _alarmAdministrator="";
	private static String _getAlaramList="select * from fks_visitalarm where allowalarm=1 limit 1";
	private int _count = 0;
  
	@Override
	public void prepare(Map stormConf, TopologyContext context,
			OutputCollector collector) {	
		this._collector = collector;
		this._emailTitle = PropertiesUtils.getValue("log.alarm.title2");
		this._alarmMailChargeA = PropertiesUtils.getValue("mail.SMTPUsername");
		this._alarmAdministrator=PropertiesUtils.getValue("log.alarm.administrator");
		try {
			this._alarmList = MailGetList.visitAlarmToArrayList(MysqlUtils.select(_getAlaramList));
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
			String statuscode = input.getStringByField("statuscode");
			String recordip = input.getStringByField("recordip");
			String refrereurl = input.getStringByField("refrereurl");
			String targeturl = input.getStringByField("targeturl");
			String logdate = input.getStringByField("logdate");
			
			String mailContent = StrUtils.format(PropertiesUtils.getValue("log.alarm.content2"), projectName, appName,statuscode,logdate, location, ip,statuscode,recordip,refrereurl,targeturl,logdate);
			final MailMessage mail = new MailMessage(StrUtils.format(_emailTitle, statuscode, projectName,appName),
					_alarmMailChargeA,
					_alarmList.get("tos").toString().split(","),// tos
					_alarmList.get("ccs").toString().split(","),// ccs
					new String[] {_alarmList.get("bcs").toString()},// bccs
					mailContent, new String[] {});
			EmailSendTask.sendMail(mail);
			LOGGER.info("Send Email for visiting log Serial Number is:"+(++_count)+"_Level:VisitErrorStatusBolt");
		} catch (Exception ex) {
			LOGGER.info("VisitError_Bolt:" + ex.toString());
		}
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {

	}

}
