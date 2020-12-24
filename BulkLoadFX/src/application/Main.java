package application;
	
import java.util.Optional;

import org.springframework.context.event.EventListener;

import com.soprasteria.connection.LoadWindchillConnection;
import com.soprasteria.export.ExportObject;
import com.soprasteria.newFeature.SaveAppPreferences;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/****
 * Main class for the application
 * @author vvigneshwaran
 *
 */
public class Main extends Application {
	
	@Override
	public void start(Stage primaryStage) {
		try {
			// Main window
			HBox root = (HBox)FXMLLoader.load(getClass().getResource("Test.fxml"));
			Scene scene = new Scene(root,645,550);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setTitle("WCMigratorFX v1.0");
			primaryStage.setResizable(false);
			primaryStage.show();
			primaryStage.setOnCloseRequest(confirmCloseEventHandler);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private EventHandler<WindowEvent> confirmCloseEventHandler = new EventHandler<WindowEvent>() {

		@Override
		public void handle(WindowEvent event) {
			// TODO Auto-generated method stub
			showConfirmation();
			
		}
	};
	
	private int showConfirmation() {
		
		Alert confirmAlert = new Alert(AlertType.CONFIRMATION,"", ButtonType.YES,ButtonType.NO);
		confirmAlert.setTitle(String.format("Closing Application"));
		confirmAlert.setHeaderText("Do you want save your preferences before closing the Application!!!");
		
		Optional<ButtonType> option = confirmAlert.showAndWait();
		
		if (option.get() == ButtonType.YES) {
			SaveAppPreferences saveAppPrefs = new SaveAppPreferences();
			saveAppPrefs.setAppPreferences();
			
			System.out.println("Config file created!!");
			
		} else if (option.get() == ButtonType.NO) {
			return 0;
		}
		return 0;
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
}
