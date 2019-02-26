package com.example.cylinderchess;

import java.util.ArrayList; // import the ArrayList class

public class PieceTest{
	public static void main (String[] args){
		//Pawn testPawn = new Pawn("p", false, 1,3,false);
		Pawn testKing = new Pawn("k", true, 6, 0);
		//System.out.println(testPawn.isLegitMove(4,3));
		ArrayList<int[]>possibleMoves = testKing.getPossibleMoves();
		System.out.printf("Possible moves for %s %s at row %d col % d%n",testKing.getColor(),testKing.name,testKing.x,testKing.y);
		for (int[] pair : possibleMoves){
			for (int element: pair){
				System.out.print(element + " ");
			}
			System.out.println("");
		}	
	}
}