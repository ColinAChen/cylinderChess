package com.example.chessApp.cylinder;

import android.util.Log;

import com.example.chessApp.cylinder.PieceCylinder;

import java.util.ArrayList; // import the ArrayList class
public class CylinderLinkedBoard{
	//square have a eight neighbors
	private class Square{
		int row;
		int col;
		PieceCylinder piece;
		Square left;
		Square right;
		Square up;
		Square down;
		Square upLeft;
		Square upRight;
		Square downLeft;
		Square downRight;
		Square (int r, int c, PieceCylinder p){
			row = r;
			col = c;
			piece = p;
			left = null; right = null;
			up = null; down = null;
			upLeft = null; upRight = null;
			downLeft = null; downRight = null;
		}
		public void setPiece(PieceCylinder p){
			piece = p;
		}
		public PieceCylinder getPiece(){
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
	boolean whiteToMove = true;
	//MoveStack previousMoves = new MoveStack();
	//BoardHashTable previousBoards = new BoardHashTable();
	CylinderLinkedBoard(){
		//Hold the squares to be connected
		Square[][] board = new Square[8][8];
		for (int i = 0; i < 8; i++){
			for (int j = 0; j < 8; j++){
				board[i][j] = new Square(7-i,j,null);
			}
		}
		//Connect each square to its neighbors
		for (int i = 0; i < 8; i++){
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

	public boolean checkForCheck(int row, int col, int newRow, int newCol){
		PieceCylinder capturePiece = null;
		PieceCylinder pieceToMove = board[row][col].getPiece();
		return false;
	}
	public ArrayList<int[]> getLegalMoves(PieceCylinder pieceToMove){
		return new ArrayList<int[]>();
	}

	public boolean move(int row, int col, int newRow, int newCol){
		PieceCylinder pieceToMove = board[row][col].getPiece();
		//Create the algebraic chess notation from a move
		String prevMove = "";
		//transform the column to a rank
		char[]ranks = {'a','b','c','d','e','f','g','h'};
		//denote the piece name
		if (pieceToMove == null){
			return false;
		}
		if (pieceToMove.getName() != "p"){
			prevMove += pieceToMove.getName().toUpperCase();
		}
		//denote the origin
		prevMove+= (ranks[col]) + Integer.toString((8-row));
		//denote a capture
		if (board[newRow][newCol] != null){
			prevMove += "x";
		}
		//check that piece to move exists
		if (pieceToMove != null){
			//check that the correct side is moving
			if(pieceToMove.getColor()){
				ArrayList<int[]> legalMoves = getLegalMoves(pieceToMove);
				//if(legalMoves.contains(newPos)){
				for(int[]legalPos:legalMoves){
					//check that the destination is a legal move
					if(legalPos[0] == newRow && legalPos[1] == newCol){
						//Handle castling
						if ("k".equals(pieceToMove.getName()) && (newCol-col) > 1){
							////System.out.println("Kingside Castle");
							prevMove = "O-O";
							//kingSideCastle(pieceToMove);
						}
						else if ("k".equals(pieceToMove.getName()) && (col-newCol) > 1){
							prevMove = "O-O-O";
							//queenSideCastle(pieceToMove);
						}
						//check if pawn is capturing with enpassant
						else if ("p".equals(pieceToMove.getName()) && pieceToMove.getCol() != newCol && board[newRow][newCol] == null){
							//System.out.println("Moving a pawn");
							//if the pawn is capturing
							if (pieceToMove.getCol() != newCol){

								//System.out.println("Pawn is capturing");
								//System.out.printf("Pawn col is %d\n",pieceToMove.getCol());
								//System.out.printf("Pawn is moving to col %d\n",newCol);
								//if the destination is empty
								if (board[newRow][newCol].getPiece() == null){
									//System.out.println("Pawn is capturing a blank square, must be enpassant");
									pieceToMove.move(newRow,newCol);
									board[newRow][newCol].setPiece(pieceToMove);
									board[row][col].setPiece(null);
									board[row][newCol].setPiece(null);
									prevMove+='x';
								}
							}
						}
						else{
							pieceToMove.move(newRow,newCol);
							////System.out.println(pieceToMove.getRow() + pieceToMove.getCol());
							board[newRow][newCol].setPiece(pieceToMove);
							board[row][col] = null;
						}

						//remove potential for castling after a king or rook moves
						if ("k".equals(pieceToMove.getName())){
							KingCylinder kingToMove = (KingCylinder)pieceToMove;
							kingToMove.moved();
						} 
						if ("r".equals(pieceToMove.getName())){
							RookCylinder rookToMove = (RookCylinder)pieceToMove;
							rookToMove.moved();
						}
					}
				}
				/*
				//denote a check
				if(whiteKingInCheck() || blackKingInCheck()){
					prevMove += "+";
				}
				//denote checkmate
				if (whiteWin() || blackWin()){
					prevMove += "#";
				}
				*/
				//denote the destination
				prevMove += (ranks[newCol]) + Integer.toString((8-newRow));
				//System.out.println(prevMove);
				//previousMoves.push(prevMove);
				//previousBoards.add(boardToString());
				//System.out.println(previousMoves);
				//printBoard();
				whiteToMove = !whiteToMove;
				//this.oneFromTwo();
				return true;
			}
		}
		return false;
	}


}