package com.soprasteria.extract;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Set;
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
import wt.part.WTPartHelper;
import wt.query.QuerySpec;
import wt.query.SearchCondition;
import wt.services.applicationcontext.implementation.DefaultServiceProvider;
import wt.session.SessionHelper;
import wt.type.TypeDefinitionReference;
import wt.type.TypedUtility;
import wt.type.TypedUtilityServiceHelper;
import wt.util.WTException;
import wt.util.WTPropertyVetoException;
import wt.vc.VersionControlException;
import wt.vc.VersionControlHelper;

public class ExtractWTPart implements RemoteAccess {

	private static final DefinitionDescriptorFactory DESCRIPTOR_FACTORY = (DefinitionDescriptorFactory) DefaultServiceProvider
			.getService(DefinitionDescriptorFactory.class, "default");

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
			rms.invoke("start", "com.soprasteria.extract.ExtractWTPart", null, new Class[] { String.class, String.class },
					new Object[] { typeName, exportPath });
			System.out.println("After this point logs will go to MS");

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

	public static void start(String type, String exportPath) throws WTException, IOException {
		// Export process starts from here
		System.out.println("Object name from the application - " + type);
		
		// Get the soft types list for input type here
		TypeIdentifier typeIdent = TypeHelper.getTypeIdentifier(type);
		TypeIdentifier[] typeIdentArray = TypedUtilityServiceHelper.service.getSubtypes(typeIdent, true, true, null);
		System.out.println("Total number of subtypes for the type - "+type+" is "+typeIdentArray.length);
		
		// Storing the part numbers of the sub types
		Set<String> subTypesNumberSet = new HashSet<String>();
		
		// Querying the part subtypes and writing on  csv files
		for(TypeIdentifier typeIdentifier:typeIdentArray) {
			List<String> rows = new ArrayList<String>();
			ArrayList<String> ibaList = new ArrayList<String>();
			ArrayList<String> ibaNameList = new ArrayList<String>();
			ArrayList<String> ibaValuesList = new ArrayList<String>();
			
			String typeName = null;
			
			HashMap<WTPart, ArrayList<String>> objectList = new HashMap<WTPart, ArrayList<String>>();
			String typeDisplayName = TypedUtilityServiceHelper.service.getLocalizedTypeName(typeIdentifier, SessionHelper.manager.getLocale());
			TypeDefinitionReference typeDefRef = TypedUtility.getTypeDefinitionReference(typeIdentifier.getTypeInternalName());
			QuerySpec qspec = new QuerySpec(WTPart.class);
			qspec.appendWhere(new SearchCondition(WTPart.class, "typeDefinitionReference.key.id", SearchCondition.EQUAL,typeDefRef.getKey().getId()),new int[] {0});
			QueryResult qresult = PersistenceHelper.manager.find(qspec);
			System.out.println("Number of parts in this type - "+typeDisplayName+" is "+qresult.size());
			while (qresult.hasMoreElements()) {
				WTPart part = (WTPart) qresult.nextElement();
				typeName = TypedUtilityServiceHelper.service.getLocalizedTypeName(part, SessionHelper.manager.getLocale());
				subTypesNumberSet.add(part.getNumber());
				String entry = preparePartEntry(part);
				System.out.println("Entry is "+entry);
				IBAHolder ibaHolder = part;
				ibaList = getAttributeValueFromIBAHolder(ibaHolder);
				// IBAList has the values of both IBAName and its value. Both are display identifiers.
				// TODO check here for future change to add internal names instead of display names
				for(String str:ibaList) {
					String[] strArr = str.split(":");
					String name = strArr[0];
					// One time addition of iba names
					if((ibaNameList.size() != ibaList.size()) ) {
					ibaNameList.add(name);
					}
					String value = strArr[1];
					if((ibaValuesList.size() != ibaList.size())) {
					ibaValuesList.add(value);
					}
				}
				
			//	rows.add(ibaList);
				System.out.println("ibaNameList is "+ibaNameList);
				String ibaValues = String.join(";", ibaValuesList);
				entry = entry + ibaValues;
				rows.add(entry);
				System.out.println("Entry after added iba values is "+entry);
				System.out.println("Size of ibaList is "+ibaList.size()+" - "+ibaList);
				ibaValuesList.clear();
			}
			// Pass the row list and iba list for csv writing
			if(typeName != null) {
			writeTocsvFile(rows,ibaNameList,typeName,exportPath);
			}
			
	//		TypeIdentifier typeIdentifier = TypeIdentifierUtility.getTypeIdentifier("wt.part.WTPart");
			System.out.println("TypeIdentifier value - "+typeIdentifier+" getTypeName - "+typeIdentifier.getTypename());
	//		getSoftAttributes(typeIdentifier, SessionHelper.manager.getLocale());
		}
		
		// Performing the query operation on normal wtparts and writing on csv file
		
		QuerySpec qspec = new QuerySpec(WTPart.class);
		QueryResult qresult = PersistenceHelper.manager.find(qspec);
		System.out.println("Query result size in wtpart class is " + qresult.size());
		
		List<String> rows_genericPart = new ArrayList<String>();
		ArrayList<String> ibaList_genericPart = new ArrayList<String>();
		ArrayList<String> ibaNameList_genericPart = new ArrayList<String>();
		ArrayList<String> ibaValuesList_genericPart = new ArrayList<String>();
		
		while(qresult.hasMoreElements()) {
			WTPart genericPart = (WTPart) qresult.nextElement();
			if(!(subTypesNumberSet.contains(genericPart.getNumber()))) {
				String entry = preparePartEntry(genericPart);
				System.out.println("Entry for WTPart is "+entry);
				IBAHolder ibaHolder = genericPart;
				ibaList_genericPart = getAttributeValueFromIBAHolder(ibaHolder);
				// IBAList has the values of both IBAName and its value. Both are display identifiers.
				// TODO check here for future change to add internal names instead of display names
				if(ibaList_genericPart.size() > 0) {
					for(String str:ibaList_genericPart) {
						String[] strArr = str.split(":");
						String name = strArr[0];
						// One time addition of iba names
						if((ibaNameList_genericPart.size() != ibaList_genericPart.size()) ) {
							ibaNameList_genericPart.add(name);
						}
						String value = strArr[1];
						if((ibaValuesList_genericPart.size() != ibaList_genericPart.size())) {
							ibaValuesList_genericPart.add(value);
						}
					}
					
					System.out.println("ibaNameList is "+ibaNameList_genericPart);
					String ibaValues = String.join(";", ibaValuesList_genericPart);
					entry = entry + ibaValues;
					rows_genericPart.add(entry);
					System.out.println("Entry after added iba values is "+entry);
					System.out.println("Size of ibaList is "+ibaList_genericPart.size()+" - "+ibaList_genericPart);
					ibaValuesList_genericPart.clear();
					
				} else {
					rows_genericPart.add(entry);
				}
				
				
			//	rows.add(ibaList);
				
			}
			
		}
		
		String typeName = "WTPart";
		writeTocsvFile(rows_genericPart,ibaNameList_genericPart,typeName,exportPath);
	}

