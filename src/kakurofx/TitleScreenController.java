package kakurofx;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;

public class TitleScreenController {
	
	@FXML
	private Label javaVersionLabel;
	
	public void goToDifficultyScreen() throws Exception {
		javaVersionLabel.getScene().setRoot(
			FXMLLoader.load(getClass().getResource("resources/DifficultyScreen.fxml"))
		);
	}
	
	public void goToCreationScreen() throws Exception {
		javaVersionLabel.getScene().setRoot(
			FXMLLoader.load(getClass().getResource("resources/CreationScreen.fxml"))
		);
	}
	
	public void goToSavedGridsScreen() throws Exception {
		javaVersionLabel.getScene().setRoot(
			FXMLLoader.load(getClass().getResource("resources/SaveScreen.fxml"))
		);
	}
	
	public void goToUserManualScreen() throws Exception {
		javaVersionLabel.getScene().setRoot(
			FXMLLoader.load(getClass().getResource("resources/UserManualScreen.fxml"))
		);
	}
	
	public void quit() {
		Platform.exit();
	}
	
	public void initialize() {
		String javaVersion = System.getProperty("java.version");
		String javafxVersion = System.getProperty("javafx.version");
		javaVersionLabel.setText("Version de Java : " + javaVersion + "        Version de JavaFX : " + javafxVersion);
	}

}
