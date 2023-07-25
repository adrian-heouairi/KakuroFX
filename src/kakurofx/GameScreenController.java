package kakurofx;

import java.util.ArrayList;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.print.Printer;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

public class GameScreenController {
	
	/**
	 * Contient la boite de l'écran de jeu, utilisé uniquement pour getScene().
	 */
	@FXML
	private Node gameScreen;
	
	@FXML
	private Node resetButton;
	
	@FXML
	private Node solutionButton;
	
	@FXML
	private Node quitButton;
	
	/**
	 * Au lieu d'ajouter directement la grille de kakuro dans la boite verticale de l'écran de jeu,
	 * on l'ajoute dans cet intermédiaire qui permet de faire en sorte qu'elle reste carrée.
	 */
	@FXML
	private Pane gridPaneContainer;
	
	/**
	 * Contient le tableau 9x9 de caractères qui représente la grille jouée : le char 'g' signifie
	 * une case grise, 'b' signifie une case noire et entre '1' et '9' signifie une case blanche
	 * contenant ce chiffre. On a donc déjà la solution dans cette variable, et les indicateurs des
	 * cases noires seront déduits à partir de cela.
	 */
	private char[][] grid;
	
	/**
	 * Contient le tableau 9x9 de cases blanches, grises et noires correspondant à grid.
	 */
	private Node[][] gridTiles;
	
	private GridPane gridPane;
	
	/**
	 * Contient toutes les les sommes horizontales et verticales de la grille.
	 */
	private ArrayList<Sum> sums;
	
	
	//Méthodes get
	public Node[][] getGridTiles() {
		return gridTiles;
	}
	
	public char[][] getGrid() {
		return grid;
	}
	
	public GridPane getGridPane() {
		return gridPane;
	}
	
	public Pane getGridPaneContainer() {
		return gridPaneContainer;
	}
	
	public ArrayList<Sum> getSums(){
		return sums;
	}
	
	//Méthodes set
	public void setGridTiles(Node[][] gridTiles){
		this.gridTiles=gridTiles;
	}
	
	
	public void setSums(ArrayList<Sum> sums){
		this.sums=sums;
	}
	
	/**
	 * Méthode appelée par le bouton quitter.
	 * @throws Exception
	 */
	public void goToTitleScreen() throws Exception {
		gameScreen.getScene().setRoot(
			FXMLLoader.load(getClass().getResource("resources/TitleScreen.fxml"))
		);
	}
	
	/**
	 * Lance un dialogue pour imprimer la grille avec une imprimante.
	 * La grille est imprimée avec ses cases blanches vides.
	 */
	public void printGrid() {
		Iterable<Printer> printers = Printer.getAllPrinters();
		Printer printer = null;
		// Cette boucle for donne le "premier" élément du Set s'il y en a un.
		for (Printer p : printers) {
			printer = p;
			break;
		}
		if (printer == null) {
			Alert alert = new Alert(AlertType.ERROR, "Pas d'imprimante trouvée.");
			alert.show();
			return;
		}
		
		PrinterJob printerJob = PrinterJob.createPrinterJob(printer);
		// Si l'utilisateur appuie sur le bouton OK du dialogue d'impression
		if (printerJob != null && printerJob.showPrintDialog(gameScreen.getScene().getWindow())) {
			// On crée un deuxième GridPane car celui de l'écran de jeu peut avoir été modifié
			Node[][] gridTiles = createGridTiles();
			fillBlackTiles(gridTiles);
			GridPane gridPane = createGridPane(gridTiles);
			
			// Mettre ce GridPane dans une nouvelle scène permet de lui appliquer notre CSS
			Scene scene = new Scene(gridPane, 450, 450);
			scene.getStylesheets().add(getClass().getResource("resources/styles.css").toExternalForm());
			scene.getStylesheets().add(getClass().getResource("resources/print.css").toExternalForm());
			
			if (printerJob.printPage(gridPane)) printerJob.endJob();
		}
	}
	
	/**
	 * Cette méthode est appelée lorsque le joueur a gagné ou affiché la solution.
	 * Elle rend la grille de jeu non-modifiable et met le bouton quitter en vert.
	 */
	private void freezeGrid() {
		resetButton.setDisable(true);
		solutionButton.setDisable(true);
		quitButton.setStyle("-fx-background-color: springgreen;");
		quitButton.requestFocus();
		
		for (int y = 0; y < 9; y++)
			for (int x = 0; x < 9; x++)
				if (gridTiles[y][x].getClass().getSimpleName().equals("WhiteTile"))
					gridTiles[y][x].setDisable(true);
	}
	
