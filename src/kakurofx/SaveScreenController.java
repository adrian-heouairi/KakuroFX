package kakurofx;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

public class SaveScreenController {
	
	@FXML
	private Parent saveScreen;
	
	public void goToCreationScreen() throws Exception {
		Parent CreationScreen = FXMLLoader.load(getClass().getResource("resources/CreationScreen.fxml"));
		saveScreen.getScene().setRoot(CreationScreen);
	}
	public void goToTitleScreen() throws Exception {
		Parent TitleScreen = FXMLLoader.load(getClass().getResource("resources/TitleScreen.fxml"));
		saveScreen.getScene().setRoot(TitleScreen);
	}
}
