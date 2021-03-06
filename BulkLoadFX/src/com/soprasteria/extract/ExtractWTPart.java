package com.soprasteria.extract;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.TreeSet;

import com.ptc.core.command.common.CommandException;
import com.ptc.core.foundation.type.server.impl.TypeHelper;
import com.ptc.core.lwc.client.util.EnumerationConstraintHelper;
import com.ptc.core.lwc.common.view.AttributeDefinitionReadView;
import com.ptc.core.lwc.common.view.PropertyValueReadView;
import com.ptc.core.lwc.common.view.TypeDefinitionReadView;
import com.ptc.core.lwc.server.LWCEnumerationEntryValuesFactory;
import com.ptc.core.lwc.server.PersistableAdapter;
import com.ptc.core.lwc.server.TypeDefinitionServiceHelper;
import com.ptc.core.meta.common.AttributeTypeIdentifier;
import com.ptc.core.meta.common.AttributeTypeIdentifierSet;
import com.ptc.core.meta.common.DataSet;
import com.ptc.core.meta.common.DefinitionIdentifier;
import com.ptc.core.meta.common.DisplayOperationIdentifier;
import com.ptc.core.meta.common.EnumeratedSet;
import com.ptc.core.meta.common.EnumerationEntryIdentifier;
import com.ptc.core.meta.common.IdentifierComparator;
import com.ptc.core.meta.common.TypeIdentifier;
import com.ptc.core.meta.container.common.AttributeTypeSummary;
import com.ptc.core.meta.descriptor.common.DefinitionDescriptor;
import com.ptc.core.meta.descriptor.common.DefinitionDescriptorFactory;
import com.ptc.core.meta.server.TypeIdentifierUtility;
import com.ptc.core.meta.type.command.typemodel.common.GetSoftSchemaAttributesCommand;

import wt.fc.Persistable;
import wt.fc.PersistenceHelper;
import wt.fc.QueryResult;
import wt.iba.definition.litedefinition.AttributeDefDefaultView;
import wt.iba.value.DefaultAttributeContainer;
import wt.iba.value.IBAHolder;
import wt.iba.value.IBAValueUtility;
import wt.iba.value.litevalue.AbstractValueView;
import wt.iba.value.litevalue.FloatValueDefaultView;
import wt.iba.value.litevalue.TimestampValueDefaultView;
import wt.iba.value.service.IBAValueHelper;
import wt.meta.LocalizedValues;
import wt.method.RemoteAccess;
import wt.method.RemoteMethodServer;
import wt.part.WTPart;
import wt.query.QuerySpec;
import wt.query.SearchCondition;
import wt.services.applicationcontext.implementation.DefaultServiceProvider;
import wt.session.SessionHelper;
import wt.type.TypeDefinitionReference;
import wt.type.TypedUtility;
import wt.type.TypedUtilityServiceHelper;
import wt.util.WTException;
import wt.util.WTPropertyVetoException;

public class ExtractWTPart implements RemoteAccess {

	private static final DefinitionDescriptorFactory DESCRIPTOR_FACTORY = (DefinitionDescriptorFactory) DefaultServiceProvider
			.getService(DefinitionDescriptorFactory.class, "default");

	public static void initialize(String typeName, String sourceServer, String username, String password)
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
			rms.invoke("start", "com.soprasteria.extract.ExtractWTPart", null, new Class[] { String.class },
					new Object[] { typeName });
			System.out.println("After this point logs will go to MS");

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

	public static void start(String type) throws WTException, RemoteException {
		// Export process starts from here
		System.out.println("Object name from the application - " + type);
		
		// Get the soft types list for input type here
		TypeIdentifier typeIdent = TypeHelper.getTypeIdentifier(type);
		TypeIdentifier[] typeIdentArray = TypedUtilityServiceHelper.service.getSubtypes(typeIdent, true, true, null);
		for(TypeIdentifier typeIdentifier:typeIdentArray) {
			TypeDefinitionReference typeDefRef = TypedUtility.getTypeDefinitionReference(typeIdentifier.getTypeInternalName());
			QuerySpec qspec = new QuerySpec(WTPart.class);
			qspec.appendWhere(new SearchCondition(WTPart.class, "typeDefinitionReference.key.id", SearchCondition.EQUAL,typeDefRef.getKey().getId()),new int[] {0});
			QueryResult qresult = PersistenceHelper.manager.find(qspec);
			System.out.println("Number of parts in this type - "+typeIdentifier.getTypeInternalName()+" is "+qresult.size());
			while (qresult.hasMoreElements()) {
				WTPart part = (WTPart) qresult.nextElement();
				System.out.println("Part Number - " + part.getNumber() + " Name - " + part.getName() + " Created On - "
						+ part.getCreateTimestamp() +" Status - "+part.getCheckoutInfo()+" Modified By "+part.getModifierName()+ " Modified On - " + part.getModifyTimestamp() + " LC State - "
						+ part.getLifeCycleState() + " Created By - " + part.getCreatorName());
				IBAHolder ibaHolder = part;
				getAttributeValueFromIBAHolder(ibaHolder,typeIdentifier.getTypeInternalName());
			}
	//		TypeIdentifier typeIdentifier = TypeIdentifierUtility.getTypeIdentifier("wt.part.WTPart");
			System.out.println("TypeIdentifier value - "+typeIdentifier+" getTypeName - "+typeIdentifier.getTypename());
			getSoftAttributes(typeIdentifier, SessionHelper.manager.getLocale());
		}
		
/*		QuerySpec qspec = new QuerySpec(WTPart.class);

		QueryResult qresult = PersistenceHelper.manager.find(qspec);
		System.out.println("Query result size in wtpart class is " + qresult.size());*/
		
		

	}

