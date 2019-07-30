package com.example.chessApp.normal;

import java.util.ArrayList;

// wrapper class for pieceCylinders
// has read only access to the given pieceCylinder
public class PieceImmutable
{
	PieceNormal piece;

	public PieceImmutable(PieceNormal p)
	{
		piece = p;
	}

	public String getColor()
	{
		if(piece != null)
			return piece.getStringColor();

		return null;
	}

	public boolean getColorBoolean()
	{
		return piece.getColor();
	}

	public String getName() 	
	{
		if(piece != null)
			return piece.getName();
	
		return null;
	}

	public ArrayList<ArrayList<int[]>> getPossibleMoves()
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