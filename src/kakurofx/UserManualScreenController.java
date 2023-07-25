package kakurofx;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;

public class UserManualScreenController {
	
	@FXML
	private Node userManualText;
	
	public void goToTitleScreen() throws Exception {
		Parent TitleScreen = FXMLLoader.load(getClass().getResource("resources/TitleScreen.fxml"));
		userManualText.getScene().setRoot(TitleScreen);
	}
}
