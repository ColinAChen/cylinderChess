package com.example.chessApp.cylinder;

import java.util.ArrayList;

public class QueenCylinder extends PieceCylinder {

	ArrayList<int[]> possibleMoves;
	public QueenCylinder(String name, boolean color, int x, int y){
		super("q",color,x,y);
	}
	public boolean isLegitMove(int newx, int newy){
		//ensure not trying to move off the board
		if (newx < 0 || newx > 7 || (newx == row && newy == col)){
			return false;
		}
		if (Math.abs(newx-row) == Math.abs(newy-col)){
			return true;
		}
		else if (newy-col == 0){
			return true;
		}
		//moving horizontally
		else if(newx-row == 0){
			return true;
		}
		return false;
	}
	
	//return all possible moves for this piece given its current position
	//does NOT take into account current board position
	public ArrayList<int[]> getPossibleMoves(){
		ArrayList<int[]> possibleMoves = new ArrayList<int[]>();
		for (int i = 0; i < 8; i++){
			for (int j = -7; j < 15; j++){
				if (this.isLegitMove(i,j)){
					int[] pair = {i,Math.abs(Math.floorMod(j,8))};
					possibleMoves.add(pair);
				}
			}
		}
		return possibleMoves;
	}
}




