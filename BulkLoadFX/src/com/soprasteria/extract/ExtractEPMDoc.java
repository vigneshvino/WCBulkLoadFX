package com.soprasteria.extract;

import java.util.ArrayList;
import java.util.List;

import wt.epm.EPMDocument;
import wt.fc.PersistenceHelper;
import wt.fc.QueryResult;
import wt.query.QueryException;
import wt.query.QuerySpec;
import wt.util.WTException;

/*
 * */
public class ExtractEPMDoc {

	public static void main(String[] args) {
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
			System.out.println(epmDocs.get(i).getName());
		}
	}
}
