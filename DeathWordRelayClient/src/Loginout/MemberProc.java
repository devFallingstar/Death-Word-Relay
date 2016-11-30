package Loginout;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.mysql.jdbc.Statement;
//import java.util.Vector;

import RoomClient.Client;

public class MemberProc {

	static String sql;
	static Connection con;
	static Statement stmt;

	static String id, pwd, nickname;
	static String memberTable = "Users";

	public static boolean create(MemberInfo dto) throws Exception {
		boolean flag = false;
		con = null;
		stmt = null;
		id = dto.getID();
		pwd = dto.getpwd();
		nickname = dto.getnickName();

		sql = "INSERT INTO " + memberTable + " VALUES (\'" + id + "\', \'" + pwd + "\', \'" + nickname + "\');";

		try {
			makeConnection();
			executeSQL(sql, 0);

			flag = true;
		} catch (Exception e) {
			System.out.println(e);
			flag = false;
		} finally {
			try {
				if (stmt != null)
					stmt.close();
				if (con != null)
					con.close();
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
		return flag;
	}

	public static boolean loginChecker(String ID, String PW) {
		try {
			makeConnection();

			sql = "SELECT * FROM " + memberTable + " WHERE ID=\'" + ID + "\' and PW=\'" + PW + "\';";
			ResultSet rs = executeSQL(sql, 1);
			if (rs != null) {
				rs.next();
				System.out.println("ASD "+rs.getString(3));
				Client.setNICK(rs.getString(3));
				return true;
			} else {
				return false;
			}

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public static void makeConnection() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://madstar.synology.me/DWR", "DWR", "1234");
			stmt = (Statement) con.createStatement();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static ResultSet executeSQL(String query, int mode) throws SQLException {
		try {
			if (mode == 0) { // execute Update (that return null)
				stmt.executeUpdate(query);

				return null;
			} else {// execute Query (that return result string)
				return stmt.executeQuery(query);
			}
		} catch (Exception e) {
			return null;
		}
	}
}
