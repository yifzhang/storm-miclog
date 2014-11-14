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
import com.mic.log.util.sms.Call;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Tuple;
/**
 * 致命错误的报警，应用日志
 * @author Chen
 *
 */
public class FatalBolt extends BaseRichBolt {
	
	private static final Logger LOGGER = Logger.getLogger(FatalBolt.class);
	private OutputCollector _collector;
	private static String _emailTitle = "";
	private Map<String, Map<String, String>> _alarmList;
	//第一负责人
	private String _alarmMailChargeA = "";
	//管理员，这个一般是上线的初期需要监控邮件的开发者
	private String _alarmAdministrator="";
	//得到报警的邮件列表及其手机号码
	private static String _getAlaramList="select * from fks_alarmlist where allowalarm=1";
	private int _count = 0;
	private String _smsUserName="";
	private String _smsPassWord="";
	private String _smsType="";
	private String _smsExt="414";
	@Override
	public void prepare(Map stormConf, TopologyContext context,
			OutputCollector collector) {	
		this._collector = collector;
		this._emailTitle = PropertiesUtils.getValue("log.alarm.title1");
		this._alarmMailChargeA = PropertiesUtils.getValue("mail.SMTPUsername");
		this._alarmAdministrator=PropertiesUtils.getValue("log.alarm.administrator");
		this._smsPassWord=PropertiesUtils.getValue("smsPassWord");
		this._smsUserName=PropertiesUtils.getValue("smsUserName");
		this._smsType=PropertiesUtils.getValue("smsType");
		this._smsExt=PropertiesUtils.getValue("smsExt");
		try {
			//获取需要报警的邮件列表及其店话
			this._alarmList = MailGetList.resultSetToArrayList(MysqlUtils.select(_getAlaramList));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void execute(Tuple input) {
		try {
			String level = input.getSourceStreamId();//错误代码的级别
			String id = input.getStringByField("id");//程序id
			String projectName = input.getStringByField("projectname");//项目名称
			String appName = input.getStringByField("appname");//应用名称
			String location = input.getStringByField("location");//位置信息
			String ip = input.getStringByField("ip");//ip地址
			String happentime = input.getStringByField("happentime");//发生时间
			String description = input.getStringByField("description");//描述
			String context = input.getStringByField("context");//上下文信息
			String alarmTitle=StrUtils.format(_emailTitle, level, projectName,appName);
			String mailContent = StrUtils.format(PropertiesUtils.getValue("log.alarm.content1"), projectName, appName,level, new SimpleDateFormat("dd/MMM/yyyy:HH:mm:ss Z",Locale.ENGLISH).format(new Date()), location, ip,description);
			final MailMessage mail = new MailMessage(alarmTitle,
					_alarmMailChargeA,//邮件持有者
					_alarmList.get(appName).get("receiveEmailList").toString().split(","),// tos发送人
					_alarmList.get(appName).get("ccEmailList").toString().split(","),// ccs抄送人
					new String[] {_alarmAdministrator},// bccs按抄
					mailContent, new String[] {});//邮件内容
			String[] paraArray=new String[10];
			paraArray[0]=_smsUserName;
			paraArray[1]=_smsPassWord;
			paraArray[2]=_alarmList.get(appName).get("phonenumber");
			paraArray[3]=_smsType;
			paraArray[4]="";
			paraArray[5]=alarmTitle;
			paraArray[6]=null;
			paraArray[7]=_smsExt;
			paraArray[8]="";
			paraArray[9]="";
			String Rv=Call.SendSms(paraArray);//发送手机短信
			LOGGER.info("Send Sms;Serial Number is:"+(++_count)+"_Level:FATAL");//记录短信
			EmailSendTask.sendMail(mail);//发送邮件
			LOGGER.info("Send Email;Serial Number is:"+(++_count)+"_Level:FATAL");//记录邮件
		} catch (Exception ex) {
			LOGGER.info("FATAL_Bolt:" + ex.toString());
		}
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {

	}

}
