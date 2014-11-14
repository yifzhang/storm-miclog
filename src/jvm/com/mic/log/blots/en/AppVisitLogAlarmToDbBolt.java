package com.mic.log.blots.en;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mic.log.constant.SContent;
import com.mic.log.spouts.AppVisitLogReaderSpout;
import com.mic.log.util.LogsWriteUtils;
import com.mic.log.util.MysqlUtils;
import com.mic.log.util.PropertiesUtils;
import com.mic.log.util.RandomUtils;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

/**
 * 这个是要保存到数据库，报警信息
 * 
 * @author Chen
 * 
 */
@SuppressWarnings("all")
public class AppVisitLogAlarmToDbBolt extends BaseRichBolt {
	public static Logger LOG = LoggerFactory.getLogger(AppVisitLogAlarmToDbBolt.class);
	private Map<String, HashMap<String, Integer>> _rMap = new HashMap<String, HashMap<String, Integer>>();
	private Map<String, Integer> _iMap = new HashMap<String, Integer>();
	private int _count = 0;
	private String _isAllowCountTotal;
	private String _startTime;
	private String _sql;//批量执行batch的
	private String _value="";//批量执行的value
	@Override
	public void prepare(Map stormConf, TopologyContext context,
			OutputCollector collector) {
		this._startTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		this._sql="insert into fks_applog(projectname,appname,ip,location,statuscode,recordip,refrereurl,targeturl,logdate,remark,createtime,isalarm)values";
		this._isAllowCountTotal = PropertiesUtils.getValue("isAllowCountTotal");
	}

	@Override
	public void execute(Tuple input) {
		try {
			
			Set<String> alarmSqlSet = (HashSet<String>) input.getValueByField("alarmSqlList");
			String logCount = input.getStringByField("logcontent");
			_count=_count+alarmSqlSet.size();
			for (String value : alarmSqlSet) {
				_value+=value+",";
			}
			if(!_value.equals(""))
			{
				_sql=_sql+_value.substring(0, _value.length()-1);
				MysqlUtils.insert(_sql);
				LOG.info(_sql);
			}
			SContent.lsAlarmSql.clear();// 清除
			if (_isAllowCountTotal.equals("1")) {
				String Id4Time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
				MysqlUtils.update("update fks_tuple set count=count+" + _count+ ",endtime='" + Id4Time + "' where id=4");
				_count = 0;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
	}
}
