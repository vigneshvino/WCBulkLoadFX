package application;

import java.rmi.RemoteException;

import com.ptc.core.foundation.type.server.impl.TypeHelper;
import com.ptc.core.meta.common.TypeIdentifier;

import wt.session.SessionHelper;
import wt.type.TypedUtilityServiceHelper;
import wt.util.WTException;

public class TestGettingSoftTypeValue {
	
	
	/**
	 * Getting the list of sub types for a given type name
	 * @param args
	 */
	public static void main(String[] args) {
		// Getting the list of soft typea
		try {

			TypeIdentifier typeIdent = TypeHelper.getTypeIdentifier("wt.part.WTPart");
			
			TypeIdentifier[] typeIdentArray = TypedUtilityServiceHelper.service.getSubtypes(typeIdent, true, true, null);
			System.out.println("size of typeIdentArray - "+typeIdentArray.length+" and array value is "+typeIdent);

			for(TypeIdentifier typeIdentifier:typeIdentArray) {
				String typeName = TypedUtilityServiceHelper.service.getLocalizedTypeName(typeIdentifier, SessionHelper.getLocale());
				System.out.println("TypeName we got here is "+typeName);
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
