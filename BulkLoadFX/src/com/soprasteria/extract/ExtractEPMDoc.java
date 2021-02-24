package com.soprasteria.extract;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.ptc.core.foundation.util.common.impl.GetOrganizationDefaultTypeRemoteWorker;
import com.ptc.core.foundation.util.common.impl.IsInstalledPropertyRemoteWorker;
import com.ptc.windchill.cadx.common.util.AssociateUtilities;
import com.ptc.windchill.cadx.common.util.ObjectDependencyUtility;

import wt.epm.EPMDocument;
import wt.fc.PersistenceHelper;
import wt.fc.QueryResult;
import wt.part.WTPart;
import wt.part.WTPartHelper;
import wt.query.QueryException;
import wt.query.QuerySpec;
import wt.util.WTException;
import wt.vc.VersionControlException;

/**
 * @author ayusrivastava
 * */
public class ExtractEPMDoc {
	
	public static String csvFilePath = "D:\\WCBulkLoadFX_POC\\CSV Files\\"; 
	
	public static void main(String[] args) throws IOException, VersionControlException {
		
		String csvFile = csvFilePath+"EPMDocument.csv";
		
		File file = new File(csvFile);
		if(!file.getParentFile().exists()) {
			System.out.println("The CSV File directory does not exists!!");
			file.getParentFile().mkdirs();
			System.out.println("The CSV File directory is created!!");
		}
		
		String header = "CADNAME;AUTHORINGAPPLICATION;COLLAPSIBLE;DEFAULTUNIT;DOCSUBTYPE;DOCTYPE;GLOBALID;NAME;NUMBER;"
				+ "OWNERAPPLICATION;SERIES;CONTAINER_ORGANIZATION_NAME;CONTAINERTYPE;CONTAINER;ORGANIZATION_NAME;"
				+ "CREATEDDATE;MODIFIEDDATE;TYPE;OBJECTNUMBER;BATCH_ID;REVISION;AUTHORINGAPPVERSIONNAME;"
				+ "AUTHORINGAPPVERSIONNUMBER;DESCRIPTION;FOLDERPATH;FORMAT;ITERATION;"
				+ "ITERATIONNOTE;CREATEDBY;MODIFIEDBY;LIFECYCLE;LIFECYCLESTATE;TEAM;TYPE" + "\n";
		
		BufferedWriter writer=null;
		try {
			writer = new BufferedWriter(new FileWriter(file));
			writer.write(header);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		QuerySpec qspec = null;
		QueryResult epmQueryRes = null;
		List<EPMDocument> epmDocs = new ArrayList<EPMDocument>();
		try {
			qspec = new QuerySpec(EPMDocument.class);
			epmQueryRes = PersistenceHelper.manager.find(qspec);	
		} catch (QueryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (WTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		while (epmQueryRes.hasMoreElements()) {
			epmDocs.add((EPMDocument) epmQueryRes.nextElement());
		}
		
		for(int i=0; i<epmDocs.size(); i++) {
			writer.write(
					epmDocs.get(i).getCADName() + ";" +
					epmDocs.get(i).getAuthoringApplication() + ";" +
/*collapsible*/					"0" + ";" +
					epmDocs.get(i).getDefaultUnit() + ";" + 
					"" + ";" + 
					epmDocs.get(i).getDocType() + ";" + 
					epmDocs.get(i).getGlobalID() + ";" +
					epmDocs.get(i).getName() + ";" + 
					epmDocs.get(i).getNumber() + ";" +
					epmDocs.get(i).getOwnerApplication() + ";" + 
/*series*/					"" + ";" + 
/*conatiner_organization_name*/	 "" + ";" +
/*conatiner_type*/			"" + ";" + 
					epmDocs.get(i).getContainerName() + ";" + 
					epmDocs.get(i).getOrganizationName() + ";" + 
					epmDocs.get(i).getCreateTimestamp() + ";" + 
					epmDocs.get(i).getModifyTimestamp() + ";" +
					epmDocs.get(i).getType() + ";" + 
/*objectnumber*/			"" + ";" + 
/*Batch_id*/				"" + ";" +
					epmDocs.get(i).getVersionIdentifier().getValue() + ";" +
					epmDocs.get(i).getAuthoringAppVersion() + ";" +
/*getAuthoringAppVersionNumber*/		"" + ";" +
					epmDocs.get(i).getDescription() + ";" +
					epmDocs.get(i).getFolderPath() + ";" +
					epmDocs.get(i).getFormat() + ";" +
					epmDocs.get(i).getIterationIdentifier().getValue() + ";" + 
					epmDocs.get(i).getIterationNote() + ";" + 
					epmDocs.get(i).getCreatorName() + ";" +
					epmDocs.get(i).getModifierName() + ";" + 
					epmDocs.get(i).getLifeCycleName() + ";" + 
					epmDocs.get(i).getLifeCycleState() + ";" +
					epmDocs.get(i).getTeamName() + ";" +
					epmDocs.get(i).getType() + "\n"
					);

			
//			WTPart linkedWTPart = null;
//			Object allAssocObj = null;
			WTPart[] wtpList = null;
			try {
//				linkedWTPart = AssociateUtilities.getActiveAssociatedPart(epmDocs.get(i));
//				allAssocObj = AssociateUtilities.getAssociatedObjects(epmDocs.get(i));
				wtpList = ObjectDependencyUtility.getAssociated(epmDocs.get(i));
			} catch (WTException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if(wtpList != null) {
//				System.out.println("Associated WTPart List Lenght : " + wtpList.length);
				for(int j=0; j<wtpList.length; j++) {
					WTPart part = wtpList[j];
//					System.out.println("Associated WTPart : " + part.getName());
				}
			}
		}
		writer.close();
	}
}
