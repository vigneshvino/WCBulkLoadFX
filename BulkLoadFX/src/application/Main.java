package application;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

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
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
