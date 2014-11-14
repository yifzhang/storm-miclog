package com.mic.log.spouts;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

import com.mic.log.util.LogUtils;
import com.mic.log.util.MysqlUtils;
import com.mic.log.util.PropertiesUtils;
import com.mic.log.util.StrUtils;
import com.mic.log.util.storm.RegUtils;

import backtype.storm.Config;
import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;

@SuppressWarnings("all")
/**
 * 处理应用日志的报警spout
 * @author Chen
 *
 */
public class AppEnReaderSpout extends BaseRichSpout {
	public static Logger LOG = LoggerFactory.getLogger(AppEnReaderSpout.class);
	public static String topicName = "";
	private SpoutOutputCollector _collector;
	private ConsumerIterator<byte[], byte[]> it = null;
	private Map<String, String> contentMap = null;
	private String[] result;
	private String logDate = "";
	private Map<String, Map<String, String>> mapAlarmInfo;//需要报警的邮件列表
	private long _lastTime;
	private long _currentTime;
	private List<String> _lsExceptionDetail;//异常欣喜
	private boolean _isNeedWait=false;//是否需要等待异常信息
	
	
	private Map<String, Map<String, String>> mapContent;
	private Map<String, String> mapLocation;
	
	private boolean _isFirstAlarm;//是否是报警信息，有的情况是有异常信息打印，但是上面报警的主日志
	private boolean _isNormal=false;//接受到正常日志为止
	private boolean _isInfoDebug=false;
	private String logdate;
	private String loglevel;
	private String threadname;
	private String classfullname;
	private String logexception;
	private String projectname;
	private String appname;
	private String ip;
	private String location;
	public void open(Map conf, TopologyContext context,
			SpoutOutputCollector collector) {
		this._collector = collector;
		this._lastTime = System.currentTimeMillis(); //初始化异常信息处理的时间戳
		this._lsExceptionDetail=new ArrayList<String>();
		this._isFirstAlarm=false;
		
		
		topicName = PropertiesUtils.getValue("topic-App");// 获得要处理的topic名称，在config.properties文件里
		Properties props = new Properties();
		props.put("zookeeper.connect",
				PropertiesUtils.getValue("zookeeper.connect"));// zookeeper的连接字符串
		props.put("group.id", PropertiesUtils.getValue("group-app"));// 消费topic消息的组名称
		// 指定kafka等待多久zookeeper回复（ms）以便放弃并继续消费。
		props.put("zookeeper.session.timeout.ms",
				PropertiesUtils.getValue("zookeeper.session.timeout.ms"));
		// 指定zookeeper同步最长延迟多久再产生异常
		props.put("zookeeper.sync.time.ms",
				PropertiesUtils.getValue("zookeeper.sync.time.ms"));
		// 指定多久消费者更新offset到zookeeper中。
		// 注意offset更新时基于time而不是每次获得的消息。
		// 一旦在更新zookeeper发生异常并重启，将可能拿到已拿到过的消息
		props.put("auto.commit.interval.ms",
				PropertiesUtils.getValue("auto.commit.interval.ms"));

		ConsumerConfig consumerConfig = new ConsumerConfig(props);// 消费者配置文件
		ConsumerConnector consumer = Consumer
				.createJavaConsumerConnector(consumerConfig);
		Map topicCountMap = new HashMap();
		// 我们要告诉kafka该进程会有多少个线程来处理对应的topic
		topicCountMap.put(topicName, new Integer(1));
		Map consumerMap = consumer.createMessageStreams(topicCountMap);
		KafkaStream stream = (KafkaStream) ((List) consumerMap.get(topicName))
				.get(0);// 读取kafka stream流消息
		this.it = stream.iterator();
	}

