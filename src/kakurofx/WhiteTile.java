package kakurofx;

import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;

/**
 * Une case blanche est un Label qui réagit aux touches de clavier 1 à 9 et backspace et
 * change son contenu en conséquence.
 *
 */
public class WhiteTile extends Label {
	
	private boolean isHorizontalDuplicate = false;
	private boolean isVerticalDuplicate = false;
	private Sum horizontalSum;
	private Sum verticalSum;
	private CustomEventHandler afterKeyTyped;
	
	public WhiteTile() {
		// Permet notamment que la case suivante soit sélectionnée lorsqu'on appuie sur TAB
		setFocusTraversable(true);
		// Permet que la case soit sélectionnée lorsqu'on clique dessus
		setOnMouseClicked(mouseEvent -> requestFocus());
		
		setOnKeyTyped(keyEvent -> {
			String character = keyEvent.getCharacter();
			
			// Si le joueur a tapé backspace
			if (character.equals("\b")) setText("");
			// Sinon si le joueur a tapé un chiffre entre 1 et 9
			else if (character.length() == 1 && character.compareTo("1") >= 0 && character.compareTo("9") <= 0)
				setText(character);
			else switch (character) { // Support de la rangée supérieure du clavier AZERTY
				case "&": setText("1"); break;
				case "é": setText("2"); break;
				case "\"": setText("3"); break;
				case "'": setText("4"); break;
				case "(": setText("5"); break;
				case "-": setText("6"); break;
				case "è": setText("7"); break;
				case "_": setText("8"); break;
				case "ç": setText("9"); break;
			}
			
			// Si du code supplémentaire à exécuter lorsqu'une touche est appuyée est donné,
			// il est exécuté ici
			if (afterKeyTyped != null) afterKeyTyped.handle(keyEvent);
		});
	}
	
	/**
	 * L'interface à implémenter pour pouvoir donner du code supplémentaire à exécuter
	 * lorsqu'une touche est appuyée.
	 */
	@FunctionalInterface
	public interface CustomEventHandler {
		void handle(KeyEvent keyEvent);
	}
	
	/**
	 * Permet de donner du code supplémentaire à exécuter lorsqu'une touche est appuyée.
	 * En général, il convient d'appeler cette méthode avec une expression lambda qui va
	 * directement implémenter l'interface CustomEventHandler.
	 * @param customEventHandler
	 */
	public void setAfterKeyTyped(CustomEventHandler customEventHandler) {
		afterKeyTyped = customEventHandler;
	}
	
	/**
	 * Cette méthode permet de retenir si le chiffre de cette case est présent plusieurs fois
	 * sur sa ligne ou sa colonne. Elle change également la couleur du chiffre : si ce chiffre
	 * est en double sur sa ligne et/ou sa colonne, il est en rouge. Sinon, il est noir.
	 * @param isHorizontal
	 * @param isDuplicate
	 */
	public void setIsDuplicate(boolean isHorizontal, boolean isDuplicate) {
		if (isHorizontal) isHorizontalDuplicate = isDuplicate;
		else isVerticalDuplicate = isDuplicate;
		
		if (isHorizontalDuplicate || isVerticalDuplicate) setStyle("-fx-text-fill: red;");
		else setStyle("-fx-text-fill: black;");
	}
	
	/**
	 * @param isHorizontal
	 * @return la somme horizontale ou verticale dans laquelle cette case blanche est présente.
	 */
	public Sum getSum(boolean isHorizontal) {
		if (isHorizontal) return horizontalSum;
		else return verticalSum;
	}
	
	public void setSum(boolean isHorizontal, Sum sum) {
		if (isHorizontal) horizontalSum = sum;
		else verticalSum = sum;
	}
	
	/**
	 * @return le chiffre tapé par l'utilisateur ou 0 si la case est vide.
	 */
	public int getDigit() {
		try {
			return Integer.parseInt(getText());
		}
		catch (NumberFormatException exception) {
			return 0;
		}
	}
	
	public void setDigit(int digit) {
		if (digit >= 1 && digit <= 9) setText(String.valueOf(digit));
		else setText("");
	}
	
}
