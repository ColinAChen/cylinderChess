package com.example.cylinderchess;

import java.util.ArrayList; // import the ArrayList class
public class Board{
	Piece[][] board;
	Piece[] oneDimensional;
	boolean whiteToMove = true;

	public Board(Piece[][] board, Piece[] oneD){
		this.board = board;
		this.oneDimensional = oneD;
	}
	public void initializeBoard(){
		//Initializes a board
		//bottom row is white, white = true
		//top rows are black, black = false
		//board = new Piece[8][8];
		for (int i = 0; i < 8; i++){
			//board[0][i] = new Piece("", false, 0, i, false);
			board[1][i] = new Pawn("", false, 1, i, false);
			board[6][i] = new Pawn("",true, 6, i, false);
			//board[7][i] = new Piece("", true, 7, i, false);
			if (i == 0 || i == 7){
				//System.out.println("Creating rooks in row " + i);
				board[0][i] = new Rook("", false, 0, i, false);
				board[7][i] = new Rook("", true, 0, i, false);
			}
			if (i == 1 || i == 6){
				//System.out.println("Creating knights");
				board[0][i] = new Knight("", false, 0, i, false);
				board[7][i] = new Knight("", true, 0, i, false);
			}
			if (i == 2 || i == 5){
				//System.out.println("Creating bishops");
				board[0][i] = new Bishop("", false, 0, i, false);
				board[7][i] = new Bishop("", true, 0, i, false);
			}
			if (i == 3){
				//System.out.println("Creating kings");
				board[0][i] = new King("", false, 0, i, false);
				board[7][i] = new King("", true, 0, i, false);
			}
			if (i == 4){
				//System.out.println("Creating queens");
				board[0][i] = new Queen("", false, 0, i, false);
				board[7][i] = new Queen("", true, 0, i, false);
			}
		}
		oneDimensional = this.oneFromTwo();
	}	
	public ArrayList<int[]> getLegalMoves(Piece pieceToMove){
		//get the pieces possible moves
		if (pieceToMove==null){
			return;
		}
		ArrayList<int[]> possibleMoves = new ArrayList<int[]>();
		possibleMoves = pieceToMove.getPossibleMoves();
		ArrayList<int[]> legalMoves = new ArrayList<int[]>();
		//define legal moves for a pawn
		if ("p".equals(pieceToMove.name)){
			for (int[] possiblePair:possibleMoves){
				//if the potential square is diagonal
				//check for capturing
				if ((pieceToMove.y != possiblePair[1]) && (board[possiblePair[0]][possiblePair[1]] != null) && (board[possiblePair[0]][possiblePair[1]].color != pieceToMove.color)){
					//only add if destination is of the opposite color
					legalMoves.add(possiblePair);
				}
				//pawn is moving straight
				else{
					if (board[possiblePair[0]][possiblePair[1]] == null){
						//System.out.println("No piece found!");
						legalMoves.add(possiblePair);
					}
					else if (board[possiblePair[0]][possiblePair[1]].color != pieceToMove.color){
						//System.out.println("Piece is of other color!");
						legalMoves.add(possiblePair);
					}
				}	
			}
			return legalMoves;
		}
		// For knight
		// Only need to check that destination is empty or of other color
		else if("n".equals(pieceToMove.name)){
			for (int[] possiblePair:possibleMoves){
				//System.out.printf("Checking for piece at row %d, col %d%n",possiblePair[0],possiblePair[1]);
				if (board[possiblePair[0]][possiblePair[1]] == null){
					//System.out.println("No piece found!");
					legalMoves.add(possiblePair);
				}
				else if (board[possiblePair[0]][possiblePair[1]].color != pieceToMove.color){
					//System.out.println("Piece is of other color!");
					legalMoves.add(possiblePair);
				}
			}
		}
		//else if("k".equals(pieceToMove.name)){

		//}
		//rook, bishop, queen
		else{
			//Piece[] temprowcoldiag = new Piece[8];
			for (int[] possiblePair:possibleMoves){
			//System.out.printf("Checking for piece at row %d, col %d%n",possiblePair[0],possiblePair[1]);
			if (board[possiblePair[0]][possiblePair[1]] == null){
				//System.out.println("No piece found!");
				legalMoves.add(possiblePair);
			}
			else if (board[possiblePair[0]][possiblePair[1]].color != pieceToMove.color){
				//System.out.println("Piece is of other color!");
				legalMoves.add(possiblePair);
			}
		}
		
		//System.out.println("Found " + legalMoves.size() + " legal moves!"); 
		}
		
		return legalMoves;
	}