	@Override
	public void nextTuple() {
		try {
			if (this.it.hasNext()) {
				String msg = new String((byte[]) this.it.next().message());// 读到一条消息，转换为字符串
				Map<String, String> exceptionLog = new HashMap<String, String>();
				// 如果消息是以20开头，说明应该是一条报警消息，不是错误异常，以为日志的格式都是定义统一的
				if (msg.substring(1, 3).toString().equals("20")) {
					_isNormal=true;
					if(_lsExceptionDetail.size()>0)
					{
						LOG.info(StrUtils.format("日志警报信息：" + "日志日期:{0}---"
								+ "线程名称:{1}---" + "日志级别:{2}---" + "类名全称:{3}---"
								+ "异常信息:{4}---" + "项目名称:{5}---" + "应用名称:{6}---"
								+ "IP地址：{7}---" + "机房地址:{8}---"+"异常信息:{9}", logdate,
								threadname, loglevel, classfullname, logexception,
								projectname, appname, ip, location,_lsExceptionDetail.toString()));
						_collector.emit(new Values(projectname, appname,location, ip, logdate, threadname, loglevel,classfullname, logexception,_lsExceptionDetail.toString()));
						_lsExceptionDetail.clear();
						_isNeedWait=false;//不需要等待了，重新检测日志
						_isFirstAlarm=false;//发送完异常主日志和异常信息后，重新设置为false
					}
					// 处理该条消息，并转换为map，方便取值
					mapContent = RegUtils.dealAppLog(msg);
					//String level = mapContent.get("logInfo").get("logLevel");// 获得报警的级别，如ERROR,FATAL,WARN等

					mapLocation = LogUtils.splitLog(msg);
					logdate = mapContent.get("logInfo").get("logDate");//日志时间
					threadname = mapContent.get("logInfo").get("threadName");//线程名称
					loglevel = mapContent.get("logInfo").get("logLevel");//日志界别
					classfullname = mapContent.get("logInfo").get("classFullName");//类全程
					logexception = mapContent.get("logInfo").get("logException");//日志异常
					projectname = mapLocation.get("projectname");//项目名称
					appname = mapLocation.get("appname");//应用名称
					ip = mapLocation.get("ip");//ip地址
					location = mapLocation.get("location");//机房信息
					
					// 如果符合WARN,ERROR,FATAL级别，则需要记录
					// WARN:不报警;但是信息要保存到数据库
					// ERROR:邮件通知
					// FATAL:邮件+短信
					if (loglevel.toUpperCase().toString().equals("ERROR")|| loglevel.toUpperCase().toString().equals("FATAL")) {
						// 返回错误日志的map
						exceptionLog.put("mainExceptionInfo",mapContent.get("logInfo").toString());
						// 返回错误日志的位置信息
						exceptionLog.put("locationInfo",mapContent.get("locationInfo").toString());
						_isNeedWait=true;//如果异常级别是ERROR或者FATAL,下面可能有异常信息
						_isFirstAlarm=true;
						_isInfoDebug=false;
					}
					else
					{
						_isNeedWait=false;//正常;下一条非异常详情的日志，不要等待了
						_isInfoDebug=true;
					}
				}
				else
				{
					_isNormal=false;//可能是异常日志
					if(_isFirstAlarm)
					{
						_lsExceptionDetail.add((LogUtils.getLogContent(msg).toString().replace("[", ""))+"<br/>");
					}
					
				}
				long _currentTime = System.currentTimeMillis();
				//需要等待详细的异常信息
				//或者等待4秒钟
				//并且是正常的日志接受情况，有一种情况是重新启动后，消息系统里有不合法信息
			/*	if(_isNormal&&!_isNeedWait)
					{
											LOG.info(StrUtils.format("日志警报信息：" + "日志日期:{0}---"
													+ "线程名称:{1}---" + "日志级别:{2}---" + "类名全称:{3}---"
													+ "异常信息:{4}---" + "项目名称:{5}---" + "应用名称:{6}---"
													+ "IP地址：{7}---" + "机房地址:{8}---"+"异常信息:{9}", logdate,
													threadname, loglevel, classfullname, logexception,
													projectname, appname, ip, location,_lsExceptionDetail.toString()));
											_collector.emit(new Values(projectname, appname,location, ip, logdate, threadname, loglevel,classfullname, logexception));
											_lsExceptionDetail.clear();
											_lastTime = _currentTime;//等待4秒
											_isNeedWait=false;//不需要等待了，重新检测日志
											_isFirstAlarm=false;//发送完异常主日志和异常信息后，重新设置为false
					}*/
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		// 定义输出列的定义
		// 项目名称，应用名称，位置，Ip地址，日志时间，线程名称，报警级别，类全程，异常信息
		declarer.declare(new Fields("projectname", "appname", "location", "ip",
				"logdate", "threadname", "level", "classfullname",
				"logexception","logexceptiondetail"));

	}
}