	/**
	 * Vide toutes les cases blanches et rafraîchit toutes les sommes.
	 */
	public void reset() {
		for (int y = 0; y < 9; y++)
			for (int x = 0; x < 9; x++)
				if (gridTiles[y][x].getClass().getSimpleName().equals("WhiteTile"))
					((WhiteTile)gridTiles[y][x]).setDigit(0);
		for (Sum sum : sums) sum.refresh();
	}
	
	/**
	 * Affiche une des solutions possibles et gèle la grille. La solution est déjà présente dans grid.
	 */
	public void solution() {
		freezeGrid();
		
		for (int y = 0; y < 9; y++)
			for (int x = 0; x < 9; x++)
				if (gridTiles[y][x].getClass().getSimpleName().equals("WhiteTile"))
					((WhiteTile)gridTiles[y][x]).setDigit(Integer.parseInt(grid[y][x] + ""));
		for (Sum sum : sums) sum.refresh();
	}
	
	/**
	 * @return un tableau 9x9 de Node contenant les cases blanches, grises
	 * et noires correspondant au tableau de char "grid" qui représente la grille qui est jouée.
	 * Les cases blanches et noires ne sont pas remplies.
	 */
	protected Node[][] createGridTiles() {
		Node[][] gridTiles = new Node[9][9];
		for (int y = 0; y < 9; y++)
			for (int x = 0; x < 9; x++) {
					if (grid[y][x] >= '1' && grid[y][x] <= '9') gridTiles[y][x] = new WhiteTile();
					else if (grid[y][x] == 'b') gridTiles[y][x] = new BlackTile();
					else gridTiles[y][x] = new GrayTile();
				}
		return gridTiles;
	}
	
	/**
	 * Remplit les cases noires de gridTiles à partir du contenu des cases blanches de grid.
	 * Les cases blanches de gridTiles n'interviennent absolument pas.
	 * Pour chaque case noire, on regarde si on trouve une case blanche à droite ou en-dessous.
	 * @param gridTiles tableau contenant les cases noires à remplir.
	 */
	protected void fillBlackTiles(Node[][] gridTiles) {
		for (int y = 0; y < 9; y++)
			for (int x = 0; x < 9; x++)
				if (gridTiles[y][x].getClass().getSimpleName().equals("BlackTile")) {
					if (x <= 7) // Pourrait être 6 car on assume qu'on a une grille valide
						if (gridTiles[y][x + 1].getClass().getSimpleName().equals("WhiteTile")) {
							int blackTileRightIndicator = 0;
							int xOffset = 1;
							while (x + xOffset < 9) {
								if (! gridTiles[y][x + xOffset].getClass().getSimpleName().equals("WhiteTile"))
									break;
								
								blackTileRightIndicator += Integer.parseInt(String.valueOf(grid[y][x + xOffset++]));
							}
							BlackTile blackTile = (BlackTile)gridTiles[y][x];
							blackTile.setIndicator(true, blackTileRightIndicator);
						}
					
					if (y <= 7) // Pourrait être 6 car on assume qu'on a une grille valide
						if (gridTiles[y + 1][x].getClass().getSimpleName().equals("WhiteTile")) {
							int blackTileDownIndicator = 0;
							int yOffset = 1;
							while (y + yOffset < 9) {
								if (! gridTiles[y + yOffset][x].getClass().getSimpleName().equals("WhiteTile"))
									break;
								
								blackTileDownIndicator += Integer.parseInt(String.valueOf(grid[y + yOffset++][x]));
							}
							BlackTile blackTile = (BlackTile)gridTiles[y][x];
							blackTile.setIndicator(false, blackTileDownIndicator);
						}
				}
	}
	
