package com.soprasteria.connection;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;

import wt.method.RemoteAccess;
import wt.method.RemoteMethodServer;

public class LoadWindchillConnection implements RemoteAccess {
	
	/*
	 * public static void main(String[] args) throws MalformedURLException,
	 * RemoteException {
	 * 
	 * String serviceName = "MethodServer"; String testUrl =
	 * "http://item-s94761.emea.msad.sopra/Windchill"; if(!testUrl.endsWith("/")) {
	 * testUrl = testUrl+"/"; } System.out.println("testUrl - "+testUrl); URL wc_url
	 * = new URL("http://item-s94761.emea.msad.sopra/Windchill/");
	 * RemoteMethodServer rms = RemoteMethodServer.getInstance(wc_url, serviceName);
	 * rms.setUserName("wcadmin"); rms.setPassword("wcadmin");
	 * System.out.println("Login success - "+rms.getInfo());
	 * 
	 * }
	 */
	
	
	
	public static boolean verifyWindchillConnection(String url, String username, String password) throws MalformedURLException, RemoteException {
		boolean connResult = false;
		String serviceName = "MethodServer";
		if(!url.endsWith("/")) {
			url = url + "/";
		}
		URL wc_url = new URL(url);
		RemoteMethodServer rms = RemoteMethodServer.getInstance(wc_url, serviceName);
		rms.setUserName(username);
		rms.setPassword(password);
		if(rms.getInfo() != null) {
			if(rms.getInfo().toString().startsWith("wt.method.MethodServerInfo")) {
				System.out.println("Login success - "+rms.getInfo());
				connResult = true;
			}
		}
		return connResult;
	}

}
