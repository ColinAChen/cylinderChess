package com.example.chessApp.cylinder;

import java.util.ArrayList;

abstract class PieceCylinder {
	String name;
	boolean color;
	int row;
	int col;

	//public PieceNormal(){};

	public PieceCylinder(String name, boolean color, int x, int y){
		this.name = name;
		this.color = color;
		this.row = x;
		this.col = y;
	}
	public void move(int newRow, int newCol){
		row = newRow;
		col = newCol;
	}
	public void changeColor(){
		color = !color;
	}
	public boolean getColor(){
		return color;
	}
	public String getColorName(){
		if (color){
			return "white";
		}
		else{
			return "black";
		}
	}

	public boolean getColorBoolean(){
		return color;
	}
	public int getRow(){
		return row;
	}
	public int getCol(){
		return col;
	}
	public String getName(){
		return name;
	}
	public ArrayList<int[]> getPossibleMoves(){
		return null;
	}
}

