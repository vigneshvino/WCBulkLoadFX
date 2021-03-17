package com.soprasteria.extract;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import wt.method.RemoteMethodServer;
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
		
		
	}

}
