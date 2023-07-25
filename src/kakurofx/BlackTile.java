package kakurofx;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Line;

/**
 * Cette classe hérite de StackPane afin de placer la ligne diagonale traditionnelle sur
 * les cases noires de kakuro par-dessus le BorderPane qui contient des labels qui affichent
 * les sommes à composer dans les cases blanches à droite et en-dessous.
 */
public class BlackTile extends StackPane {
	
	/**
	 * La valeur à composer dans les cases blanches à droite de cette case noire.
	 */
	private int rightIndicator = 0;
	
	/**
	 * La valeur à composer dans les cases blanches en bas de cette case noire.
	 */
	private int downIndicator = 0;
	
	private Label rightIndicatorDisplay = new Label();
	private Label downIndicatorDisplay = new Label();
	
	public BlackTile() {
		// Permet notamment que la case suivante soit sélectionnée lorsqu'on appuie sur TAB
		setFocusTraversable(true);
		// Permet que la case soit sélectionnée lorsqu'on clique dessus
		setOnMouseClicked(mouseEvent -> requestFocus());
		
		// Par défaut, un BorderPane qui n'a que des enfants à droite et en bas va les placer
		// en haut à droite et en bas à gauche respectivement. Ceci correspond parfaitement
		// à une case noire de kakuro.
		BorderPane borderPane = new BorderPane();
		borderPane.setRight(rightIndicatorDisplay);
		borderPane.setBottom(downIndicatorDisplay);
		getChildren().add(borderPane);
		
		Line line = new Line(0, 0, 0, 0);
		// La ligne commencera en haut à gauche
		StackPane.setAlignment(line, Pos.TOP_LEFT);
		// Fait que la ligne finit toujours en bas à droite de la case noire
		line.endXProperty().bind(widthProperty());
		line.endYProperty().bind(heightProperty());
		// Fait que le StackPane ne considérera pas la taille de la ligne pour décider
		// sa propre taille lorsqu'il est redimensionné.
		// Ceci règle un problème qui fait que la case noire grandit bien lorsqu'on agrandit la
		// fenêtre, mais ne rétrécit plus.
		line.setManaged(false);
		getChildren().add(line);
	}
	
	public int getIndicator(boolean isHorizontal) {
		if (isHorizontal) return rightIndicator;
		else return downIndicator;
	}
	
	public void setIndicator(boolean isHorizontal, int indicator) {
		if (isHorizontal) {
			rightIndicator = indicator;
			rightIndicatorDisplay.setText(String.valueOf(indicator));
		}
		else {
			downIndicator = indicator;
			downIndicatorDisplay.setText(String.valueOf(indicator));
		}
	}
	
	public void setTextColor(boolean isHorizontal, String color) {
		if (isHorizontal) rightIndicatorDisplay.setStyle("-fx-text-fill: " + color + ";");
		else downIndicatorDisplay.setStyle("-fx-text-fill: " + color + ";");
	}
	
}