	private static void getAttributeValueFromIBAHolder(IBAHolder ibaHolder, String typeName) throws RemoteException, WTException {
		// TODO Auto-generated method stub
		NumberFormat formatter = new DecimalFormat("#.########");
		
		ibaHolder = IBAValueHelper.service.refreshAttributeContainer(ibaHolder, null, SessionHelper.manager.getLocale(), null);
		DefaultAttributeContainer attContainer = (DefaultAttributeContainer) ibaHolder.getAttributeContainer();
		System.out.println("AttContainer - "+attContainer);
		if(attContainer != null) {
			AttributeDefDefaultView[] theAtts = attContainer.getAttributeDefinitions();
			System.out.println("Attribute Definitions length - "+theAtts.length);
			String key = null;
			String value = null;
			
			for(int i = 0; i < theAtts.length; i++) {
				AbstractValueView[] theValues = attContainer.getAttributeValues(theAtts[i]);
				System.out.println("Getting the attribute values for "+theAtts[i]+" and value is "+theValues);
				
				if(theValues != null) {
					key = new String();
					value = new String();
					
					if(theValues[0] instanceof FloatValueDefaultView) {
						FloatValueDefaultView tempValue = (FloatValueDefaultView) theValues[0];
						key = theAtts[i].getName();
						value = formatter.format(tempValue.getValue());
						
						for(int j = 1;j < theValues.length; j++) {
							tempValue = (FloatValueDefaultView) theValues[j];
							value = value + "," + formatter.format(tempValue.getValue());
						}
					} else if(theValues[0] instanceof TimestampValueDefaultView) {
						TimestampValueDefaultView tempValue = (TimestampValueDefaultView) theValues[0];
						
						key = theAtts[i].getName();
						Timestamp timeStampTemp = tempValue.getValue();
						if((timeStampTemp.getYear()+1900)!=1899) {
							// Add Logic to convert timestamp to locale timezone
						}
					} else {
						key = theAtts[i].getDisplayName();
						System.out.println("theValues length is "+theValues.length);
						for(int j=0; j < theValues.length; j++) {
							System.out.println("theValues[j] - "+theValues[j].getLocalizedDisplayString(SessionHelper.manager.getLocale())+" --- "+theValues[j].getDefinition().getDisplayName());
							System.out.println("IBA value for key - "+key+" is "+theValues[j]);
							value = IBAValueUtility.getLocalizedIBAValueDisplayString(theValues[j], SessionHelper.manager.getLocale());
						}
					}
					
					System.out.println("Key -- "+key+" value -- "+value);
					System.out.println("Internal name of the key is "+theAtts[i].getName()+" Hierarichal display name is "+theAtts[i].getHierarchyDisplayName());
					String internalName = theAtts[i].getName();
					
					PersistableAdapter persAdapter = new PersistableAdapter((Persistable) ibaHolder, null, SessionHelper.manager.getLocale(),new DisplayOperationIdentifier());
					persAdapter.load(internalName);
					Object attValue = persAdapter.get(internalName);
					System.out.println("Value of internal name attribute - "+internalName+" is "+attValue.toString());
					if(attValue != null) {
						AttributeTypeSummary ats = persAdapter.getAttributeDescriptor(internalName);
						
						/*AttributeDefinitionReadView adrv = TypeDefinitionServiceHelper.service.getAttributeDefView(ats.getAttributeTypeIdentifier());
						System.out.println("adrv value is "+adrv);
						if(adrv != null) {
							PropertyValueReadView pvrv = adrv.getPropertyValueByName("displayName");
							if(pvrv != null) {
								String displayValueIBA = pvrv.getValueAsString();
								System.out.println("*** displayValueIBA - "+displayValueIBA);
							}
						}*/
						DataSet legalValueSet = ats.getLegalValueSet();
						if(legalValueSet instanceof EnumeratedSet) {
							System.out.println("Selected attribute is global enumeration ..!! "+legalValueSet);
						}
					/*	EnumerationEntryIdentifier enumIdentifier = ((EnumeratedSet) legalValueSet).getElementByKey(attValue.toString());
						LWCEnumerationEntryValuesFactory eevf = new LWCEnumerationEntryValuesFactory();
						LocalizedValues values = eevf.get(enumIdentifier, Locale.ENGLISH);
						System.out.println("*** Display value is "+values.getDisplay());*/
						if(legalValueSet instanceof EnumeratedSet) {
							EnumeratedSet enumSet = (EnumeratedSet) legalValueSet.getIntersection(legalValueSet);
							System.out.println("enumSet getElements value is "+enumSet.getElements());
							if(enumSet != null) {
								EnumerationEntryIdentifier identifier = enumSet.getElementByKey(attValue.toString());
								System.out.println("identifier value is "+identifier);
								if((identifier != null) && (identifier.getKey() != null)) {
									String enumKey = (String) identifier.getKey();
									if(attValue.equals(enumKey)) {
										DefinitionDescriptor defValue = DESCRIPTOR_FACTORY.get(identifier, null, SessionHelper.manager.getLocale());
										String displayIBAValue = defValue.getDisplay();
										System.out.println("*** display name of iba got through enum definition is "+displayIBAValue);
									}
								}
							}
						}
					}
					// Getting iba display value using typename
			/*		TypeIdentifier identifier = TypedUtility.getTypeIdentifier(typeName);
					TypeDefinitionReadView view = TypeDefinitionServiceHelper.service.getTypeDefView(identifier);
					AttributeDefinitionReadView attReadView = view.getAttributeByName(internalName);
					String displayName = attReadView.getPropertyValueByName("displayName").getValue(SessionHelper.manager.getLocale(), false).toString();
					System.out.println("Display Name of attribute- "+internalName+" and its value is "+displayName);*/
				}
			}
		}
	}

