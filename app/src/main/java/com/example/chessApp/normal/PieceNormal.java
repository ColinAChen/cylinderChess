package com.example.chessApp.normal;

import java.util.ArrayList; // import the ArrayList class

abstract class PieceNormal {
	String name;
	boolean color;
	int x;
	int y;

	//public PieceNormal(){};
	
	public PieceNormal(String name, boolean color, int x, int y){
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
}

