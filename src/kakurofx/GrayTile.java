package kakurofx;

import javafx.scene.layout.Region;

/**
 * Cette classe hérite de Region, le type le plus simple possible en JavaFX.
 */
public class GrayTile extends Region {
	
	public GrayTile() {
		// Permet notamment que la case suivante soit sélectionnée lorsqu'on appuie sur TAB
		setFocusTraversable(true);
		// Permet que la case soit sélectionnée lorsqu'on clique dessus
		setOnMouseClicked(mouseEvent -> requestFocus());
	}
	
}
