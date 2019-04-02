package com.example.chessApp.cylinder;

import java.util.ArrayList;

// wrapper class for pieceCylinders
// has read only access to the given pieceCylinder
public class PieceImmutable
{
	PieceCylinder piece;

	public PieceImmutable(PieceCylinder p)
	{
		piece = p;
	}

	public String getColor()
	{
		if(piece != null)
			return piece.getColor();

		return null;
	}

	public boolean getColorBoolean()
	{
		return piece.getColorBoolean();
	}

	public String getName() 	
	{
		if(piece != null)
			return piece.getName();
	
		return null;
	}

	public ArrayList<int[]> getPossibleMoves()
	{
		if(piece != null)
			return piece.getPossibleMoves();
	
		return null;
	}

	public int[] getPosition()
	{
		if(piece != null)
			return piece.getPosition();
	
		return null;
	}
}