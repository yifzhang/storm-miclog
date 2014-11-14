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
 * 实现对访问日志的报警，业务逻辑比较简单，后续需要分析模型来实现较为负责的算法，
 * 比如爬虫轨迹，恶意访问等等
 * @author Chen
 *
 */
public class AppVisitAlarmSpout extends BaseRichSpout {
	private static final Logger LOGGER = Logger.getLogger(AppVisitAlarmSpout.class);
	private ResultSetMetaData m=null;
	private Map<String,String> mapEmailChargeA=new HashMap<String,String>();//主要负责人
	private ResultSet rsAlarmList;
	private ResultSet rsAlarmInfoList;
	private ResultSet rsAlarmInfoVisitList;
	private SpoutOutputCollector _collector;
	//更新发送邮件完毕的状态
	private static String updateFksAlarmInfo="update fks_applog set isalarm=1 where id={0}";
	//获得需要报警的邮件、手机列表
	private static String selectFksAlarmLit="select * from fks_alarmlist where allowalarm=1";
	@Override
	public void open(Map conf, TopologyContext context,SpoutOutputCollector collector) {
		this._collector=collector;
		try {
			rsAlarmList=MysqlUtils.select(selectFksAlarmLit);
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	@Override
	public void nextTuple() {
		try {
			Thread.sleep(3000);
			rsAlarmInfoList=MysqlUtils.select("select * from fks_applog where isalarm=0 order by id desc LIMIT 1");
			ResultSetMetaData md = rsAlarmInfoList.getMetaData();
			while (rsAlarmInfoList.next()) {
				String id=rsAlarmInfoList.getString("id");
				String projectName=rsAlarmInfoList.getString("projectname");
				String appName=rsAlarmInfoList.getString("appname");
				String location=rsAlarmInfoList.getString("location");
				String ip=rsAlarmInfoList.getString("ip");
				String statuscode=rsAlarmInfoList.getString("statuscode");
				String recordip=rsAlarmInfoList.getString("recordip");
				String refrereurl=rsAlarmInfoList.getString("refrereurl");
				String targeturl=rsAlarmInfoList.getString("targeturl");
				String logdate=rsAlarmInfoList.getString("logdate");
				MysqlUtils.update(StrUtils.format(updateFksAlarmInfo, id));
				_collector.emit(new Values(id,projectName,appName,location,ip,statuscode,recordip,refrereurl,targeturl,logdate));
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
		declarer.declare(new Fields("id","projectname","appname","location","ip","statuscode","recordip","refrereurl","targeturl","logdate"));
	}

}
