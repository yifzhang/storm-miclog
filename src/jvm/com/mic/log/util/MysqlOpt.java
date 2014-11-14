package com.mic.log.util;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
public class MysqlOpt implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1252519029798560134L;
	public Connection conn = null;
	PreparedStatement statement = null;
	public boolean connSQL(String host_p, String database, String username,
			String password) {
		String url = "jdbc:mysql://" + host_p + "/" + database
				+ "?characterEncoding=UTF-8";
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(url, username, password);
			return true;
		} catch (ClassNotFoundException cnfex) {
			System.out
					.println("MysqlBolt-- Error: Loading JDBC/ODBC dirver failed!");
			cnfex.printStackTrace();
		} catch (SQLException sqlex) {
			System.out.println("MysqlBolt-- Error: Connect database failed!");
			sqlex.printStackTrace();
		}
		return false;
	}
	public boolean insertSQL(String sql) {
		try {
			if(statement.isClosed())
			statement = conn.prepareStatement(sql);
			statement.executeUpdate();
			return true;
		} catch (SQLException e) {
			System.out.println("MysqlBolt-- Error: Insert database failed!");
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println("MysqlBolt-- Error: Insert failed!");
			e.printStackTrace();
		}
		return false;
	}
	public void deconnSQL() {
		try {
			if (conn != null)
				conn.close();
		} catch (Exception e) {
			System.out.println("MysqlBolt-- Error: Deconnect database failed!");
			e.printStackTrace();
		}
	}
}
