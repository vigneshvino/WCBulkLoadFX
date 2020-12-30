package com.soprasteria.export;

import java.beans.XMLDecoder;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;
import java.sql.Statement;

import com.soprasteria.newFeature.AppPreferences;

import javafx.scene.control.CheckBox;

/**
 * @author ayusrivastava
 * */

public class ExportObject {
	
	private static String SOURCE_URL = "";
	private static String DB_HOST = "";
	private static String DB_NAME = "";
	private static String DB_PORT = "";
	private static String SOURCE_USER = "";
	private static String SOURCE_PASS = "";
	
	private static CheckBox DISTINCT;
	private static String DISTINCT_VALUE = "";
	private static CheckBox WHERE;
	private static String WHERE_VALUE = "";
	private static String TABLENAME = "";
	private static String FILEPATH = "";
	private static String FILENAME = "";
	private static String FILEFORMAT = "";
	private static String DELIMITER = ""; 
	
	private static int records=0;

	/**
	 * Initializes the variables by the values entered by the user under Database Tab 
	 * @param dbHost, dbName, dbPort, username, password, tableName, filepath, filename, fileformat, delimiter
	 * */
	public static void initializeValues(String dbHost, String dbName, String dbPort, String username, 
			String password, String tableName, CheckBox distinct, String distinctValue, CheckBox where, 
			String whereValue, String filepath, String filename, String fileformat,String delimiter) {
		
		DB_HOST=dbHost;
		DB_NAME=dbName;
		DB_PORT=dbPort;
		SOURCE_URL="jdbc:oracle:thin:@"+DB_HOST+":"+DB_PORT+":"+DB_NAME;
		SOURCE_USER=username;
		SOURCE_PASS=password;
		DISTINCT=distinct;
		DISTINCT_VALUE=distinctValue;
		WHERE=where;
		WHERE_VALUE=whereValue;
		TABLENAME = tableName.toUpperCase();
		FILEPATH=filepath;
		FILENAME=filename;
		FILEFORMAT=fileformat.toLowerCase();
		DELIMITER=delimiter;	
	}
	
	/**
	 * Gets all the data from the source database and writes it to one a file*/
	public static void exportObj()  throws SQLException, IOException{
		
		String queryStatement = "";
		
		try {
			File fs = new File(FILEPATH + "\\" +FILENAME + "."+FILEFORMAT);
//			if(!fs.getParentFile().isDirectory()) {
//				fs.getParentFile().mkdir();
//			}
			fs.createNewFile();
			
			FileWriter csvWriter = new FileWriter(fs);
			
			Connection connect = DriverManager.getConnection(SOURCE_URL, SOURCE_USER, SOURCE_PASS);
			
			if (DISTINCT.isSelected() && WHERE.isSelected()) {
				queryStatement = "SELECT DISTINCT "+DISTINCT_VALUE+" FROM "+TABLENAME+" WHERE "+WHERE_VALUE;
			}else if(DISTINCT.isSelected()) {
				queryStatement = "SELECT DISTINCT ("+DISTINCT_VALUE+") FROM "+TABLENAME;
			}else if (WHERE.isSelected()) {
				queryStatement = "SELECT * FROM "+TABLENAME+" WHERE "+WHERE_VALUE;
			}else {
				queryStatement = "SELECT * FROM " + TABLENAME;
			}
			System.out.println("SQL Query : " + queryStatement);
			Statement stmt = connect.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
			ResultSet result = stmt.executeQuery(queryStatement);
			ResultSetMetaData resultMetaData = result.getMetaData();
			
			int columnSize = resultMetaData.getColumnCount();
			
			for (int i=1;i<=columnSize;i++) {
				String columname = resultMetaData.getColumnName(i);
				
				if(i<=columnSize-1) {
					csvWriter.append(columname + DELIMITER);
				}
				else {
					csvWriter.append(columname);
					csvWriter.append("\n");
				}
			}
			
			while(result.next()) {
				for(int i=1; i<=columnSize; i++) {
					
					if (resultMetaData.getColumnTypeName(i).equalsIgnoreCase("BLOB")) {
						Blob columnValue = result.getBlob(i);
						
						if (i<=columnSize-1) {
							csvWriter.append(columnValue + DELIMITER);
						} else {
							csvWriter.append(columnValue.toString());
						}
					} else {
						String columnValue = result.getString(i);
						String columnName = resultMetaData.getColumnName(i);
						
						if (i<=columnSize-1) {
							csvWriter.append(columnValue + DELIMITER);
						} else {
							csvWriter.append(columnValue);
						}
					}

				}
				if(!result.isLast()) {
					csvWriter.append("\n");
				}
				records++;
			}
			
			System.out.println("Successfull!!");
			System.out.println("Total no. of Records : " + records);
			
/*			if (connect != null) {
				System.out.println("Connection is Succussfull!!");
			}
			
			else {
				System.out.println("Failed to connect with DB!!");
			}
*/
			connect.close();
			csvWriter.flush();
			csvWriter.close();
			records = 0;
		}catch (SQLSyntaxErrorException e) {
			// TODO: handle exception
			System.out.println("Ohh, There is some issue with the Query Syntax!!!");
			System.out.println("Check the SQL Query Again : " + queryStatement);
			System.out.println("Error : " + e.getMessage());
		}catch(SQLException e) {
			System.out.println("Ohh, some error occured : " + e.getSQLState() +" "+ e.getMessage());
			e.printStackTrace();
		}catch(IOException e) {
			System.out.println("Ohh! Some error occured while creating the folder : " + e.getMessage());
			e.printStackTrace();
		}
	}
	
}
