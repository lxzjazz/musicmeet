package cn.musicmeet.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBUtils {

	public static void main(String[] args) throws Exception {
		testEvent2();
	}

	public static void testEventFrom() throws Exception {
		Class.forName("com.mysql.jdbc.Driver");
		Connection conn = DriverManager.getConnection("jdbc:mysql://114.215.118.82:3306/xds?useUnicode=true&characterEncoding=utf8", "root", "xuanchengxds2015");

		PreparedStatement pstmt1 = null;
		PreparedStatement pstmt2 = null;
		try {
			int i = 0;
			while (i < 180) {
				// 关闭自动提交
				conn.setAutoCommit(false);
				// 查询数据
				pstmt1 = conn.prepareStatement("SELECT from_id, from_name, create_time FROM cs_event_from ORDER BY from_id LIMIT ?, 10");
				pstmt1.setInt(1, i);
				ResultSet rs = pstmt1.executeQuery();
				while (rs.next()) {
					// 插入数据
					pstmt2 = conn.prepareStatement("INSERT INTO cs_museum_from(from_id, from_name, create_time) VALUES(?, ?, ?)");
					pstmt2.setInt(1, rs.getInt("from_id"));
					pstmt2.setString(2, rs.getString("from_name"));
					pstmt2.setString(3, rs.getString("create_time"));
					pstmt2.executeUpdate();
					// 提交事务
					conn.commit();
				}
				i = i + 10;
			}

		} catch (Exception e) {
			e.printStackTrace();
			try {
				// 事务回滚
				conn.rollback();
			} catch (Exception ec) {
				ec.printStackTrace();
			}
		} finally {
			// 关闭连接
			try {
				if (pstmt1 != null) {
					pstmt1.close();
				}
				if (pstmt2 != null) {
					pstmt2.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static void testEvent() throws Exception {
		Class.forName("com.mysql.jdbc.Driver");
		Connection conn = DriverManager.getConnection("jdbc:mysql://114.215.118.82:3306/xds?useUnicode=true&characterEncoding=utf8", "root", "xuanchengxds2015");

		PreparedStatement pstmt1 = null;
		PreparedStatement pstmt2 = null;
		try {
			int i = 0;
			while (i < 460) {
				// 关闭自动提交
				conn.setAutoCommit(false);
				// 查询数据
				pstmt1 = conn.prepareStatement("SELECT eid, title, address_city, address_detail, address_lbs, event_detail, event_desc, event_remind, event_begin_time, event_end_time, from_id, from_name, index_number, signup, signup_begin_time, signup_end_time, xds_value, pub_time, status, buy_num, buy_total, create_time FROM cs_event ORDER BY eid LIMIT ?, 10");
				pstmt1.setInt(1, i);
				ResultSet rs = pstmt1.executeQuery();
				while (rs.next()) {
					// 插入数据
					pstmt2 = conn.prepareStatement("INSERT INTO cs_museum(mid, title, address_city, address_detail, address_lbs, museum_detail, museum_desc, museum_remind, museum_begin_time, museum_end_time, from_id, from_name, index_number, signup, signup_begin_time, signup_end_time, xds_value, pub_time, status, buy_num, buy_total, create_time) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
					pstmt2.setInt(1, rs.getInt("eid"));
					pstmt2.setString(2, rs.getString("title"));
					pstmt2.setString(3, rs.getString("address_city"));
					pstmt2.setString(4, rs.getString("address_detail"));
					pstmt2.setString(5, rs.getString("address_lbs"));
					pstmt2.setString(6, rs.getString("event_detail").replace("/mobile/pic/event/", "/mobile/pic/museum/"));
					pstmt2.setString(7, rs.getString("event_desc"));
					pstmt2.setString(8, rs.getString("event_remind"));
					pstmt2.setString(9, rs.getString("event_begin_time"));
					pstmt2.setString(10, rs.getString("event_end_time"));
					pstmt2.setInt(11, rs.getInt("from_id"));
					pstmt2.setString(12, rs.getString("from_name"));
					pstmt2.setInt(13, rs.getInt("index_number"));
					pstmt2.setInt(14, rs.getInt("signup"));
					pstmt2.setString(15, rs.getString("signup_begin_time"));
					pstmt2.setString(16, rs.getString("signup_end_time"));
					pstmt2.setDouble(17, rs.getDouble("xds_value"));
					pstmt2.setString(18, rs.getString("pub_time"));
					pstmt2.setInt(19, rs.getInt("status"));
					pstmt2.setInt(20, rs.getInt("buy_num"));
					pstmt2.setInt(21, rs.getInt("buy_total"));
					pstmt2.setString(22, rs.getString("create_time"));
					pstmt2.executeUpdate();
					// 提交事务
					conn.commit();
				}
				i = i + 10;
			}

		} catch (Exception e) {
			e.printStackTrace();
			try {
				// 事务回滚
				conn.rollback();
			} catch (Exception ec) {
				ec.printStackTrace();
			}
		} finally {
			// 关闭连接
			try {
				if (pstmt1 != null) {
					pstmt1.close();
				}
				if (pstmt2 != null) {
					pstmt2.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void testEvent2() throws Exception {
		Class.forName("com.mysql.jdbc.Driver");
		Connection conn = DriverManager.getConnection("jdbc:mysql://114.215.118.82:3306/xds?useUnicode=true&characterEncoding=utf8", "root", "xuanchengxds2015");

		PreparedStatement pstmt1 = null;
		PreparedStatement pstmt2 = null;
		try {
			int i = 0;
			while (i < 460) {
				// 关闭自动提交
				conn.setAutoCommit(false);
				// 查询数据
				pstmt1 = conn.prepareStatement("SELECT mid, title, address_city, address_detail, address_lbs, museum_detail, museum_desc, museum_remind, museum_begin_time, museum_end_time, from_id, from_name, index_number, signup, signup_begin_time, signup_end_time, xds_value, pub_time, status, buy_num, buy_total, create_time FROM cs_museum WHERE mid IN (12, 13, 27, 67, 68, 100016)");
				//pstmt1.setInt(1, i);
				ResultSet rs = pstmt1.executeQuery();
				while (rs.next()) {
					// 插入数据
					pstmt2 = conn.prepareStatement("INSERT INTO cs_event(eid, title, address_city, address_detail, address_lbs, event_detail, event_desc, event_remind, event_begin_time, event_end_time, from_id, from_name, index_number, signup, signup_begin_time, signup_end_time, xds_value, pub_time, status, buy_num, buy_total, create_time) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
					pstmt2.setInt(1, rs.getInt("mid"));
					pstmt2.setString(2, rs.getString("title"));
					pstmt2.setString(3, rs.getString("address_city"));
					pstmt2.setString(4, rs.getString("address_detail"));
					pstmt2.setString(5, rs.getString("address_lbs"));
					pstmt2.setString(6, rs.getString("museum_detail").replace("/mobile/pic/museum/", "/mobile/pic/event/"));
					pstmt2.setString(7, rs.getString("museum_desc"));
					pstmt2.setString(8, rs.getString("museum_remind"));
					pstmt2.setString(9, rs.getString("museum_begin_time"));
					pstmt2.setString(10, rs.getString("museum_end_time"));
					pstmt2.setInt(11, rs.getInt("from_id"));
					pstmt2.setString(12, rs.getString("from_name"));
					pstmt2.setInt(13, rs.getInt("index_number"));
					pstmt2.setInt(14, rs.getInt("signup"));
					pstmt2.setString(15, rs.getString("signup_begin_time"));
					pstmt2.setString(16, rs.getString("signup_end_time"));
					pstmt2.setDouble(17, rs.getDouble("xds_value"));
					pstmt2.setString(18, rs.getString("pub_time"));
					pstmt2.setInt(19, rs.getInt("status"));
					pstmt2.setInt(20, rs.getInt("buy_num"));
					pstmt2.setInt(21, rs.getInt("buy_total"));
					pstmt2.setString(22, rs.getString("create_time"));
					pstmt2.executeUpdate();
					// 提交事务
					conn.commit();
				}
				i = i + 10;
			}

		} catch (Exception e) {
			e.printStackTrace();
			try {
				// 事务回滚
				conn.rollback();
			} catch (Exception ec) {
				ec.printStackTrace();
			}
		} finally {
			// 关闭连接
			try {
				if (pstmt1 != null) {
					pstmt1.close();
				}
				if (pstmt2 != null) {
					pstmt2.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
