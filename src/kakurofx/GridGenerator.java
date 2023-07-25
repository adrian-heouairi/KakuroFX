package kakurofx;

import java.util.ArrayList;
import java.util.Random;

public class GridGenerator {

	public static char[][] generateGrid(String difficulty) {
		double whiteTileProbability = .5;
		switch (difficulty) {
			case "Easy": whiteTileProbability = .35; break;
			case "Hard": whiteTileProbability = .7; break;
		}
		
		char[][] grid = new char[9][9];
		Random random = new Random();
		
		int[] horizontalConsecutiveWhite = new int[9];
		int[] verticalConsecutiveWhite = new int[9];
		
		for (int y = 0; y < 9; y++) grid[y][0] = 'b';
		for (int x = 0; x < 9; x++) grid[0][x] = 'b';
		
		for (int y = 1; y < 9; y++) {
			for (int x = 1; x < 9; x++) {
				if (horizontalConsecutiveWhite[y] == 1 || verticalConsecutiveWhite[x] == 1) {
					grid[y][x] = 'w';
					horizontalConsecutiveWhite[y]++;
					verticalConsecutiveWhite[x]++;
				}
				else if (x < 8 && y < 8) {
					if (random.nextDouble() < whiteTileProbability) {
						grid[y][x] = 'w';
						horizontalConsecutiveWhite[y]++;
						verticalConsecutiveWhite[x]++;
					}
					else {
						grid[y][x] = 'b';
						horizontalConsecutiveWhite[y] = 0;
						verticalConsecutiveWhite[x] = 0;
					}
				}
				else {
					grid[y][x] = 'b';
					horizontalConsecutiveWhite[y] = 0;
					verticalConsecutiveWhite[x] = 0;
				}
			}
		}
		
		for (int y = 1; y < 9; y++) {
			if (grid[y][8] == 'w') {
				if (grid[y][7] == 'b') grid[y][7] = 'w';
			}
		}
		for (int x = 1; x < 9; x++) {
			if (grid[8][x] == 'w') {
				if (grid[7][x] == 'b') grid[7][x] = 'w';
			}
		}
		
		for (int y = 1; y < 9; y++) {
			for (int x = 1; x < 9; x++) {
				if (grid[y][x] == 'w') {
					boolean[] alreadyTaken = new boolean[10];
					
					int xOffset = 1;
					while (grid[y][x - xOffset] != 'b') {
						int digit = Integer.parseInt(grid[y][x - xOffset++] + "");
						alreadyTaken[digit] = true;
					}
					
					int yOffset = 1;
					while (grid[y - yOffset][x] != 'b') {
						int digit = Integer.parseInt(grid[y - yOffset++][x] + "");
						alreadyTaken[digit] = true;
					}
					
					ArrayList<Integer> notTaken = new ArrayList<Integer>();
					for (int digit = 1; digit <= 9; digit++)
						if (! alreadyTaken[digit]) notTaken.add(digit);
					
					if (notTaken.size() != 0) {
						int randomDigit = notTaken.get(random.nextInt(notTaken.size()));
						grid[y][x] = (char)(randomDigit + '0');
					}
					else {
						grid[y][x] = 'b';
						boolean isGridColorIncoherent = true;
						while (isGridColorIncoherent) {
							isGridColorIncoherent = false;
							for (int y2 = 1; y2 < 9; y2++)
								for (int x2 = 1; x2 < 9; x2++)
									if (grid[y2][x2] != 'b') {
										boolean hasWhiteAtLeft = grid[y2][x2 - 1] != 'b';
										boolean hasWhiteAbove = grid[y2 - 1][x2] != 'b';
										boolean hasWhiteAtRight = false;
										if (x2 <= 7) hasWhiteAtRight = grid[y2][x2 + 1] != 'b';
										boolean hasWhiteBelow = false;
										if (y2 <= 7) hasWhiteBelow = grid[y2 + 1][x2] != 'b';
										
										if (! ((hasWhiteAtLeft || hasWhiteAtRight) && (hasWhiteAbove || hasWhiteBelow))) {
											grid[y2][x2] = 'b';
											isGridColorIncoherent = true;
										}
									}
						}
					}
				}
			}
		}
		
		for (int y = 0; y < 9; y++)
			for (int x = 0; x < 9; x++)
				if (grid[y][x] == 'b') {
					boolean shouldBeGray = true;
					
					if (x <= 7)
						if (grid[y][x + 1] != 'b') shouldBeGray = false;
					
					if (y <= 7)
						if (grid[y + 1][x] != 'b') shouldBeGray = false;
					
					if (shouldBeGray) grid[y][x] = 'g';
				}
		
		return grid;
	}
	
}
