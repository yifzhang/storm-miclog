package com.mic.log.util.email;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
 
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
 
import org.apache.log4j.Logger;

import com.mic.log.util.PropertiesUtils;
import com.mic.log.util.StrUtils;

 
public class MailUtils {
     
    private static final Logger LOGGER = Logger.getLogger(MailUtils.class);
     
    private static String SMTPServer;
    private static String SMTPUsername;
    private static String SMTPPassword;
    private static String POP3Server;
    private static String POP3Username;
    private static String POP3Password;
     
    static {
        loadConfigProperties();
    }

    public static void main(String[] args) throws Exception {
        //发送邮件
    	StringBuilder sb=new StringBuilder();
    	sb.append("Caused by: org.apache.zookeeper.KeeperException$ConnectionLossException: KeeperErrorCode = ConnectionLoss for /supervisors");
    	sb.append("at org.apache.zookeeper.KeeperException.create(KeeperException.java:99) ~[zookeeper-3.4.5.jar:3.4.5-1392090]");
    	sb.append("at org.apache.zookeeper.KeeperException.create(KeeperException.java:51) ~[zookeeper-3.4.5.jar:3.4.5-1392090]");
    	sb.append("at org.apache.zookeeper.ZooKeeper.exists(ZooKeeper.java:1041) ~[zookeeper-3.4.5.jar:3.4.5-1392090]");
    	sb.append("at org.apache.curator.framework.imps.ExistsBuilderImpl$2.call(ExistsBuilderImpl.java:172) ~[curator-framework-2.4.0.jar:na]");
    	sb.append("at org.apache.curator.framework.imps.ExistsBuilderImpl$2.call(ExistsBuilderImpl.java:161) ~[curator-framework-2.4.0.jar:na]");
    	sb.append("at org.apache.curator.RetryLoop.callWithRetry(RetryLoop.java:107) ~[curator-client-2.4.0.jar:na]");
    	sb.append("at org.apache.curator.framework.imps.ExistsBuilderImpl.pathInForeground(ExistsBuilderImpl.java:157) ~[curator-framework-2.4.0.jar:na]");
		sb.append("at org.apache.curator.framework.imps.ExistsBuilderImpl.forPath(ExistsBuilderImpl.java:148) ~[curator-framework-2.4.0.jar:na]");
		sb.append("at org.apache.curator.framework.imps.ExistsBuilderImpl.forPath(ExistsBuilderImpl.java:36) ~[curator-framework-2.4.0.jar:na]");
		sb.append("at backtype.storm.zookeeper$exists_node_QMARK_$fn__1153.invoke(zookeeper.clj:101) ~[storm-core-0.9.2-incubating.jar:0.9.2-incubating]");
		sb.append("... 15 common frames omitted");
		StringBuilder sbContext=new StringBuilder();
		sbContext.append("2014-10-28 21:53:13 o.a.z.ClientCnxn [INFO] Opening socket connection to server vm132/192.168.128.132:2181. Will not attempt to authenticate using SASL (unknown error)");
		sbContext.append("2014-10-28 21:53:13 o.a.z.ClientCnxn [INFO] Socket connection established to vm132/192.168.128.132:2181, initiating session");
		sbContext.append("2014-10-28 21:53:13 o.a.z.ClientCnxn [INFO] Unable to read additional data from server sessionid 0x0, likely server has closed socket, closing socket connection and attempting reconnect");
		sbContext.append("2014-10-28 21:53:13 b.s.util [INFO] Halting process: (\"Error when processing an event\")");
		sbContext.append("2014-10-28 22:42:59 o.a.z.ZooKeeper [INFO] Client environment:zookeeper.version=3.4.5-1392090, built on 09/30/2012 17:52 GMT");
		sbContext.append("2014-10-28 22:42:59 o.a.z.ZooKeeper [INFO] Client environment:host.name=vm132");
		sbContext.append("2014-10-28 22:42:59 o.a.z.ZooKeeper [INFO] Client environment:java.version=1.7.0_60");
		sbContext.append("2014-10-28 22:42:59 o.a.z.ZooKeeper [INFO] Client environment:java.vendor=Oracle Corporation");
		sbContext.append("2014-10-28 22:42:59 o.a.z.ZooKeeper [INFO] Client environment:java.home=/usr/java/jdk1.7.0_60/jre");
		sbContext.append("2014-10-28 22:42:59 o.a.z.ZooKeeper [INFO] Client environment:java.class.path=/usr/local/storm-0.9.2/lib/kryo-2.21.jar:/usr/local/storm-0.9.2/lib/core.incubator-0.1.0.jar:/usr/local/storm-0.9.2/lib/commons-io-2.4.jar:/usr/local/storm-0.9.2/lib/jgrapht-core-0.9.0.jar:/usr/local/storm-0.9.2/lib/netty-3.6.3.Final.jar:/usr/local/storm-0.9.2/lib/jetty-6.1.26.jar:/usr/local/storm-0.9.2/lib/netty-3.2.2.Final.jar:/usr/local/storm-0.9.2/lib/minlog-1.2.jar:/usr/local/storm-0.9.2/lib/kafka_2.9.2-0.8.0-beta1.jar:/usr/local/storm-0.9.2/lib/chill-java-0.3.5.jar:/usr/local/storm-0.9.2/lib/disruptor-2.10.1.jar:/usr/local/storm-0.9.2/lib/curator-client-2.4.0.jar:/usr/local/storm-0.9.2/lib/guava-13.0.jar:/usr/local/storm-0.9.2/lib/zookeeper-3.4.5.jar:/usr/local/storm-0.9.2/lib/commons-lang-2.5.jar:/usr/local/storm-0.9.2/lib/storm-core-0.9.2-incubating.jar:/usr/local/storm-0.9.2/lib/servlet-api-2.5.jar:/usr/local/storm-0.9.2/lib/commons-fileupload-1.2.1.jar:/usr/local/storm-0.9.2/lib/carbonite-1.4.0.jar:/usr/local/storm-0.9.2/lib/logback-core-1.0.6.jar:/usr/local/storm-0.9.2/lib/metrics-core-2.2.0.jar:/usr/local/storm-0.9.2/lib/reflectasm-1.07-shaded.jar:/usr/local/storm-0.9.2/lib/clj-time-0.4.1.jar:/usr/local/storm-0.9.2/lib/logback-classic-1.0.6.jar:/usr/local/storm-0.9.2/lib/objenesis-1.2.jar:/usr/local/storm-0.9.2/lib/hiccup-0.3.6.jar:/usr/local/storm-0.9.2/lib/ring-jetty-adapter-0.3.11.jar:/usr/local/storm-0.9.2/lib/tools.cli-0.2.4.jar:/usr/local/storm-0.9.2/lib/asm-4.0.jar:/usr/local/storm-0.9.2/lib/joda-time-2.0.jar:/usr/local/storm-0.9.2/lib/snakeyaml-1.11.jar:/usr/local/storm-0.9.2/lib/slf4j-api-1.6.5.jar:/usr/local/storm-0.9.2/lib/clj-stacktrace-0.2.4.jar:/usr/local/storm-0.9.2/lib/mysql-connector-java-5.1.26.jar:/usr/local/storm-0.9.2/lib/compojure-1.1.3.jar:/usr/local/storm-0.9.2/lib/clojure-1.5.1.jar:/usr/local/storm-0.9.2/lib/ring-devel-0.3.11.jar:/usr/local/storm-0.9.2/lib/commons-codec-1.6.jar:/usr/local/storm-0.9.2/lib/commons-logging-1.1.3.jar:/usr/local/storm-0.9.2/lib/httpclient-4.3.3.jar:/usr/local/storm-0.9.2/lib/log4j-over-slf4j-1.6.6.jar:/usr/local/storm-0.9.2/lib/math.numeric-tower-0.0.1.jar:/usr/local/storm-0.9.2/lib/tools.macro-0.1.0.jar:/usr/local/storm-0.9.2/lib/tools.logging-0.2.3.jar:/usr/local/storm-0.9.2/lib/jline-2.11.jar:/usr/local/storm-0.9.2/lib/json-simple-1.1.jar:/usr/local/storm-0.9.2/lib/jetty-util-6.1.26.jar:/usr/local/storm-0.9.2/lib/servlet-api-2.5-20081211.jar:/usr/local/storm-0.9.2/lib/httpcore-4.3.2.jar:/usr/local/storm-0.9.2/lib/curator-framework-2.4.0.jar:/usr/local/storm-0.9.2/lib/zkclient-0.3.jar:/usr/local/storm-0.9.2/lib/scala-library-2.9.2.jar:/usr/local/storm-0.9.2/lib/ring-servlet-0.3.11.jar:/usr/local/storm-0.9.2/lib/ring-core-1.1.5.jar:/usr/local/storm-0.9.2/lib/commons-exec-1.1.jar:/usr/local/storm-0.9.2/lib/clout-1.0.1.jar:/usr/local/storm-0.9.2/conf");
		sbContext.append("2014-10-28 22:42:59 o.a.z.ZooKeeper [INFO] Client environment:java.library.path=/usr/local/lib:/opt/local/lib:/usr/lib");
		sbContext.append("2014-10-28 22:42:59 o.a.z.ZooKeeper [INFO] Client environment:java.io.tmpdir=/tmp");
		sbContext.append("2014-10-28 22:42:59 o.a.z.ZooKeeper [INFO] Client environment:java.compiler=<NA>");
		sbContext.append("2014-10-28 22:42:59 o.a.z.ZooKeeper [INFO] Client environment:os.name=Linux");
		sbContext.append("2014-10-28 22:42:59 o.a.z.ZooKeeper [INFO] Client environment:os.arch=amd64");
		sbContext.append("2014-10-28 22:42:59 o.a.z.ZooKeeper [INFO] Client environment:os.version=2.6.32-431.el6.x86_64");
		sbContext.append("2014-10-28 22:42:59 o.a.z.ZooKeeper [INFO] Client environment:user.name=root");
		sbContext.append("2014-10-28 22:42:59 o.a.z.ZooKeeper [INFO] Client environment:user.home=/root");
		sbContext.append("2014-10-28 22:42:59 o.a.z.ZooKeeper [INFO] Client environment:user.dir=/usr/local/storm-0.9.2/bin");
		sbContext.append("2014-10-28 22:42:59 o.a.z.s.ZooKeeperServer [INFO] Server environment:zookeeper.version=3.4.5-1392090, built on 09/30/2012 17:52 GMT");
		sbContext.append("2014-10-28 22:42:59 o.a.z.s.ZooKeeperServer [INFO] Server environment:host.name=vm132");
		sbContext.append("2014-10-28 22:42:59 o.a.z.s.ZooKeeperServer [INFO] Server environment:java.version=1.7.0_60");
		sbContext.append("2014-10-28 22:42:59 o.a.z.s.ZooKeeperServer [INFO] Server environment:java.vendor=Oracle Corporation");
		sbContext.append("2014-10-28 22:42:59 o.a.z.s.ZooKeeperServer [INFO] Server environment:java.home=/usr/java/jdk1.7.0_60/jre");
		sbContext.append("2014-10-28 22:42:59 o.a.z.s.ZooKeeperServer [INFO] Server environment:java.class.path=/usr/local/storm-0.9.2/lib/kryo-2.21.jar:/usr/local/storm-0.9.2/lib/core.incubator-0.1.0.jar:/usr/local/storm-0.9.2/lib/commons-io-2.4.jar:/usr/local/storm-0.9.2/lib/jgrapht-core-0.9.0.jar:/usr/local/storm-0.9.2/lib/netty-3.6.3.Final.jar:/usr/local/storm-0.9.2/lib/jetty-6.1.26.jar:/usr/local/storm-0.9.2/lib/netty-3.2.2.Final.jar:/usr/local/storm-0.9.2/lib/minlog-1.2.jar:/usr/local/storm-0.9.2/lib/kafka_2.9.2-0.8.0-beta1.jar:/usr/local/storm-0.9.2/lib/chill-java-0.3.5.jar:/usr/local/storm-0.9.2/lib/disruptor-2.10.1.jar:/usr/local/storm-0.9.2/lib/curator-client-2.4.0.jar:/usr/local/storm-0.9.2/lib/guava-13.0.jar:/usr/local/storm-0.9.2/lib/zookeeper-3.4.5.jar:/usr/local/storm-0.9.2/lib/commons-lang-2.5.jar:/usr/local/storm-0.9.2/lib/storm-core-0.9.2-incubating.jar:/usr/local/storm-0.9.2/lib/servlet-api-2.5.jar:/usr/local/storm-0.9.2/lib/commons-fileupload-1.2.1.jar:/usr/local/storm-0.9.2/lib/carbonite-1.4.0.jar:/usr/local/storm-0.9.2/lib/logback-core-1.0.6.jar:/usr/local/storm-0.9.2/lib/metrics-core-2.2.0.jar:/usr/local/storm-0.9.2/lib/reflectasm-1.07-shaded.jar:/usr/local/storm-0.9.2/lib/clj-time-0.4.1.jar:/usr/local/storm-0.9.2/lib/logback-classic-1.0.6.jar:/usr/local/storm-0.9.2/lib/objenesis-1.2.jar:/usr/local/storm-0.9.2/lib/hiccup-0.3.6.jar:/usr/local/storm-0.9.2/lib/ring-jetty-adapter-0.3.11.jar:/usr/local/storm-0.9.2/lib/tools.cli-0.2.4.jar:/usr/local/storm-0.9.2/lib/asm-4.0.jar:/usr/local/storm-0.9.2/lib/joda-time-2.0.jar:/usr/local/storm-0.9.2/lib/snakeyaml-1.11.jar:/usr/local/storm-0.9.2/lib/slf4j-api-1.6.5.jar:/usr/local/storm-0.9.2/lib/clj-stacktrace-0.2.4.jar:/usr/local/storm-0.9.2/lib/mysql-connector-java-5.1.26.jar:/usr/local/storm-0.9.2/lib/compojure-1.1.3.jar:/usr/local/storm-0.9.2/lib/clojure-1.5.1.jar:/usr/local/storm-0.9.2/lib/ring-devel-0.3.11.jar:/usr/local/storm-0.9.2/lib/commons-codec-1.6.jar:/usr/local/storm-0.9.2/lib/commons-logging-1.1.3.jar:/usr/local/storm-0.9.2/lib/httpclient-4.3.3.jar:/usr/local/storm-0.9.2/lib/log4j-over-slf4j-1.6.6.jar:/usr/local/storm-0.9.2/lib/math.numeric-tower-0.0.1.jar:/usr/local/storm-0.9.2/lib/tools.macro-0.1.0.jar:/usr/local/storm-0.9.2/lib/tools.logging-0.2.3.jar:/usr/local/storm-0.9.2/lib/jline-2.11.jar:/usr/local/storm-0.9.2/lib/json-simple-1.1.jar:/usr/local/storm-0.9.2/lib/jetty-util-6.1.26.jar:/usr/local/storm-0.9.2/lib/servlet-api-2.5-20081211.jar:/usr/local/storm-0.9.2/lib/httpcore-4.3.2.jar:/usr/local/storm-0.9.2/lib/curator-framework-2.4.0.jar:/usr/local/storm-0.9.2/lib/zkclient-0.3.jar:/usr/local/storm-0.9.2/lib/scala-library-2.9.2.jar:/usr/local/storm-0.9.2/lib/ring-servlet-0.3.11.jar:/usr/local/storm-0.9.2/lib/ring-core-1.1.5.jar:/usr/local/storm-0.9.2/lib/commons-exec-1.1.jar:/usr/local/storm-0.9.2/lib/clout-1.0.1.jar:/usr/local/storm-0.9.2/conf");
		sbContext.append("2014-10-28 22:42:59 o.a.z.s.ZooKeeperServer [INFO] Server environment:java.library.path=/usr/local/lib:/opt/local/lib:/usr/lib");
		sbContext.append("2014-10-28 22:42:59 o.a.z.s.ZooKeeperServer [INFO] Server environment:java.io.tmpdir=/tmp");
		sbContext.append("2014-10-28 22:42:59 o.a.z.s.ZooKeeperServer [INFO] Server environment:java.compiler=<NA>");
		sbContext.append("2014-10-28 22:42:59 o.a.z.s.ZooKeeperServer [INFO] Server environment:os.name=Linux");
		sbContext.append("2014-10-28 22:42:59 o.a.z.s.ZooKeeperServer [INFO] Server environment:os.arch=amd64");
		sbContext.append("2014-10-28 22:42:59 o.a.z.s.ZooKeeperServer [INFO] Server environment:os.version=2.6.32-431.el6.x86_64");
		sbContext.append("2014-10-28 22:42:59 o.a.z.s.ZooKeeperServer [INFO] Server environment:user.name=root");
		sbContext.append("2014-10-28 22:42:59 o.a.z.s.ZooKeeperServer [INFO] Server environment:user.home=/root");
		sbContext.append("2014-10-28 22:42:59 o.a.z.s.ZooKeeperServer [INFO] Server environment:user.dir=/usr/local/storm-0.9.2/bin");
		sbContext.append("2014-10-28 22:43:00 o.a.c.f.i.CuratorFrameworkImpl [INFO] Starting");
		sbContext.append("2014-10-28 22:43:00 o.a.z.ZooKeeper [INFO] Initiating client connection, connectString=192.168.128.131:2181,192.168.128.132:2181 sessionTimeout=20000 watcher=org.apache.curator.ConnectionState@53abd462");
		sbContext.append("2014-10-28 22:43:00 o.a.z.ClientCnxn [INFO] Opening socket connection to server vm132/192.168.128.132:2181. Will not attempt to authenticate using SASL (unknown error)");
		sbContext.append("2014-10-28 22:43:00 o.a.z.ClientCnxn [INFO] Socket connection established to vm132/192.168.128.132:2181, initiating session");
		sbContext.append("2014-10-28 22:43:00 o.a.z.ClientCnxn [INFO] Session establishment complete on server vm132/192.168.128.132:2181, sessionid = 0x2495a6a51e20023, negotiated timeout = 20000");
		sbContext.append("2014-10-28 22:43:00 o.a.c.f.s.ConnectionStateManager [INFO] State change: CONNECTED");
		sbContext.append("2014-10-28 22:43:00 o.a.c.f.s.ConnectionStateManager [WARN] There are no ConnectionStateListeners registered.");
		sbContext.append("2014-10-28 22:43:00 b.s.zookeeper [INFO] Zookeeper state update: :connected:none");
		sbContext.append("2014-10-28 22:43:01 o.a.z.ClientCnxn [INFO] EventThread shut down");
		sbContext.append("2014-10-28 22:43:01 o.a.z.ZooKeeper [INFO] Session: 0x2495a6a51e20023 closed");
		sbContext.append("2014-10-28 22:43:01 o.a.c.f.i.CuratorFrameworkImpl [INFO] Starting");
		sbContext.append("2014-10-28 22:43:01 o.a.z.ZooKeeper [INFO] Initiating client connection, connectString=192.168.128.131:2181,192.168.128.132:2181/storm sessionTimeout=20000 watcher=org.apache.curator.ConnectionState@64074bf8");
		sbContext.append("2014-10-28 22:43:01 o.a.z.ClientCnxn [INFO] Opening socket connection to server vm132/192.168.128.132:2181. Will not attempt to authenticate using SASL (unknown error)");
		sbContext.append("2014-10-28 22:43:01 o.a.z.ClientCnxn [INFO] Socket connection established to vm132/192.168.128.132:2181, initiating session");
		sbContext.append("2014-10-28 22:43:01 o.a.z.ClientCnxn [INFO] Session establishment complete on server vm132/192.168.128.132:2181, sessionid = 0x2495a6a51e20024, negotiated timeout = 20000");
		sbContext.append("2014-10-28 22:43:01 o.a.c.f.s.ConnectionStateManager [INFO] State change: CONNECTED");
		//MysqlUtils.insert("insert into fks_alarminfo(description,context)values('"+sb.toString()+"','"+sbContext.toString()+"')");
    	String mailContent= StrUtils.format(
    			PropertiesUtils.getValue("log.alarm.content1"),
    			"PROJECT NAME",
    			"APP NAME",
    			"ERROR",
    			new SimpleDateFormat("dd/MMM/yyyy:HH:mm:ss Z",Locale.ENGLISH).format(new Date()),
    			"MainLand-1",
    			"192.168.128.131",
    			sb.toString(),
    			sbContext.toString());
        MailMessage mail = new MailMessage(
        		StrUtils.format(PropertiesUtils.getValue("log.alarm.title1"),"ERROR","EN"), 
               "chenzhao@made-in-china.com",
               new String[]{"chenzhao@made-in-china.com","chenzhao@made-in-china.com"},
               new String[]{"chenzhao@made-in-china.com"},
               new String[]{"attitudedecidesall@126.com"},
               mailContent,
               new String[]{}
        		);
        //set attachments
     /*   String[] attachments = new String[]{
                "C:\\AndroidManifest.xml", 
                "C:\\ic_launcher-web.png"};*/
       // mail.setFileNames(attachments);
        sendEmail(mail);
         
        //接收邮件
        //receiveEmail(POP3Server, POP3Username, POP3Password);
         
        //发送匿名邮件
        //MailMessage anonymousMail = new MailMessage("subject", 
          //  "a@a.a", "zzzz@qq.com", "content");
        //anonymousMail.setFileNames(attachments);
        //sendAnonymousEmail(anonymousMail);
    }
           
