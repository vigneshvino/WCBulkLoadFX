package com.soprasteria.extract;

import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import wt.change2.FormalizedBy;
import wt.change2.WTChangeActivity2;
import wt.change2.WTChangeIssue;
import wt.change2.WTChangeOrder2;
import wt.change2.WTChangeRequest2;
import wt.fc.PersistenceHelper;
import wt.fc.QueryResult;
import wt.method.RemoteAccess;
import wt.method.RemoteMethodServer;
import wt.query.QueryException;
import wt.query.QuerySpec;
import wt.util.WTException;

public class ChangeObjectExtraction implements RemoteAccess{

	public static void main(String[] args) throws MalformedURLException {
		// TODO Auto-generated method stub
		
		String serviceName = "MethodServer";
		URL wc_url = new URL("http://localhost/Windchill/");
		System.out.println(wc_url.toString());
		
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
	
	public static void extractChangeObj() {
		
		List<WTChangeIssue> changeIssue = new ArrayList<WTChangeIssue>();
		List<WTChangeRequest2> changeRequest = new ArrayList<WTChangeRequest2>();
		List<WTChangeActivity2> changeActivity = new ArrayList<WTChangeActivity2>();
		List<WTChangeOrder2> changeOrder = new ArrayList<WTChangeOrder2>();
		
		QuerySpec qspec=null;
		QuerySpec qspec1 = null;
		QuerySpec qspec2 = null;
		QuerySpec qspec3 = null;
		try {
			//qspec
			qspec = new QuerySpec(WTChangeIssue.class);
			QueryResult qres = PersistenceHelper.manager.find(qspec);
			
			while (qres.hasMoreElements()) {
				changeIssue.add((WTChangeIssue) qres.nextElement());
			}
			
			//qspec1
			qspec1 = new QuerySpec(WTChangeRequest2.class);
			QueryResult qres1 = PersistenceHelper.manager.find(qspec1);
			
			while(qres1.hasMoreElements()) {
				changeRequest.add((WTChangeRequest2) qres1.nextElement());
			}
			
			//qspec2
			qspec2 = new QuerySpec(WTChangeActivity2.class);
			QueryResult qres2 = PersistenceHelper.manager.find(qspec2);
			
			while(qres2.hasMoreElements()) {
				changeActivity.add((WTChangeActivity2) qres2.nextElement());
			}
			
			//qspec3
			qspec3 = new QuerySpec(WTChangeOrder2.class);
			QueryResult qres3 = PersistenceHelper.manager.find(qspec3);
			
			while(qres3.hasMoreElements()) {
				changeOrder.add((WTChangeOrder2) qres3.nextElement());
			}
		} catch (WTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for (int i=0; i<changeIssue.size(); i++) {
			System.out.println("ChangeIssue Name : " + changeIssue.get(i).getName());
			System.out.println("ChangeIssue Number : " + changeIssue.get(i).getNumber());
			System.out.println("ChangeIssue Master : " + changeIssue.get(i).getMaster());
			System.out.println("ChangeIssue Description : " + changeIssue.get(i).getDescription());
			System.out.println("ChangeIssue Requester : " + changeIssue.get(i).getRequester());
			System.out.println("ChangeIssue LifeCycleName : " + changeIssue.get(i).getLifeCycleName());
			System.out.println("ChangeIssue IssuePriority : " + changeIssue.get(i).getIssuePriority());
			System.out.println("ChangeIssue Category : " + changeIssue.get(i).getCategory());
			System.out.println("ChangeIssue ConfirmationCategory : " + changeIssue.get(i).getConfirmationCategory());
			System.out.println("*********************************************");
			
		}
		
		
		for (int i=0; i<changeRequest.size(); i++) {
			System.out.println("ChangeRequest Name : " + changeRequest.get(i).getName());
			System.out.println("ChangeRequest Number : " + changeRequest.get(i).getNumber());
			System.out.println("ChangeRequest Master : " + changeRequest.get(i).getMaster());
			System.out.println("ChangeRequest Description : " + changeRequest.get(i).getDescription());
			System.out.println("ChangeRequest LifeCycleName : " + changeRequest.get(i).getLifeCycleName());
			System.out.println("ChangeRequest Category : " + changeRequest.get(i).getCategory());
			System.out.println("*********************************************");
			
		}
		
		for (int i=0; i<changeActivity.size(); i++) {
			System.out.println("ChangeActivity Name : " + changeActivity.get(i).getName());
			System.out.println("ChangeActivity Number : " + changeActivity.get(i).getNumber());
			System.out.println("ChangeActivity Master : " + changeActivity.get(i).getMaster());
			System.out.println("ChangeActivity Description : " + changeActivity.get(i).getDescription());
			System.out.println("ChangeActivity LifeCycleName : " + changeActivity.get(i).getLifeCycleName());
			System.out.println("*********************************************");
			
		}
		
		for (int i=0; i<changeOrder.size(); i++) {
			System.out.println("ChangeOrder Name : " + changeOrder.get(i).getName());
			System.out.println("ChangeOrder Number : " + changeOrder.get(i).getNumber());
			System.out.println("ChangeOrder Master : " + changeOrder.get(i).getMaster());
			System.out.println("ChangeOrder Description : " + changeOrder.get(i).getDescription());
			System.out.println("ChangeOrder LifeCycleName : " + changeOrder.get(i).getLifeCycleName());
			System.out.println("*********************************************");
			
		}
		
	}
}