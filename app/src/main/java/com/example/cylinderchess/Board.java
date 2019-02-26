//package com.example.cylinderchess;

import java.util.ArrayList; // import the ArrayList class
public class Board{
	Piece[][] board;
	Piece[] oneDimensional;
	boolean whiteToMove = true;
	//left = queenside, right = kingside;
	String castleDirection = null;

	public Board(Piece[][] board, Piece[] oneD){
		this.board = board;		this.oneDimensional = oneD;
	}
	public void initializeBoard(){
		//Initializes a board
		//bottom row is white, white = true
		//top rows are black, black = false
		//board = new Piece[8][8];
		for (int i = 0; i < 8; i++){
			//board[0][i] = new Piece("", false, 0, i, false);
			board[1][i] = new Pawn("", false, 1, i);
			board[6][i] = new Pawn("",true, 6, i);
			//board[7][i] = new Piece("", true, 7, i, false);
			if (i == 0 || i == 7){
				//System.out.println("Creating rooks in row " + i);
				board[0][i] = new Rook("", false, 0, i);
				board[7][i] = new Rook("", true, 7, i);
			}
			if (i == 1 || i == 6){
				//System.out.println("Creating knights");
				board[0][i] = new Knight("", false, 0, i);
				board[7][i] = new Knight("", true, 7, i);
			}
			if (i == 2 || i == 5){
				//System.out.println("Creating bishops");
				board[0][i] = new Bishop("", false, 0, i);
				board[7][i] = new Bishop("", true, 7, i);
			}
			if (i == 4){
				//System.out.println("Creating kings");
				board[0][i] = new King("", false, 0, i);
				board[7][i] = new King("", true, 7, i);
			}
			if (i == 3){
				//System.out.println("Creating queens");
				board[0][i] = new Queen("", false, 0, i);
				board[7][i] = new Queen("", true, 7, i);
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
						if ((possiblePair[0] - pieceToMove.x) == shortestDistances[1] &&board[possiblePair[0]][possiblePair[1]] != null && board[possiblePair[0]][possiblePair[1]].color != pieceToMove.color){
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
					//Check for castling
					/*
					if ("k".equals(pieceToMove.name)){
							if(!pieceToMove.hasMoved && !kingInCheck(pieceToMove)){
								//check queenside (left)
								//Check that the corner rook hasn't moved
								if (board[king.x][0] != null && "r".equals(board[king.x][0].name)  && !board[king.x][0].hasMoved){
									//check that the squares in between the rook and king are empty
									(board[king.x][1] == null && board[king.x][2] == null && board[king.x][3] == null){
											if (checkForCheck(pieceToMove.x,pieceToMove.y,pieceToMove.x,pieceToMove.y-1) && checkForCheck(pieceToMove.x,pieceToMove.y,pieceToMove.x,pieceToMove.y-2)){
												int[]castleMove = {pieceToMove.x,pieceToMove.y-2};
												legalMoves.add(castlePair);
											}
									}
								}
								//check kingside (right)

							}
					}*/
				}
			}
			return legalMoves;
		}	
		//return null;
	}

