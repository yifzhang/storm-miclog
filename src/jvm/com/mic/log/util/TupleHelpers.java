package com.mic.log.util;

import java.io.Serializable;

import backtype.storm.Constants;
import backtype.storm.tuple.Tuple;

public final class TupleHelpers implements Serializable{
	  /**
	 * 
	 */
	private static final long serialVersionUID = -3040239532201703045L;

	private TupleHelpers() {
	  }

	  public static boolean isTickTuple(Tuple tuple) {
	    return tuple.getSourceComponent().equals(Constants.SYSTEM_COMPONENT_ID) && tuple.getSourceStreamId().equals(
	        Constants.SYSTEM_TICK_STREAM_ID);
	  }

}
