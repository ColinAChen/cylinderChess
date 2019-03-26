//package com.example.chessApp.cylinder;

import java.util.ArrayList;

abstract class PieceCylinder {
	String name;
	boolean color;
	int x;
	int y;

	//public PieceNormal(){};

	public PieceCylinder(String name, boolean color, int x, int y){
		this.name = name;
		this.color = color;
		this.x = x;
		this.y = y;
	}
	public void move(int xMove, int yMove){
		x=xMove;
		y=yMove;
	}
	public void changeColor(){
		color = !color;
	}
	public String getColor(){
		if (color){
			return "white";
		}
		else{
			return "black";
		}
	}
	public String getName(){
		return name;
	}
	public ArrayList<int[]> getPossibleMoves(){
		return null;
	}

	public int[] getPosition()
	{
		int[] pos = new int[] { x, y };	
		return pos;
	}
}

