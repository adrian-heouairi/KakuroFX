package kakurofx;

import java.util.ArrayList;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.input.MouseButton;

public class CreationScreenController extends GameScreenController{
	
	@FXML
	private Node save;
	
	//On ne peut cliquer sur sauvegarder que si la grille est correcte
	private void saveChange(boolean victory){
		if (victory)
			save.setDisable(true);
		else
			save.setDisable(false);
	}
	
	private boolean noError(Node[][] gridTiles){
		boolean asNoError=false;
		for (int y = 0; y < 9; y++)
			for (int x = 0; x < 9; x++){
				if (gridTiles[y][x].getClass().getSimpleName().equals("WhiteTile")){
					int xpos=1;
						while (!gridTiles[y][x-xpos].getClass().getSimpleName().equals("BlackTile") 
								&& (!gridTiles[y][x-xpos].getClass().getSimpleName().equals("GrayTile")) && (xpos<x)){
							xpos++;
						}
						if (gridTiles[y][x-xpos].getClass().getSimpleName().equals("BlackTile")) {
							if (xpos>1){
								asNoError=true;
							}
							else if (gridTiles[y][x+1].getClass().getSimpleName().equals("WhiteTile")){
								asNoError=true;
							}
					}	
					}
					if (asNoError=true){
							int ypos=1;
							while (!gridTiles[y-ypos][x].getClass().getSimpleName().equals("BlackTile") 
									&& (!gridTiles[y-ypos][x].getClass().getSimpleName().equals("GrayTile")) &&(ypos<y)){
								ypos++;
							}
							if (gridTiles[y-ypos][x].getClass().getSimpleName().equals("GrayTile")) {
								asNoError=false;
							}
							else if (ypos==1 && (!gridTiles[y-ypos][x].getClass().getSimpleName().equals("WhiteTile"))){
								asNoError=false;
							}
						}
					}
		return asNoError;
	}
		
	/**
	 * Changement de la couleur des cases
	 * @param x
	 * @param y
	 */
	private void changeTile(int x, int y){
		getGridPane().getChildren().remove(getGridTiles()[y][x]);
		if (getGridTiles()[y][x].getClass().getSimpleName().equals("WhiteTile")) {
			getGridTiles()[y][x]= new GrayTile();
		}
		else if (getGridTiles()[y][x].getClass().getSimpleName().equals("GrayTile")) {
			getGridTiles()[y][x]= new BlackTile();
		}
		else if (getGridTiles()[y][x].getClass().getSimpleName().equals("BlackTile")) {
			getGridTiles()[y][x]= new WhiteTile();
		}
		getGridTiles()[y][x].setOnMouseClicked(mouseEvent -> {
			if (mouseEvent.getButton()==MouseButton.SECONDARY){
			changeTile(x,y);
			}
			else{
			((Node)mouseEvent.getSource()).requestFocus();
			}
		});
		getGridPane().add(getGridTiles()[y][x],x,y);
		getGridTiles()[y][x].requestFocus();
	}
	
	private void tileModif(Node[][] gridTiles){
		for (int y = 0; y < 9; y++)
			for (int x = 0; x < 9; x++){
				int fx=x,fy=y;
				gridTiles[fy][fx].setOnMouseClicked(mouseEvent -> {
					if (mouseEvent.getButton()==MouseButton.SECONDARY){
					changeTile(fx,fy);
					}
					else{
					((Node)mouseEvent.getSource()).requestFocus();
					}
				});
			}
	}
	
	// Donne à chaque case blanche le code à lancer lorsqu'elle est modifiée
	private void connectWhiteTiles(Node[][] gridTiles) {
		for (int y = 0; y < 9; y++)
			for (int x = 0; x < 9; x++)
				if (getGridTiles()[y][x].getClass().getSimpleName().equals("WhiteTile")) {
					((WhiteTile)getGridTiles()[y][x]).setAfterKeyTyped(keyEvent -> {
						WhiteTile modifiedWhiteTile = (WhiteTile)keyEvent.getSource();
						boolean horizontalIsAllOk = modifiedWhiteTile.getSum(true).refresh();
						boolean verticalIsAllOk = modifiedWhiteTile.getSum(false).refresh();
						saveChange(horizontalIsAllOk && verticalIsAllOk && noError(getGridTiles()));
						int blackTileRightIndicator = 0;
						int blackTileDownIndicator = 0;
						ArrayList<WhiteTile> whiteTiles = new ArrayList<WhiteTile>(5);
						ArrayList<WhiteTile> whiteTiles2 = new ArrayList<WhiteTile>(5);
						Sum sum = new Sum(true, modifiedWhiteTile.getSum(false).getBlackTile(), whiteTiles);
						Sum sum2 = new Sum(false, modifiedWhiteTile.getSum(false).getBlackTile(), whiteTiles2);
						getSums().add(sum);
						getSums().add(sum2);
						for (int a = 0; a < 9; a++)
							for (int b = 0; b < 9; b++)
								if (getGridTiles()[a][b].getClass().getSimpleName().equals("WhiteTile")) {
										WhiteTile diffWhiteTile = (WhiteTile)getGridTiles()[a][b];
										if (diffWhiteTile.getSum(true)==modifiedWhiteTile.getSum(true)){
											blackTileRightIndicator += Integer.parseInt(String.valueOf(diffWhiteTile.getDigit()));
											diffWhiteTile.setSum(true, sum);
											whiteTiles.add(diffWhiteTile);
										}
										else if (diffWhiteTile.getSum(false)==modifiedWhiteTile.getSum(false)){
											blackTileDownIndicator += Integer.parseInt(String.valueOf(diffWhiteTile.getDigit()));
											diffWhiteTile.setSum(false, sum2);
											whiteTiles2.add(diffWhiteTile);
										}
								}
						modifiedWhiteTile.getSum(false).getBlackTile().setIndicator(false, blackTileDownIndicator);
						modifiedWhiteTile.getSum(true).getBlackTile().setIndicator(true, blackTileRightIndicator);		
						getSums().remove(modifiedWhiteTile.getSum(true));
						getSums().remove(modifiedWhiteTile.getSum(false));
						modifiedWhiteTile.setSum(true, sum);
						modifiedWhiteTile.setSum(false, sum2);
						whiteTiles.add(modifiedWhiteTile);
						whiteTiles2.add(modifiedWhiteTile);
						fillBlackTiles(getGridTiles());					
				});
			}
	}

	
	public void initialize() {
		// Initialise tout en case grise
		char[][] grayGrid = new char[9][9];
		for (int y = 0; y < 9; y++)
			for (int x = 0; x < 9; x++) {
				grayGrid[y][x] = 'g';
			}
		setGrid(grayGrid);
		
		//connectWhiteTiles(getGridTiles());
		
		tileModif(getGridTiles());
	}
}