	private static void getSoftAttributes(TypeIdentifier typeIdentifier, Locale locale) throws WTException {
		// TODO Auto-generated method stub
		GetSoftSchemaAttributesCommand getSoftSchemaAttributesCommand = new GetSoftSchemaAttributesCommand();
		getSoftSchemaAttributesCommand.setType_id(typeIdentifier);
		try {
			getSoftSchemaAttributesCommand.setLocale(locale);
		} catch (WTPropertyVetoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		getSoftSchemaAttributesCommand = (GetSoftSchemaAttributesCommand) getSoftSchemaAttributesCommand.execute();
		AttributeTypeIdentifierSet attributeSet = getSoftSchemaAttributesCommand.getAttributes();
		System.out.println(" Size of attribute set "+attributeSet.size());
		LinkedHashMap<String, String> resultAttMap = getAttributesFromATISet(attributeSet);
		System.out.println("Size of attribute map is "+resultAttMap.size());
		System.out.println("resultAttMap is "+resultAttMap);

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static LinkedHashMap<String, String> getAttributesFromATISet(AttributeTypeIdentifierSet attributeSet)
			throws WTException {
		// TODO Auto-generated method stub
		LinkedHashMap linkedHashMap = new LinkedHashMap();
		TreeSet treeSet = new TreeSet(new IdentifierComparator());

		treeSet.addAll(attributeSet);

		DefinitionIdentifier[] arrayDefinitionIdentifier = new DefinitionIdentifier[treeSet.size()];
		byte b = 0;
		for (Iterator iterator = treeSet.iterator(); iterator.hasNext();) {
			arrayDefinitionIdentifier[b++] = (DefinitionIdentifier) iterator.next();
		}
		DefinitionDescriptor[] arrayDefinitionDescriptor = DESCRIPTOR_FACTORY.get(arrayDefinitionIdentifier, null,
				SessionHelper.manager.getLocale());
		String str = "";
		for(DefinitionDescriptor definitionDescriptor: arrayDefinitionDescriptor) {
			AttributeDefinitionReadView attributeDefinitionReadView = TypeDefinitionServiceHelper.service
					.getAttributeDefView((AttributeTypeIdentifier) definitionDescriptor.getIdentifier());
			if(attributeDefinitionReadView != null) {
				PropertyValueReadView propertyValueReadView = attributeDefinitionReadView.getPropertyValueByName("displayName");
				if(propertyValueReadView != null) {
					str = propertyValueReadView.getValueAsString();
					System.out.println("propertyValueReadView not null and IBA label --> "+str+" IBA Internal name --> "+attributeDefinitionReadView.getName());
				}
			}
			linkedHashMap.put(str, attributeDefinitionReadView.getName());
			System.out.println("size of linkedHashmap is "+linkedHashMap.size());
		}
		return linkedHashMap;
	}

}
