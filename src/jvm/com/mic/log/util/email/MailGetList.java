package com.mic.log.util.email;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mic.log.util.MysqlUtils;
@SuppressWarnings("all")
public class MailGetList {
	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		Map<String,Map<String,String>> alarmList = resultSetToArrayList(MysqlUtils
				.select("select * from fks_alarmlist"));
		String[] listEmail=alarmList.get("mic_en_www").get("ccEmailList").toString().split(",");
		System.out.println(listEmail[0]);
	}

	public static Map<String,Map<String,String>> resultSetToArrayList(ResultSet rs) throws SQLException {
		Map<String,Map<String,String>>  mapListResult=new HashMap<String,Map<String,String>>();
		ResultSetMetaData md = rs.getMetaData();
		int columns = md.getColumnCount();
		String keyField="";
		String infoField="";
		while (rs.next()) {
			HashMap row = new HashMap(columns);
			for (int i = 1; i <= columns; ++i) {
				row.put(md.getColumnName(i), rs.getObject(i));
				keyField=(String) rs.getObject("appname");
			}
			mapListResult.put(keyField, row);
		}
		return mapListResult;
	}
	public static Map<String,String> visitAlarmToArrayList(ResultSet rs) throws SQLException {
		Map<String,String>  mapListResult=new HashMap<String,String>();
		ResultSetMetaData md = rs.getMetaData();
		int columns = md.getColumnCount();
		String keyField="";
		String infoField="";
		while (rs.next()) {
			HashMap row = new HashMap(columns);
			for (int i = 1; i <= columns;i++) {
				mapListResult.put(md.getColumnName(i).toString(), rs.getObject(i).toString());
			}
		}
		return mapListResult;
	}
}
