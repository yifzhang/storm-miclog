package com.mic.log.spouts;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.esotericsoftware.minlog.Log;
import com.mic.log.constant.SContent;
import com.mic.log.util.LogUtils;
import com.mic.log.util.MysqlUtils;
import com.mic.log.util.PropertiesUtils;
import com.mic.log.util.TupleHelpers;
import com.mic.log.util.storm.RegUtils;


import backtype.storm.Config;
import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
@SuppressWarnings("all")
public class AppVisitLogReaderSpout extends BaseRichSpout {
	private static final long serialVersionUID = -818025713344772096L;
	public static Logger LOG = LoggerFactory.getLogger(AppVisitLogReaderSpout.class);
	public static String _topicName = "";
	private SpoutOutputCollector _collector;
	private ConsumerIterator<byte[], byte[]> it = null;
	private Map<String, String> _contentMap = null;
	private String[] _result;
	private String _logDate="";
	private int _iCount=0;//统计接受到的tuple
	private int _excuteCyle=10000;//每多少毫秒执行一次数据更新接受的tuple总数
	private long _lastTime;
	private long _currentTime;
	private String _isAllowCountTotal;//是否允许统计接受tuple的总数到数据库
	private int _x50=0;
	public void open(Map conf, TopologyContext context,
			SpoutOutputCollector collector) {
		this._collector = collector;
		_topicName=PropertiesUtils.getValue("topic-visit");//获得要处理的topic名称，在config.properties文件里
		_excuteCyle=PropertiesUtils.getIntValue("itupleExcuteTime");//
		_lastTime = System.currentTimeMillis(); //上次批量处理的时间戳
		_isAllowCountTotal=PropertiesUtils.getValue("isAllowCountTotal");
		
		Properties props=new Properties();
		props.put("zookeeper.connect",PropertiesUtils.getValue("zookeeper.connect"));//zookeeper的连接字符串
		props.put("group.id", PropertiesUtils.getValue("group-visit"));//消费topic消息的组名称
		// 指定kafka等待多久zookeeper回复（ms）以便放弃并继续消费。
		props.put("zookeeper.session.timeout.ms",PropertiesUtils.getValue("zookeeper.session.timeout.ms"));
		// 指定zookeeper同步最长延迟多久再产生异常
		props.put("zookeeper.sync.time.ms",PropertiesUtils.getValue("zookeeper.sync.time.ms"));
		// 指定多久消费者更新offset到zookeeper中。
		//注意offset更新时基于time而不是每次获得的消息。
		//一旦在更新zookeeper发生异常并重启，将可能拿到已拿到过的消息
		props.put("auto.commit.interval.ms",PropertiesUtils.getValue("auto.commit.interval.ms"));
		
		
		ConsumerConfig consumerConfig = new ConsumerConfig(props);//消费者配置文件
		ConsumerConnector consumer = Consumer.createJavaConsumerConnector(consumerConfig);
		Map topicCountMap = new HashMap();
		// 我们要告诉kafka该进程会有多少个线程来处理对应的topic
		topicCountMap.put(_topicName, new Integer(1));
		Map consumerMap = consumer.createMessageStreams(topicCountMap);
		KafkaStream stream = (KafkaStream) ((List) consumerMap.get(_topicName)).get(0);//读取kafka stream流消息
		this.it = stream.iterator();
	}
	@Override
	public void nextTuple() {
		try {
			if (this.it.hasNext()) {//读取kafka消息流		
				//转换流消息
				String msg = new String((byte[]) this.it.next().message());
				_contentMap = LogUtils.splitLog(msg);
				//分组
				_result=msg.split("\\]\\[\\{");
				String statusCode=RegUtils.getListByReg(_result[0]).get(SContent.STATUS);
				//如果状态码是50开头，或者是404的就需要报警
				//这样存在一个情况，如果是用户故意在URL里输入错误url就会报错404
				//所以404就不手机短信报警了，完全没有必要
				if(statusCode.toString().substring(0,2).equals("50")||statusCode.toString().substring(0,2).equals("404"))//服务端
				{
					String Id5Time=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
					//得到日期
					_logDate=LogUtils.getDate(_result[0], "\\[.*\\]");
					_collector.emit("log", new Values(
							_result[0],//日志内容
							_topicName,//kafka的topic的名称
							_contentMap.get("projectname"),//项目名称
							_contentMap.get("appname"),//应用名称
							_contentMap.get("location"),//位置信息
							_contentMap.get("ip"),//ip地址
							new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())
							));
					_x50++;
					//LOG.info(msg);
				}
				String Id1Time=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
				long currentTime = System.currentTimeMillis();
				_iCount++;
				if(_isAllowCountTotal.equals("1")&&currentTime >= _lastTime + _excuteCyle)
				{
					//统计总共接受tuple的数量；上线稳定后建议去掉
					MysqlUtils.update("update fks_tuple set count=count+"+_iCount+",endtime='"+Id1Time+"' where id=1");
					MysqlUtils.update("update fks_tuple set count=count+"+(_x50++)+",endtime='"+Id1Time+"' where id=5");
					_lastTime = currentTime;
					_iCount=0;
					_x50=0;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		//定义输出字段
		declarer.declareStream("log", new Fields("logcontent", "topicname", "projectname","appname","location","ip","spouttime"));
	}
/*	@Override
	public Map<String, Object> getComponentConfiguration() {
		Map<String, Object> conf = new HashMap<String, Object>();
		conf.put(Config.TOPOLOGY_TICK_TUPLE_FREQ_SECS, _excuteCyle);
		return conf;
	}*/
}
