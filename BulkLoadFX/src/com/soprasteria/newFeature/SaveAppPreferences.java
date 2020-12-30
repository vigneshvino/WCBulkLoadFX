package com.soprasteria.newFeature;

import java.io.IOException;

import javafx.scene.control.RadioButton;
import javafx.scene.control.Toggle;

/***
 * * /
 * @author ayusrivastava
 *
 */


/**
 * This class will be used to save the user preferences in each tab and will use AppPreference class object
 * to save those preferences*/
public class SaveAppPreferences {

	private static String SRCSERVERNAME;
	private static String SRCSERVERCERTNAME;
	private static String SRCSERVERUSERNAME;
	private static String SRCSERVERPASSWORD;
	private static String SRCVERSION;
	private static String HOST;
	private static String DATABASE;
	private static String PORT;
	private static String USERNAME;
	private static String PASSWORD;
	
	
	/**
	 * This method will take all the values of following parameters from Windchill Tab
	 * @param srcServerName
	 * @param srcServerCertName
	 * @param srcServerUsername
	 * @param srcServerPassword
	 * @param srcVersion
	 */
	
	public static void saveWCTabPreferences(String srcServerName, String srcServerCertName, String srcServerUsername, 
			String srcServerPassword, Toggle srcVersion) {
		SRCSERVERNAME=srcServerName;
		SRCSERVERCERTNAME=srcServerCertName;
		SRCSERVERUSERNAME=srcServerUsername;
		SRCSERVERPASSWORD=srcServerPassword;
		SRCVERSION=((RadioButton) srcVersion).getText();
//		System.out.println(SRCVERSION);
	}
	
	/**
	 * This method will take all the values of following parameters from Database Tab
	 * @param host
	 * @param database
	 * @param port
	 * @param username
	 * @param password
	 */
	
	
	public static void saveDBTabPreferences(String host, String database, String port, String username, String password) {
		HOST=host;
		DATABASE=database;
		PORT=port;
		USERNAME=username;
		PASSWORD=password;
	}
	
	
	/**
	 * This method will create AppPrefernences class object and sets all the values and calls setAppConfig() 
	 * to create the xml mapping for user preferences
	 */
	
	public void setAppPreferences() {
		AppPreferences appPrefs = new AppPreferences();
		
		appPrefs.setSrcServerName(SRCSERVERNAME);
		appPrefs.setSrcServerCertName(SRCSERVERCERTNAME);
		appPrefs.setSrcServerUsername(SRCSERVERUSERNAME);
		appPrefs.setSrcServerPassword(SRCSERVERPASSWORD);
		appPrefs.setSrcVersion(SRCVERSION);
		appPrefs.setHost(HOST);
		appPrefs.setDatabase(DATABASE);
		appPrefs.setPort(PORT);
		appPrefs.setUsername(USERNAME);
		appPrefs.setPassword(PASSWORD);
//		System.out.println(appPrefs.getSrcVersion());
		try {
			appPrefs.setAppConfig(appPrefs);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