    /**
     * Load configuration properties to initialize attributes.
     */
    private static void loadConfigProperties() {
        try {
        	 SMTPServer = PropertiesUtils.getValue("mail.SMTPServer");
             SMTPUsername =PropertiesUtils.getValue("mail.SMTPUsername");
             SMTPPassword = PropertiesUtils.getValue("mail.SMTPPassword");
             POP3Server = PropertiesUtils.getValue("mail.POP3Server");
             POP3Username = PropertiesUtils.getValue("mail.POP3Username");
             POP3Password = PropertiesUtils.getValue("mail.POP3Password");
        } catch (Exception e) {
            LOGGER.error("File not found at ", e);
        }
    }
     
    /**
     * Send email. Note that the fileNames of MailMessage are the absolute path of file.
     * @param mail The MailMessage object which contains at least all the required 
     *        attributes to be sent.
     */
    public static void sendEmail(MailMessage mail) {
        sendEmail(null, mail, false);
    }
     
    /**
     * Send anonymous email. Note that although we could give any address as from address,
     * (for example: <b>'a@a.a' is valid</b>), the from of MailMessage should always be the 
     * correct format of email address(for example the <b>'aaaa' is invalid</b>). Otherwise 
     * an exception would be thrown say that username is invalid.
     * @param mail The MailMessage object which contains at least all the required 
     *        attributes to be sent.
     */
    public static void sendAnonymousEmail(MailMessage mail) {
        String dns = "dns://";
        Hashtable<String, String> env = new Hashtable<String, String>();
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.dns.DnsContextFactory");
        env.put(Context.PROVIDER_URL, dns);
        String[] tos = mail.getTos();
        try {
            DirContext ctx = new InitialDirContext(env);
            for(String to:tos) {
                String domain = to.substring(to.indexOf('@') + 1);
                //Get MX(Mail eXchange) records from DNS
                Attributes attrs = ctx.getAttributes(domain, new String[] { "MX" });
                if (attrs == null || attrs.size() <= 0) {
                    throw new java.lang.IllegalStateException(
                        "Error: Your DNS server has no Mail eXchange records!");
                }
                @SuppressWarnings("rawtypes")
                NamingEnumeration servers = attrs.getAll();
                String smtpHost = null;
                boolean isSend = false;
                StringBuffer buf = new StringBuffer();
                //try all the mail exchange server to send the email.
                while (servers.hasMore()) {
                    Attribute hosts = (Attribute) servers.next();
                    for (int i = 0; i < hosts.size(); ++i) {
                        //sample: 20 mx2.qq.com
                        smtpHost = (String) hosts.get(i);
                        //parse the string to get smtpHost. sample: mx2.qq.com
                        smtpHost = smtpHost.substring(smtpHost.lastIndexOf(' ') + 1);
                        try {
                            sendEmail(smtpHost, mail, true);
                            isSend = true;
                            return;
                        } catch (Exception e) {
                            LOGGER.error("", e);
                            buf.append(e.toString()).append("\r\n");
                            continue;
                        }
                    }
                }
                if (!isSend) {
                    throw new java.lang.IllegalStateException("Error: Send email error."
                            + buf.toString());
                }
            }
        } catch (NamingException e) {
            LOGGER.error("", e);
        }
    } 
     
