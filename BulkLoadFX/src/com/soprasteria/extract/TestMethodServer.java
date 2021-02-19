package com.soprasteria.extract;

import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;

import wt.method.RemoteAccess;
import wt.method.RemoteMethodServer;

public class TestMethodServer implements RemoteAccess{
	public static void main(String[] args) throws MalformedURLException {
		String serviceName = "MethodServer";
		final URL wc_url = new URL("http://localhost:80/Windchill/");
		
		RemoteMethodServer rms = RemoteMethodServer.getInstance(wc_url, serviceName);
		
		try {
			System.out.println("Please, Check the MS");
			rms.invoke("testMethod", "com.soprasteria.extract.TestMethodServer", null, new Class[] {}, new Object[] {});
			System.out.println("Printing is done on MS");
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void testMethod() {
		System.out.println("Hello!!, Printing on MS");
	}
}
