package com.example.cylinderchess;

import java.util.ArrayList; // import the ArrayList class


abstract class Piece{
	String name;
	boolean color;
	int x;
	int y;

	//public Piece(){};
	
	public Piece(String name, boolean color, int x,int y){
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
	public boolean isAttacked(){
		return isAttacked;
	}
	public ArrayList<int[]> getPossibleMoves(){
		return null;
	}
}