    /**
     * Send Email. Use string array to represents attachments file names.
     * @see #sendEmail(String, String, String[], String[], String[], String, File[])
     */
    private static void sendEmail(String smtpHost, 
        MailMessage mail, boolean isAnonymousEmail) {
        if(mail == null) {
            throw new IllegalArgumentException("Param mail can not be null.");
        }
        String[] fileNames = mail.getFileNames();
        //only needs to check the param: fileNames, other params would be checked through
        //the override method.
        File[] files = null;
        if(fileNames != null && fileNames.length > 0) {
            files = new File[fileNames.length];
            for(int i = 0; i < files.length; i++) {
                File file = new File(fileNames[i]);
                files[i] = file;
            }
        }
        sendEmail(smtpHost, mail.getSubject(), mail.getFrom(), mail.getTos(), 
                mail.getCcs(), mail.getBccs(), mail.getContent(), files, isAnonymousEmail);
    }
    
    /**
     * Send Email. Note that content and attachments cannot be empty at the same time.
     * @param smtpHost The SMTPHost. This param is needed when sending an anonymous email.
     *        When sending normal email, the param is ignored and the default SMTPServer
     *        configured is used.
     * @param subject The email subject.
     * @param from The sender address. This address must be available in SMTPServer.
     * @param tos The receiver addresses. At least 1 address is valid.
     * @param ccs The 'copy' receiver. Can be empty.
     * @param bccs The 'encrypt copy' receiver. Can be empty.
     * @param content The email content.
     * @param attachments The file array represent attachments to be send.
     * @param isAnonymousEmail If this mail is send in anonymous mode. When set to true, the 
     *        param smtpHost is needed and sender's email address from should be in correct
     *        pattern.
     */
    private static void sendEmail(String smtpHost, String subject, 
            String from, String[] tos, String[] ccs, String[] bccs, 
            String content, File[] attachments, boolean isAnonymousEmail) {
        //parameter check
        if(isAnonymousEmail && smtpHost == null) {
            throw new IllegalStateException(
                "When sending anonymous email, param smtpHost cannot be null");
        }
        if(subject == null || subject.length() == 0) {
            subject = "Auto-generated subject";
        }
        if(from == null) {
            throw new IllegalArgumentException("Sender's address is required.");
        }
        if(tos == null || tos.length == 0) {
            throw new IllegalArgumentException(
                "At lease 1 receive address is required.");
        }
        if(content == null && (attachments == null || attachments.length == 0)) {
            throw new IllegalArgumentException(
                "Content and attachments cannot be empty at the same time");
        }
        if(attachments != null && attachments.length > 0) {
            List<File> invalidAttachments = new ArrayList<File>();
            for(File attachment:attachments) {
                if(!attachment.exists() || attachment.isDirectory() 
                    || !attachment.canRead()) {
                    invalidAttachments.add(attachment);
                }
            }
            if(invalidAttachments.size() > 0) {
                String msg = "";
                for(File attachment:invalidAttachments) {
                    msg += "\n\t" + attachment.getAbsolutePath();
                }
                throw new IllegalArgumentException(
                    "The following attachments are invalid:" + msg);
            }
        }
        Session session;
        Properties props = new Properties();
        props.put("mail.transport.protocol", "smtp");
         
        if(isAnonymousEmail) {
            //only anonymous email needs param smtpHost
            props.put("mail.smtp.host", smtpHost);
            props.put("mail.smtp.auth", "false");
            session = Session.getInstance(props, null);
        } else {
            //normal email does not need param smtpHost and uses the default host SMTPServer
            props.put("mail.smtp.host", SMTPServer); 
            props.put("mail.smtp.auth", "true");
            session = Session.getInstance(props, 
                new MailAuthenticator(SMTPUsername, SMTPPassword));
        }
        //create message
        MimeMessage msg = new MimeMessage(session);
        try {
            //Multipart is used to store many BodyPart objects.
            Multipart multipart=new MimeMultipart();
             
            BodyPart part = new MimeBodyPart();
            part.setContent(content,"text/html;charset=gb2312");
            //add email content part.
            multipart.addBodyPart(part);
             
            //add attachment parts.
            if(attachments != null && attachments.length > 0) {
                for(File attachment: attachments) {
                    String fileName = attachment.getName();
                    DataSource dataSource = new FileDataSource(attachment);
                    DataHandler dataHandler = new DataHandler(dataSource);
                    part = new MimeBodyPart();
                    part.setDataHandler(dataHandler);
                    //solve encoding problem of attachments file name.
                    try {
                        fileName = MimeUtility.encodeText(fileName);
                    } catch (UnsupportedEncodingException e) {
                        LOGGER.error(
                            "Cannot convert the encoding of attachments file name.", e);
                    }
                    //set attachments the original file name. if not set, 
                    //an auto-generated name would be used.
                    part.setFileName(fileName);
                    multipart.addBodyPart(part);
                }
            }
            msg.setSubject(subject);
            msg.setSentDate(new Date());
            //set sender
            msg.setFrom(new InternetAddress(from));
            //set receiver, 
            for(String to: tos) {
                msg.addRecipient(RecipientType.TO, new InternetAddress(to));
            }
            if(ccs != null && ccs.length > 0) {
                for(String cc: ccs) {
                    msg.addRecipient(RecipientType.CC, new InternetAddress(cc));
                }
            }
            if(bccs != null && bccs.length > 0) {
                for(String bcc: bccs) {
                    msg.addRecipient(RecipientType.BCC, new InternetAddress(bcc));
                }
            }
            msg.setContent(multipart);
            //save the changes of email first.
            msg.saveChanges();
            //to see what commands are used when sending a email, use session.setDebug(true)
            //session.setDebug(true);
            //send email
            Transport.send(msg); 
            LOGGER.info("Send email success.");
        } catch (NoSuchProviderException e) {
            LOGGER.error("Email provider config error.", e);
        } catch (MessagingException e) {
            LOGGER.error("Send email error.", e);
        }
    }
     
 
    /**
     * Receive Email from POPServer. Use POP3 protocal by default. Thus,
     * call this method, you need to provide a pop3 mail server address.
     * @param emailAddress The email account in the POPServer.
     * @param password The password of email address.
     */
    public static void receiveEmail(String host, String username, String password) {
        //param check. If param is null, use the default configured value.
        if(host == null) {
            host = POP3Server;
        }
        if(username == null) {
            username = POP3Username;
        }
        if(password == null) {
            password = POP3Password;
        }
        Properties props = System.getProperties();
        //MailAuthenticator authenticator = new MailAuthenticator(username, password);
        try {
            Session session = Session.getDefaultInstance(props, null);
            // Store store = session.getStore("imap");
            Store store = session.getStore("pop3");
            // Connect POPServer
            store.connect(host, username, password);
            Folder inbox = store.getFolder("INBOX");
            if (inbox == null) {
                throw new RuntimeException("No inbox existed.");
            }
            // Open the INBOX with READ_ONLY mode and start to read all emails.
            inbox.open(Folder.READ_ONLY);
            System.out.println("TOTAL EMAIL:" + inbox.getMessageCount());
            Message[] messages = inbox.getMessages();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            for (int i = 0; i < messages.length; i++) {
                Message msg = messages[i];
                String from = InternetAddress.toString(msg.getFrom());
                String replyTo = InternetAddress.toString(msg.getReplyTo());
                String to = InternetAddress.toString(
                    msg.getRecipients(Message.RecipientType.TO));
                String subject = msg.getSubject();
                Date sent = msg.getSentDate();
                Date ress = msg.getReceivedDate();
                String type = msg.getContentType();
                System.out.println((i + 1) + ".---------------------------------------------");
                System.out.println("From:" + mimeDecodeString(from));
                System.out.println("Reply To:" + mimeDecodeString(replyTo));
                System.out.println("To:" + mimeDecodeString(to));
                System.out.println("Subject:" + mimeDecodeString(subject));
                System.out.println("Content-type:" + type);
                if (sent != null) {
                    System.out.println("Sent Date:" + sdf.format(sent));
                }
                if (ress != null) {
                    System.out.println("Receive Date:" + sdf.format(ress));
                }
//                //Get message headers.
//                @SuppressWarnings("rawtypes")
//                Enumeration headers = msg.getAllHeaders();
//                while (headers.hasMoreElements()) {
//                    Header h = (Header) headers.nextElement();
//                    String name = h.getName();
//                    String val = h.getValue();
//                    System.out.println(name + ": " + val);
//                }
                 
//                //get the email content.
//                Object content = msg.getContent();
//                System.out.println(content);
//                //print content
//                Reader reader = new InputStreamReader(
//                        messages[i].getInputStream());
//                int a = 0;
//                while ((a = reader.read()) != -1) {
//                    System.out.print((char) a);
//                }
            }
            // close connection. param false represents do not delete messaegs on server.
            inbox.close(false);
            store.close();
//        } catch(IOException e) {
//            LOGGER.error("IOException caught while printing the email content", e);
        } catch (MessagingException e) {
            LOGGER.error("MessagingException caught when use message object", e);
        }
    }
     
    /**
     * For receiving an email, the sender, receiver, reply-to and subject may 
     * be messy code. The default encoding of HTTP is ISO8859-1, In this situation, 
     * use MimeUtility.decodeTex() to convert these information to GBK encoding.
     * @param res The String to be decoded.
     * @return A decoded String.
     */
    private static String mimeDecodeString(String res) {
        if(res != null) {
            String from = res.trim();
            try {
                if (from.startsWith("=?GB") || from.startsWith("=?gb")
                        || from.startsWith("=?UTF") || from.startsWith("=?utf")) {
                    from = MimeUtility.decodeText(from);
                }
            } catch (Exception e) {
                LOGGER.error("Decode string error. Origin string is: " + res, e);
            }
            return from;
        }
        return null;
    }
}

