package application;


import java.beans.XMLDecoder;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import com.soprasteria.connection.LoadDBConnection;
import com.soprasteria.connection.LoadWindchillConnection;
import com.soprasteria.export.ExportObject;
import com.soprasteria.extract.ExtractObjects;
import com.soprasteria.newFeature.AppPreferences;
import com.soprasteria.newFeature.SaveAppPreferences;


import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;

import javafx.scene.layout.HBox;
import javafx.stage.DirectoryChooser;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;


public class SampleController implements Initializable {
	

    @FXML
    private HBox hboxpane;

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
    private TextField saveDirectoryTF;
    
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
    private RadioButton firstButton;
    
    @FXML
    private RadioButton secondButton;
    
    @FXML
    private RadioButton thirdButton;

    @FXML
    private TextField dbPort;

    @FXML
    private PasswordField dbPassword;

    @FXML
    private Button dbTestConnection;

    @FXML
    private TextField tableName;

    @FXML
    private CheckBox distinct;

    @FXML
    private CheckBox where;

    @FXML
    private TextField whereValue;

    @FXML
    private TextField distinctValue;

    @FXML
    private TextField saveDirectory;

    @FXML
    private TextField filename;

    @FXML
    private RadioButton csvButton;

    @FXML
    private ToggleGroup outputFileFormat;

    @FXML
    private RadioButton xlsxButton;

    @FXML
    private TextField csvdelimiter;

    @FXML
    private Button exportdbButton;
    
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

    @FXML
    private ToggleGroup extractionType;

    
   // private ObservableSet<CheckBox> selectedExpObjectsCB = FXCollections.observableSet();
    
    
    @FXML
    void enableDistinctField(ActionEvent event) {
    	if(distinct.isSelected()) {
    		distinctValue.setDisable(false);
    	}else {
    		distinctValue.setDisable(true);
    	}
    }
    
    @FXML
    void enableWhereField(ActionEvent event) {
    	if(where.isSelected()) {
    		whereValue.setDisable(false);
    	}else {
    		whereValue.setDisable(true);
    	}
    }
    
    @FXML
    void enableDelimiterField(ActionEvent event) {
    	if(csvButton.isSelected()) {
    		csvdelimiter.setDisable(false);
    	}else {
    		csvdelimiter.setDisable(true);
    	}
    }
    
	@FXML
	public void handleClick() {
		DirectoryChooser dirChooser = new DirectoryChooser();
		File dir = dirChooser.showDialog(hboxpane.getScene().getWindow());
		if (dir != null) {
			System.out.println("Path : "+dir.getPath());
			saveDirectory.setText(dir.getPath());
		}
		else {
			System.out.println("Chooser was closed!!");
		}
		
	}

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
		
		final String CONFIG = "D:\\WCBulkLoadFX_POC\\appconfig.xml";
		
		File file = new File(CONFIG);
		
		if (file.getParentFile().exists()) {
			AppPreferences appPrefs = null;
			try {
				FileInputStream fin = new FileInputStream(file);
				XMLDecoder x = new XMLDecoder(new BufferedInputStream(fin));
				appPrefs = (AppPreferences) x.readObject();
				
				srcServerHostName.setText(appPrefs.getSrcServerName());
				srcServerCertName.setText(appPrefs.getSrcServerCertName());
				srcServerUsername.setText(appPrefs.getSrcServerUsername());
				srcServerPassword.setText(appPrefs.getSrcServerPassword());
				
				if (appPrefs.getSrcVersion() != null && (appPrefs.getSrcVersion()).equals(firstButton.getText())) {
					firstButton.setSelected(true);
				}else if (appPrefs.getSrcVersion() != null && (appPrefs.getSrcVersion()).equals(secondButton.getText())) {
					secondButton.setSelected(true);
				}else if (appPrefs.getSrcVersion() != null && (appPrefs.getSrcVersion()).equals(thirdButton.getText())) {
					thirdButton.setSelected(true);
				}
				
				
				dbHostField.setText(appPrefs.getHost());
				dbServiceName.setText(appPrefs.getDatabase());
				dbPort.setText(appPrefs.getPort());
				dbUsername.setText(appPrefs.getUsername());
				dbPassword.setText(appPrefs.getPassword());
				
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				System.out.println("The User Config file not found!!!!");
			}
		} else {
			file.getParentFile().mkdirs();
			System.out.println("it doesn't exist!!");
		}
		
		distinctValue.setDisable(true);
		whereValue.setDisable(true);
		csvdelimiter.setDisable(true);
		
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
					SaveAppPreferences.saveDBTabPreferences(dbHostField.getText(), dbServiceName.getText(), 
							dbPort.getText(), dbUsername.getText(), dbPassword.getText());
				} else {
					alert.setContentText("DB Connection Failed.!! Check the input.");
				}
				alert.show();
			}
		});
		
		

		
		
		// Event when onClick of Export button
		exportdbButton.setOnAction(new EventHandler<ActionEvent>() {
						
			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
			
				RadioButton fileformat = (RadioButton) outputFileFormat.getSelectedToggle();
				
				System.out.println("Export Button is Clicked!!");
				
				try {
					ExportObject.initializeValues(dbHostField.getText(), dbServiceName.getText(), dbPort.getText(), 
							dbUsername.getText(), dbPassword.getText(), tableName.getText(), distinct,
							distinctValue.getText(), where, whereValue.getText(), saveDirectory.getText(), 
							filename.getText(), fileformat.getText(), csvdelimiter.getText());
					
					ExportObject.exportObj();
					
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					System.out.println("Ohh! SOmething went wrong : " + e.getMessage());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					System.out.println("Ohh! SOmething went wrong : " + e.getMessage());
				}
				
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
					SaveAppPreferences.saveWCTabPreferences(srcServerHostName.getText(), 
							srcServerCertName.getText(), srcServerUsername.getText(), srcServerPassword.getText(), 
							srcVersion.getSelectedToggle());
				} else {
					alert.setContentText("Source Windchill Connection Failed.!! Check the input.");
				}
				alert.show();
			}
			
			
		});
		
		// Getting values from radio buttons selections
		String sourceVersionSelected = UIUtilityActions.getSelectedValue(srcVersion);
		String targetVersionSelected = UIUtilityActions.getSelectedValue(targetVersion);
		String outputFileFormatSelected = UIUtilityActions.getSelectedValue(outputFileFormat);
		String extractionTypeSelected = UIUtilityActions.getSelectedValue(extractionType);
		String preloadValidationSelected = UIUtilityActions.getSelectedValue(preLoadValidationSchema);
		
		
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
