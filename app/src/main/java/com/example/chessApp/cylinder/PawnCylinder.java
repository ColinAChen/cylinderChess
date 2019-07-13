package com.example.chessApp.cylinder;

import java.util.ArrayList;
public class PawnCylinder extends PieceCylinder {

	ArrayList<int[]> possibleMoves;
	public PawnCylinder(String name, boolean color, int x, int y){
		super("p",color,x,y);
	}
	public boolean isLegitMove(int newx, int newy){
		//System.out.printf("oldx: %d, oldy: %d, newx: %d, newy: %d%n", x,y,newx,newy);
		
		if (newx < -1 || newx > 8 || (newx == x && newy == y)){
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
			//movement for WHITE
			if (color){
				//can move one or two squares
				if (x == 1 &&  newx == 3){
					//System.out.println("unmoved, moving two");
					return true;
				}
				//move one square up
				else if(newx - x == 1){
					//System.out.println("moving one");
					return true;
				}
				//otherwise is in invalid move up
				else{
					return false;
				}
			}
			// movement for BLACK
			else{
				//unmoved two squares
				if (x == 6 && newx == 4){
					//System.out.println("unmoved, moving two");
					return true;
				}
				// move one square
				else if(x - newx == 1){
					//System.out.println("moving one");
					return true;
				}
				//otherwise is invalid move
				else{
					return false;
				}				
			}
		}
		//movement for capturing
		else{
			//capturing for white
			if (color){
				if (newx - x == 1){
					return true;
				}
				else return false;
			}
			//capturing for black
			else{
				if (x - newx == 1){
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
			//Additional columns to check for edge traversal
			for (int j = -1; j < 9; j++){
				//System.out.println(i +" "+  j);
				if (this.isLegitMove(i,j)){
					//System.out.printf("Adding possible move to %d %d%n",i,j);
					int[] pair = {i,Math.abs(j%8)};
					possibleMoves.add(pair);
				}
			}
		}
		return possibleMoves;
	}
}




