package kakurofx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

	/**
	 * Cette méthode est indirectement lancée par la méthode main.
	 * Elle prend en argument le stage qui représente la fenêtre graphique du jeu.
	 * Elle charge l'écran titre et l'affiche.
	 */
	@Override
	public void start(Stage stage) throws Exception {
		// Chargement de la boite de l'écran titre à partir de son fichier FXML
		Parent titleScreen = FXMLLoader.load(getClass().getResource("resources/TitleScreen.fxml"));

		// Création de la scène qui contiendra les différents écrans
		// Elle contient au départ l'écran titre
		Scene scene = new Scene(titleScreen);
		
		// Application des styles CSS à la scène et donc à tous les écrans qu'elle contiendra
		scene.getStylesheets().add(getClass().getResource("resources/styles.css").toExternalForm());

		// La fenêtre du jeu fait par défaut 700x700
		stage.setWidth(700);
		stage.setHeight(700);
		
		// Ceci change le titre de la fenêtre
		stage.setTitle("KakuroFX");
		
		// On place la scène dans la fenêtre graphique
		stage.setScene(scene);
		
		// On montre la fenêtre graphique qui était cachée jusqu'à présent
		stage.show();
	}

}
