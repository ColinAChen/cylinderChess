package com.example.chessApp.normal;

import java.util.ArrayList; // import the ArrayList class
public class QueenNormal extends PieceNormal {
	
	ArrayList<int[]> possibleMoves;
	public QueenNormal(String name, boolean color, int x, int y){
		super("q",color,x,y);
	}
	public boolean isLegitMove(int newx, int newy){
		//ensure not trying to move off the board
		if (newx < 0 || newx > 7 || (newx == x && newy == y)){
			return false;
		}
		if (Math.abs(newx-x) == Math.abs(newy-y)){
			return true;
		}
		else if (newy-y == 0){
			return true;
		}
		//moving horizontally
		else if(newx-x == 0){
			return true;
		}
		return false;
	}
	
	//return all possible moves for this piece given its current position
	//does NOT take into account current board position
	public ArrayList<int[]> getPossibleMoves(){
		ArrayList<int[]> possibleMoves = new ArrayList<int[]>();
		for (int i = 0; i < 8; i++){
			for (int j = 0; j < 8; j++){
				if (this.isLegitMove(i,j)){
					int[] pair = {i,j};
					possibleMoves.add(pair);
				}
			}
		}
		return possibleMoves;
	}
}



