package com.example.chessApp.normal;

import java.util.ArrayList;

public class BishopNormal extends PieceNormal
{
	public BishopNormal(boolean color, int r, int c)
	{
		super("b", color, r, c);
	}

	public ArrayList<ArrayList<int[]>> getPossibleMoves()
	{
		ArrayList<int[]> upRight = new ArrayList<int[]>();
		ArrayList<int[]> upLeft = new ArrayList<int[]>();
		ArrayList<int[]> downRight = new ArrayList<int[]>();
		ArrayList<int[]> downLeft = new ArrayList<int[]>();
		
		int row = getRow();
		int col = getCol();
		int stepCount;	
			
		// downRight and downLeft
		stepCount = 1;
		for(int r = row + 1; r < 8; r++)
		{
			if(col + stepCount < 8)
				downRight.add(new int[] { row, col, r, col + stepCount });
			
			if(col - stepCount >= 0)
				downLeft.add(new int[] { row, col, r, col - stepCount });
			
			stepCount++;
		}

		// upRight and upLeft
		stepCount = 1;
		for(int r = row - 1; r >= 0; r--)
		{
			if(col + stepCount < 8)
				upRight.add(new int[] { row, col, r, col + stepCount });
			
			if(col - stepCount >= 0)
				upLeft.add(new int[] { row, col, r, col - stepCount });
		
			stepCount++;
		}

		ArrayList<ArrayList<int[]>> allMoves = new ArrayList<ArrayList<int[]>>();
		allMoves.add(upRight);
		allMoves.add(upLeft);
		allMoves.add(downRight);
		allMoves.add(downLeft);

		return allMoves;
	}				
}