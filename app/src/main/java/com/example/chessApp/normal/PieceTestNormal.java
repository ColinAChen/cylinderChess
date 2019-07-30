package com.example.chessApp.normal;

import java.util.ArrayList; // import the ArrayList class

public class PieceTestNormal {
	public static void main (String[] args){
		//PawnNormal testPawn = new PawnNormal("p", false, 1,3,false);
		PawnNormal testKing = new PawnNormal(true, 6, 0);
		//System.out.println(testPawn.isLegitMove(4,3));
		ArrayList<ArrayList<int[]>>possibleMoves = testKing.getPossibleMoves();
		System.out.printf("Possible moves for %s %s at row %d col % d%n",testKing.getColor(),testKing.getName(),testKing.getRow(),testKing.getCol());
		for(ArrayList<int[]> path : possibleMoves)
		{
			for (int[] pair : path)
			{
				for (int element: pair){
					System.out.print(element + " ");
				}
				System.out.println("");
			}
		}
	}
}