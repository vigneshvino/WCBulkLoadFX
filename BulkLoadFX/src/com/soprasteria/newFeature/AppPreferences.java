package com.soprasteria.newFeature;

import java.beans.XMLEncoder;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;


/**
 * @author ayusrivastava
 * */

/**
 * This class will save all preferences of the user while closing the application.
 * Set the value of CONFIG_FILE*/

public class AppPreferences {
	
	public static String CONFIG_FILE="D:\\WCBulkLoadFX_POC\\appconfig.xml";
	
	private String srcServerName;
	private String srcServerCertName;
	private String srcServerUsername;
	private String srcServerPassword;
	private String srcVersion;
	private String host;
	private String database;
	private String port;
	private String username;
	private String password;
	
	public AppPreferences() {
		
	}
	

	public String getSrcServerName() {
		return srcServerName;
	}


	public void setSrcServerName(String srcServerName) {
		this.srcServerName = srcServerName;
	}


	public String getSrcServerCertName() {
		return srcServerCertName;
	}


	public void setSrcServerCertName(String srcServerCertName) {
		this.srcServerCertName = srcServerCertName;
	}


	public String getSrcServerUsername() {
		return srcServerUsername;
	}


	public void setSrcServerUsername(String srcServerUsername) {
		this.srcServerUsername = srcServerUsername;
	}


	public String getSrcServerPassword() {
		return srcServerPassword;
	}


	public void setSrcServerPassword(String srcServerPassword) {
		this.srcServerPassword = srcServerPassword;
	}


	public String getSrcVersion() {
		return srcVersion;
	}


	public void setSrcVersion(String srcVersion) {
		this.srcVersion = srcVersion;
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

	/***
	 * Saves all the user preferences in a xml file
	 * @param app
	 * @throws IOException
	 */
	
	public void setAppConfig(AppPreferences app) throws IOException {
/*		
		System.out.println(app.getHost());
		System.out.println(app.getDatabase());
		System.out.println(app.getPort());
		System.out.println(app.getUsername());
		System.out.println(app.getPassword());
*/		
		XMLEncoder x = new XMLEncoder(new BufferedOutputStream(new FileOutputStream(CONFIG_FILE)));
		x.writeObject(app);
		x.close();
	}
	
}
