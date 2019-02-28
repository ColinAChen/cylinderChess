package com.example.chessApp;

import java.util.ArrayList; // import the ArrayList class
public class Rook extends Piece{
	boolean hasMoved = false;
	ArrayList<int[]> possibleMoves;
	public Rook(String name, boolean color, int x,int y){
		super("r",color,x,y);
	}
	public boolean isLegitMove(int newx, int newy){
		//ensure not trying to move off the board or to the same square
		if (newx < 0 || newx > 7 || (newx == x && newy == y)){
			return false;
		}
		//moving vertically
		if (newy-y == 0){
			return true;
		}
		//moving horizontally
		else if(newx-x == 0){
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




