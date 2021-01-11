package com.soprasteria.extract;

import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;

import wt.change2.WTChangeIssue;
import wt.fc.PersistenceHelper;
import wt.fc.QueryResult;
import wt.method.RemoteAccess;
import wt.method.RemoteMethodServer;
import wt.query.QuerySpec;
import wt.util.WTException;

public class ChangeObjectExtraction implements RemoteAccess{

	public static void main(String[] args) throws MalformedURLException {
		// TODO Auto-generated method stub
		
		String serviceName = "MethodServer";
		URL wc_url = new URL("http://localhost/Windchill/");
		
		RemoteMethodServer rms = RemoteMethodServer.getInstance(wc_url, serviceName);
		rms.setUserName("wcadmin");
		rms.setPassword("wcadmin");
		
		try {
			rms.invoke("extractChangeObj", "com.soprasteria.extract.ChangeObjectExtraction", null,
					new Class[] {}, new Object[] {});
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void extractChangeObj() {
		
		QuerySpec qspec=null;
		try {
			qspec = new QuerySpec(WTChangeIssue.class);
			QueryResult qres = PersistenceHelper.manager.find(qspec);
			
			while (qres.hasMoreElements()) {
				System.out.println(qres.nextElement());
			}
		} catch (WTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
