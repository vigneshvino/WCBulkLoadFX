package com.soprasteria.extract;

import java.net.MalformedURLException;
import java.util.List;

import javafx.scene.control.TextField;

public class ExtractObjects {

	public static void beginExtract(List<String> selectedValueList, TextField srcServerHostName, TextField srcServerUsername, TextField srcServerPassword) throws MalformedURLException {
		// Retrieve the objects selected to be exported
		String sourceServer = srcServerHostName.getText();
		String username = srcServerUsername.getText();
		String password = srcServerPassword.getText();
		
		for(String objName:selectedValueList) {
			if(objName.equalsIgnoreCase("WTPart")) {
				// WTPart extraction process should begin
				String typeName = "wt.part.WTPart";
				ExtractWTPart.initialize(typeName,sourceServer,username,password);
			}
			
			if(objName.equalsIgnoreCase("All Administrative Objects")) {
				// ALL Administrative Object extraction process begins here
				ExtractAdminObjects.initialize(sourceServer, username, password);
			}
		}
	}

}