	public void move(int row, int col, int newrow,int newcol){
		Piece pieceToMove = board[row][col];
		if(pieceToMove.color == whiteToMove){
			int[] newPos = {newrow,newcol};
			//System.out.printf("Moving %s at row %d, col %d%n", pieceToMove.name, row,col);
			ArrayList<int[]> legalMoves = getLegalMoves(pieceToMove);
			//if(legalMoves.contains(newPos)){
			for(int[]legalPos:legalMoves){
				if(legalPos[0] == newrow && legalPos[1] == newcol){
					//System.out.printf("Moving %s at row %d, col %d to row %d, col %d%n", pieceToMove.name, row,col,newrow,newcol);
					pieceToMove.move(newrow,newcol);
					//System.out.println(pieceToMove.x + pieceToMove.y);
					board[newrow][newcol] = pieceToMove;
					board[row][col] = null;
				}
			}
		whiteToMove = !whiteToMove;
		}
		else{
			System.out.printf("Not %s's turn to move!%n", pieceToMove.getColor());
		}
		
	}
	public Piece findWhiteKing(){
		for(int i = 0; i < board.length; i++){
			for (int j = 0; j < board[0].length; j++){
				if(board[i][j].color && "k".equals(board[i][j].name)){
					return board[i][j];
				}
			}
		}
		return null;
	}
	public Piece findBlackKing(){
		for(int i = 0; i < board.length; i++){
			for (int j = 0; j < board[0].length; j++){
				if (!board[i][j].color && "k".equals(board[i][j].name)){
					return board[i][j];
				}
			}
		}
		return null;
	}

	public boolean whiteKingInCheck(){
		Piece king = findWhiteKing();
		ArrayList<int[]> legalMoves = new ArrayList<int[]>();
		for(int i = 0; i < board.length; i++){
			for (int j = 0; j < board[0].length; j++){
				//for each black piece
				if (!board[i][j].color){
					legalMoves = this.getLegalMoves(board[i][j]);
					for (int[]pair:legalMoves){
						if(pair[0] == king.x && pair[1]==king.y){
							return true;
						}
					}
				}
			}
		}
		return false;
	}
	public boolean blackKingInCheck(){
		Piece king = findBlackKing();
		ArrayList<int[]> legalMoves = new ArrayList<int[]>();
		for(int i = 0; i < board.length; i++){
			for (int j = 0; j < board[0].length; j++){
				//for each black piece
				if (board[i][j].color){
					legalMoves = this.getLegalMoves(board[i][j]);
					for (int[]pair:legalMoves){
						if(pair[0] == king.x && pair[1]==king.y){
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	public boolean whiteWin(){
		ArrayList<int[]> kingMoves = new ArrayList<int[]>();
		kingMoves = this.getLegalMoves(findBlackKing());
		if (blackKingInCheck() && (kingMoves.size() == 0)){
			return true;
		}
		return false;
	}
	public boolean blackWin(){
		ArrayList<int[]> kingMoves = new ArrayList<int[]>();
		kingMoves = this.getLegalMoves(findWhiteKing());
		if (whiteKingInCheck() && (kingMoves.size() == 0)){
			return true;
		}
		return false;
	}
	public boolean stalemate(){
		Piece[] board = oneFromTwo();
		int numWhiteMoves = 0;
		int numBlackMoves = 0;
		ArrayList<int[]> legalMoves = new ArrayList<int[]>();
		for(int i = 0; i < board.length; i++){
			if (board[i]!=null){
				legalMoves = getLegalMoves(board[i]);
				if (board[i].color){
					numWhiteMoves+=legalMoves.size();
				}
				else{
					numBlackMoves+=legalMoves.size();
				}
			}
		}
		if ((numWhiteMoves == 0 && !whiteKingInCheck()) || (numBlackMoves == 0 && !blackKingInCheck())){
			return true;
		}
		return false;
		
	}
	public Piece[] oneFromTwo(){
		//Piece[] oneD = new Piece[64];
		for (int i = 0; i < board.length; i++){
			for (int j = 0; j < board[i].length; j++){
				oneDimensional[(8*i) + j] = board[i][j];
			}
		}
		return oneDimensional;
	}
	public void printBoard(){
		for (int i = 0; i < board.length; i++){
			for (int j = 0; j < board[0].length; j++){
				if (board[i][j] != null){
					System.out.print(board[i][j].name + " ");
				}
				else System.out.print("0 ");
			}
			System.out.print("\n");
		}
		System.out.println("\n");
	}
}