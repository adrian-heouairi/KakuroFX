package kakurofx;

import java.util.ArrayList;

/**
 * Un objet de type Sum regroupe une case noire et les cases blanches qu'elle concerne
 * à sa droite ou en-dessous d'elle. Cette classe contient des méthodes qui permettent
 * de vérifier si ce que le joueur a tapé est correct.
 */
public class Sum {
	
	/**
	 * Permet de savoir à tout moment si tout est correct dans cette somme :
	 * bonne somme composée et pas de chiffres en double. En vérifiant ceci pour toutes
	 * les sommes, on détermine si le joueur a gagné ou non.
	 */
	private boolean isAllOk = false;
	
	/**
	 * Permet de savoir si cette somme est en ligne ou en colonne.
	 */
	private boolean isHorizontal;
	
	/**
	 * La case noire qui contient le nombre qui doit être composé par le joueur.
	 */
	private BlackTile blackTile;
	
	/**
	 * Cette liste contient toutes les cases blanches directement à droite ou
	 * en-dessous de la case noire.
	 */
	private ArrayList<WhiteTile> whiteTiles;
	
	public Sum(boolean isHorizontal, BlackTile blackTile, ArrayList<WhiteTile> whiteTiles) {
		this.isHorizontal = isHorizontal;
		this.blackTile = blackTile;
		this.whiteTiles = whiteTiles;
	}

	public boolean isAllOk() {
		return isAllOk;
	}
	
	/**
	 * @return l'état de la somme composée par le joueur :
	 * incomplète (au moins une case blanche vide), correct ou incorrect.
	 * Ne prend pas en compte les chiffres en double.
	 */
	private String typedSumState() {
		int typedSum = 0;
		for (WhiteTile whiteTile : whiteTiles) {
			int whiteTileDigit = whiteTile.getDigit();
			if (whiteTileDigit == 0) return "Incomplete";
			typedSum += whiteTileDigit;
		}
		if (blackTile.getIndicator(isHorizontal) == typedSum) return "Correct";
		else return "Incorrect";
	}
	
	/**
	 * Range les cases blanches de la somme dans dix catégories : vide et contient un chiffre
	 * entre 1 et 9. Si plusieurs cases sont dans une seule catégorie entre 1 et 9, alors elles
	 * sont des doublons les unes des autres et sont marquées comme tel. Les cases dans la
	 * catégorie vide sont marquées comme n'étant pas des doublons au cas où elles l'étaient avant.
	 * Une case marquée comme doublon a son texte automatiquement mis en rouge.
	 * @return false si aucun chiffre n'est présent plusieurs fois, true sinon.
	 */
	private boolean hasDuplicates() {
		ArrayList<ArrayList<WhiteTile>> duplicateDigits = new ArrayList<ArrayList<WhiteTile>>(10);
		for (int i = 0; i < 10; i++) duplicateDigits.add(new ArrayList<WhiteTile>(0));
		for (WhiteTile whiteTile : whiteTiles)
			duplicateDigits.get(whiteTile.getDigit()).add(whiteTile);
		
		for (WhiteTile whiteTile : duplicateDigits.get(0)) whiteTile.setIsDuplicate(isHorizontal, false);
		boolean hasDuplicates = false;
		duplicateDigits.remove(0);
		for (ArrayList<WhiteTile> digit : duplicateDigits) {
			if (digit.size() > 1) {
				hasDuplicates = true;
				for (WhiteTile whiteTile : digit) whiteTile.setIsDuplicate(isHorizontal, true);
			}
			else
				for (WhiteTile whiteTile : digit) whiteTile.setIsDuplicate(isHorizontal, false);
		}
		
		return hasDuplicates;
	}
	
	/**
	 * Rafraichit l'état de la somme. Cette méthode doit être lancée après qu'une case
	 * blanche de la somme a été modifiée. La couleur du texte de la case noire de la somme
	 * est modifié : il sera rouge s'il y a des chiffres en double ou si la mauvaise somme
	 * est tapée. Il sera vert si tout est correct. Il sera blanc si les cases blanches ne sont pas
	 * toutes remplies et ne comportent pas de chiffre en double.
	 * 
	 * @return le booléen isAllOk, qui dit si tout est correct dans la somme.
	 */
	public boolean refresh() {
		if (hasDuplicates()) {
			blackTile.setTextColor(isHorizontal, "red");
			return (isAllOk = false);
		}
		
		String typedSumState = typedSumState();
		
		if (typedSumState.equals("Correct")) {
			blackTile.setTextColor(isHorizontal, "lightgreen");
			return (isAllOk = true);
		}
		else if (typedSumState.equals("Incorrect")) blackTile.setTextColor(isHorizontal, "red");
		else blackTile.setTextColor(isHorizontal, "white");
		return (isAllOk = false);
	}
	
	public boolean getIsHorizontal(){
		return isHorizontal;
	}
	
	public BlackTile getBlackTile() {
		return blackTile;
	}
}
