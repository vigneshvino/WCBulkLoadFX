package com.soprasteria.extract;

import java.awt.List;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import wt.fc.PersistenceHelper;
import wt.fc.QueryResult;
import wt.fc.WTObject;
import wt.lifecycle.LifeCycleHelper;
import wt.lifecycle.LifeCycleTemplate;
import wt.method.RemoteAccess;
import wt.method.RemoteMethodServer;
import wt.org.WTOrganization;
import wt.org.WTUser;
import wt.query.QueryException;
import wt.query.QuerySpec;
import wt.team.TeamTemplate;
import wt.util.WTException;
import wt.vc.views.View;

public class ExtractAdminObjects implements RemoteAccess{
	
	public static void initialize(String sourceServer, String username, String password) throws MalformedURLException {
		
		String serviceName = "MethodServer";
		if(!sourceServer.endsWith("/")) {
			sourceServer = sourceServer + "/";
		}
		
		URL wc_url = new URL(sourceServer);
		RemoteMethodServer rms = RemoteMethodServer.getInstance(wc_url, serviceName);
		rms.setUserName(username);
		rms.setPassword(password);
		
		ArrayList<ArrayList<String>> result = new ArrayList<ArrayList<String>>();
		HashMap<String, ArrayList<String>> mapLCStates = new HashMap<String, ArrayList<String>>();
		
		try {
			result = (ArrayList<ArrayList<String>>) rms.invoke("fetchData", "com.soprasteria.extract.ExtractAdminObjects", null, new Class[] {String.class}, new Object[] {});
			mapLCStates = (HashMap<String, ArrayList<String>>) rms.invoke("fetchLCStates", "com.soprasteria.extract.ExtractAdminObjects", null, new Class[] {String.class}, new Object[] {});
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("----**USERS**-------------------------");
		System.out.println(result.get(0));
		System.out.println("--------------------------------------");
		System.out.println();
		System.out.println("----**TEAM_TEMPLATES**------------------");
		System.out.println(result.get(1));
		System.out.println("----------------------------------------");
		System.out.println();
		System.out.println("----**ORAGNIZATIONS**------------------");
		System.out.println(result.get(2));
		System.out.println("----------------------------------------");
		System.out.println();
		System.out.println("----**TABLE_VIEWS**------------------");
		System.out.println(result.get(3));
		System.out.println("----------------------------------------");
		System.out.println();
		System.out.println("----**LIFE_CYCLE && STATES**------------------");
		
		for (Map.Entry<String, ArrayList<String>> lcEntry : mapLCStates.entrySet()) {
			String lcName = lcEntry.getKey();
			ArrayList<String> lcstate = lcEntry.getValue();
			System.out.println("LifeCycle_Template : " + lcName);
			System.out.println("LifeCycle_States : " + lcstate);
			System.out.println("********");
		}
		System.out.println("----------------------------------------");
		
		
	}
	
	public static ArrayList<ArrayList<String>> fetchData() throws WTException {
		
		ArrayList<ArrayList<String>> fullData = new ArrayList<ArrayList<String>>();
		
		ArrayList<WTUser> user = new ArrayList<WTUser>();
		ArrayList<String> username = new ArrayList<String>();
		
		QuerySpec qs1 = new QuerySpec(WTUser.class);
		QueryResult userQueryResult = PersistenceHelper.manager.find(qs1);
		
		while(userQueryResult.hasMoreElements()) {
			user.add((WTUser)userQueryResult.nextElement());
		}
		
		for(int i=0; i<user.size(); i++) {
			username.add(user.get(i).getName().toString());
		}
		
		fullData.add(username);
		
		ArrayList<TeamTemplate> teamTemplate = new ArrayList<TeamTemplate>();
		ArrayList<String> teamTemplateName = new ArrayList<String>();
		
		QuerySpec qs2 = new QuerySpec(TeamTemplate.class);
		QueryResult teamTempQueryResult = PersistenceHelper.manager.find(qs2);
		
		while(teamTempQueryResult.hasMoreElements()) {
			teamTemplate.add((TeamTemplate) teamTempQueryResult.nextElement());
		}
		
		for(int i=0; i<teamTemplate.size(); i++) {
			teamTemplateName.add(teamTemplate.get(i).getName().toString());
		}
		
		fullData.add(teamTemplateName);
		
		ArrayList<WTOrganization> orgs = new ArrayList<WTOrganization>();
		ArrayList<String> orgName =  new ArrayList<String>();
		
		QuerySpec qs3 =  new QuerySpec(WTOrganization.class);
		QueryResult orgQueryResult = PersistenceHelper.manager.find(qs3);
		
		while(orgQueryResult.hasMoreElements()) {
			orgs.add((WTOrganization) orgQueryResult.nextElement());
		}
		
		for(int i=0; i<orgs.size(); i++) {
			orgName.add(orgs.get(i).getName().toString());
		}
		
		fullData.add(orgName);
		
		ArrayList<View> view = new ArrayList<View>();
		ArrayList<String> viewName = new ArrayList<String>();
		
		QuerySpec qs5 = new QuerySpec(View.class);
		QueryResult viewQueryResult = PersistenceHelper.manager.find(qs5);
		
		while (viewQueryResult.hasMoreElements()) {
			view.add((View) viewQueryResult.nextElement());
		}
		
		for(int i=0; i<view.size(); i++) {
			viewName.add(view.get(i).getName());
		}
		
		fullData.add(viewName);
		
		return fullData;
	}
	
	
	public static HashMap<String, ArrayList<String>> fetchLCStates() throws WTException {
			
			HashMap<String, ArrayList<String>> lcStates = new HashMap<String, ArrayList<String>>();
			
			ArrayList<LifeCycleTemplate> lcTemplate = new ArrayList<LifeCycleTemplate>();
/*			String lcTemplateName = null;
			Vector<LifeCycleState> states = new Vector<LifeCycleState>();
			ArrayList<String> states = new ArrayList<String>();
	 
*/
		QuerySpec qs4 = new QuerySpec(LifeCycleTemplate.class);
		QueryResult lcQueryResult = PersistenceHelper.manager.find(qs4);
		
		while(lcQueryResult.hasMoreElements()) {
			lcTemplate.add((LifeCycleTemplate) lcQueryResult.nextElement());
		}
		
/*		ListIterator<LifeCycleTemplate> lcIterator = lcTemplate.listIterator();
			
		while(lcIterator.hasNext()) {
		lcTemplateName = lcIterator.next().getName();
		System.out.println("LifeCycle : " + lcTemplateName);
		states.addAll(LifeCycleHelper.service.findStates(lcIterator.next()));
		System.out.println("States : " + states.toString());
		lcStates.put(lcTemplateName, states.toArray());
		}
*/
		
		for (int i=0; i<lcTemplate.size(); i++) {
			
			String lcTemplateName = null;
			ArrayList<String> states = new ArrayList<String>();
			lcTemplateName = lcTemplate.get(i).getName();
			states.addAll(LifeCycleHelper.service.findStates(lcTemplate.get(i)));
			
			System.out.println("LifeCycle_Name : " + lcTemplateName + " , " + "LifeCycle_States : " + states);
			lcStates.put(lcTemplateName, states);
		}
		
		
		for (Map.Entry<String, ArrayList<String>> lcEntry : lcStates.entrySet()) {
			String lcName = lcEntry.getKey();
			ArrayList<String> lcstate = lcEntry.getValue();
			System.out.println("LifeCycle_Template : " + lcName + " , " + "LifeCycle_States : " + lcstate);
		}
		
		return lcStates;	
	}
}
