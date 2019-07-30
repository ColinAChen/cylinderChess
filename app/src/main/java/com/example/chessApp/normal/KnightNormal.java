package com.example.chessApp.normal;

import java.util.ArrayList;

public class KnightNormal extends PieceNormal
{
	public KnightNormal(boolean color, int r, int c)
	{
		super("n", color, r, c);
	}

	private boolean isLegitMove(int newR, int newC)
	{
		int r = getRow();
		int c = getCol();

		// ensure not trying to move off the board
		if(newR < 0 || newR > 7 || newC < 0 || newC > 7)
			return false;

		if(newR == r && newC == c)
			return false;

		if (Math.abs(newC - c) == 1 && Math.abs(newR - r) == 2)
		{
			return true;
		}
		else if(Math.abs(newC - c) == 2 && Math.abs(newR - r) == 1)
		{
			return true;
		}
		
		return false;
	}

	public ArrayList<ArrayList<int[]>> getPossibleMoves()
	{
		ArrayList<ArrayList<int[]>> allMoves = new ArrayList<ArrayList<int[]>>();
		for(int r = getRow() - 2; r <= getRow() + 2; r++)
		{
			for(int c = getCol() - 2; c <= getCol() + 2; c++)
			{
				if(isLegitMove(r, c))
				{
					ArrayList<int[]> path = new ArrayList<int[]>();
					int[] move = new int[] { getRow(), getCol(), r, c };
					
					path.add(move);
					allMoves.add(path);
				}	
			}
		}

		return allMoves;					
	}	
}


