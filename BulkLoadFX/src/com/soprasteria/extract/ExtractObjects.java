package com.soprasteria.extract;

import java.net.MalformedURLException;
import java.util.List;

import com.soprasteria.extract.ExtractAdminObjects;

import javafx.scene.control.TextField;

public class ExtractObjects {

	public static void beginExtract(List<String> selectedValueList, TextField srcServerHostName, TextField srcServerUsername, TextField srcServerPassword, String exportPath) throws MalformedURLException {
		// Retrieve the objects selected to be exported
		String sourceServer = srcServerHostName.getText();
		String username = srcServerUsername.getText();
		String password = srcServerPassword.getText();
		
		for(String objName:selectedValueList) {
			System.out.println("Extraction begins for the object - "+objName);
			
			if(objName.equalsIgnoreCase("WTPart")) {
				// WTPart extraction process should begin
				String typeName = "wt.part.WTPart";
				ExtractWTPart.initialize(typeName,sourceServer,username,password,exportPath);
			}
			
			if(objName.equalsIgnoreCase("All Administrative Objects")) {
				// ALL Administrative Object extraction process begins here
				ExtractAdminObjects.initialize(sourceServer, username, password);
			}
			
			// Extraction process for WTDocument objects
			if(objName.equalsIgnoreCase("WTDocument")) {
				String typeName = "wt.doc.WTDocument";
				ExtractWTDocument.initialize(typeName, sourceServer, username, password, exportPath);
			}
		}
	}

}
