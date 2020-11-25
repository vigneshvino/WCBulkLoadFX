package application;

import java.util.ArrayList;
import java.util.List;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;

public class UIUtilityActions {
	
	private static String selectedRadioValue = null;
	
	/**
	 * Getting the selected radio button value from a toggle group
	 * @param inputToggleGroup
	 * @return
	 */
	public static String getSelectedValue(ToggleGroup inputToggleGroup) {
		
		inputToggleGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {

			@Override
			public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
				// TODO Auto-generated method stub
				RadioButton btn = (RadioButton) newValue;
				selectedRadioValue = btn.getText();
				System.out.println(selectedRadioValue);
			}
		});
		return selectedRadioValue;
	}
	
	public static List<String> getSelectedExportObjectsList(CheckBox CBWTPart, CheckBox CBWTDocument,
			CheckBox CBWfProcess, CheckBox CBIncludeSubtypes, CheckBox CBEPMDocument, CheckBox CBChangeObjects,
			CheckBox CBAdminObj) {
		// TODO Auto-generated method stub
		List<String> resultingList = new ArrayList<String>();
		if(CBWTPart.isSelected()) {
			resultingList.add(CBWTPart.getText());
			System.out.println("Value - "+CBWTPart.getText()+" added to the list");
		}
		if(CBWTDocument.isSelected()) {
			resultingList.add(CBWTDocument.getText());
			System.out.println("Value - "+CBWTDocument.getText()+" added to the list");
		}
		if(CBWfProcess.isSelected()) {
			resultingList.add(CBWfProcess.getText());
			System.out.println("Value - "+CBWfProcess.getText()+" added to the list");
		}
		if(CBIncludeSubtypes.isSelected()) {
			resultingList.add(CBIncludeSubtypes.getText());
			System.out.println("Value - "+CBIncludeSubtypes.getText()+" added to the list");
		}
		if(CBEPMDocument.isSelected()) {
			resultingList.add(CBEPMDocument.getText());
			System.out.println("Value - "+CBEPMDocument.getText()+" added to the list");
		}
		if(CBChangeObjects.isSelected()) {
			resultingList.add(CBChangeObjects.getText());
			System.out.println("Value - "+CBChangeObjects.getText()+" added to the list");
		}
		if(CBAdminObj.isSelected()) {
			resultingList.add(CBAdminObj.getText());
			System.out.println("Value - "+CBAdminObj.getText()+" added to the list");
		}
		return resultingList;
	}

}
