package com.mic.log.spouts;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.mic.log.util.MysqlUtils;
import com.mic.log.util.StrUtils;


import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;

/**
 * 定时轮询数据库需要发送邮件或者短信的报警消息
 * 访问日志
 * @author Chen
 *
 */
public class AppAlarmSpout extends BaseRichSpout {
	private static final Logger LOGGER = Logger.getLogger(AppAlarmSpout.class);
	private ResultSetMetaData m=null;//返回数据库结果的meta信息
	private Map<String,String> mapEmailChargeA=new HashMap<String,String>();//主要负责人
	private ResultSet rsAlarmList;//通知人列表
	private ResultSet rsAlarmInfoList;
	private SpoutOutputCollector _collector;
	private int FATAL=0;
	//更新已经发送通知的状态
	private static String updateFksAlarmInfo="update fks_alarminfo set isalarm=1 where id={0}";
	//读取需要报警的记录，默认一次只读取一条，理想的方式是把这些记录放在kafka里
	private static String selectFksAlarmInfo="select * from fks_alarminfo where isalarm=0 order by id desc LIMIT 1";
	//通知人的邮件、电话记录等
	private static String selectAlarmList="select * from fks_alarmlist where allowalarm=1";
	@Override
	public void open(Map conf, TopologyContext context,SpoutOutputCollector collector) {
		this._collector=collector;
		try {
			//获得通知人的列表
			rsAlarmList=MysqlUtils.select(selectAlarmList);
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}

	@Override
	public void nextTuple() {
		try {
			Thread.sleep(1000);
			rsAlarmInfoList=MysqlUtils.select(selectFksAlarmInfo);
			ResultSetMetaData md = rsAlarmInfoList.getMetaData();
			while (rsAlarmInfoList.next()) {
				String id=rsAlarmInfoList.getString("id");
				String projectName=rsAlarmInfoList.getString("projectname");//项目名称
				String appName=rsAlarmInfoList.getString("appname");//应用名称
				String location=rsAlarmInfoList.getString("location");//位置信息
				String ip=rsAlarmInfoList.getString("ip");//Ip地址
				String happentime=rsAlarmInfoList.getString("logtime");//日志时间
				String description=rsAlarmInfoList.getString("description");//描述
				String context=rsAlarmInfoList.getString("context");//上下文信息
				String level=rsAlarmInfoList.getString("level");//报警级别
				_collector.emit(level.toUpperCase(), new Values(id,projectName,appName,location,ip,happentime,description,context,level));
				//发完了就更新，不管有没有发送邮件或者短信成功。
				MysqlUtils.update(StrUtils.format(updateFksAlarmInfo, id));
			}
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static int random(int min, int max) {
		return (int) (Math.random() * (max + 1 - min) + min);
	}
	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declareStream("FATAL",new Fields("id","projectname","appname","location","ip","happentime","description","context","level"));
		declarer.declareStream("ERROR",new Fields("id","projectname","appname","location","ip","happentime","description","context","level"));
		declarer.declareStream("WARN",new Fields("id","projectname","appname","location","ip","happentime","description","context","level"));
		declarer.declareStream("INFO",new Fields("id","projectname","appname","location","ip","happentime","description","context","level"));
		declarer.declareStream("DEBUG",new Fields("id","projectname","appname","location","ip","happentime","description","context","level"));
	}

}
