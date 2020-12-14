package com.soprasteria.export;

import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import com.ptc.windchill.jdbc.client.ResultMetaData;

public class Test {
	
	public static void main(String[] args) {
		
		String url = "jdbc:oracle:thin:@localhost:1521:wind";
		
		try {
			Connection conn = DriverManager.getConnection(url, "awcuser", "awcuser");
			
			Statement stmt = conn.createStatement();
			ResultSet result = stmt.executeQuery("SELECT * FROM WTPART");
			ResultSetMetaData resMeta = result.getMetaData();
			
			int columnsize = resMeta.getColumnCount();
//			while(result.next()) {
//				for(int i=1;i<columnsize;i++) {
//					if(result.getBlob(i) == null) {
//						String columnval = result.getString(i);
//						System.out.print(columnval);
//					}
//					else {
//						String columnval = result.getBlob(i).toString();
//						System.out.print(columnval);
//					}
//					System.out.print(";");
//				}
//				System.out.print("\n");
//			}
			
			while(result.next()) {
				for(int i=1;i<columnsize;i++) {
					if(resMeta.getColumnTypeName(i).equalsIgnoreCase("BLOB")) {
						Blob columnval = result.getBlob(i);
						System.out.print(columnval);
					}
					else {
						String columnval = result.getString(i);
						System.out.print(columnval);
					}
					System.out.print(";");
				}
				System.out.print("\n");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
