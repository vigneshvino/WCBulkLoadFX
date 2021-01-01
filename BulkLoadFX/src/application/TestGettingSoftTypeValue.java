package application;

import java.rmi.RemoteException;

import com.ptc.core.foundation.type.server.impl.TypeHelper;
import com.ptc.core.meta.common.TypeIdentifier;

import wt.fc.PersistenceHelper;
import wt.fc.QueryResult;
import wt.part.WTPart;
import wt.query.QuerySpec;
import wt.query.SearchCondition;
import wt.session.SessionHelper;
import wt.type.TypeDefinitionReference;
import wt.type.TypedUtility;
import wt.type.TypedUtilityServiceHelper;
import wt.util.WTException;

public class TestGettingSoftTypeValue {
	
	public static void main(String[] args) {
		// Getting the list of soft typea
		try {

			TypeIdentifier typeIdent = TypeHelper.getTypeIdentifier("WCTYPE|wt.part.WTPart");
			
			TypeIdentifier[] typeIdentArray = TypedUtilityServiceHelper.service.getSubtypes(typeIdent, true, true, null);
			System.out.println("size of typeIdentArray - "+typeIdentArray.length+" and array value is ");

			for(TypeIdentifier typeIdentifier:typeIdentArray) {
				String typeName = TypedUtilityServiceHelper.service.getLocalizedTypeName(typeIdentifier, SessionHelper.getLocale());
				System.out.println("TypeIdentifier Value is "+typeIdentifier+" typename is "+typeName);
				TypeDefinitionReference typeDefReference = TypedUtility.getTypeDefinitionReference(typeIdentifier.getTypename());
				System.out.println("TypeDefinitionReference value is "+typeDefReference);
				
			}
			
		} catch (RemoteException | WTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} /*
			  catch (WTPropertyVetoException e) { // TODO Auto-generated catch block
			  e.printStackTrace(); }
			 */
	}

}
