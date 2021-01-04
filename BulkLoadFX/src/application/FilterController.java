package application;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;

public class FilterController implements Initializable {
	
	@FXML
	private ToggleGroup containerType;
	
	@FXML
	private TitledPane containerPane;
	
	@FXML
	private TitledPane folderPane;
	
	@FXML
	private TitledPane objectPane;

	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		//Event when a container type is selected
		containerType.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {

			@Override
			public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
				
				RadioButton radio = (RadioButton)newValue;
				String val = radio.getText();
				if(val.equals("Container")) {
					containerPane.setVisible(true);
					folderPane.setVisible(false);
					objectPane.setVisible(false);
				}
				else if(val.equals("Folder")) {
					containerPane.setVisible(false);
					folderPane.setVisible(true);
					objectPane.setVisible(false);
				}
				else {
					containerPane.setVisible(false);
					folderPane.setVisible(false);
					objectPane.setVisible(true);
				}
			}
		});		
		
	}

}
