package com.soprasteria.extract;

import java.net.MalformedURLException;
import java.net.URL;

import wt.fc.PersistenceHelper;
import wt.fc.QueryResult;
import wt.method.RemoteAccess;
import wt.method.RemoteMethodServer;
import wt.part.WTPart;
import wt.query.QueryException;
import wt.query.QuerySpec;
import wt.util.WTException;

public class ExtractWTPart implements RemoteAccess {

	public static void initialize(String typeName, String sourceServer, String username, String password) throws MalformedURLException {
		// TODO Auto-generated method stub
		String serviceName = "MethodServer";
		if(!sourceServer.endsWith("/")) {
			sourceServer = sourceServer + "/";
		}
		URL wc_url = new URL(sourceServer);
		RemoteMethodServer rms = RemoteMethodServer.getInstance(wc_url, serviceName);
		rms.setUserName(username);
		rms.setPassword(password);
		
		try {
			rms.invoke("start", "com.soprasteria.extract.ExtractWTPart", null, new Class[] {String.class}, new Object[] {typeName});
			System.out.println("After this point logs will go to MS");
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
	}
	
	public static void start(String type) throws WTException {
		// Export process starts from here
		System.out.println("Object name from the application - "+type);
		QuerySpec qspec = new QuerySpec(WTPart.class);
		QueryResult qresult = PersistenceHelper.manager.find(qspec);
		System.out.println("Query result size in wtpart class is "+qresult.size());
		
	}

}
