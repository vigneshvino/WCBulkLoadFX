package application;
	
import java.util.Optional;

import com.soprasteria.newFeature.SaveAppPreferences;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/****
 * Main class for the application
 * @author vvigneshwaran
 *
 */
public class Main extends Application {

	//to get the value of primary stage object from other classes
	private static Stage pStage;
	
	public static Stage getpStage() {
		return pStage;
	}

	@Override
	public void start(Stage primaryStage) {
		try {
			// Main window
			pStage = primaryStage;
			HBox root = (HBox)FXMLLoader.load(getClass().getResource("Test.fxml"));
			Scene scene = new Scene(root,645,550);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setTitle("WCMigratorFX v1.0");
			primaryStage.setResizable(false);
			primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {

				@Override
				public void handle(WindowEvent event) {
					// TODO Auto-generated method stub
					Alert confirmAlert = new Alert(AlertType.CONFIRMATION,"", ButtonType.YES,ButtonType.NO,ButtonType.CANCEL);
					confirmAlert.setTitle(String.format("Closing Application"));
					confirmAlert.setHeaderText("Do you want save your preferences before closing the Application!!!");
					
					Optional<ButtonType> option = confirmAlert.showAndWait();
										
					if (option.get() == ButtonType.YES) {
						
						SaveAppPreferences saveAppPrefs = new SaveAppPreferences();
						saveAppPrefs.setAppPreferences();
						
						System.out.println("Config file created!!");
						
					} else if (option.get() == ButtonType.NO) {
						return;
					} else if (option.get() == ButtonType.CANCEL) {
						event.consume();
						
					}
					
					return;
				}
			});
			
			primaryStage.show();
		} catch(Exception e) {
			System.out.println("Error : " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
}
