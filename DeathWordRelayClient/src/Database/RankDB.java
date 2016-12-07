package Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import GUI.Waiting;
import RoomClient.Client;

public class RankDB {

	/* Basic variables for SQL query. */
	static String queryLose, queryWin, queryRate, queryRank;
	static Connection con = null;
	static Statement stmt = null;
	static String rateTable = "WinLose";

	/* Basic variables for user information */
	static String id;
	static int win, lose;
	// Client clnt = new Client();

	private static Waiting myWaitGUI;

	/**
	 * If user win the game, It updates _ID's rate table (Win++)
	 * 
	 * @param _ID
	 * 
	 *
	 */
	public static void updateWin(String _ID) {

		queryWin = "update WinLose set Win = Win+1 where ID = '" + _ID + "'";

		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://madstar.synology.me/DWR", "DWR", "1234");
			stmt = (Statement) con.createStatement();
			stmt.executeUpdate(queryWin);

		} catch (SQLException sqle) {
			System.out.println("추가 도중  에러 발생  ㅡ! " + sqle.toString());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * If user lose the game, It updates _ID's rate table (lose++)
	 * 
	 * @param _ID
	 * 
	 *
	 */
	public static void updateLose(String _ID) {

		queryLose = "update WinLose set Lose= Lose + 1 where ID = '" + _ID + "'";
		// UPDATE WinLose SET Win= Win + 1 WHERE ID = 'somi';
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://madstar.synology.me/DWR", "DWR", "1234");
			stmt = (Statement) con.createStatement();
			stmt.executeUpdate(queryLose);

		} catch (SQLException sqle) {
			System.out.println("추가 도중  에러 발생  ㅡ! " + sqle.toString());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Get _ID's rate
	 * 
	 * @param _ID
	 * 
	 *
	 */

	public static double getRate(String _ID) {

		double rate = 0.00;
		int nWin = 1, nLose = 1;
		ResultSet rs = null;
		queryRate = "SELECT Win, Lose from WinLose where ID = '" + _ID + "'";

		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://madstar.synology.me/DWR", "DWR", "1234");
			stmt = con.createStatement();
			rs = stmt.executeQuery(queryRate);
			ResultSetMetaData rsmd = rs.getMetaData();// result set으로 받는다
			// win/win+lose * 100
			while (rs.next()) {
				if ((rs.getInt("Lose") + rs.getInt("Win")) != 0) {
					nWin = rs.getInt("Win");
					nLose = rs.getInt("Lose");
					rate = nWin / (nWin + nLose) * 100;
				}
			}

			System.out.println(rate + "\n");

		} catch (SQLException sqle) {
			System.out.println("추가 도중  에러 발생  ㅡ! " + sqle.toString());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return rate;

	}

	public static String floatingRank() {

		ResultSet rs = null;
		String arg="";
		queryRank = "SELECT ID,(Win/(Win+Lose))*100 as rank from WinLose order by rank desc";
		



try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://madstar.synology.me/DWR", "DWR", "1234");

			stmt = con.createStatement();
			rs = stmt.executeQuery(queryRank);
			ResultSetMetaData rsmd = rs.getMetaData();// result set으로 받는다
			int i = 1;
			
			while (rs.next()) {
				arg += i +" |  "+ rs.getString("ID") +"	" + rs.getInt("rank") +"\n";
				i++;
			System.out.println(arg);
			}
			
			System.out.println("나와서"+ arg);
				

		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return arg;

	}

}
