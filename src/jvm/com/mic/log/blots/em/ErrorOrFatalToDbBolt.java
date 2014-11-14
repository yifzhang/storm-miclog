package com.mic.log.blots.em;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.apache.log4j.Logger;

import com.mic.log.util.MysqlUtils;
import com.mic.log.util.StrUtils;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Tuple;

/**
 * 实现对应用日志报警信息的记录到数据库
 * 然后通过另外一个AppLogAlarmMain实现
 * 对报警信息的报警（邮件+短信）
 * @version 1.0
 * @author Chen
 *
 */
public class ErrorOrFatalToDbBolt extends BaseRichBolt {
	private static final Logger LOG = Logger.getLogger(ErrorOrFatalToDbBolt.class);
	private OutputCollector _collector;
	@Override
	public void prepare(Map stormConf, TopologyContext context,
			OutputCollector collector) {
		this._collector=collector;
	}
	@Override
	public void execute(Tuple input) {
		String insertSql="";
		try {
			//Thread.sleep(2000);
			//获取Spout过来的值
			String level = input.getStringByField("level");//报警级别
			String projectName = input.getStringByField("projectname");//项目名称
			String appName = input.getStringByField("appname");//应用名称
			String location = input.getStringByField("location");//位置信息
			String ip = input.getStringByField("ip");//ip地址
			String logdate = input.getStringByField("logdate");//日志时间
			String threadname = input.getStringByField("threadname");//线程名称
			String classfullname = input.getStringByField("classfullname");//类全称
			String logexception = input.getStringByField("logexception");//日志异常信息
			String logexceptiondetail=input.getStringByField("logexceptiondetail");//日志异常详细信息
			String fullInfoException=logdate+"--"+threadname+"--"+classfullname+"--"+logexception;
			String createTime=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()).toString();
			insertSql="insert into fks_alarminfo(projectname,appname,location,ip,logtime,level,description,context,isalarm)values({0},{1},{2},{3},{4},{5},{6},{7},{8})";
			insertSql=StrUtils.format(insertSql, 
					"'"+projectName+"'","'"+appName+"'","'"
					   +location+"'","'"+ip+"'","'"+logdate+"'","'"
					   +level+"'","'"+fullInfoException.replaceAll("'","\\\\'")+"'","'"+logexceptiondetail.replaceAll("'","\\\\'")+"'","0");
		   //格式化SQL字符串，并且保存到数据库，让另外一个spout实现报警
			MysqlUtils.insert(insertSql);
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
		catch(Exception ex)
		{
			LOG.error(ex.getMessage());
			LOG.error(insertSql);
		}
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		
	}

}
