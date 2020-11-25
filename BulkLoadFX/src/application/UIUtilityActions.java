package application;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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

}