	//check if a move is legal, then check if current turn's king is in check
	//true if valid move, false if not
	public boolean checkForCheck(int row, int col, int newrow, int newcol){
		//move the piece
		Piece capturePiece = null;
		Piece pieceToMove = board[row][col];
		if (board[row][col] != null){
			board[row][col].move(newrow,newcol);
			//Piece pieceToMove = board[row][col];
		}
		else{
			System.out.printf("No piece found on row %d col %d%n", row, col);
			return true;
		}
		if (board[newrow][newcol] != null){
			capturePiece = board[newrow][newcol];
		}
		//board[row][col].move(newrow,newcol);
		board[newrow][newcol] = board[row][col];
		board[row][col] = null;

		//if it is white's turn, check if the white king is in check after the possible move
		if (whiteToMove){
			Piece whiteKing = findWhiteKing();
			if (kingInCheck(whiteKing)){
				board[newrow][newcol].move(row,col);
				board[row][col] = board[newrow][newcol];
				board[newrow][newcol] = capturePiece;
				return false;
			}
		}
		else{
			Piece blackKing = findBlackKing();
			if (kingInCheck(blackKing)){
				board[newrow][newcol].move(row,col);
				board[row][col] = board[newrow][newcol];
				board[newrow][newcol] = capturePiece;
				return false;
			}
		}
		board[newrow][newcol].move(row,col);
		board[row][col] = board[newrow][newcol];
		board[newrow][newcol] = capturePiece;
		System.out.printf("Moving %s %s to row %d col %d will not cause check!%n", pieceToMove.getColor(), pieceToMove.name,newrow,newcol);
		return true;
	}
	//true if king is in check, false if not in check
	public boolean kingInCheck(Piece king){
		//check for knights
		if (king==null){
			return false;
		}
		for (int i = 0; i < board.length; i++){
			for (int j = 0; j < board[0].length; j++){
				if ((Math.abs(king.x - i) == 2 && Math.abs(king.y - j) == 1) || (Math.abs(king.x - i) == 1 && Math.abs(king.y - j) == 2)){
					if (board[i][j] != null && board[i][j].color!=king.color && "n".equals(board[i][j].name)){
						return true;
					}
				}
				//return false;
			}
		}
		//check for pawns
		//if white king, only need to check top left and top right
		if (king.color){
			if (king.x < 7 && king.y > 0 && board[king.x + 1][king.y - 1] != null){
				if (!board[king.x + 1][king.y - 1].color && "p".equals(board[king.x + 1][king.y - 1].name)){
					System.out.printf("%s %s found at row %d col %d%n", board[king.x + 1][king.y - 1],board[king.x + 1][king.y - 1].getColor(), king.x + 1, king.y +1 );
					return true;
				}

			}
			else if (king.x < 7 && king.y < 7 && board[king.x + 1][king.y + 1] != null){
				if (!board[king.x + 1][king.y + 1].color && "p".equals(board[king.x + 1][king.y + 1].name)){
					System.out.printf("%s %s found at row %d col %d%n", board[king.x + 1][king.y - 1],board[king.x + 1][king.y - 1].getColor(), king.x + 1, king.y +1 );					
					return true;
				}
			}
		}
		//check for black king
		//only need to check bottom left and right
		else if(!king.color){
			if (king.x > 0 && king.y > 0 &&board[king.x - 1][king.y - 1] != null){
				if (!board[king.x - 1][king.y - 1].color && "p".equals(board[king.x - 1][king.y - 1].name)){
					System.out.printf("%s %s found at row %d col %d%n", board[king.x + 1][king.y - 1],board[king.x + 1][king.y - 1].getColor(), king.x + 1, king.y +1 );
					
					return true;
				}
			}
			else if (king.x > 0 && king.y < 7 &&board[king.x - 1][king.y + 1] != null){
				if (!board[king.x - 1][king.y + 1].color && "p".equals(board[king.x - 1][king.y + 1].name)){
					System.out.printf("%s %s found at row %d col %d%n", board[king.x + 1][king.y - 1],board[king.x + 1][king.y - 1].getColor(), king.x + 1, king.y +1 );

					return true;
				}
			}
		}
		//Check for rooks and queens
		//check up a column
		for (int i = king.x - 1; i >= 0; i--){
			if (board[i][king.y] != null && board[i][king.y].color != king.color){
				if ("q".equals(board[i][king.y].name) || "r".equals(board[i][king.y].name)){
					System.out.printf("%s %s found at row %d col %d%n",board[i][king.y].getColor(), board[i][king.y].name,i,king.y);
					return true;
				}
				break;
			}
			else if(board[i][king.y] != null){
				break;
			}
		}
		//check down a column
		for (int i = king.x + 1; i < 8; i++){
			System.out.println(i + " " + king.y);
			if (board[i][king.y] != null && board[i][king.y].color != king.color){
				if ("q".equals(board[i][king.y].name) || "r".equals(board[i][king.y].name)){

					System.out.printf("%s %s found at row %d col %d%n",board[i][king.y].getColor(), board[i][king.y].name,i,king.y);
				
					return true;
				}
				System.out.println(board[i][king.y].getColor());
				//break;
			}
			else if(board[i][king.y] != null){
				break;
			}
		}
		//check to the left of the row
		for (int j = king.y - 1; j >=0;j--){
			if (board[king.x][j] != null && board[king.x][j].color != king.color){
				if ("q".equals(board[king.x][j].name) || "r".equals(board[king.x][j].name)){
					System.out.printf("%s %s found at row %d col %d%n",board[king.x][j].getColor(), board[king.x][j].name,king.x,j);
						
					return true;
				}
				//break;
			}
			else if(board[king.x][j] != null){
				break;
			}
		}
		//check to the right of the row
		for (int j = king.y + 1; j < 8;j++){
			if (board[king.x][j] != null && board[king.x][j].color != king.color){
				if ("q".equals(board[king.x][j].name) || "r".equals(board[king.x][j].name)){
					System.out.printf("%s %s found at row %d col %d%n",board[king.x][j].getColor(), board[king.x][j].name,king.x,j);

					return true;
				}
				//break;
			}
			else if(board[king.x][j] != null){
				break;
			}
		}

		//Check up left diagonal
		int kingRow = king.x - 1;
		int kingCol = king.y - 1;
		while(kingRow > -1 && kingCol > -1){
			if (board[kingRow][kingCol] != null){
				if (board[kingRow][kingCol].color != king.color && ("b".equals(board[kingRow][kingCol].name) || "q".equals(board[kingRow][kingCol].name ))){
					System.out.printf("%s %s found at row %d col %d%n",board[kingRow][kingCol].getColor(), board[kingRow][kingCol].name,kingRow,kingCol);
					
					return true;
				}
				else if(board[kingRow][kingCol] != null){
					break;
				}
			}
			kingRow--;
			kingCol--;
		}

		//check up right
		kingRow = king.x - 1;
		kingCol = king.y + 1;
		while(kingRow > -1 && kingCol < 8){
			if (board[kingRow][kingCol] != null){
				if (board[kingRow][kingCol].color != king.color && ("b".equals(board[kingRow][kingCol].name) || "q".equals(board[kingRow][kingCol].name ))){
					System.out.printf("%s %s found at row %d col %d%n",board[kingRow][kingCol].getColor(), board[kingRow][kingCol].name,kingRow,kingCol);
				
					return true;
				}
				else if(board[kingRow][kingCol] != null){
					break;
				}
			}
			kingRow--;
			kingCol++;
		}

		//check down left
		kingRow = king.x + 1;
		kingCol = king.y - 1;
		while(kingRow < 8 && kingCol > -1){
			if (board[kingRow][kingCol] != null){
				if (board[kingRow][kingCol].color != king.color && ("b".equals(board[kingRow][kingCol].name) || "q".equals(board[kingRow][kingCol].name ))){
					System.out.printf("%s %s found at row %d col %d%n",board[kingRow][kingCol].getColor(), board[kingRow][kingCol].name,kingRow,kingCol);
				
					return true;
				}
				else if(board[kingRow][kingCol] != null){
					break;
				}
			}
			kingRow++;
			kingCol--;
		}
		//check down right
		kingRow = king.x + 1;
		kingCol = king.y + 1;
		while(kingRow < 8 && kingCol < 8){
			if (board[kingRow][kingCol] != null){
				if (board[kingRow][kingCol].color != king.color && ("b".equals(board[kingRow][kingCol].name) || "q".equals(board[kingRow][kingCol].name ))){
					System.out.printf("%s %s found at row %d col %d%n",board[kingRow][kingCol].getColor(), board[kingRow][kingCol].name,kingRow,kingCol);
					
					return true;
				}
				else if(board[kingRow][kingCol] != null){
					break;
				}
			}
			kingRow++;
			kingCol++;
		}
		System.out.printf("%s king is safe!%n", king.getColor());
		return false;
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
						pieceToMove.move(newrow,newcol);
						//System.out.println(pieceToMove.x + pieceToMove.y);
						board[newrow][newcol] = pieceToMove;
						board[row][col] = null;
						if ("k".equals(pieceToMove.name) || "r".equals(pieceToMove.name)){
							//pieceToMove.moved();
						}
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
	//rook moves to 7 to 5
	//king moves to 4 to 6
	public void kingSideCastle(Piece king){
		//tempRook = board[king.x][7];
		//move king
		king.move(king.x,6);
		//move rook
		board[king.x][7].move(king.x,5);
		board[king.x][6] = king; 
		board[king.x][5] = board[king.x][7];
		board[king.x][4] = null;
		board[king.x][7] = null;
		castleDirection = "r";
		



	}
	//king moves 4 to 2
	//rook moves 0 to 3
	public void queenSideCastle(Piece king){
		//tempRook = board[king.x][0];
		//tempRook.move(king.x,3);
		king.move(king.x,2);
		board[king.x][0].move(king.x,3);
		board[king.x][2] = king;
		board[king.x][3] = board[king.x][0];
		board[king.x][4] = null;
		board[king.x][0] = null;
		castleDirection = "l";


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
	/*
	A color wins IF
	The other color's king has no legal moves
	The other color has no move to capture the piece attacking the king that does not place them in check
	CALL DURING THE POTENTIALLY LOSING COLOR'S TURN
	*/

	public boolean whiteWin(){
		ArrayList<int[]> kingMoves = new ArrayList<int[]>();
		//ArrayList<int[]> blackMoves = new ArrayList<int[]>();
		kingMoves = this.getLegalMoves(findBlackKing());
		for (int i = 0; i < board.length; i++){
			for (int j = 0; j < board[0].length; j++){
				if (board[i][j] != null && !board[i][j].color){
					for (int[] pair : this.getLegalMoves(board[i][j])){
						if (this.checkForCheck(i,j,pair[0],pair[1])){
							return false;
						}
					}
				}
			}
		}
		/*
		if (blackKingInCheck() && (kingMoves.size() == 0)){

			return true;
		}*/
		return true;
	}

	public boolean blackWin(){
		ArrayList<int[]> kingMoves = new ArrayList<int[]>();
		//ArrayList<int[]> whiteMoves = new ArrayList<int[]>();
		kingMoves = this.getLegalMoves(findWhiteKing());
		for (int i = 0; i < board.length; i++){
			for (int j = 0; j < board[0].length; j++){
				if (board[i][j] != null && board[i][j].color){
					for (int[] pair : this.getLegalMoves(board[i][j])){
						if (this.checkForCheck(i,j,pair[0],pair[1])){
							return false;
						}
					}
				}
			}

		}
		/*
		if (whiteKingInCheck() && (kingMoves.size() == 0)){
			return true;
		}*/
		return true;
	}
	//Stalemate (Draw)
	/*
	stalemate if a color has no legal moves AND a color is NOT in check
	*/
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
	public void shiftLeft(){
		for (int i = 0; i < board.length; i++){
			Piece tempPiece = board[i][0];
			for (int j = 1; j < board[i].length; j++){
				board[i][j].move(i,j-1);
				board[i][j-1] = board[i][j];
			}
			board[i][7] = tempPiece;
		}

		for(int i = 0; i<board.length; i++)
		{
			for(int j = 0; j<board[i].length; j++)
			{
				if(board[i][j] != null)
				{
					board[i][j].x = i;
					board[i][j].y = j;
				}
			}
		}
	}
	public void shiftRight(){
		for (int i = 0; i <board.length ; i++){
			Piece tempPiece =  board[i][board[i].length-1];
			for (int j = board.length-2; j >= 0; j--){
				board[i][j].move(i,j+1);
				board[i][j+1] = board[i][j];
			}
			board[i][0] = tempPiece;
		}

		for(int i = 0; i<board.length; i++)
		{
			for(int j = 0; j<board[i].length; j++)
			{
				if(board[i][j] != null)
				{
					board[i][j].x = i;
					board[i][j].y = j;
				}
			}
		}
	}
}