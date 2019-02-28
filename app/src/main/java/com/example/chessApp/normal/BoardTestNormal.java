package com.example.chessApp.normal;

public class BoardTestNormal {
	public static void main(String[] args){
		PieceNormal[][] boardArray = new PieceNormal[8][8];
		PieceNormal[] oneD = new PieceNormal[64];
		BoardNormal board = new BoardNormal(boardArray, oneD);
		board.initializeBoard();
		board.printBoard();
		board.move(6,0,4,0);
		board.printBoard();
		board.move(6,1,4,1);
		board.printBoard();
	}
}