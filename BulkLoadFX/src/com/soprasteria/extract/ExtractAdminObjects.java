package com.soprasteria.extract;


import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.List;

import wt.fc.PersistenceHelper;
import wt.fc.QueryResult;
import wt.lifecycle.LifeCycleHelper;
import wt.lifecycle.LifeCycleTemplate;
import wt.method.RemoteAccess;
import wt.method.RemoteMethodServer;
import wt.org.WTOrganization;
import wt.org.WTUser;
import wt.query.QuerySpec;
import wt.team.TeamTemplate;
import wt.util.WTException;
import wt.vc.views.View;
import wt.lifecycle.State;

/***
 * @author ayusrivastava
 * */


public class ExtractAdminObjects implements RemoteAccess{
	
	public static void initialize(String sourceServer, String username, String password) throws MalformedURLException {
	
		String serviceName = "MethodServer";
		if(!sourceServer.endsWith("/")) {
			sourceServer = sourceServer + "/";
		}
		System.out.println(sourceServer);
		URL wc_url = new URL(sourceServer);
		RemoteMethodServer rms = RemoteMethodServer.getInstance(wc_url,serviceName);
		rms.setUserName(username);
		rms.setPassword(password);
			
		try {
				rms.invoke("fetchData", "com.soprasteria.extract.ExtractAdminObjects", null, new Class[] {}, 
						new Object[] {});
			    rms.invoke("fetchLCStates", "com.soprasteria.extract.ExtractAdminObjects", null, new Class[] {}, 
			    		new Object[] {});
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}
	
	/***
	 * Retrieves Users, Team Templates, Organizations, Views 
	 *  @param
	 *  @return fullData
	 *  @throws WTException
	 * */
	
	public static void fetchData() throws WTException {

		List<List<String>> fullData = new ArrayList<List<String>>();
		
		List<WTUser> user = new ArrayList<WTUser>();
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
		 
		List<TeamTemplate> teamTemplate = new ArrayList<TeamTemplate>();
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
		
		List<WTOrganization> orgs = new ArrayList<WTOrganization>();
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
		
		List<View> view = new ArrayList<View>();
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
		
		System.out.println("----**USERS**-------------------------");
		System.out.println(fullData.get(0));
		CreateXMLFiles.createUserXml(fullData.get(0));
		System.out.println("--------------------------------------");
		System.out.println();
		System.out.println("----**TEAM_TEMPLATES**------------------");
		System.out.println(fullData.get(1));
		CreateXMLFiles.createTeamTemplateXml(fullData.get(1));
		System.out.println("----------------------------------------");
		System.out.println();
		System.out.println("----**ORAGNIZATIONS**------------------");
		System.out.println(fullData.get(2));
		System.out.println("----------------------------------------");
		System.out.println();
		System.out.println("----**VIEWS_TEMPLATES**------------------");
		System.out.println(fullData.get(3));
		CreateXMLFiles.createViewXml(fullData.get(3));;
		System.out.println("----------------------------------------");
		
	}
	
	/***
	 * Retrieves LifeCycle Templates and their states 
	 *  @param
	 *  @return lcStates
	 *  @throws WTException
	 * */
	
	public static void fetchLCStates() throws WTException {
		
		HashMap<String, ArrayList<String>> lcStates = new HashMap<String, ArrayList<String>>();
		
		List<LifeCycleTemplate> lcTemplate = new ArrayList<LifeCycleTemplate>();

		QuerySpec qs4 = new QuerySpec(LifeCycleTemplate.class);
		QueryResult lcQueryResult = PersistenceHelper.manager.find(qs4);
		
		while(lcQueryResult.hasMoreElements()) {
			lcTemplate.add((LifeCycleTemplate) lcQueryResult.nextElement());
		}
		
/*		
  		String lcTemplateName = null;
		Vector<LifeCycleState> states = new Vector<LifeCycleState>();
		ArrayList<String> states = new ArrayList<String>();
		ListIterator<LifeCycleTemplate> lcIterator = lcTemplate.listIterator();
		
		while(lcIterator.hasNext()) {
			lcTemplateName = lcIterator.next().getName();
			System.out.println("LifeCycle : " + lcTemplateName);
			states.addAll(LifeCycleHelper.service.findStates(lcIterator.next()));
			System.out.println("States : " + states.toString());
			lcStates.put(lcTemplateName, states.toArray());
		} 
*/
		String lcTemplateName = "";
		Vector<State> states = new Vector<State>();
		
		
		for (int i=0; i<lcTemplate.size(); i++) {
			
			lcTemplateName = null;
			states.clear();
			ArrayList<String> allstates = new ArrayList<String>();
			
			lcTemplateName = lcTemplate.get(i).getName();
			states.addAll(LifeCycleHelper.service.findStates(lcTemplate.get(i)));
						
			for (int j=0;j<states.size();j++) {
//				System.out.println("LifeCycle_Template : " + lcTemplateName + "\tStates : " + states.get(j).toString());
				allstates.add(states.get(j).toString());
			}
			
			lcStates.put(lcTemplateName, allstates);
		}
		
				
		System.out.println();
		System.out.println("----**LIFE_CYCLE && STATES**------------------");
		CreateXMLFiles.createLifeCycleXml(lcStates);
		
		for (Map.Entry<String, ArrayList<String>> lcEntry : lcStates.entrySet()) {
			String lcName = lcEntry.getKey();
			ArrayList<String> lcstate = lcEntry.getValue();
			System.out.println("LifeCycle_Template : " + lcName);
			System.out.println("LifeCycle_States : " + lcstate);
			System.out.println("********");
		}
		System.out.println("----------------------------------------");
		
	}

}