	private static String preparePartEntry(WTPart part) throws VersionControlException {
		// TODO Auto-generated method stub
		String entry = part.getNumber() + ";" + part.getName() + ";" + part.getType() + ";"
				+ Boolean.toString(part.isEndItem()) + ";" + part.getDefaultTraceCode().toString() + ";"
				+ part.getGenericType().toString() + ";" + part.getFolderPath() + ";"
				+ part.getOrganizationName() + ";" + VersionControlHelper.getVersionIdentifier(part).getValue()
				+ ";" + VersionControlHelper.getIterationDisplayIdentifier(part).toString() + ";"
				+ part.getViewName() + ";" + part.getLifeCycleState().getDisplay() + ";" + part.getLifeCycleName() + ";" + part.getSource().getDisplay() + ";" + part.getDefaultUnit().getDisplay() + ";" + Boolean.toString(part.isCollapsible()) + ";" + part.getCreatorName() + ";" + part.getModifierName() + ";" + part.getCreateTimestamp().toGMTString() + ";" +
				part.getModifyTimestamp().toGMTString() + ";";
		return entry;
	}

	private static void writeTocsvFile(List<String> rows, ArrayList<String> ibaList, String typeName, String exportPath) throws IOException {
		// TODO Auto-generated method stub
		FileWriter csvWriter = new FileWriter(exportPath+File.separator+typeName+".csv");
		csvWriter.append("NUMBER;NAME;OBJECTTYPE;ENDITEM;TRACECODE;GENERICTYPE;FOLDER_LOCATION;ORGANIZATION_ID;REVISION;ITERATION;VIEW;STATE;LIFECYCLE_TEMPLATE;SOURCE;DEFAULT_UNIT;COLLAPSIBLE;CREATED_BY;MODIFIED_BY;CREATED_DATE;MODIFIED_DATE");
		csvWriter.append(";");
		for (String ibaName:ibaList) {
			csvWriter.append(ibaName);
			csvWriter.append(";");
		}
		csvWriter.append("\n");
		for(String entry:rows) {
			csvWriter.append(entry);
			csvWriter.append("\n");
		}
		
		csvWriter.flush();
		csvWriter.close();
		
		System.out.println("CSV File was generated successfully");
	}

	private static ArrayList<String> getAttributeValueFromIBAHolder(IBAHolder ibaHolder) throws RemoteException, WTException {
		// TODO Auto-generated method stub
		NumberFormat formatter = new DecimalFormat("#.########");
		ArrayList<String> ibaValuesArrayList = new ArrayList<String>();
		
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
						
						// Getting the Display name of attribute's internal name
			//			TypeIdentifier attTypeidentifier = TypedUtility.getTypeIdentifier(typeIdentifier);
			//			TypeDefinitionReadView typeView = TypeDefinitionServiceHelper.service.getTypeDefView(typeIdentifier);
						AttributeDefinitionReadView attView = TypeDefinitionServiceHelper.service.getAttributeDefView(ats.getAttributeTypeIdentifier());/*typeView.getAttributeByName(internalName);*/
						String ibaDisplayNameValue = attView.getPropertyValueByName("displayName").getValueAsString();
						System.out.println("IBADisplayName - "+ibaDisplayNameValue);
						
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
						String displayIBAValue = null;
						if(legalValueSet instanceof EnumeratedSet) {
							EnumeratedSet enumSet = (EnumeratedSet) legalValueSet.getIntersection(legalValueSet);
							System.out.println("enumSet getElements value is "+enumSet.getElements());
							if(enumSet != null) {
								System.out.println("attValue here is "+attValue);
								EnumerationEntryIdentifier identifier = enumSet.getElementByKey(attValue.toString());
								System.out.println("identifier value is "+identifier);
								if((identifier != null) && (identifier.getKey() != null)) {
									String enumKey = (String) identifier.getKey();
									if(attValue.equals(enumKey)) {
										DefinitionDescriptor defValue = DESCRIPTOR_FACTORY.get(identifier, null, SessionHelper.manager.getLocale());
										displayIBAValue = defValue.getDisplay();
										System.out.println("*** display name of iba got through enum definition is "+displayIBAValue);
									}
								}
							}
						}
						ibaValuesArrayList.add(ibaDisplayNameValue+":"+displayIBAValue);
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
		return ibaValuesArrayList;
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
