package wsc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ConnectionManager {
	private Connection conn = null;

	public ConnectionManager() {

		/* Load driver for MySQL */
		try {
			Class.forName(DatabaseInfo.drivername);
			createConnection();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	private Connection createConnection() {
		try {
			conn = DriverManager.getConnection(DatabaseInfo.connectionUrl, 
					DatabaseInfo.username, DatabaseInfo.userpass);
			System.out.println("================== Connection created ====================");
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return conn;
	}
	
	void closeConnection() {
		try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public Connection getConnection() {
		if (conn == null)
			createConnection();
		return conn;
	}
	
	public static void main(String[] args) {
		ConnectionManager cm = new ConnectionManager();
		cm.closeConnection();
	}
}