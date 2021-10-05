package com.soprasteria.extract;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.ptc.core.foundation.type.server.impl.TypeHelper;
import com.ptc.core.meta.common.TypeIdentifier;

import wt.doc.WTDocument;
import wt.fc.PersistenceHelper;
import wt.fc.QueryResult;
import wt.method.RemoteMethodServer;
import wt.query.QuerySpec;
import wt.query.SearchCondition;
import wt.session.SessionHelper;
import wt.type.TypeDefinitionReference;
import wt.type.TypedUtility;
import wt.type.TypedUtilityServiceHelper;
import wt.util.WTException;

public class ExtractWTDocument {
	
	
	public static void initialize(String typeName, String sourceServer, String username, String password, String exportPath)
			throws MalformedURLException {
		// TODO Auto-generated method stub
		String serviceName = "MethodServer";
		if (!sourceServer.endsWith("/")) {
			sourceServer = sourceServer + "/";
		}
		URL wc_url = new URL(sourceServer);
		RemoteMethodServer rms = RemoteMethodServer.getInstance(wc_url, serviceName);
		rms.setUserName(username);
		rms.setPassword(password);

		try {
			rms.invoke("start", "com.soprasteria.extract.ExtractWTDocument", null, new Class[] { String.class, String.class },
					new Object[] { typeName, exportPath });
			System.out.println("After this point logs will go to MS");

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}
	
	/**
	 * Exporting of wtdocument objects
	 * @param type
	 * @param exportPath
	 * @throws WTException
	 * @throws IOException
	 */
	public static void start(String type, String exportPath) throws WTException, IOException {
		
		System.out.println("Object name from the application - " + type);
		
		// Get the soft types list for input type here
		TypeIdentifier typeIdent = TypeHelper.getTypeIdentifier(type);
		TypeIdentifier[] typeIdentArray = TypedUtilityServiceHelper.service.getSubtypes(typeIdent, true, true, null);
		System.out.println("Total number of subtypes for the type - "+type+" is "+typeIdentArray.length);
		
		// Storing the part numbers of the sub types
		Set<String> subTypesNumberSet = new HashSet<String>();
		
		// Querying for the document subtypes and writing it on the csv files
		for(TypeIdentifier typeIdentifier:typeIdentArray) {
			List<String> rows = new ArrayList<String>();
			ArrayList<String> ibaList = new ArrayList<String>();
			ArrayList<String> ibaNameList = new ArrayList<String>();
			ArrayList<String> ibaValuesList = new ArrayList<String>();
			
			String typeName = null;
			
			HashMap<WTDocument, ArrayList<String>> objectList = new HashMap<WTDocument, ArrayList<String>>();
			String typeDisplayName = TypedUtilityServiceHelper.service.getLocalizedTypeName(typeIdentifier, SessionHelper.manager.getLocale());
			TypeDefinitionReference typeDefRef = TypedUtility.getTypeDefinitionReference(typeIdentifier.getTypeInternalName());
			
			QuerySpec qspec = new QuerySpec(WTDocument.class);
			qspec.appendWhere(new SearchCondition(WTDocument.class, "typeDefinitionReference.key.id", SearchCondition.EQUAL, typeDefRef.getKey().getId()),new int[] {0});
			QueryResult qresult = PersistenceHelper.manager.find(qspec);
			System.out.println("Number of documents in this type - "+typeDisplayName+" is "+qresult.size());
			while(qresult.hasMoreElements()) {
				WTDocument doc = (WTDocument) qresult.nextElement();
				
			}
			
		}
		
	}

}
