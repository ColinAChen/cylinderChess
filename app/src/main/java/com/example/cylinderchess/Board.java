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
				board[7][i] = new Rook("", true, 7, i, false);
			}
			if (i == 1 || i == 6){
				//System.out.println("Creating knights");
				board[0][i] = new Knight("", false, 0, i, false);
				board[7][i] = new Knight("", true, 7, i, false);
			}
			if (i == 2 || i == 5){
				//System.out.println("Creating bishops");
				board[0][i] = new Bishop("", false, 0, i, false);
				board[7][i] = new Bishop("", true, 7, i, false);
			}
			if (i == 3){
				//System.out.println("Creating kings");
				board[0][i] = new King("", false, 0, i, false);
				board[7][i] = new King("", true, 7, i, false);
			}
			if (i == 4){
				//System.out.println("Creating queens");
				board[0][i] = new Queen("", false, 0, i, false);
				board[7][i] = new Queen("", true, 7, i, false);
			}
		}
		this.oneFromTwo();
	}
	public boolean checkForCheck(int row, int col, int newrow,int newcol){
		Piece pieceToMove = board[row][col];
		if (pieceToMove != null){
			board[newrow][newcol] = pieceToMove;
			board[row][col] = null;
			if ((!whiteToMove && whiteKingInCheck()) ||(whiteToMove && blackKingInCheck()) ){
				return false;
			}
			board[row][col] = pieceToMove;
		}
		return true;
	}	
	public ArrayList<int[]> getLegalMoves(Piece pieceToMove){
		//get the pieces possible moves
		if (pieceToMove==null){
			return new ArrayList<int[]>();
		}
		System.out.printf("Finding moves for %s %s%n", pieceToMove.getColor(), pieceToMove.name);
		ArrayList<int[]> possibleMoves = new ArrayList<int[]>();
		possibleMoves = pieceToMove.getPossibleMoves();
		ArrayList<int[]> legalMoves = new ArrayList<int[]>();
		System.out.printf("Initially has %d possible moves%n", possibleMoves.size());
		//define legal moves for a pawn
		if ("p".equals(pieceToMove.name)){
			for (int[] possiblePair:possibleMoves){
				if ((pieceToMove.y != possiblePair[1]) && (board[possiblePair[0]][possiblePair[1]] != null) && (board[possiblePair[0]][possiblePair[1]].color != pieceToMove.color)){
					//only add if destination is of the opposite color
					legalMoves.add(possiblePair);
				}
				//pawn is moving straight
				else if(possiblePair[1] == pieceToMove.y){
					//if the pawn is trying to jump two squares, check that both squares in front of it are empty
					if (Math.abs(possiblePair[0] - pieceToMove.x) > 1){
						//System.out.println("Trying to jump two square");
						if (pieceToMove.color && board[possiblePair[0]][possiblePair[1]] == null && board[2][possiblePair[1]] == null){
							legalMoves.add(possiblePair);
						}
						else if(board[possiblePair[0]][possiblePair[1]] == null && board[5][possiblePair[1]] == null){
							legalMoves.add(possiblePair);
						}
					}
					else if (board[possiblePair[0]][possiblePair[1]] == null){
						//System.out.println("No piece found!");
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
					//System.out.println(legalMoves.size());
				}
				else if (board[possiblePair[0]][possiblePair[1]] != null && board[possiblePair[0]][possiblePair[1]].color != pieceToMove.color){
					//System.out.println("Piece is of other color!");
					legalMoves.add(possiblePair);
					//System.out.println(legalMoves.size());
				}
			}
			return legalMoves;
		}
		//else if("k".equals(pieceToMove.name)){
		//}
		//rook, bishop, queen, king
		else{
			//hold the closest pieces that are in the path of a rook, bishop, queen, or king
			//ArrayList<Piece> blockPieces = new ArrayList<Piece>();
			//up col, down col, left row, right row, up left, up right, down left, down right
			//Piece[] blockPieces = new Piece[9];
			int[] shortestDistances = {9,9,9,9,9,9,9,9};
			for (int[] possiblePairTemp:possibleMoves){
				System.out.printf("Potential square row %d col %d, pieceToMove square row %d col %d%n",possiblePairTemp[0], possiblePairTemp[1], pieceToMove.x, pieceToMove.y );
				if (board[possiblePairTemp[0]][possiblePairTemp[1]] != null){
					System.out.printf("%s %s exists at row %d col %d%n",board[possiblePairTemp[0]][possiblePairTemp[1]].getColor(),board[possiblePairTemp[0]][possiblePairTemp[1]].name,possiblePairTemp[0], possiblePairTemp[1]);
					//same column
					if (possiblePairTemp[1] == pieceToMove.y){
						//up column
						if (possiblePairTemp[0] < pieceToMove.x && (pieceToMove.x - possiblePairTemp[0]) < shortestDistances[0]){
							System.out.println("upcol");
							shortestDistances[0] = pieceToMove.x - possiblePairTemp[0];
							//blockPieces[0] = board[possiblePair[0]][possiblePair[1]];
							//if (shortestDistances[0])
						}
						//down column
						else if(pieceToMove.x < possiblePairTemp[0] && (possiblePairTemp[0] - pieceToMove.x) < shortestDistances[1]){
							System.out.println("downcol");
							shortestDistances[1] = possiblePairTemp[0] - pieceToMove.x;
							//blockPieces[1] = board[possiblePair[0]][possiblePair[1]];
						}
					}
					//same row
					else if(possiblePairTemp[0] == pieceToMove.x){
						//left row
						if (possiblePairTemp[1] < pieceToMove.y && (pieceToMove.y - possiblePairTemp[1]) < shortestDistances[2]){
							//System.out.println("leftrow");
							shortestDistances[2] = pieceToMove.y - possiblePairTemp[1];
							//blockPieces[2] = board[possiblePair[0]][possiblePair[1]];
						}
						//right row
						else if(pieceToMove.y < possiblePairTemp[1] && (possiblePairTemp[1] - pieceToMove.y) < shortestDistances[3]){
							//System.out.println("rightrow");
							shortestDistances[3] = possiblePairTemp[1] - pieceToMove.y;
							//blockPieces[3] = board[possiblePair[0]][possiblePair[1]];
						}
					}
					// same diagonal
					//Sysetm.out.println((Math.abs(possiblePairTemp[0] - pieceToMove.x)));
					//System.out.println(Math.abs(possiblePairTemp[1] - pieceToMove.y)));
					else if((Math.abs(possiblePairTemp[0] - pieceToMove.x)) == (Math.abs(possiblePairTemp[1] - pieceToMove.y))){
						//up left
						if (possiblePairTemp[0] < pieceToMove.x && possiblePairTemp[1] < pieceToMove.y){
							System.out.println("upleft");
							shortestDistances[4] = pieceToMove.x - possiblePairTemp[0];
							//blockPieces[4] = board[possiblePair[0]][possiblePair[1]];
						}
						//up right
						else if(possiblePairTemp[0] < pieceToMove.x && possiblePairTemp[1] > pieceToMove.y){
							System.out.println("upright");
							shortestDistances[5] = pieceToMove.x - possiblePairTemp[0];
							//blockPieces[5] = board[possiblePair[0]][possiblePair[1]];
						}
						//down left
						else if(possiblePairTemp[0] > pieceToMove.x && possiblePairTemp[1] < pieceToMove.y){
							System.out.println("downleft");
							shortestDistances[6] = possiblePairTemp[0] - pieceToMove.x;
							//blockPieces[6] = board[possiblePair[0]][possiblePair[1]];
						}
						//down right
						else if(possiblePairTemp[0] > pieceToMove.x && possiblePairTemp[1] > pieceToMove.y){
							System.out.println("downright");
							shortestDistances[7] = possiblePairTemp[0] - pieceToMove.x;
							//blockPieces[7] = board[possiblePair[0]][possiblePair[1]];
						}
					//blockPieces.add(board[possiblePair[0]][possiblePair[1]]);
					}
				}
			}
			for(int test:shortestDistances){
				System.out.println(test);
			}
			for (int[] possiblePair:possibleMoves){
				//same column
				if (possiblePair[1] == pieceToMove.y){
					//up column
					if (possiblePair[0] < pieceToMove.x && (pieceToMove.x - possiblePair[0]) < shortestDistances[0]){
						//System.out.println("Checking col");
						if ((pieceToMove.x - possiblePair[0]) == shortestDistances[0] && board[possiblePair[0]][possiblePair[1]].color != pieceToMove.color){
							//System.out.println("Capture upcol");
							legalMoves.add(possiblePair);
						}
						else if((pieceToMove.x - possiblePair[0]) < shortestDistances[0]){
							//System.out.println("less than");
							legalMoves.add(possiblePair);
						}
						//if (shortestDistances[0])
					}
					//down column
					else if(pieceToMove.x < possiblePair[0] && (possiblePair[0] - pieceToMove.x) < shortestDistances[1]){
						if ((possiblePair[0] - pieceToMove.x) == shortestDistances[0] && board[possiblePair[0]][possiblePair[1]].color != pieceToMove.color){
							legalMoves.add(possiblePair);
						}
						else if((possiblePair[0] - pieceToMove.x) < shortestDistances[1]){
							legalMoves.add(possiblePair);
						}
					}
				}
				//same col
				else if(possiblePair[0] == pieceToMove.x){
					//left row
					if (possiblePair[1] < pieceToMove.y && (pieceToMove.y - possiblePair[1]) <= shortestDistances[2]){
						if ((pieceToMove.y - possiblePair[1]) == shortestDistances[2] && board[possiblePair[0]][possiblePair[1]].color != pieceToMove.color){
							legalMoves.add(possiblePair);
						}
						else if((pieceToMove.y - possiblePair[1]) < shortestDistances[2]){
							legalMoves.add(possiblePair);
						}
					}
					//right row
					else if(pieceToMove.y < possiblePair[1] && (possiblePair[1] - pieceToMove.y) <= shortestDistances[3]){
						if ((possiblePair[1] - pieceToMove.y) == shortestDistances[3] && board[possiblePair[0]][possiblePair[1]].color != pieceToMove.color){
							legalMoves.add(possiblePair);
						}
						else if((possiblePair[1] - pieceToMove.y) < shortestDistances[3]){
							legalMoves.add(possiblePair);
						}
					}
				}
				// same diagonal
				else if((Math.abs(possiblePair[0] - pieceToMove.x)) == (Math.abs(possiblePair[1] - pieceToMove.y))){
					//up left
					if (possiblePair[0] < pieceToMove.x && possiblePair[1] < pieceToMove.y){
						if ((pieceToMove.x - possiblePair[0]) == shortestDistances[4] && board[possiblePair[0]][possiblePair[1]].color != pieceToMove.color){
							legalMoves.add(possiblePair);
						}
						else if((pieceToMove.x - possiblePair[0]) < shortestDistances[4]){
							legalMoves.add(possiblePair);
						}
					}
					//up right
					else if(possiblePair[0] < pieceToMove.x && possiblePair[1] > pieceToMove.y){
						if ((pieceToMove.x - possiblePair[0]) == shortestDistances[5] && board[possiblePair[0]][possiblePair[1]].color != pieceToMove.color){
							legalMoves.add(possiblePair);
						}
						else if((pieceToMove.x - possiblePair[0]) < shortestDistances[5]){
							legalMoves.add(possiblePair);
						}
					}
					//down left
					else if(possiblePair[0] > pieceToMove.x && possiblePair[1] < pieceToMove.y){
						if ((possiblePair[0] - pieceToMove.x) == shortestDistances[6] && board[possiblePair[0]][possiblePair[1]].color != pieceToMove.color){
							legalMoves.add(possiblePair);
						}
						else if((possiblePair[0] - pieceToMove.x) < shortestDistances[6]){
							legalMoves.add(possiblePair);
						}
					}
					//down right
					else if(possiblePair[0] > pieceToMove.x && possiblePair[1] > pieceToMove.y){
						if ((pieceToMove.x - possiblePair[0]) == shortestDistances[7] && board[possiblePair[0]][possiblePair[1]].color != pieceToMove.color){
							legalMoves.add(possiblePair);
						}
						else if((pieceToMove.x - possiblePair[0]) < shortestDistances[7]){
							legalMoves.add(possiblePair);
						}
					}
				}
			}
			return legalMoves;
		}	
		//return null;
	}
	

			
	
	public void printLegalMoves(int row, int col){
		Piece pieceToMove = board[row][col];
		if(pieceToMove==null){
			System.out.printf("No piece found at row %d and column %d%n", row, col);
			return;
		}
		ArrayList<int[]> legalMoves = new ArrayList<int[]>();
		legalMoves = this.getLegalMoves(pieceToMove);
		if (legalMoves.size() > 0){
			System.out.printf("Legal moves for %s %s at row %d and column %d are: %n", pieceToMove.getColor(), pieceToMove.name, row,col);
		}
		else{
			System.out.printf("No legal moves for %s %s at row %d and column %d%n", pieceToMove.getColor(), pieceToMove.name, row,col);
		}
		for(int[] legalSquare:legalMoves){
			System.out.println(legalSquare[0] + " " + legalSquare[1]);
		}
		this.printBoard();
	}
	public boolean move(int row, int col, int newrow,int newcol){
		Piece pieceToMove = board[row][col];
		if (pieceToMove != null){
			if(pieceToMove.color == whiteToMove){
				int[] newPos = {newrow,newcol};
				//System.out.printf("Moving %s at row %d, col %d%n", pieceToMove.name, row,col);
				ArrayList<int[]> legalMoves = getLegalMoves(pieceToMove);
				//if(legalMoves.contains(newPos)){
				for(int[]legalPos:legalMoves){
					if(legalPos[0] == newrow && legalPos[1] == newcol){
						//System.out.printf("Moving %s at row %d, col %d to row %d, col %d%n", pieceToMove.name, row,col,newrow,newcol);
						pieceToMove.move(newrow-row,newcol-col);
						//System.out.println(pieceToMove.x + pieceToMove.y);
						board[newrow][newcol] = pieceToMove;
						board[row][col] = null;
					}
				}
				whiteToMove = !whiteToMove;
				this.oneFromTwo();
				return true;
			}
		}
		else{
			System.out.printf("Not %s's turn to move!%n", pieceToMove.getColor());
		}
		return false;
	}
	public Piece findWhiteKing(){
		for(int i = 0; i < board.length; i++){
			for (int j = 0; j < board[0].length; j++){
				if(board[i][j] != null && board[i][j].color && "k".equals(board[i][j].name)){
					return board[i][j];
				}
			}
		}
		return null;
	}
	public Piece findBlackKing(){
		for(int i = 0; i < board.length; i++){
			for (int j = 0; j < board[0].length; j++){
				if (board[i][j] != null && !board[i][j].color && "k".equals(board[i][j].name)){
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
				if (board[i][j] != null && !board[i][j].color){
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
				if (board[i][j] != null && board[i][j].color){
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
		this.oneFromTwo();
		int numWhiteMoves = 0;
		int numBlackMoves = 0;
		ArrayList<int[]> legalMoves = new ArrayList<int[]>();
		for(int i = 0; i < oneDimensional.length; i++){
			if (oneDimensional[i]!=null){
				legalMoves = getLegalMoves(oneDimensional[i]);
				if (oneDimensional[i].color){
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
	public void oneFromTwo(){
		//Piece[] oneD = new Piece[64];
		for (int i = 0; i < board.length; i++){
			for (int j = 0; j < board[i].length; j++){
				oneDimensional[(8*i) + j] = board[i][j];
			}
		}
		//return oneDimensional;
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
	public void clearBoard(){
		for (int i = 0; i < board.length;i++){
			for (int j = 0; j < board[0].length; j++){
				board[i][j] = null;
			}
		}
	}
	public void place(Piece pieceToPlace, int row, int col){
		board[row][col] = pieceToPlace;
	}
}