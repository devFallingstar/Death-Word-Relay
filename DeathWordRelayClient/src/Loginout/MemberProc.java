package Loginout;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import RoomClient.Client;

/**
 * This class is used for processing the member information that already joined
 * or will be joined.
 * 
 * @author YYS
 *
 */
public class MemberProc {

	/* Basic variables for SQL query. */
	static String sql1, sql2;
	static Connection con;
	static Statement stmt1, stmt2;
	static String memberTable = "Users";

	/* Basic variables for user information */
	static String id, pwd, nickname;

	/**
	 * When user submit with new informations, this method will create a new
	 * user with a checking of duplicates ID or nick name.
	 * 
	 * @param dto
	 *            new member's information
	 * @return boolean flag that has information about if user join successfully
	 *         or not.
	 * @throws Exception
	 */
	public static int create(MemberInfo dto) throws Exception {
		id = dto.getID();
		pwd = dto.getpwd();
		nickname = dto.getnickName();

		sql1 = "SELECT COUNT(*) from " + memberTable + " WHERE ID=\'" + id + "\'";
		sql2 = "INSERT INTO " + memberTable + " VALUES (\'" + id + "\',\'" + pwd + "\',\'" + nickname + "\')";

		try {
			makeConnection();
			ResultSet rs = executeSQL(sql1, 1);
			rs.next();
			if (rs.getInt(1) != 1) {
				sql1 = "SELECT COUNT(*) from " + memberTable + " WHERE NICK=\'" + nickname + "\'";
				rs = executeSQL(sql1, 1);
				rs.next();
				if (rs.getInt(1) != 1) {
					executeSQL(sql2, 0);
					return 1;
				} else {
					return -2;
				}
			} else {
				return -1;
			}
		} catch (Exception e) {
			System.out.println(e);
			return -3;
		} finally {
			try {
				if (stmt2 != null)
					stmt2.close();
				if (con != null)
					con.close();
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
	}

	/**
	 * If user login to the server, this method will check if user ID and PW is
	 * available or not.
	 * 
	 * @param ID
	 * @param PW
	 * @return boolean flag that has information about if user logins
	 *         successfully or not.
	 */
	public static boolean loginChecker(String ID, String PW) {
		try {
			makeConnection();

			String loginsql = "SELECT * FROM " + memberTable + " WHERE ID=\'" + ID + "\' and PW=\'" + PW + "\';";
			ResultSet rs = executeSQL(loginsql, 1);
			if (rs != null) {
				rs.next();
				Client.setNICK(rs.getString(3));

				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			System.out.println("ERROR : Wrong login");
			return false;
		}
	}

	/**
	 * Initialize default connection.
	 */
	public static void makeConnection() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://madstar.synology.me/DWR", "DWR", "1234");
			stmt1 = (Statement) con.createStatement();
			stmt2 = (Statement) con.createStatement();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Send SQL query and get a result set if the query was SELECT.
	 * 
	 * @param query
	 * @param mode
	 * @return result set of the query if query was SELECT, null if query wasn't
	 * @throws SQLException
	 */
	public static ResultSet executeSQL(String query, int mode) throws SQLException {
		try {
			if (mode == 0) { // execute Update (that return null)
				stmt2.executeUpdate(query);

				return null;
			} else {// execute Query (that return result string)
				return stmt1.executeQuery(query);
			}
		} catch (Exception e) {
			return null;
		}
	}
}