	/**
	 * @param gridTiles tableau dont les cases seront dans les sommes
	 * @return la liste des sommes horizontales et verticales de la grille
	 */
	protected ArrayList<Sum> createSums(Node[][] gridTiles) {
		ArrayList<Sum> sums = new ArrayList<Sum>();
		for (int y = 0; y < 9; y++)
			for (int x = 0; x < 9; x++)
				if (gridTiles[y][x].getClass().getSimpleName().equals("BlackTile")) {
					if (x <= 7) // Pourrait être 6 car on assume qu'on a une grille valide
						if (gridTiles[y][x + 1].getClass().getSimpleName().equals("WhiteTile")) {
							BlackTile blackTile = (BlackTile)gridTiles[y][x];
							ArrayList<WhiteTile> whiteTiles = new ArrayList<WhiteTile>(5);
							Sum sum = new Sum(true, blackTile, whiteTiles);
							sums.add(sum);
							
							int xOffset = 1;
							while (x + xOffset < 9) {
								if (! gridTiles[y][x + xOffset].getClass().getSimpleName().equals("WhiteTile"))
									break;
								
								WhiteTile whiteTile = (WhiteTile)gridTiles[y][x + xOffset++];
								whiteTile.setSum(true, sum);
								whiteTiles.add(whiteTile);
							}
						}
					
					if (y <= 7) // Pourrait être 6 car on assume qu'on a une grille valide
						if (gridTiles[y + 1][x].getClass().getSimpleName().equals("WhiteTile")) {
							BlackTile blackTile = (BlackTile)gridTiles[y][x];
							ArrayList<WhiteTile> whiteTiles = new ArrayList<WhiteTile>(5);
							Sum sum = new Sum(false, blackTile, whiteTiles);
							sums.add(sum);
							
							int yOffset = 1;
							while (y + yOffset < 9) {
								if (! gridTiles[y + yOffset][x].getClass().getSimpleName().equals("WhiteTile"))
									break;
								
								WhiteTile whiteTile = (WhiteTile)gridTiles[y + yOffset++][x];
								whiteTile.setSum(false, sum);
								whiteTiles.add(whiteTile);
							}
						}
				}
		
		return sums;
	}
	
	/**
	 * Donne à chaque case blanche le code à lancer lorsqu'elle est modifiée.
	 * Ce code rafraichit les deux sommes de cette case. De plus, si tout est correct dans ces deux sommes,
	 * le joueur a peut-être gagné. On lance alors une boucle qui vérifie toutes les sommes
	 * de la grille. S'il a gagné, on gèle la grille.
	 * @param gridTiles
	 */
	private void connectWhiteTiles(Node[][] gridTiles) {
		for (int y = 0; y < 9; y++)
			for (int x = 0; x < 9; x++)
				if (gridTiles[y][x].getClass().getSimpleName().equals("WhiteTile")) {
					((WhiteTile)gridTiles[y][x]).setAfterKeyTyped(keyEvent -> {
						WhiteTile modifiedWhiteTile = (WhiteTile)keyEvent.getSource();
						boolean horizontalIsAllOk = modifiedWhiteTile.getSum(true).refresh();
						boolean verticalIsAllOk = modifiedWhiteTile.getSum(false).refresh();
						if (horizontalIsAllOk && verticalIsAllOk) {
							boolean victory = true;
							for (Sum sum : sums) {
								if (! sum.isAllOk()) {
									victory = false;
									break;
								}
							}
							if (victory) freezeGrid();
						}
					});
				}
	}
	
	/**
	 * @param gridTiles
	 * @return un GridPane contenant les cases du tableau 9x9 de Node.
	 */
	protected GridPane createGridPane(Node[][] gridTiles) {
		GridPane gridPane = new GridPane();
		for (int y = 0; y < 9; y++)
			for (int x = 0; x < 9; x++) gridPane.add(gridTiles[y][x], x, y);
		return gridPane;
	}
	
	public void setGrid(char[][] receivedGrid) {
		// Temporaire : met dans grid une grille de test. Plus tard, ceci sera généré aléatoirement.
		/*char[][] gridTmp = {{'g', 'g', 'b', 'b'}, {'g', 'b', '9', '3'}, {'b', '4', '5', '6'}, {'b', '7', '8', '9'}};
		grid = new char[9][9];
		for (int y = 0; y < 9; y++)
			for (int x = 0; x < 9; x++) {
				if (x < 4 && y < 4) grid[y][x] = gridTmp[y][x];
				else grid[y][x] = 'g';
			}*/
		
		grid = receivedGrid;
		
		// Génère le tableau 2D des cases à partir du tableau 2D de caractères
		gridTiles = createGridTiles();
		
		// Calcul du contenu des cases noires à partir des cases blanches
		fillBlackTiles(gridTiles);
		
		sums = createSums(gridTiles);
		
		connectWhiteTiles(gridTiles);
		
		// Crée un objet graphique grille et met les cases dedans
		gridPane = createGridPane(gridTiles);
		
		// Les côtés de la grille sont toujours mis à la même longueur que le
		// plus petit des côtés de son conteneur qui, lui, prend toute la place possible.
		// Ainsi, la grille reste carrée tout en prenant le plus de place possible.
		NumberBinding gridPaneSideLength =
			Bindings.min(gridPaneContainer.widthProperty(), gridPaneContainer.heightProperty());
		gridPane.maxWidthProperty().bind(gridPaneSideLength);
		gridPane.maxHeightProperty().bind(gridPaneSideLength);
		
		gridPaneContainer.getChildren().add(gridPane);
	}
	
}
