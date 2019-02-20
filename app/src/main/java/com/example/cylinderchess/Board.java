//package com.example.cylinderchess;

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
	
	public ArrayList<int[]> getLegalMoves(Piece pieceToMove){
		//get the pieces possible moves
		if (pieceToMove==null){
			return new ArrayList<int[]>();
		}
		System.out.printf("Finding moves for %s %s on row %d col %d%n", pieceToMove.getColor(), pieceToMove.name, pieceToMove.x, pieceToMove.y);
		ArrayList<int[]> possibleMoves = new ArrayList<int[]>();
		possibleMoves = pieceToMove.getPossibleMoves();
		ArrayList<int[]> legalMoves = new ArrayList<int[]>();
		System.out.printf("Initially has %d possible moves%n", possibleMoves.size());
		//define legal moves for a pawn
		if ("p".equals(pieceToMove.name)){
			for (int[] possiblePair:possibleMoves){
				if ((pieceToMove.y != possiblePair[1]) && (board[possiblePair[0]][possiblePair[1]] != null) && (board[possiblePair[0]][possiblePair[1]].color != pieceToMove.color)){
					//only add if destination is of the opposite color
					if(checkForCheck(pieceToMove.x,pieceToMove.y,possiblePair[0],possiblePair[1])){
						legalMoves.add(possiblePair);
					}
				}
				//pawn is moving straight
				else if(possiblePair[1] == pieceToMove.y){
					//if the pawn is trying to jump two squares, check that both squares in front of it are empty
					if (Math.abs(possiblePair[0] - pieceToMove.x) > 1){
						//System.out.println("Trying to jump two square");
						if (pieceToMove.color && board[possiblePair[0]][possiblePair[1]] == null && board[5][possiblePair[1]] == null){
							if(checkForCheck(pieceToMove.x,pieceToMove.y,possiblePair[0],possiblePair[1])){
								legalMoves.add(possiblePair);
							}
						}
						else if(!pieceToMove.color && board[possiblePair[0]][possiblePair[1]] == null && board[2][possiblePair[1]] == null){
							if(checkForCheck(pieceToMove.x,pieceToMove.y,possiblePair[0],possiblePair[1])){
								legalMoves.add(possiblePair);
							}
						}
					}
					else if (board[possiblePair[0]][possiblePair[1]] == null){
						//System.out.println("No piece found!");
						if(checkForCheck(pieceToMove.x,pieceToMove.y,possiblePair[0],possiblePair[1])){
							legalMoves.add(possiblePair);
						}
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
					if(checkForCheck(pieceToMove.x,pieceToMove.y,possiblePair[0],possiblePair[1])){
								legalMoves.add(possiblePair);
							}
					//System.out.println(legalMoves.size());
				}
				else if (board[possiblePair[0]][possiblePair[1]] != null && board[possiblePair[0]][possiblePair[1]].color != pieceToMove.color){
					//System.out.println("Piece is of other color!");
					if(checkForCheck(pieceToMove.x,pieceToMove.y,possiblePair[0],possiblePair[1])){
								legalMoves.add(possiblePair);
							}
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
				//System.out.printf("Potential square row %d col %d, pieceToMove square row %d col %d%n",possiblePairTemp[0], possiblePairTemp[1], pieceToMove.x, pieceToMove.y );
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
						if (possiblePairTemp[0] < pieceToMove.x && possiblePairTemp[1] < pieceToMove.y && pieceToMove.x - possiblePairTemp[0] < shortestDistances[4]){
							System.out.println("upleft");
							shortestDistances[4] = pieceToMove.x - possiblePairTemp[0];
							//blockPieces[4] = board[possiblePair[0]][possiblePair[1]];
						}
						//up right
						else if(possiblePairTemp[0] < pieceToMove.x && possiblePairTemp[1] > pieceToMove.y && pieceToMove.x - possiblePairTemp[0] < shortestDistances[5]){
							System.out.println("upright");
							shortestDistances[5] = pieceToMove.x - possiblePairTemp[0];
							//blockPieces[5] = board[possiblePair[0]][possiblePair[1]];
						}
						//down left
						else if(possiblePairTemp[0] > pieceToMove.x && possiblePairTemp[1] < pieceToMove.y && possiblePairTemp[0] - pieceToMove.x < shortestDistances[6] ){
							System.out.println("downleft");
							shortestDistances[6] = possiblePairTemp[0] - pieceToMove.x;
							//blockPieces[6] = board[possiblePair[0]][possiblePair[1]];
						}
						//down right
						else if(possiblePairTemp[0] > pieceToMove.x && possiblePairTemp[1] > pieceToMove.y && possiblePairTemp[0] - pieceToMove.x < shortestDistances[7]){
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

				System.out.printf("checking row %d col %d%n", possiblePair[0], possiblePair[1]);
				if (possiblePair[1] == pieceToMove.y){
					if (possiblePair[0] < pieceToMove.x && (pieceToMove.x - possiblePair[0]) <= shortestDistances[0]){
						System.out.println("Checking col");
						if ((pieceToMove.x - possiblePair[0]) == shortestDistances[0] && board[possiblePair[0]][possiblePair[1]] != null &&board[possiblePair[0]][possiblePair[1]].color != pieceToMove.color){
							System.out.println("Capture upcol");
							if(checkForCheck(pieceToMove.x,pieceToMove.y,possiblePair[0],possiblePair[1])){
								legalMoves.add(possiblePair);
							}
						}
						else if((pieceToMove.x - possiblePair[0]) < shortestDistances[0]){
							System.out.println("less than");
							if(checkForCheck(pieceToMove.x,pieceToMove.y,possiblePair[0],possiblePair[1])){
								legalMoves.add(possiblePair);
							}
						}
						//if (shortestDistances[0])
						System.out.println("not in same col");
					}
					//System.out.printf("Cannot move on row %d col %d%n", possiblePair[0], possiblePair[1]);
					//down column
					else if(pieceToMove.x < possiblePair[0] && (possiblePair[0] - pieceToMove.x) <= shortestDistances[1]){
						//System.out.println("checking row");
						if ((possiblePair[0] - pieceToMove.x) == shortestDistances[0] &&board[possiblePair[0]][possiblePair[1]] != null && board[possiblePair[0]][possiblePair[1]].color != pieceToMove.color){
							System.out.println("Capture downcol");
							if(checkForCheck(pieceToMove.x,pieceToMove.y,possiblePair[0],possiblePair[1])){
								legalMoves.add(possiblePair);
							}
						}
						else if((possiblePair[0] - pieceToMove.x) < shortestDistances[1]){
							if(checkForCheck(pieceToMove.x,pieceToMove.y,possiblePair[0],possiblePair[1])){
								legalMoves.add(possiblePair);
							}
						}
					}
				}
				//same col
				else if(possiblePair[0] == pieceToMove.x){
					//left row
					if (possiblePair[1] < pieceToMove.y && (pieceToMove.y - possiblePair[1]) <= shortestDistances[2]){
						if ((pieceToMove.y - possiblePair[1]) == shortestDistances[2] &&board[possiblePair[0]][possiblePair[1]] != null && board[possiblePair[0]][possiblePair[1]].color != pieceToMove.color){
							if(checkForCheck(pieceToMove.x,pieceToMove.y,possiblePair[0],possiblePair[1])){
								legalMoves.add(possiblePair);
							}
						}
						else if((pieceToMove.y - possiblePair[1]) < shortestDistances[2]){
							if(checkForCheck(pieceToMove.x,pieceToMove.y,possiblePair[0],possiblePair[1])){
								legalMoves.add(possiblePair);
							}
						}
					}
					//right row
					else if(pieceToMove.y < possiblePair[1] && (possiblePair[1] - pieceToMove.y) <= shortestDistances[3]){
						if ((possiblePair[1] - pieceToMove.y) == shortestDistances[3] && board[possiblePair[0]][possiblePair[1]] != null &&board[possiblePair[0]][possiblePair[1]].color != pieceToMove.color){
							if(checkForCheck(pieceToMove.x,pieceToMove.y,possiblePair[0],possiblePair[1])){
								legalMoves.add(possiblePair);
							}
						}
						else if((possiblePair[1] - pieceToMove.y) < shortestDistances[3]){
							if(checkForCheck(pieceToMove.x,pieceToMove.y,possiblePair[0],possiblePair[1])){
								legalMoves.add(possiblePair);
							}
						}
					}
				}
				// same diagonal
				else if((Math.abs(possiblePair[0] - pieceToMove.x)) == (Math.abs(possiblePair[1] - pieceToMove.y))){
					//up left
					if (possiblePair[0] <= pieceToMove.x && possiblePair[1] <= pieceToMove.y){
						if ((pieceToMove.x - possiblePair[0]) == shortestDistances[4] && board[possiblePair[0]][possiblePair[1]] != null &&board[possiblePair[0]][possiblePair[1]].color != pieceToMove.color){
							if(checkForCheck(pieceToMove.x,pieceToMove.y,possiblePair[0],possiblePair[1])){
								legalMoves.add(possiblePair);
							}
						}
						else if((pieceToMove.x - possiblePair[0]) < shortestDistances[4]){
							if(checkForCheck(pieceToMove.x,pieceToMove.y,possiblePair[0],possiblePair[1])){
								legalMoves.add(possiblePair);
							}
						}
					}
					//up right
					else if(possiblePair[0] <= pieceToMove.x && possiblePair[1] >= pieceToMove.y){
						if ((pieceToMove.x - possiblePair[0]) == shortestDistances[5] &&board[possiblePair[0]][possiblePair[1]] != null && board[possiblePair[0]][possiblePair[1]].color != pieceToMove.color){
							if(checkForCheck(pieceToMove.x,pieceToMove.y,possiblePair[0],possiblePair[1])){
								legalMoves.add(possiblePair);
							}
						}
						else if((pieceToMove.x - possiblePair[0]) < shortestDistances[5]){
							if(checkForCheck(pieceToMove.x,pieceToMove.y,possiblePair[0],possiblePair[1])){
								legalMoves.add(possiblePair);
							}
						}
					}
					//down left
					else if(possiblePair[0] >= pieceToMove.x && possiblePair[1] <= pieceToMove.y){
						if ((possiblePair[0] - pieceToMove.x) == shortestDistances[6] &&board[possiblePair[0]][possiblePair[1]] != null && board[possiblePair[0]][possiblePair[1]].color != pieceToMove.color){
							if(checkForCheck(pieceToMove.x,pieceToMove.y,possiblePair[0],possiblePair[1])){
								legalMoves.add(possiblePair);
							}
						}
						else if((possiblePair[0] - pieceToMove.x) < shortestDistances[6]){
							if(checkForCheck(pieceToMove.x,pieceToMove.y,possiblePair[0],possiblePair[1])){
								legalMoves.add(possiblePair);
							}
						}
					}
					//down right
					else if(possiblePair[0] >= pieceToMove.x && possiblePair[1] >= pieceToMove.y){
						if ((possiblePair[0] - pieceToMove.x) == shortestDistances[7] &&board[possiblePair[0]][possiblePair[1]] != null && board[possiblePair[0]][possiblePair[1]].color != pieceToMove.color){
							legalMoves.add(possiblePair);
						}
						else if((possiblePair[0] - pieceToMove.x) < shortestDistances[7]){
							if(checkForCheck(pieceToMove.x,pieceToMove.y,possiblePair[0],possiblePair[1])){
								legalMoves.add(possiblePair);
							}
							
						}
					}
				}
			}
			return legalMoves;
		}	
		//return null;
	}

	//check if a move is legal, then check if current turn's king is in check
	//true if valid move, false if not
	public boolean checkForCheck(int row, int col, int newrow, int newcol){
		Piece[][] tempBoard = board;
		/*
		Piece[][] tempBoard = new Piece[8][8];
		for (int i = 0; i < board.length;i++){
			for (int j = 0; j < board[0].length; j++){
				tempBoard[i][j] = board[i][j];
			}
		}*/
		//Piece[][] tempBoard = board.clone();
		System.out.println(tempBoard.length);
		System.out.println(tempBoard[0].length);
		System.out.println(tempBoard[0][0].name);
		//move the piece
		Piece pieceToMove = tempBoard[row][col];
		if (tempBoard[row][col] != null){
			tempBoard[row][col].move(newrow,newcol);
			//Piece pieceToMove = tempBoard[row][col];
		}
		else{
			System.out.printf("No piece found on row %d col %d%n", row, col);
			return true;
		}
		//tempBoard[row][col].move(newrow,newcol);
		tempBoard[newrow][newcol] = tempBoard[row][col];
		tempBoard[row][col] = null;

		//if it is white's turn, check if the white king is in check after the possible move
		if (whiteToMove){
			Piece whiteKing = findWhiteKing(tempBoard);
			if (kingInCheck(tempBoard, whiteKing)){
				tempBoard[newrow][newcol].move(row,col);
				tempBoard[row][col] = tempBoard[newrow][newcol];
				tempBoard[newrow][newcol] = null;
				return false;
			}
		}
		else{
			Piece blackKing = findBlackKing(tempBoard);
			if (kingInCheck(tempBoard, blackKing)){
				tempBoard[newrow][newcol].move(row,col);
				tempBoard[row][col] = tempBoard[newrow][newcol];
				tempBoard[newrow][newcol] = null;
				return false;
			}
		}
		tempBoard[newrow][newcol].move(row,col);
		tempBoard[row][col] = tempBoard[newrow][newcol];
		tempBoard[newrow][newcol] = null;
		System.out.printf("Moving %s %s to row %d col %d will not cause check!%n", pieceToMove.getColor(), pieceToMove.name,newrow,newcol);
		return true;
	}
	//true if king is in check, false if not in check
	public boolean kingInCheck(Piece[][] tempBoard, Piece king){
		//check for knights
		for (int i = 0; i < tempBoard.length; i++){
			for (int j = 0; j < tempBoard[0].length; j++){
				if ((Math.abs(king.x - i) == 2 && Math.abs(king.y - j) == 1) || (Math.abs(king.x - i) == 1 && Math.abs(king.y - j) == 2)){
					if (tempBoard[i][j] != null && tempBoard[i][j].color!=king.color && "n".equals(tempBoard[i][j].name)){
						return true;
					}
				}
			}
		}
		//check for pawns
		//if white king, only need to check top left and top right
		if (king.color){
			if (king.x < 7 && king.y > 0 && board[king.x + 1][king.y - 1] != null){
				if (!board[king.x + 1][king.y - 1].color && "p".equals(board[king.x + 1][king.y - 1].name)){
					return true;
				}
			}
			else if (king.x < 7 && king.y < 7 && board[king.x + 1][king.y + 1] != null){
				if (!board[king.x + 1][king.y + 1].color && "p".equals(board[king.x + 1][king.y + 1].name)){
					return true;
				}
			}
		}
		//check for black king
		//only need to check bottom left and right
		else if(!king.color){
			if (king.x > 0 && king.y > 0 &&board[king.x - 1][king.y - 1] != null){
				if (!board[king.x - 1][king.y - 1].color && "p".equals(board[king.x - 1][king.y - 1].name)){
					return true;
				}
			}
			else if (king.x > 0 && king.y < 7 &&board[king.x - 1][king.y + 1] != null){
				if (!board[king.x - 1][king.y + 1].color && "p".equals(board[king.x - 1][king.y + 1].name)){
					return true;
				}
			}
		}
		//Check for rooks and queens
		//check up a column
		for (int i = king.x; i >= 0; i--){
			if (tempBoard[i][king.y] != null && tempBoard[i][king.y].color != king.color){
				if ("q".equals(tempBoard[i][king.y].name) || "r".equals(tempBoard[i][king.y].name)){
					return true;
				}
			}
		}
		//check down a column
		for (int i = king.x; i < 8; i++){
			if (tempBoard[i][king.y] != null && tempBoard[i][king.y].color != king.color){
				if ("q".equals(tempBoard[i][king.y].name) || "r".equals(tempBoard[i][king.y].name)){
					return true;
				}
			}
		}
		//check to the left of the row
		for (int j = king.y; j >=0;j--){
			if (tempBoard[king.x][j] != null && tempBoard[king.x][j].color != king.color){
				if ("q".equals(tempBoard[king.x][j].name) || "r".equals(tempBoard[king.x][j].name)){
					return true;
				}
			}
		}
		//check to the right of the row
		for (int j = king.y; j < 8;j++){
			if (tempBoard[king.x][j] != null && tempBoard[king.x][j].color != king.color){
				if ("q".equals(tempBoard[king.x][j].name) || "r".equals(tempBoard[king.x][j].name)){
					return true;
				}
			}
		}

		//Check up left diagonal
		int kingRow = king.x - 1;
		int kingCol = king.y - 1;
		while(kingRow > -1 && kingCol > -1){
			if (tempBoard[kingRow][kingCol] != null){
				if (tempBoard[kingRow][kingCol].color != king.color && ("b".equals(tempBoard[kingRow][kingCol].name) || "q".equals(tempBoard[kingRow][kingCol].name ))){
					return true;
				}
			}
			kingRow--;
			kingCol--;
		}

		//check up right
		kingRow = king.x - 1;
		kingCol = king.y + 1;
		while(kingRow > -1 && kingCol < 8){
			if (tempBoard[kingRow][kingCol] != null){
				if (tempBoard[kingRow][kingCol].color != king.color && ("b".equals(tempBoard[kingRow][kingCol].name) || "q".equals(tempBoard[kingRow][kingCol].name ))){
					return true;
				}
			}
			kingRow--;
			kingCol++;
		}

		//check down left
		kingRow = king.x + 1;
		kingCol = king.y - 1;
		while(kingRow < 8 && kingCol > -1){
			if (tempBoard[kingRow][kingCol] != null){
				if (tempBoard[kingRow][kingCol].color != king.color && ("b".equals(tempBoard[kingRow][kingCol].name) || "q".equals(tempBoard[kingRow][kingCol].name ))){
					return true;
				}
			}
			kingRow++;
			kingCol--;
		}
		//check down right
		kingRow = king.x + 1;
		kingCol = king.y + 1;
		while(kingRow < 8 && kingCol < 8){
			if (tempBoard[kingRow][kingCol] != null){
				if (tempBoard[kingRow][kingCol].color != king.color && ("b".equals(tempBoard[kingRow][kingCol].name) || "q".equals(tempBoard[kingRow][kingCol].name ))){
					return true;
				}
			}
			kingRow++;
			kingCol++;
		}
		return false;


	}
	/*public boolean checkForCheck(int row, int col, int newrow,int newcol){
		Piece pieceToMove = board[row][col];
		if (pieceToMove != null){
			board[newrow][newcol] = pieceToMove;
			board[row][col] = null;
			if ((!whiteToMove && checkWhite()) ||(whiteToMove && blackKingInCheck()) ){
				return false;
			}
			board[row][col] = pieceToMove;
		}
		return true;
	}*/	
	/*
	public boolean checkWhiteCheck(int row, int col, int newrow, int newcol){
		Piece[][] tempBoard = board.clone();
		Piece pieceToMove = tempBoard[row][col];
		tempBoard[newrow][newcol] = pieceToMove;
		tempBoard[row][col] = null;
		Piece whiteKing = findWhiteKing(tempBoard);
		int whiteKingRow = whiteKing.x;
		int whiteKingCol = whiteKing.y;
		//check for knights
		for (int i = 0; i < tempBoard.length; i++){
			for (int j = 0; j < tempBoard[0].length; j++){
				if ((Math.abs(whiteKingRow - i) == 2 && Math.abs(whiteKingCol - j) == 1) || (Math.abs(whiteKingRow - i) == 1 && Math.abs(whiteKingCol - j) == 2)){
					if (tempBoard[i][j] != null && !tempBoard[i][j].color && "n".equals(tempBoard[i][j].name)){
						return false;
					}
				}
			}
		}
		//Check for rooks and queens
		//check up a column
		for (int i = whiteKing.x; i >= 0; i--){
			if (tempBoard[i][newcol] != null && !tempBoard[i][newcol].color){
				if ("q".equals(tempBoard[i][col].name) || "r".equals(tempBoard[i][col].name)){
					return false;
				}
			}
		}
		//check down a column
		for (int i = whiteKing.x; i < 8; i++){
			if (tempBoard[i][newcol] != null && !tempBoard[i][newcol].color){
				if ("q".equals(tempBoard[i][col].name) || "r".equals(tempBoard[i][col].name)){
					return false;
				}
			}
		}
		//check to the left of the row
		for (int j = whiteKing.y; j >=0;j--){
			if (tempBoard[newcol][j] != null && !tempBoard[newcol][j].color){
				if ("q".equals(tempBoard[newrow][j].name) || "r".equals(tempBoard[newrow][j].name)){
					return false;
				}
			}
		}
		//check to the right of the row
		for (int j = whiteKing.y; j < 8;j++){
			if (tempBoard[newcol][j] != null && !tempBoard[newcol][j].color){
				if ("q".equals(tempBoard[newrow][j].name) || "r".equals(tempBoard[newrow][j].name)){
					return false;
				}
			}
		}
		//Check for pawns
		if (tempBoard[whiteKingRow-1][whiteKingCol-1] != null){
			if (!tempBoard[whiteKingRow-1][whiteKingCol-1].color && "p".equals(tempBoard[whiteKingRow-1][whiteKingCol-1].name)){
				return false;
			}
		}
		if (tempBoard[whiteKingRow-1][whiteKingCol+1] != null){
			if (!tempBoard[whiteKingRow-1][whiteKingCol+1].color && "p".equals(tempBoard[whiteKingRow-1][whiteKingCol+1].name)){
				return false;
			}
		}
		//Check up left diagonal

		while(whiteKingRow > -1 && whiteKingCol > -1){
			if (tempBoard[whiteKingRow][whiteKingCol] != null){
				if (!tempBoard[whiteKingRow][whiteKingCol].color && ("b".equals(tempBoard[whiteKingRow][whiteKingCol].name) || "q".equals(tempBoard[whiteKingRow][whiteKingCol].name ))){
					return false;
				}
			}
			whiteKingRow--;
			whiteKingCol--;
		}
		whiteKingRow = whiteKing.x;
		whiteKingCol = whiteKing.y;
		//check up right
		while(whiteKingRow > -1 && whiteKingCol < 8){
			if (tempBoard[whiteKingRow][whiteKingCol] != null){
				if (!tempBoard[whiteKingRow][whiteKingCol].color && ("b".equals(tempBoard[whiteKingRow][whiteKingCol].name) || "q".equals(tempBoard[whiteKingRow][whiteKingCol].name ))){
					return false;
				}
			}
			whiteKingRow--;
			whiteKingCol++;
		}
		whiteKingRow = whiteKing.x;
		whiteKingCol = whiteKing.y;
		//check down left
		while(whiteKingRow < 8 && whiteKingCol > -1){
			if (tempBoard[whiteKingRow][whiteKingCol] != null){
				if (!tempBoard[whiteKingRow][whiteKingCol].color && ("b".equals(tempBoard[whiteKingRow][whiteKingCol].name) || "q".equals(tempBoard[whiteKingRow][whiteKingCol].name ))){
					return false;
				}
			}
			whiteKingRow++;
			whiteKingCol--;
		}
		whiteKingRow = whiteKing.x;
		whiteKingCol = whiteKing.y;
		//cehck down right
		while(whiteKingRow < 8 && whiteKingCol < 8){
			if (tempBoard[whiteKingRow][whiteKingCol] != null){
				if (!tempBoard[whiteKingRow][whiteKingCol].color && ("b".equals(tempBoard[whiteKingRow][whiteKingCol].name) || "q".equals(tempBoard[whiteKingRow][whiteKingCol].name ))){
					return false;
				}
			}
			whiteKingRow++;
			whiteKingCol++;
		}
		whiteKingRow = whiteKing.x;
		whiteKingCol = whiteKing.y;
		return true;
	}
	public boolean checkBlackCheck(int row, int col, int newrow, int newcol){
		Piece[][] tempBoard = board.clone();
		Piece pieceToMove = tempBoard[row][col];
		tempBoard[newrow][newcol] = pieceToMove;
		tempBoard[row][col] = null;
		Piece blackKing = findBlackKing(tempBoard);
		int blackKingRow = blackKing.x;
		int blackKingCol = blackKing.y;
		//check for knights
		for (int i = 0; i < tempBoard.length; i++){
			for (int j = 0; j < tempBoard[0].length; j++){
				if ((Math.abs(blackKingRow - i) == 2 && Math.abs(blackKingCol - j) == 1) || (Math.abs(blackKingRow - i) == 1 && Math.abs(blackKingCol - j) == 2)){
					if (tempBoard[i][j] != null && tempBoard[i][j].color && "n".equals(tempBoard[i][j].name)){
						return false;
					}
				}
			}
		}
		//Check for rooks and queens
		//check up a column
		for (int i = blackKingRow; i >= 0; i--){
			if (tempBoard[i][newcol] != null && tempBoard[i][newcol].color){
				if ("q".equals(tempBoard[i][newcol].name) || "r".equals(tempBoard[i][newcol].name)){
					return false;
				}
			}
		}
		//check down a column
		for (int i = blackKingRow; i < 8; i++){
			if (tempBoard[i][newcol] != null && tempBoard[i][newcol].color){
				if ("q".equals(tempBoard[i][newcol].name) || "r".equals(tempBoard[i][newcol].name)){
					return false;
				}
			}
		}
		//check to the left of the row
		for (int j = blackKingCol; j >=0;j--){
			if (tempBoard[newcol][j] != null && tempBoard[newcol][j].color){
				if ("q".equals(tempBoard[newrow][j].name) || "r".equals(tempBoard[newrow][j].name)){
					return false;
				}
			}
		}
		//check to the right of the row
		for (int j = blackKingCol; j < 8;j++){
			if (tempBoard[newcol][j] != null && tempBoard[newcol][j].color){
				if ("q".equals(tempBoard[newrow][j].name) || "r".equals(tempBoard[newrow][j].name)){
					return false;
				}
			}
		}
		//Check for pawns
		if (tempBoard[blackKingRow-1][blackKingCol-1] != null){
			if (tempBoard[blackKingRow-1][blackKingCol-1].color && "p".equals(tempBoard[blackKingRow-1][blackKingCol-1].name)){
				return false;
			}
		}
		if (tempBoard[blackKingRow-1][blackKingCol+1] != null){
			if (tempBoard[blackKingRow-1][blackKingCol+1].color && "p".equals(tempBoard[blackKingRow-1][blackKingCol+1].name)){
				return false;
			}
		}
		//Check up left diagonal

		while(blackKingRow > -1 && blackKingCol > -1){
			if (tempBoard[blackKingRow][blackKingCol] != null){
				if (tempBoard[blackKingRow][blackKingCol].color && ("b".equals(tempBoard[blackKingRow][blackKingCol].name) || "q".equals(tempBoard[blackKingRow][blackKingCol].name ))){
					return false;
				}
			}
			blackKingRow--;
			blackKingCol--;
		}
		blackKingRow = blackKing.x;
		blackKingCol = blackKing.y;
		//check up right
		while(blackKingRow > -1 && blackKingCol < 8){
			if (tempBoard[blackKingRow][blackKingCol] != null){
				if (tempBoard[blackKingRow][blackKingCol].color && ("b".equals(tempBoard[blackKingRow][blackKingCol].name) || "q".equals(tempBoard[blackKingRow][blackKingCol].name ))){
					return false;
				}
			}
			blackKingRow--;
			blackKingCol++;
		}
		blackKingRow = blackKing.x;
		blackKingCol = blackKing.y;
		//check down left
		while(blackKingRow < 8 && blackKingCol > -1){
			if (tempBoard[blackKingRow][blackKingCol] != null){
				if (tempBoard[blackKingRow][blackKingCol].color && ("b".equals(tempBoard[blackKingRow][blackKingCol].name) || "q".equals(tempBoard[blackKingRow][blackKingCol].name ))){
					return false;
				}
			}
			blackKingRow++;
			blackKingCol--;
		}
		blackKingRow = blackKing.x;
		blackKingCol = blackKing.y;
		//cehck down right
		while(blackKingRow < 8 && blackKingCol < 8){
			if (tempBoard[blackKingRow][blackKingCol] != null){
				if (tempBoard[blackKingRow][blackKingCol].color && ("b".equals(tempBoard[blackKingRow][blackKingCol].name) || "q".equals(tempBoard[blackKingRow][blackKingCol].name ))){
					return false;
				}
			}
			blackKingRow++;
			blackKingCol++;
		}
		blackKingRow = blackKing.x;
		blackKingCol = blackKing.y;
		return true;
	}
	*/		
	
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
						pieceToMove.move(newrow,newcol);
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

	public Piece findWhiteKing(Piece[][] tempBoard){
		for(int i = 0; i < tempBoard.length; i++){
			for (int j = 0; j < tempBoard[0].length; j++){
				if(tempBoard[i][j] != null && tempBoard[i][j].color && "k".equals(tempBoard[i][j].name)){
					return tempBoard[i][j];
				}
			}
		}
		return null;
	}
	public Piece findBlackKing(Piece[][] tempBoard){
		for(int i = 0; i < tempBoard.length; i++){
			for (int j = 0; j < tempBoard[0].length; j++){
				if (tempBoard[i][j] != null && !tempBoard[i][j].color && "k".equals(tempBoard[i][j].name)){
					return tempBoard[i][j];
				}
			}
		}
		return null;
	}

	public boolean whiteKingInCheck(Piece[][] tempBoard){
		Piece king = findWhiteKing(tempBoard);
		ArrayList<int[]> legalMoves = new ArrayList<int[]>();
		for(int i = 0; i < tempBoard.length; i++){
			for (int j = 0; j < tempBoard[0].length; j++){
				//for each black piece
				if (tempBoard[i][j] != null && !tempBoard[i][j].color){
					legalMoves = this.getLegalMoves(tempBoard[i][j]);
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
	public boolean blackKingInCheck(Piece[][] tempBoard){
		Piece king = findBlackKing(tempBoard);
		ArrayList<int[]> legalMoves = new ArrayList<int[]>();
		for(int i = 0; i < tempBoard.length; i++){
			for (int j = 0; j < tempBoard[0].length; j++){
				//for each black piece
				if (tempBoard[i][j] != null && tempBoard[i][j].color){
					legalMoves = this.getLegalMoves(tempBoard[i][j]);
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
		kingMoves = this.getLegalMoves(findBlackKing(board));
		if (blackKingInCheck(board) && (kingMoves.size() == 0)){
			return true;
		}
		return false;
	}
	public boolean blackWin(){
		ArrayList<int[]> kingMoves = new ArrayList<int[]>();
		kingMoves = this.getLegalMoves(findWhiteKing(board));
		if (whiteKingInCheck(board) && (kingMoves.size() == 0)){
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
		if ((numWhiteMoves == 0 && !whiteKingInCheck(board)) || (numBlackMoves == 0 && !blackKingInCheck(board))){
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