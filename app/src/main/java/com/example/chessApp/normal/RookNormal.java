package com.example.chessApp.normal;

import java.util.ArrayList;
	
public class RookNormal extends PieceNormal
{
	private boolean hasMoved;
	
	public RookNormal(boolean color, int r, int c)
	{
		super("r", color, r, c);
		hasMoved = false;
	}

	public void moved()
	{
		hasMoved = true;
	}

	public boolean hasMoved()
	{
		return hasMoved;
	}

	public ArrayList<ArrayList<int[]>> getPossibleMoves()
	{
		int row = getRow();
		int col = getCol();
	
		ArrayList<int[]> left = new ArrayList<int[]>();
		ArrayList<int[]> right = new ArrayList<int[]>();
		ArrayList<int[]> up = new ArrayList<int[]>();
		ArrayList<int[]> down = new ArrayList<int[]>();
	
		// down
		for(int r = row + 1; r < 8; r++)
			down.add(new int[] { row, col, r, col });

		// up
		for(int r = row - 1; r >= 0; r--)  
			up.add(new int[] { row, col, r, col });
		
		// right
		for(int c = col + 1; c < 8; c++)
			right.add(new int[] { row, col, row, c });
		
		// left
		for(int c = col - 1; c >= 0; c--)
			left.add(new int[] { row, col, row, c });

		ArrayList<ArrayList<int[]>> allMoves = new ArrayList<ArrayList<int[]>>();
		allMoves.add(left);
		allMoves.add(right);
		allMoves.add(up);
		allMoves.add(down);

		return allMoves;
	}
}



