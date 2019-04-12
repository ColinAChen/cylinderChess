// package com.example.chessApp.cylinder;

import java.util.ArrayList;

abstract class PieceNormal
{
	private String name;
	private boolean color;
	private int r;
	private int c;

	public PieceNormal(String name, boolean color, int r, int c)
	{
		this.name = name;
		this.color = color;
		this.r = r;
		this.c = c;
	}

	public void move(int rMove, int cMove)
	{
		r = rMove;
		c = cMove;
	}

	public boolean getColor()
	{
		return color;
	}
	
	public String getStringColor()
	{
		if(color)
			return "white";
		else
			return "black";
	}

	public String getName()
	{
		return name;
	}

	public int getRow()
	{
		return r;
	}

	public int getCol()
	{
		return c;
	}

	public ArrayList<ArrayList<int[]>> getPossibleMoves()	
	{
		return null;
	}

	/*public void changeColor()
	{
		color = !color;
	}


	public int[] getPosition()
	{
		int[] pos = { r, c };
		return pos;
	}

	public void setRow(int row)
	{
		x = row;
	}

	public void setCol(int col)
	{
		y = col;
	}*/
}