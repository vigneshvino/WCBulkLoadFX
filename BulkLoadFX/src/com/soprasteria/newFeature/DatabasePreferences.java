package com.soprasteria.newFeature;

import java.beans.XMLEncoder;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import wt.pds.db2.db2Resource;

public class DatabasePreferences {
	
	public static String CONFIG_FILE="D:\\WCBulkLoadFX_POC\\dbtabconfig.xml";
	
	String host;
	String database;
	String port;
	String username;
	String password;
	
//	public DatabasePreferences(String host,
//		String database,
//		String port,
//		String username,
//		String password) {
//		
//		this.host=host;
//		this.database=database;
//		this.port=port;
//		this.username=username;
//		this.password=password;
//	}
	
	public DatabasePreferences() {
		
	}
	
	public String getHost() {
		return host;
	}
	
	public void setHost(String host) {
		this.host = host;
	}
	
	public String getDatabase() {
		return database;
	}
	
	public void setDatabase(String database) {
		this.database = database;
	}
	
	public String getPort() {
		return port;
	}
	
	public void setPort(String port) {
		this.port = port;
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	
	public void setDBConfig(DatabasePreferences dp) throws IOException {
//		FileWriter writer = null;
//		Gson gson = new GsonBuilder().setPrettyPrinting().create();
//		String json = gson.toJson(dp);
//		
//		try {
//			writer = new FileWriter(CONFIG_FILE);
//			writer.write(json);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}finally {
//			writer.flush();
//			writer.close();
//		}
		
		System.out.println(dp.getHost());
		System.out.println(dp.getDatabase());
		System.out.println(dp.getPort());
		System.out.println(dp.getUsername());
		System.out.println(dp.getPassword());
		
		XMLEncoder x = new XMLEncoder(new BufferedOutputStream(new FileOutputStream(CONFIG_FILE)));
		x.writeObject(dp);
		x.close();
	}

	
	
//	public static void saveConfig() {
//		Gson gson = new Gson();
//		Writer writer = null;
//		try {
//			writer = new FileWriter(CONFIG_FILE);
//			gson.toJson(jsonElement, writer);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
}
