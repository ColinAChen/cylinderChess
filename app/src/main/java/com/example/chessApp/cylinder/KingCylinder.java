package com.example.chessApp.cylinder;

import java.util.ArrayList;

public class KingCylinder extends PieceCylinder {
	boolean hasMoved = false;
	ArrayList<int[]> possibleMoves;
	public KingCylinder(String name, boolean color, int x, int y){
		super("k",color,x,y);
	}
	public boolean isLegitMove(int newx, int newy){
		//ensure not trying to move off the board
		if (newx < 0 || newx > 7 || (newx == row && newy == col)){
			return false;
		}
		if (Math.abs(newx-row) < 2 && Math.abs(newy-col) < 2){
			return true;
		}
		return false;
	}
	public void moved(){
		hasMoved = true;
	}
	//return all possible moves for this piece given its current position
	//does NOT take into account current board position
	public ArrayList<int[]> getPossibleMoves(){
		ArrayList<int[]> possibleMoves = new ArrayList<int[]>();
		for (int i = 0; i < 8; i++){
			for (int j = -1; j < 9; j++){
				if (this.isLegitMove(i,j)){
					int[] pair = {i,Math.abs(Math.floorMod(j,8))};
					possibleMoves.add(pair);
				}
			}
		}
		return possibleMoves;
	}
}




