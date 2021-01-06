package com.soprasteria.extract;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;


/**
 * @author ayusrivastava
 * */


/*
 * Generates XML Mapping files depending upon the input
 * */

public class CreateXMLFiles {
	
	public static String xmlFilePath = "D:\\WCBulkLoadFX_POC\\xmlFileMappings\\";
	
	/***
	 * Generates the XML Mapping for Users
	 * */
	
	
	public static void createUserXml(List<String> userList) {
		
		String userXmlFile = xmlFilePath + "UserMapping.xml";
		File file = new File(userXmlFile);
		if (!file.getParentFile().exists()) {
			System.out.println("The xmlFileMapping directory does not exists!!");
			file.getParentFile().mkdirs();
			System.out.println("The xmlFileMapping directory created!!");
		}
		
		try {
			DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
			Document document = documentBuilder.newDocument();
			
			//root element
			Element root = document.createElement("ExtractorMapping");
			document.appendChild(root);
			
			//Users element
			Element users = document.createElement("Users");
			root.appendChild(users);
			
			for (int i=0; i<userList.size(); i++) {
				
				//User element
				Element user = document.createElement("User");
				users.appendChild(user);
				
				//srcDbName Attribute
				Attr srcDbName = document.createAttribute("srcDbName");
				srcDbName.setValue(userList.get(i));
				user.setAttributeNode(srcDbName);
				
				//disable Attribute
				Attr disable = document.createAttribute("disable");
				disable.setValue("0");
				user.setAttributeNode(disable);
				
				//targetName Attribute
				Attr targetName = document.createAttribute("targetName");
				targetName.setValue(userList.get(i));
				user.setAttributeNode(targetName);				
			}
		
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			DOMSource domSource = new DOMSource(document);
			StreamResult streamResult = new StreamResult(new File(userXmlFile));
			transformer.transform(domSource, streamResult);
			
		} catch (ParserConfigurationException pce) {
			
			pce.printStackTrace();
		} catch (TransformerConfigurationException tfe) {
			// TODO Auto-generated catch block
			tfe.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/***
	 * Generates the XML Mapping for Team Template
	 * */
	
	
	public static void createTeamTemplateXml(List<String> teamTemplateList) {
		
		String teamTemplateXMLFile = xmlFilePath + "TeamTemplateMapping.xml";
		
		try {
			DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
			Document document = documentBuilder.newDocument();
			
			//root Element
			Element root = document.createElement("ExtractorMapping");
			document.appendChild(root);
			
			//Organizations Element
			Element organizations = document.createElement("Organizations");
			root.appendChild(organizations);
			
			//Organization Element
			Element organization = document.createElement("Organization");
			organizations.appendChild(organization);
			
			//Organization --> name,type,id elements
			
			Attr name = document.createAttribute("name");
			name.setValue("ptc");
			organization.setAttributeNode(name);
			
			Attr type = document.createAttribute("type");
			type.setValue("wt.org.WTOrganization");
			organization.setAttributeNode(type);
			
			Attr id = document.createAttribute("id");
			id.setValue("445");
			organization.setAttributeNode(id);
			
			//Container Element
			Element container = document.createElement("Container");
			organization.appendChild(container);
			
			//Container -->type,name Attribute
			Attr type1 = document.createAttribute("type");
			type1.setValue("wt.inf.container.ExchangeContainer");
			container.setAttributeNode(type1);
			
			Attr name2 = document.createAttribute("name");
			name2.setValue("Site");
			container.setAttributeNode(name2);
			
			for(int i=0; i<teamTemplateList.size(); i++) {
				//TeamTemplate Element
				Element teamTemplate = document.createElement("TeamTemplate");
				container.appendChild(teamTemplate);
				
				//TeamTemplate  --> srcName,targetName Attributes
				Attr srcName = document.createAttribute("srcName");
				srcName.setValue(teamTemplateList.get(i));
				teamTemplate.setAttributeNode(srcName);
				
				Attr targetName = document.createAttribute("targetName");
				targetName.setValue(teamTemplateList.get(i));
				teamTemplate.setAttributeNode(targetName);
			}
			
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			DOMSource domSource = new DOMSource(document);
			StreamResult streamResult = new StreamResult(new File(teamTemplateXMLFile));
			transformer.transform(domSource, streamResult);
			
		} catch (ParserConfigurationException pce) {
			// TODO: handle exception
			pce.printStackTrace();
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/***
	 * Generates the XML Mapping for View
	 * */
	
	public static void createViewXml(List<String> viewList) {
		
		String viewXMLFile = xmlFilePath + "ViewMapping.xml";
		
		try {
			
			DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
			Document document = documentBuilder.newDocument();
			
			//View Element
			Element root = document.createElement("Views");
			document.appendChild(root);
			
			for(int i=0; i<viewList.size(); i++) {
				
				Element view = document.createElement("View");
				root.appendChild(view);
				
				//View  --> srcName,targetName Attributes
				Attr srcName = document.createAttribute("srcName");
				srcName.setValue(viewList.get(i));
				view.setAttributeNode(srcName);
				
				Attr targetName = document.createAttribute("targetName");
				targetName.setValue(viewList.get(i));
				view.setAttributeNode(targetName);
			}
			
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			DOMSource domSource = new DOMSource(document);
			StreamResult streamResult = new StreamResult(new File(viewXMLFile));
			transformer.transform(domSource, streamResult);
			
			
		} catch (ParserConfigurationException pce) {
			// TODO: handle exception
			pce.printStackTrace();
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/***
	 * Generates the XML Mapping for LifeCycle Template
	 * */
	
	public static void createLifeCycleXml(HashMap<String, ArrayList<String>> lifecycleMap) {
		
		String lifecycleXMLFile = xmlFilePath + "LifeCycleMapping.xml";
		
		try {
			
			DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
			Document document = documentBuilder.newDocument();
			
			//root Element
			Element root = document.createElement("ExtractorMapping");
			document.appendChild(root);
			
			//Organizations Element
			Element organizations = document.createElement("Organizations");
			root.appendChild(organizations);
			
			//Organization Element
			Element organization = document.createElement("Organization");
			organizations.appendChild(organization);
			
			//Organization --> name,type,id elements
			
			Attr name = document.createAttribute("name");
			name.setValue("ptc");
			organization.setAttributeNode(name);
			
			Attr type = document.createAttribute("type");
			type.setValue("wt.org.WTOrganization");
			organization.setAttributeNode(type);
			
			Attr id = document.createAttribute("id");
			id.setValue("445");
			organization.setAttributeNode(id);
			
			//Container Element
			Element container = document.createElement("Container");
			organization.appendChild(container);
			
			//Container -->type,name Attribute
			Attr type1 = document.createAttribute("type");
			type1.setValue("wt.inf.container.ExchangeContainer");
			container.setAttributeNode(type1);
			
			Attr name2 = document.createAttribute("name");
			name2.setValue("Site");
			container.setAttributeNode(name2);
			
			for(Map.Entry<String, ArrayList<String>> lcEntry : lifecycleMap.entrySet()) {
				
				String lcName = lcEntry.getKey();
				ArrayList<String> lcState = lcEntry.getValue();
				
				//LifeCycleTemplateMaster Template
				Element lifecycleTemplateMaster = document.createElement("LifeCycleTemplateMaster");
				container.appendChild(lifecycleTemplateMaster);
				
				//LifeCycleTemplateMaster --> srcName,targetName Attribute
				Attr srcName = document.createAttribute("srcName");
				srcName.setValue(lcName);
				lifecycleTemplateMaster.setAttributeNode(srcName);
				
				Attr targetName = document.createAttribute("targetName");
				targetName.setValue(lcName);
				lifecycleTemplateMaster.setAttributeNode(targetName);
				
				for(int i=0; i<lcState.size(); i++) {
					
	//				String state = lcState.get(i);
					//PhaseTemplate Element
					Element phaseTemplate = document.createElement("PhaseTemplate");
					lifecycleTemplateMaster.appendChild(phaseTemplate);
					
					//PhaseTemplate -->srcPhaseState,targetPhaseState,inLatest
					Attr srcPhaseState = document.createAttribute("srcPhaseState");
					srcPhaseState.setValue(lcState.get(i));
					phaseTemplate.setAttributeNode(srcPhaseState);
					
					Attr targetPhaseState = document.createAttribute("targetPhaseState");
					targetPhaseState.setValue(lcState.get(i));
					phaseTemplate.setAttributeNode(targetPhaseState);
					
					Attr inLatest = document.createAttribute("inLatest");
					inLatest.setValue("true");
					phaseTemplate.setAttributeNode(inLatest);
				}
				
				TransformerFactory transformerFactory = TransformerFactory.newInstance();
				Transformer transformer = transformerFactory.newTransformer();
				transformer.setOutputProperty(OutputKeys.INDENT, "yes");
				DOMSource domSource = new DOMSource(document);
				StreamResult streamResult = new StreamResult(new File(lifecycleXMLFile));
				transformer.transform(domSource, streamResult);
			}
			
		} catch (ParserConfigurationException pce) {
			// TODO: handle exception
			pce.printStackTrace();
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
