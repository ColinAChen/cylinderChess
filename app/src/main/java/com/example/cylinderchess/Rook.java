package com.example.cylinderchess;

import java.util.ArrayList;

public class Rook extends Piece{
	public Rook(boolean color, int x, int y){
		super.name = "r";
		super.color = color;
		super.x = x;
		super.y = y;
	}
	public boolean isLegitMove(int newx, int newy){
		//invalid move if moving more than one square left or right
			if (newx-super.x ==0){
				return true;
			}
			else if(newy-super.y ==0){
				return true;
			}
			else{
				return false;
			}
		}

	public ArrayList<int[]> getPossibleMoves(){
		int[] pair = new Integer[2];
		ArrayList<int[]> possibleMoves = new ArrayList<Integer[]>();
		for (int i = 0; i < 8; i++){
			for (int j = 0; j < 8; j++){
				if (this.isLegitMove(i,j)){
					pair[0] = i;
					pair[1] = j;
					possibleMoves.add(pair);
				}
			}
		}
		return possibleMoves;
	}
}




