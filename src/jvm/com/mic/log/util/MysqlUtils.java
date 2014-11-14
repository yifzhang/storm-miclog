package com.mic.log.util;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 * <strong>mysql数据库操作帮助类</strong>
 * 
 * @author chenzhao
 */
public class MysqlUtils implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8675709949016241934L;
	private static String DBDRIVER;// = "com.mysql.jdbc.Driver";// 驱动类类名
	private static String DBURL;// = "jdbc:mysql://localhost:3306/" ;// 连接URL
	private static String DBUSER;// = "root";// 数据库用户名
	private static String DBPASSWORD;// = "root";// 数据库密码
	private static Connection conn = null;
	private static PreparedStatement ps = null;
	public static Statement stmt;
	public static ResultSet rs;
	static {
		DBDRIVER = PropertiesUtils.getValue("jdbc.driverClassName");
		DBURL = PropertiesUtils.getValue("jdbc.url");
		DBUSER = PropertiesUtils.getValue("jdbc.username");
		DBPASSWORD = PropertiesUtils.getValue("jdbc.password");
	}

	// 获取数据库连接
	public static Connection getConnection() {
		try {
			Class.forName(DBDRIVER);// 注册驱动
			conn = DriverManager.getConnection(DBURL, DBUSER, DBPASSWORD);// 获得连接对象
			stmt = conn.createStatement();
		} catch (ClassNotFoundException e) {// 捕获驱动类无法找到异常
			e.printStackTrace();
		} catch (SQLException e) {// 捕获SQL异常
			e.printStackTrace();
		}
		return conn;
	}

	public static int update(String sql) throws Exception {
		int num = 0;// 计数
		int retryCount = 5;
		boolean transactionCompleted = false;
		do {
			try {
				conn = getConnection();
				ps = conn.prepareStatement(sql);
				num = ps.executeUpdate();
				transactionCompleted = true;
			} catch (SQLException sqle) {
				String sqlState = sqle.getSQLState();
				System.out.println("出错了:"+sqlState);
				// 这个08S01就是这个异常的sql状态。单独处理手动重新链接就可以了。
				if ("08S01".equals(sqlState) || "40001".equals(sqlState)) {
					retryCount--;
				} else {
					retryCount = 0;
				}
				// throw new SQLException("update data Exception: "+
				// sqle.getMessage());
			} finally {
				try {
					if (ps != null) {
						ps.close();
					}
				} catch (Exception e) {
					throw new Exception("ps close Exception: " + e.getMessage());
				}
				try {
					if (conn != null) {
						conn.close();
					}
				} catch (Exception e) {
					throw new Exception("conn close Exception: "
							+ e.getMessage());
				}
			}
		} while (!transactionCompleted && (retryCount > 0));
		return num;
	}

	// 查询数据
	public static ResultSet select(String sql) throws Exception {
		int num = 0;// 计数
		int retryCount = 5;
		boolean transactionCompleted = false;
		do {
			try {
				conn = getConnection();
				ps = conn.prepareStatement(sql);
				rs = ps.executeQuery(sql);
				transactionCompleted = true;
				return rs;
			} catch (SQLException sqle) {
				String sqlState = sqle.getSQLState();
				System.out.println("出错了:"+sqlState);
				// 这个08S01就是这个异常的sql状态。单独处理手动重新链接就可以了。
				if ("08S01".equals(sqlState) || "40001".equals(sqlState)) {
					retryCount--;
				} else {
					retryCount = 0;
				}
				// throw new SQLException("update data Exception: "+
				// sqle.getMessage());
			} finally {
			/*	try {
					if (ps != null) {
						ps.close();
					}
				} catch (Exception e) {
					throw new Exception("ps close Exception: " + e.getMessage());
				}
				try {
					if (conn != null) {
						conn.close();
					}
				} catch (Exception e) {
					throw new Exception("conn close Exception: "
							+ e.getMessage());
				}*/
			}
		} while (!transactionCompleted && (retryCount > 0));
		return rs;
	}

	// 查询数据
	public static boolean isExists(String sql) throws Exception {
		try {
			conn = getConnection();
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery(sql);
			return rs.first();
		} catch (SQLException sqle) {
			throw new SQLException("select data Exception: "
					+ sqle.getMessage());
		} catch (Exception e) {
			throw new Exception("System error: " + e.getMessage());
		}
	}

	// 插入数据
	public static int insert(String sql) throws Exception {
		return update(sql);
	}

	// 删除数据
	public static int delete(String sql) throws Exception {
		int num = 0;// 计数
		try {
			conn = getConnection();
			ps = conn.prepareStatement(sql);
			num = ps.executeUpdate();
		} catch (SQLException sqle) {
			throw new SQLException("delete data Exception: "+ sqle.getMessage());
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
			} catch (Exception e) {
				throw new Exception("ps close Exception: " + e.getMessage());
			}
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (Exception e) {
				throw new Exception("conn close Exception: " + e.getMessage());
			}
		}
		return num;
	}
	public static void addBatch(String sql) throws Exception
	{
	     try {
	       conn=getConnection();
	       conn.setAutoCommit(false);
	       ps = conn.prepareStatement(sql);
	      ps.executeUpdate();
	      conn.commit();
	     }catch(Exception ex)
	     {
	    	 ex.printStackTrace();
	     }
	     finally {
				try {
					if (ps != null) {
						ps.close();
					}
				} catch (Exception e) {
					throw new Exception("ps close Exception: " + e.getMessage());
				}
				try {
					if (conn != null) {
						conn.close();
					}
				} catch (Exception e) {
					throw new Exception("conn close Exception: " + e.getMessage());
				}
			}
	     
	}
	/**
	 * 关闭连接
	 * 
	 * @return
	 */
	public static void close(Connection conn) {
		try {
			if (conn != null) {
				conn.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws Exception {
		int rowEffective = MysqlUtils
				.update("update fks_tuple set count=count+1 where id=3");
		System.out.println(rowEffective);
	}
}
