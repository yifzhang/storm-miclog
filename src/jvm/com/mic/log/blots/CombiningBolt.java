package com.mic.log.blots;

import java.io.Serializable;  
import java.util.ArrayList;  
import java.util.Collections;  
import java.util.Iterator;  
import java.util.List;  
import java.util.Map;  
import java.util.concurrent.ConcurrentLinkedQueue;  
import org.apache.log4j.Logger;  

import com.mic.log.service.Combinable;

import backtype.storm.task.TopologyContext;  
import backtype.storm.topology.BasicOutputCollector;  
import backtype.storm.topology.OutputFieldsDeclarer;  
import backtype.storm.topology.base.BaseBasicBolt;  
import backtype.storm.tuple.Fields;  
import backtype.storm.tuple.Tuple;  
import backtype.storm.tuple.Values;  
import backtype.storm.utils.TimeCacheMap;
import backtype.storm.utils.TimeCacheMap.ExpiredCallback;
  
/** 
 * CombiningBolt is used for combining intermediate objects which implement the Combinable 
 * interface. 
 *  bulilder.setSpout("GetFromMessageQueueBolt",new GetFromMessageQueueSpout(),2);
 *  builder.setBolt("ParseMessageBolt",new ParseMessageBolt(),10)
 *  	   .shuffleGrouping("GetFromMessageQueueBolt");
 *  builder.setBolt("CombiningBolt",new CombiningBolt(10,3000,5),20)
 *  		.filedGrouping("ParseMessageBolt",new Fileds("ident"));
 *  builder.setBolt("StoreResultsBolt",new StoreResultBolt(),200)
 *  	   .fieldGrouping("CombiningBolt",new Fields("ident"));
 * 
 */  
  
public class CombiningBolt extends BaseBasicBolt{  
    private static final Logger LOG = Logger.getLogger(CombiningBolt.class);  
    private TimeCacheMap<String,Combinable> combiningMap ;  
    private ConcurrentLinkedQueue<Object> expiredList;  
    private static int DEFAULT_COMBINING_WINDOW = 100;  
    private static int DEFAULT_NUM_KEYS = 10000;  
    private static int DEFAULT_EXPIRED_SECONDS= 5;  
    private static int NUM_BUCKETS = 30;  
      
    private int combiningWindow;  
    private int numKeys;  
    private int expiredSeconds;  
    public CombiningBolt(){  
        this(DEFAULT_COMBINING_WINDOW,DEFAULT_NUM_KEYS,DEFAULT_EXPIRED_SECONDS);  
    }  
      
    public CombiningBolt(int CombiningWindow, int numKeys, int expiredSeconds){  
        this.combiningWindow = CombiningWindow;  
        this.numKeys = numKeys;  
        this.expiredSeconds = expiredSeconds;  
          
    }  
     @Override  
    public void prepare(Map stormConf, TopologyContext context) {  
         expiredList = new ConcurrentLinkedQueue<Object>();  
         ExpiredCallback<String,Combinable> callBack = new CallBackMove<String,Combinable>();  
         combiningMap = new TimeCacheMap(expiredSeconds,NUM_BUCKETS,callBack);  
    }  
       
    @Override  
    public void execute(Tuple input, BasicOutputCollector collector) {  
        String ident = input.getString(0);  
        Combinable combinable = (Combinable)input.getValue(1);  
          
        if(!combiningMap.containsKey(ident)){  
            if(combiningMap.size() >= numKeys){  
                collector.emit(new Values(ident,combinable));  
            }else{  
                combiningMap.put(ident, combinable);  
            }  
        }else{  
            Combinable old = combiningMap.get(ident);  
            Combinable new_combinable = old.combine(combinable);  
            if(new_combinable.getCombinedCount() >= combiningWindow){  
                collector.emit(new Values(ident,new_combinable));  
                combiningMap.remove(ident);  
            }else{  
                combiningMap.put(ident, new_combinable);  
            }  
        }  
        int length = expiredList.size();  
        while(length > 0){  
            Combinable e = (Combinable)expiredList.poll();  
            if(e != null)  
                collector.emit(new Values(e.ident(),e));  
            length--;  
        }  
          
    }  
  
    @Override  
    public void declareOutputFields(OutputFieldsDeclarer declarer) {  
        declarer.declare(new Fields("ident","combinable"));     
    }  
    private class CallBackMove<K,V> implements ExpiredCallback<K,V>,Serializable{  
        private static final long serialVersionUID = 1L;  
        @Override  
        public void expire(K key, V val) {  
            expiredList.offer(val);  
        }     
    }  
  
}  
