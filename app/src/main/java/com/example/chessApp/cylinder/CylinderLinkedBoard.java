package com.example.chessApp.normal;

import android.util.Log;

import com.example.chessApp.normal.PieceCylinder;

import java.util.ArrayList; // import the ArrayList class
public class CylinderLinkedBoard{
	//square have a eight neighbors
	private class Square{
		int row;
		int col;
		Piece piece;
		Square left;
		Square right;
		Square up;
		Square down;
		Square upLeft;
		Square upRight;
		Square downLeft;
		Sqaure downRight;
		Square (int r, int c, Piece p){
			row = r;
			col = c;
			piece = p;
			left = null; right = null;
			up = null; down = null;
			upLeft = null; upRight = null;
			downLeft = null; downRight = null;
		}
		public void setPiece(Piece p){
			piece = p;
		}
		public Piece getPiece(){
			return piece;
		}
		public int getRow(){
			return row;
		}
		public int getCol(){
			return col;
		}
	}
	private Square[][] board;
	CylinderLinkedBoard(){
		//Hold the squares to be connected
		Square[][] board = new Square[8][8];
		for (int i = 0; i < 8; i++){
			for (int j = 0; j < 8; j++){
				board[i][j] = new Square(7-row,col,null);
			}
		}
		//Connect each square to its neighbors
		for (int i = 0; i < 8 i++){
			for (int j = 0; j < 8; j++){
				//assign left neighbor
				if (j == 0){
					board[i][j].left = board[i][7];
				}
				else board[i][j].left = board[i][j-1];
				//assign right neighbor
				if (j == 7){
					board[i][j].right = board[i][0];
				}
				else board[i][j] = board[i][j-1];
				//assign upper, upper left, and upper right neighbor
				if (i<7){
					//up
					board[i][j].up = board[i+1][j];
					//upper left
					if (j == 0){
						board[i][j].upLeft = board[i+1][7];
					}
					else board[i][j].upLeft = board[i+1][j-1];
					//upper right
					if (j == 7){
						board[i][j].upRight = board[i+1][0];
					}
					else board[i][j].upRight = board[i+1][j+1];
				}
				//assign bottom, bottom left, and bottom right neighbor
				if (i > 0){
					//down
					board[i][j].down = board[i-1][j];
					//down left
					if (j == 0){
						board[i][j].downLeft = board[i-1][7];
					}
					else board[i][j].downLeft = board[i-1][j-1];
					//down right
					if (j == 7){
						board[i][j].downRight = board[i-1][0];
					}
					else board[i][j] = board[i-1][j+1];
				}
				//assign upper left neighbor
			}
		}
	}

	public void initializeBoard(){
		for (int i = 0; i < 8; i++){
			//Create pawns
			board[1][i].setPiece(new PawnCylinder("", true, 1, i));
			board[6][i].setPiece(new PawnCylinder("", false, 6, i));
			//Create rooks
			if (i%7 == 0){
				board[0][i].setPiece(new RookCylinder("",true,0,i));
				board[7][i].setPiece(new RookCylinder("",false,7,i));
			}
			//Create knights
			if (i == 1 || i == 6){
				board[0][i].setPiece(new KnightCylinder("",true,0,i));
				board[7][i].setPiece(new KnightCylinder("",false,7,i));
			}
			//Create bishops
			if (i == 2 || i == 5){
				board[0][i].setPiece(new BishopCylinder("", true, 0, i));
				board[7][i].setPiece( new BishopCylinder("", false, 7, i));
			}
			//Create queens
			if (i == 3){
				board[0][i].setPiece(new QueenCylinder("", true, 0, i));
				board[7][i].setPiece(new QueenCylinder("", false, 7, i));
			}
			//Create kings
			if (i == 4){
				board[0][i].setPiece(new KingCylinder("", true, 0, i));
				board[7][i].setPiece(new KingCylinder("", false, 7, i));
			}
		}
	}


}