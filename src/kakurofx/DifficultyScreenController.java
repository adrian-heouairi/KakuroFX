package kakurofx;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.Parent;

public class DifficultyScreenController {
	
	@FXML
	private Label difficultyLabel;
	
	public void goToEasy() throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("resources/GameScreen.fxml"));
		Parent gameScreen = loader.load();
		((GameScreenController)loader.getController()).setGrid(GridGenerator.generateGrid("Easy"));
		difficultyLabel.getScene().setRoot(gameScreen);
	}
	
	public void goToMedium() throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("resources/GameScreen.fxml"));
		Parent gameScreen = loader.load();
		((GameScreenController)loader.getController()).setGrid(GridGenerator.generateGrid("Medium"));
		difficultyLabel.getScene().setRoot(gameScreen);
	}
	
	public void goToHard() throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("resources/GameScreen.fxml"));
		Parent gameScreen = loader.load();
		((GameScreenController)loader.getController()).setGrid(GridGenerator.generateGrid("Hard"));
		difficultyLabel.getScene().setRoot(gameScreen);
	}
	
	public void goToTitleScreen() throws Exception {
		Parent titleScreen = FXMLLoader.load(getClass().getResource("resources/TitleScreen.fxml"));
		difficultyLabel.getScene().setRoot(titleScreen);
	}
}
