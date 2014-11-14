package com.mic.log.blots.en;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.omg.CORBA.Current;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mic.log.constant.SContent;
import com.mic.log.spouts.AppVisitLogReaderSpout;
import com.mic.log.util.MysqlUtils;
import com.mic.log.util.PropertiesUtils;
import com.mic.log.util.storm.RegUtils;


import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

/**
 * 访问日志集中处理
 * 并按时处理
 * @author Chen
 *
 */
public class AppVisitLogStatBolt extends BaseRichBolt {
	public static Logger LOG = LoggerFactory.getLogger(AppVisitLogStatBolt.class);
	private OutputCollector _collector;
	private List<String> _lsExecuteSql;//需要执行的sql
	private int _logCount=0;
	private String _isAllowCountTotal;
	private String _sqlSave="";
	private String _startTime;
	@Override
	public void prepare(Map stormConf, TopologyContext context,
			OutputCollector collector) {
		this._collector = collector;
		this._lsExecuteSql=new ArrayList<String>();
		this._isAllowCountTotal=PropertiesUtils.getValue("isAllowCountTotal");
		this._startTime=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
	
	}
	@SuppressWarnings("all")
	@Override
	public void execute(Tuple input) {
		String streamId = input.getSourceStreamId();
		Map<String,String> _mapList=new HashMap<String,String>();
		Map<String, Integer> _pvMap = new HashMap<String, Integer>();
		if (streamId.equals("log")) {
			_logCount++;
			String logContent=input.getStringByField("logcontent");
			_mapList=RegUtils.getListByReg(logContent);
			String logDate=_mapList.get(SContent.TIME_LOCAL).substring(0,11);
			String projectName=input.getStringByField("projectname");
			String appName=input.getStringByField("appname");
			String ip=input.getStringByField("ip");
			String location=input.getStringByField("location");
			String appname = input.getStringByField("projectname")+"|"
							+input.getStringByField("appname")+"|"
							+input.getStringByField("ip")+"|"
							+input.getStringByField("location")+"|"
							+logDate;
			String createTime=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
			String isalarm=_mapList.get(SContent.STATUS).toString().substring(0, 2).equals("50")?"0":"1";//如果是50开始的则手机报警
			//String sqlSave="insert into fks_applog(projectname,appname,ip,location,statuscode,recordip,refrereurl,targeturl,logdate,remark,createtime,isalarm)";
			_sqlSave=_sqlSave+"('"+projectName+"','"+appName+"','"+ip+"','"+location+"','"+_mapList.get(SContent.STATUS)+"','"+_mapList.get(SContent.REMOTE_ADDR)+"','"+_mapList.get(SContent.HTTP_REFERER)+"','"+_mapList.get(SContent.REQUEST_URL)+"','"+logDate+"','"+"remark"+"','"+createTime+"','"+isalarm+"')";
			SContent.lsAlarmSql.add(_sqlSave);
			//LOG.info(_sqlSave);
			_sqlSave="";
			//_lsExecuteSql.add(sqlSave);
		}
		//如果接受到stop的命令，则发送到下个bolt，实现统一更新
		if (streamId.equals("stop")) {
			try {
				
				if(_logCount>0&&_isAllowCountTotal.equals("1"))
				{
					String Id3Time=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
					MysqlUtils.update("update fks_tuple set count=count+"+_logCount+",endtime='"+Id3Time+"' where id=3");
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			if(SContent.lsAlarmSql.size()>0)
			{
				_collector.emit(new Values(SContent.lsAlarmSql,String.valueOf(_logCount)));
			}
			_logCount=0;
		}
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("alarmSqlList","logcontent"));
	}

}
