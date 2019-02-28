package com.example.chessApp;

import java.util.ArrayList; // import the ArrayList class
public class Pawn extends Piece{
	
	ArrayList<int[]> possibleMoves;
	public Pawn(String name, boolean color, int x,int y){
		super("p",color,x,y);
	}
	public boolean isLegitMove(int newx, int newy){
		//System.out.printf("oldx: %d, oldy: %d, newx: %d, newy: %d%n", x,y,newx,newy);
		//check that piece exists
		/*
		if (board[super.x][super.y] == null){
			System.out.println("No piece found");
			return false;
		}
		*/
		//check that destination is either empty or of the other color
		/*
		if (board[newx][newy] != null || (board[newx][newy].color).equals(super.color)){
			return false;
		}*/
		if (newx < 0 || newx > 7 || (newx == x && newy == y)){
			//System.out.println("cannot move outside of board or to same square");
			return false;
		}

		//invalid move if moving more than one square left or right
		//Can only capture if the opposite piece is in an adjacent column
		if (Math.abs(newy - y) > 1){
			//System.out.println(Math.abs(newy - y));
			//System.out.println("Can't move pawn farther than one square left or right");
			return false;
		}
		//System.out.println(Math.abs(newy - y));
		//DESTINATION WILL EITHER BE EMPTY OR OF OTHER SQUARE
		//movement for straight
		if (y == newy){
			//System.out.println("moving straight");
			//movement for black
			//unmoved black pawn
			if (color){
				//System.out.println("black");
				//can move one or two squares
				if (x == 6 &&  newx == 4){
					//System.out.println("unmoved, moving two");

					return true;
				}
				//move one square up
				else if(x - newx == 1){
					//System.out.println("moving one");

					return true;
				}
				//promote if pawn reaches top row
				else if(newx == 0){
					//this.queen();
				}
				//otherwise is in invalid move up
				else{
					return false;
				}
			}
			// movement for white pawn
			else{
				//System.out.println("white");
				// unmoved white pawn
				if (x == 1 && newx == 3){
					//System.out.println("unmoved, moving two");
					return true;
				}
				// move one square
				else if(newx-x == 1){
					//System.out.println("moving one");
					return true;
				}
				//promote if pawn reaches top row{
				else if(newx == 7){
					//this.queen();
				}
				//otherwise is invalid move
				else{
					return false;
				}				
			}
		}
		//movement for capturing
		else{
			//capturing for black
			if (color){
				if (x-newx ==1 ){
					return true;
				}
				else return false;
			}
			//capturing for white
			else{
				if (newx-x == 1){
					return true;
				}
				else return false;
			}
		}
		return false;
	}
	
	//return all possible moves for this piece given its current position
	//does NOT take into account current board position
	public ArrayList<int[]> getPossibleMoves(){
		//int[] pair = new int[2];
		ArrayList<int[]> possibleMoves = new ArrayList<int[]>();
		for (int i = 0; i < 8; i++){
			for (int j = 0; j < 8; j++){
				//System.out.println(i +" "+  j);
				if (this.isLegitMove(i,j)){
					//System.out.printf("Adding possible move to %d %d%n",i,j);
					int[] pair = {i,j};
					possibleMoves.add(pair);

				}
			}
		}
		return possibleMoves;
	}
}




