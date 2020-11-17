package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class LoadDBConnection {
	
	/***
	 * Verify the DB Connection
	 * @param dbHost
	 * @param dbService
	 * @param port
	 * @param username
	 * @param password
	 * @return
	 * @throws SQLException 
	 */
	public static boolean verifyDBConnection(String dbHost, String dbService, String port, String username, String password) throws SQLException {
		// Generate db url
		String DB_URL = "jdbc:oracle:thin:@"+dbHost+":"+port+":"+dbService;
		Connection conn = null;
		boolean connResult = false;
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			System.out.println("Connecting to database..."+DB_URL);
			conn = DriverManager.getConnection(DB_URL, username, password);
			connResult = conn.isValid(10);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if(conn != null) {
				conn.close();
			}
		}
		
		return connResult;
	}

}
