package com.mic.log.blots.em;

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
 * 应用日志打印ERROR实现邮件通知
 * @author Chen
 *
 */
public class ErrorBolt extends BaseRichBolt {
	private static final Logger LOGGER = Logger.getLogger(FatalBolt.class);
	private OutputCollector _collector;
	private static String _emailTitle = "";
	private Map<String, Map<String, String>> _alarmList;
	//项目第一负责人
	private String _alarmMailChargeA = "";
	//管理员，这个一般是上线的初期需要监控邮件的开发者
	private String _alarmAdministrator="";
	//得到报警的邮件列表及其手机号码
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
			String level = input.getSourceStreamId();//错误代码的级别
			String id = input.getStringByField("id");//id
			String projectName = input.getStringByField("projectname");//项目名称
			String appName = input.getStringByField("appname");//应用名称
			String location = input.getStringByField("location");//机器位置
			String ip = input.getStringByField("ip");//ip地址
			String happentime = input.getStringByField("happentime");//发生时间
			String description = input.getStringByField("description");//描述
			String context = input.getStringByField("context");
			//邮件内容
			String mailContent = StrUtils.format(
					PropertiesUtils.getValue("log.alarm.content1"), 
					projectName, appName,level, 
					new SimpleDateFormat("dd/MMM/yyyy:HH:mm:ss Z",Locale.ENGLISH).format(new Date()), 
					location, ip,description, context);
			//定义发送邮件的主题
			final MailMessage mail = new MailMessage(StrUtils.format(_emailTitle, level, projectName,appName),
					_alarmMailChargeA,
					_alarmList.get(appName).get("receiveEmailList").toString().split(","),// tos
					_alarmList.get(appName).get("ccEmailList").toString().split(","),// ccs
					new String[] {_alarmAdministrator},// bccs
					mailContent, new String[] {});
			EmailSendTask.sendMail(mail);//发送邮件
			LOGGER.info("Send Email;Serial Number is:"+(++_count)+"_Level:ERROR");
		} catch (Exception ex) {
			LOGGER.info("ERROR_Bolt:" + ex.toString());
		}	
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		
	}
}
