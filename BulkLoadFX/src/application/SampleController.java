package application;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import com.soprasteria.connection.LoadDBConnection;
import com.soprasteria.connection.LoadWindchillConnection;
import com.soprasteria.extract.ExtractObjects;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

public class SampleController implements Initializable {
	

    @FXML
    private TextField srcServerHostName;

    @FXML
    private TextField srcServerCertName;

    @FXML
    private TextField srcServerUsername;

    @FXML
    private TextField srcServerPassword;

    @FXML
    private Button srcTestConnectionBtn;
    
    @FXML
    private Button exportButton;
    
    @FXML
    private ToggleGroup srcVersion;

    @FXML
    private ToggleGroup targetVersion;

    @FXML
    private TextField dbHostField;
    
    @FXML
    private TextField dbServiceName;

    @FXML
    private TextField dbUsername;

    @FXML
    private TextField dbPort;

    @FXML
    private TextField dbPassword;

    @FXML
    private Button dbTestConnection;

    @FXML
    private ToggleGroup outputFileFormat;

    @FXML
    private ToggleGroup extractionType;

    @FXML
    private ToggleGroup preLoadValidationSchema;

	@FXML
	private Pane userDefinedMapping;

	@FXML
	private Pane stagedData;
	
	@FXML
	private AnchorPane loadTabAnchorPane;
	
	@FXML
	private TextField dirChooserTextField;
	
    @FXML
    private CheckBox CBWTPart;

    @FXML
    private CheckBox CBWTDocument;

    @FXML
    private CheckBox CBEPMDocument;

    @FXML
    private CheckBox CBChangeObjects;

    @FXML
    private CheckBox CBAdminObj;

    @FXML
    private CheckBox CBWfProcess;

    @FXML
    private CheckBox CBIncludeSubtypes;
    
   // private ObservableSet<CheckBox> selectedExpObjectsCB = FXCollections.observableSet();

	//ActionEvent for directory chooser in Load tab
	@FXML
	private void dirChooserAction(ActionEvent event) {
		DirectoryChooser chooser = new DirectoryChooser();
		
		Stage stage = (Stage)loadTabAnchorPane.getScene().getWindow();
		
		File file = chooser.showDialog(stage);
		
		if(file!=null) {
			dirChooserTextField.setText(file.getAbsolutePath());
		}
	}
    
	@SuppressWarnings("unused")
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
		// Event when onClick of Test DB Connection button
		dbTestConnection.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				System.out.println("Test Connection button is clicked..!!!");
				boolean connectionResult = false;
				try {
					connectionResult = LoadDBConnection.verifyDBConnection(dbHostField.getText(),dbServiceName.getText(),dbPort.getText(),dbUsername.getText(),dbPassword.getText());
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Alert alert = new Alert(Alert.AlertType.INFORMATION);
				alert.setTitle("DB Connection Test");
				alert.setHeaderText("Windchill Migration Utility");
				if(connectionResult) {
					alert.setContentText("DB Connection success !!!");
				} else {
					alert.setContentText("DB Connection Failed.!! Check the input.");
				}
				alert.show();
			}
		});
		
		// Event when onClick of source windchill connection button
		srcTestConnectionBtn.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				boolean wc_connectionResult = false;
				System.out.println("Test Connection button on source windchill test clicked");
				try {
					wc_connectionResult = LoadWindchillConnection.verifyWindchillConnection(srcServerHostName.getText(), srcServerUsername.getText(), srcServerPassword.getText());
				} catch (MalformedURLException | RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Alert alert = new Alert(Alert.AlertType.INFORMATION);
				alert.setTitle("Windchill Connection Test");
				alert.setHeaderText("Windchill Migration Utility");
				if(wc_connectionResult) {
					alert.setContentText("Source Windchill Connection success !!!");
				} else {
					alert.setContentText("Source Windchill Connection Failed.!! Check the input.");
				}
				alert.show();
			}
		});
		
		// Event when a validation type is selected
		preLoadValidationSchema.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {

			@Override
			public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {

				RadioButton btn = (RadioButton) newValue;
				String typeOfValidation = btn.getText();
				if(typeOfValidation.equals("User Defined Mapping")) {
					userDefinedMapping.setVisible(true);
					stagedData.setVisible(false);
				}
				else {
					userDefinedMapping.setVisible(false);
					stagedData.setVisible(true);
				}
			}
		});
		
		// Getting values from radio buttons selections
		String sourceVersionSelected = UIUtilityActions.getSelectedValue(srcVersion);
		String targetVersionSelected = UIUtilityActions.getSelectedValue(targetVersion);
		String outputFileFormatSelected = UIUtilityActions.getSelectedValue(outputFileFormat);
		String extractionTypeSelected = UIUtilityActions.getSelectedValue(extractionType);
		//String preloadValidationSelected = UIUtilityActions.getSelectedValue(preLoadValidationSchema);
		
		
		// Get the selected text boxes in extraction tab under 'select specific type' pane
		exportButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				List<String> selectedValueList = new ArrayList<String>();
				selectedValueList = UIUtilityActions.getSelectedExportObjectsList(CBWTPart,CBWTDocument,CBWfProcess,CBIncludeSubtypes,CBEPMDocument,CBChangeObjects,CBAdminObj);
				System.out.println("Total number of objects selected is "+selectedValueList.size()+" list - "+selectedValueList);
				try {
					ExtractObjects.beginExtract(selectedValueList, srcServerHostName, srcServerUsername, srcServerPassword);
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}
	
}
