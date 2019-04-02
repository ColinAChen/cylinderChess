package com.example.chessApp.normal;

import com.example.chessApp.normal.PieceNormal;

import java.util.ArrayList; // import the ArrayList class
public class BoardNormal {
	PieceNormal[][] board = new PieceNormal[8][8];
	PieceNormal[] oneDimensional = new PieceNormal[64];
	MoveStack previousMoves = new MoveStack();
	BoardHashTable previousBoards = new BoardHashTable();
	boolean whiteToMove = true;
	//left = queenside, right = kingside;
	String castleDirection = null;
	//ArrayList<String> moveNotation = new ArrayList<>();

	public BoardNormal()
	{
		initializeBoard();
	}

	public BoardNormal(PieceNormal[][] board, PieceNormal[] oneD){
		this.board = board;		this.oneDimensional = oneD;
	}

	public BoardNormal(BoardNormal copyBoard)
	{
		for(int r = 0; r < 8; r++)
		{
			for(int c = 0; c < 8; c++)
			{
				PieceImmutable piece = copyBoard.getPiece(r, c);
				if(piece == null)
					continue;

				if(piece.getName() == "p")
				{
					if(piece.getColor() == "white")
						board[r][c] = new PawnNormal("p", true, r, c);
					else
						board[r][c] = new PawnNormal("p", false, r, c);
				}
				else if(piece.getName() == "n")
				{
					if(piece.getColor() == "white")
						board[r][c] = new KnightNormal("n", true, r, c);
					else
						board[r][c] = new KnightNormal("n", false, r, c);
				}
				else if(piece.getName() == "b")
				{
					if(piece.getColor() == "white")
						board[r][c] = new BishopNormal("b", true, r, c);
					else
						board[r][c] = new BishopNormal("b", false, r, c);
				}
				else if(piece.getName() == "r")
				{
					if(piece.getColor() == "white")
						board[r][c] = new RookNormal("r", true, r, c);
					else
						board[r][c] = new RookNormal("r", false, r, c);
				}
				else if(piece.getName() == "q")
				{
					if(piece.getColor() == "white")
						board[r][c] = new QueenNormal("q", true, r, c);
					else
						board[r][c] = new QueenNormal("q", false, r, c);
				}
				else if(piece.getName() == "k")
				{
					if(piece.getColor() == "white")
						board[r][c] = new KingNormal("k", true, r, c);
					else
						board[r][c] = new KingNormal("k", false, r, c);
				}
			}
		}

		oneFromTwo();
	}

	public void initializeBoard(){
		//Initializes a board
		//bottom row is white, white = true
		//top rows are black, black = false
		//board = new PieceNormal[8][8];
		for (int i = 0; i < 8; i++){
			//board[0][i] = new PieceNormal("", false, 0, i, false);
			board[1][i] = new PawnNormal("", false, 1, i);
			board[6][i] = new PawnNormal("",true, 6, i);
			//board[7][i] = new PieceNormal("", true, 7, i, false);
			if (i == 0 || i == 7){
				//System.out.println("Creating rooks in row " + i);
				board[0][i] = new RookNormal("", false, 0, i);
				board[7][i] = new RookNormal("", true, 7, i);
			}
			if (i == 1 || i == 6){
				//System.out.println("Creating knights");
				board[0][i] = new KnightNormal("", false, 0, i);
				board[7][i] = new KnightNormal("", true, 7, i);
			}
			if (i == 2 || i == 5){
				//System.out.println("Creating bishops");
				board[0][i] = new BishopNormal("", false, 0, i);
				board[7][i] = new BishopNormal("", true, 7, i);
			}
			if (i == 4){
				//System.out.println("Creating kings");
				board[0][i] = new KingNormal("", false, 0, i);
				board[7][i] = new KingNormal("", true, 7, i);
			}
			if (i == 3){
				//System.out.println("Creating queens");
				board[0][i] = new QueenNormal("", false, 0, i);
				board[7][i] = new QueenNormal("", true, 7, i);
			}
		}
		this.oneFromTwo();
	}

	public boolean getColorToMove()
	{
		return whiteToMove;
	}

	public void setColorToMove(boolean color)
	{
		whiteToMove = color;
	}

	public PieceImmutable getPiece(int r, int c)
	{
		if(board[r][c] == null)
			return null;

		PieceImmutable piece = new PieceImmutable(board[r][c]);
		return piece;
	}

	public ArrayList<int[]> getLegalMoves(PieceNormal pieceToMove){
		//get the pieces possible moves
		if (pieceToMove==null){
			return new ArrayList<int[]>();
		}
		//System.out.printf("Finding moves for %s %s on row %d col %d%n", pieceToMove.getStringColor(), pieceToMove.getName(), pieceToMove.getRow(), pieceToMove.getCol());
		ArrayList<int[]> possibleMoves = new ArrayList<int[]>();
		possibleMoves = pieceToMove.getPossibleMoves();
		ArrayList<int[]> legalMoves = new ArrayList<int[]>();
		System.out.printf("Initially has %d possible moves%n", possibleMoves.size());
		//define legal moves for a pawn
		if ("p".equals(pieceToMove.getName())){
			for (int[] possiblePair:possibleMoves){
				//check for capturing
				if ((pieceToMove.getCol() != possiblePair[1]) && (board[possiblePair[0]][possiblePair[1]] != null) && (board[possiblePair[0]][possiblePair[1]].getColor() != pieceToMove.getColor())){
					//only add if destination is of the opposite color
					if(checkForCheck(pieceToMove.getRow(),pieceToMove.getCol(),possiblePair[0],possiblePair[1])){
						legalMoves.add(possiblePair);
					}
				}
				//check for enpassant
				//enpassant if different column and space is empty
				else if ((pieceToMove.getCol() != possiblePair[1]) && board[possiblePair[0]][possiblePair[1]] == null){
					System.out.println("Checking for enpassant");
					String[]ranks = {"a","b","c","d","e","f","g","h"};
					String prevMoveString = previousMoves.peek();
					System.out.println(prevMoveString);
					int col = 0;
					//System.out.println(prevMoveString.substring(0,1));
					for (String rank:ranks){
						System.out.println(rank);

						//if a pawn was the last move
						if (prevMoveString.substring(0,1).equals(rank)){
							System.out.println("Ranks match!");
							//int col = ranks.indexOf(rank);
							int row = 8-Integer.parseInt(prevMoveString.substring(1,2));
							System.out.println(row);
							System.out.println(pieceToMove.getRow());
							int newrow = 8-Integer.parseInt(prevMoveString.substring(prevMoveString.length()-1,prevMoveString.length()));
							if (newrow == pieceToMove.getRow()){

								System.out.println(newrow-row);
								if (Math.abs(row-newrow) > 1){
									System.out.println("Pawn moved two last move!");
									System.out.println(col);
									System.out.println(col - possiblePair[1]);

									if(Math.abs(col - possiblePair[1]) == 0){
										System.out.println("pawn is in next row");
										legalMoves.add(possiblePair);
									}
								}
							}
						}
						col++;
					}
					
				}
				//pawn is moving straight
				else if(possiblePair[1] == pieceToMove.getCol()){
					//if the pawn is trying to jump two squares, check that both squares in front of it are empty
					if (Math.abs(possiblePair[0] - pieceToMove.getRow()) > 1){
						//System.out.println("Trying to jump two square");
						if (pieceToMove.getColor() && board[possiblePair[0]][possiblePair[1]] == null && board[5][possiblePair[1]] == null){
							if(checkForCheck(pieceToMove.getRow(),pieceToMove.getCol(),possiblePair[0],possiblePair[1])){
								legalMoves.add(possiblePair);
							}
						}
						else if(!pieceToMove.getColor() && board[possiblePair[0]][possiblePair[1]] == null && board[2][possiblePair[1]] == null){
							if(checkForCheck(pieceToMove.getRow(),pieceToMove.getCol(),possiblePair[0],possiblePair[1])){
								legalMoves.add(possiblePair);
							}
						}
					}
					else if (board[possiblePair[0]][possiblePair[1]] == null){
						//System.out.println("No piece found!");
						if(checkForCheck(pieceToMove.getRow(),pieceToMove.getCol(),possiblePair[0],possiblePair[1])){
							legalMoves.add(possiblePair);
						}
					}
				}	
			}
			return legalMoves;
		}
		// For knight
		// Only need to check that destination is empty or of other color
		else if("n".equals(pieceToMove.getName())){
			for (int[] possiblePair:possibleMoves){
				//System.out.printf("Checking for piece at row %d, col %d%n",possiblePair[0],possiblePair[1]);
				if (board[possiblePair[0]][possiblePair[1]] == null){
					//System.out.println("No piece found!");
					if(checkForCheck(pieceToMove.getRow(),pieceToMove.getCol(),possiblePair[0],possiblePair[1])){
								legalMoves.add(possiblePair);
							}
					//System.out.println(legalMoves.size());
				}
				else if (board[possiblePair[0]][possiblePair[1]] != null && board[possiblePair[0]][possiblePair[1]].getColor() != pieceToMove.getColor()){
					//System.out.println("PieceNormal is of other color!");
					if(checkForCheck(pieceToMove.getRow(),pieceToMove.getCol(),possiblePair[0],possiblePair[1])){
								legalMoves.add(possiblePair);
							}
					//System.out.println(legalMoves.size());
				}
			}
			return legalMoves;
		}
		//else if("k".equals(pieceToMove.getName())){
		//}
		//rook, bishop, queen, king
		else{
			//hold the closest pieces that are in the path of a rook, bishop, queen, or king
			//ArrayList<PieceNormal> blockPieces = new ArrayList<PieceNormal>();
			//up col, down col, left row, right row, up left, up right, down left, down right
			//PieceNormal[] blockPieces = new PieceNormal[9];
			int[] shortestDistances = {9,9,9,9,9,9,9,9};
			for (int[] possiblePairTemp:possibleMoves){
				//System.out.printf("Potential square row %d col %d, pieceToMove square row %d col %d%n",possiblePairTemp[0], possiblePairTemp[1], pieceToMove.getRow(), pieceToMove.getCol() );
				if (board[possiblePairTemp[0]][possiblePairTemp[1]] != null){
					System.out.printf("%s %s exists at row %d col %d%n",board[possiblePairTemp[0]][possiblePairTemp[1]].getStringColor(),board[possiblePairTemp[0]][possiblePairTemp[1]].getName(),possiblePairTemp[0], possiblePairTemp[1]);
					//same column
					if (possiblePairTemp[1] == pieceToMove.getCol()){
						//up column
						if (possiblePairTemp[0] < pieceToMove.getRow() && (pieceToMove.getRow() - possiblePairTemp[0]) < shortestDistances[0]){
							System.out.println("upcol");
							shortestDistances[0] = pieceToMove.getRow() - possiblePairTemp[0];
							//blockPieces[0] = board[possiblePair[0]][possiblePair[1]];
							//if (shortestDistances[0])
						}
						//down column
						else if(pieceToMove.getRow() < possiblePairTemp[0] && (possiblePairTemp[0] - pieceToMove.getRow()) < shortestDistances[1]){
							System.out.println("downcol");
							shortestDistances[1] = possiblePairTemp[0] - pieceToMove.getRow();
							//blockPieces[1] = board[possiblePair[0]][possiblePair[1]];
						}
					}
					//same row
					else if(possiblePairTemp[0] == pieceToMove.getRow()){
						//left row
						if (possiblePairTemp[1] < pieceToMove.getCol() && (pieceToMove.getCol() - possiblePairTemp[1]) < shortestDistances[2]){
							//System.out.println("leftrow");
							shortestDistances[2] = pieceToMove.getCol() - possiblePairTemp[1];
							//blockPieces[2] = board[possiblePair[0]][possiblePair[1]];
						}
						//right row
						else if(pieceToMove.getCol() < possiblePairTemp[1] && (possiblePairTemp[1] - pieceToMove.getCol()) < shortestDistances[3]){
							//System.out.println("rightrow");
							shortestDistances[3] = possiblePairTemp[1] - pieceToMove.getCol();
							//blockPieces[3] = board[possiblePair[0]][possiblePair[1]];
						}
					}
					// same diagonal
					//Sysetm.out.println((Math.abs(possiblePairTemp[0] - pieceToMove.getRow())));
					//System.out.println(Math.abs(possiblePairTemp[1] - pieceToMove.getCol())));
					else if((Math.abs(possiblePairTemp[0] - pieceToMove.getRow())) == (Math.abs(possiblePairTemp[1] - pieceToMove.getCol()))){
						//up left
						if (possiblePairTemp[0] < pieceToMove.getRow() && possiblePairTemp[1] < pieceToMove.getCol() && pieceToMove.getRow() - possiblePairTemp[0] < shortestDistances[4]){
							System.out.println("upleft");
							shortestDistances[4] = pieceToMove.getRow() - possiblePairTemp[0];
							//blockPieces[4] = board[possiblePair[0]][possiblePair[1]];
						}
						//up right
						else if(possiblePairTemp[0] < pieceToMove.getRow() && possiblePairTemp[1] > pieceToMove.getCol() && pieceToMove.getRow() - possiblePairTemp[0] < shortestDistances[5]){
							System.out.println("upright");
							shortestDistances[5] = pieceToMove.getRow() - possiblePairTemp[0];
							//blockPieces[5] = board[possiblePair[0]][possiblePair[1]];
						}
						//down left
						else if(possiblePairTemp[0] > pieceToMove.getRow() && possiblePairTemp[1] < pieceToMove.getCol() && possiblePairTemp[0] - pieceToMove.getRow() < shortestDistances[6] ){
							System.out.println("downleft");
							shortestDistances[6] = possiblePairTemp[0] - pieceToMove.getRow();
							//blockPieces[6] = board[possiblePair[0]][possiblePair[1]];
						}
						//down right
						else if(possiblePairTemp[0] > pieceToMove.getRow() && possiblePairTemp[1] > pieceToMove.getCol() && possiblePairTemp[0] - pieceToMove.getRow() < shortestDistances[7]){
							System.out.println("downright");
							shortestDistances[7] = possiblePairTemp[0] - pieceToMove.getRow();
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
				if (possiblePair[1] == pieceToMove.getCol()){
					if (possiblePair[0] < pieceToMove.getRow() && (pieceToMove.getRow() - possiblePair[0]) <= shortestDistances[0]){
						System.out.println("Checking col");
						if ((pieceToMove.getRow() - possiblePair[0]) == shortestDistances[0] && board[possiblePair[0]][possiblePair[1]] != null &&board[possiblePair[0]][possiblePair[1]].getColor() != pieceToMove.getColor()){
							System.out.println("Capture upcol");
							if(checkForCheck(pieceToMove.getRow(),pieceToMove.getCol(),possiblePair[0],possiblePair[1])){
								legalMoves.add(possiblePair);
							}
						}
						else if((pieceToMove.getRow() - possiblePair[0]) < shortestDistances[0]){
							System.out.println("less than");
							if(checkForCheck(pieceToMove.getRow(),pieceToMove.getCol(),possiblePair[0],possiblePair[1])){
								legalMoves.add(possiblePair);
							}
						}
						//if (shortestDistances[0])
						System.out.println("not in same col");
					}
					//System.out.printf("Cannot move on row %d col %d%n", possiblePair[0], possiblePair[1]);
					//down column
					else if(pieceToMove.getRow() < possiblePair[0] && (possiblePair[0] - pieceToMove.getRow()) <= shortestDistances[1]){
						//System.out.println("checking row");
						if ((possiblePair[0] - pieceToMove.getRow()) == shortestDistances[1] &&board[possiblePair[0]][possiblePair[1]] != null && board[possiblePair[0]][possiblePair[1]].getColor() != pieceToMove.getColor()){
							System.out.println("Capture downcol");
							if(checkForCheck(pieceToMove.getRow(),pieceToMove.getCol(),possiblePair[0],possiblePair[1])){
								legalMoves.add(possiblePair);
							}
						}
						else if((possiblePair[0] - pieceToMove.getRow()) < shortestDistances[1]){
							if(checkForCheck(pieceToMove.getRow(),pieceToMove.getCol(),possiblePair[0],possiblePair[1])){
								legalMoves.add(possiblePair);
							}
						}
					}
				}
				//same col
				else if(possiblePair[0] == pieceToMove.getRow()){
					//left row
					if (possiblePair[1] < pieceToMove.getCol() && (pieceToMove.getCol() - possiblePair[1]) <= shortestDistances[2]){
						if ((pieceToMove.getCol() - possiblePair[1]) == shortestDistances[2] &&board[possiblePair[0]][possiblePair[1]] != null && board[possiblePair[0]][possiblePair[1]].getColor() != pieceToMove.getColor()){
							if(checkForCheck(pieceToMove.getRow(),pieceToMove.getCol(),possiblePair[0],possiblePair[1])){
								legalMoves.add(possiblePair);
							}
						}
						else if((pieceToMove.getCol() - possiblePair[1]) < shortestDistances[2]){
							if(checkForCheck(pieceToMove.getRow(),pieceToMove.getCol(),possiblePair[0],possiblePair[1])){
								legalMoves.add(possiblePair);
							}
						}
					}
					//right row
					else if(pieceToMove.getCol() < possiblePair[1] && (possiblePair[1] - pieceToMove.getCol()) <= shortestDistances[3]){
						if ((possiblePair[1] - pieceToMove.getCol()) == shortestDistances[3] && board[possiblePair[0]][possiblePair[1]] != null &&board[possiblePair[0]][possiblePair[1]].getColor() != pieceToMove.getColor()){
							if(checkForCheck(pieceToMove.getRow(),pieceToMove.getCol(),possiblePair[0],possiblePair[1])){
								legalMoves.add(possiblePair);
							}
						}
						else if((possiblePair[1] - pieceToMove.getCol()) < shortestDistances[3]){
							if(checkForCheck(pieceToMove.getRow(),pieceToMove.getCol(),possiblePair[0],possiblePair[1])){
								legalMoves.add(possiblePair);
							}
						}
					}
				}
				// same diagonal
				else if((Math.abs(possiblePair[0] - pieceToMove.getRow())) == (Math.abs(possiblePair[1] - pieceToMove.getCol()))){
					//up left
					if (possiblePair[0] <= pieceToMove.getRow() && possiblePair[1] <= pieceToMove.getCol()){
						if ((pieceToMove.getRow() - possiblePair[0]) == shortestDistances[4] && board[possiblePair[0]][possiblePair[1]] != null &&board[possiblePair[0]][possiblePair[1]].getColor() != pieceToMove.getColor()){
							if(checkForCheck(pieceToMove.getRow(),pieceToMove.getCol(),possiblePair[0],possiblePair[1])){
								legalMoves.add(possiblePair);
							}
						}
						else if((pieceToMove.getRow() - possiblePair[0]) < shortestDistances[4]){
							if(checkForCheck(pieceToMove.getRow(),pieceToMove.getCol(),possiblePair[0],possiblePair[1])){
								legalMoves.add(possiblePair);
							}
						}
					}
					//up right
					else if(possiblePair[0] <= pieceToMove.getRow() && possiblePair[1] >= pieceToMove.getCol()){
						if ((pieceToMove.getRow() - possiblePair[0]) == shortestDistances[5] &&board[possiblePair[0]][possiblePair[1]] != null && board[possiblePair[0]][possiblePair[1]].getColor() != pieceToMove.getColor()){
							if(checkForCheck(pieceToMove.getRow(),pieceToMove.getCol(),possiblePair[0],possiblePair[1])){
								legalMoves.add(possiblePair);
							}
						}
						else if((pieceToMove.getRow() - possiblePair[0]) < shortestDistances[5]){
							if(checkForCheck(pieceToMove.getRow(),pieceToMove.getCol(),possiblePair[0],possiblePair[1])){
								legalMoves.add(possiblePair);
							}
						}
					}
					//down left
					else if(possiblePair[0] >= pieceToMove.getRow() && possiblePair[1] <= pieceToMove.getCol()){
						if ((possiblePair[0] - pieceToMove.getRow()) == shortestDistances[6] &&board[possiblePair[0]][possiblePair[1]] != null && board[possiblePair[0]][possiblePair[1]].getColor() != pieceToMove.getColor()){
							if(checkForCheck(pieceToMove.getRow(),pieceToMove.getCol(),possiblePair[0],possiblePair[1])){
								legalMoves.add(possiblePair);
							}
						}
						else if((possiblePair[0] - pieceToMove.getRow()) < shortestDistances[6]){
							if(checkForCheck(pieceToMove.getRow(),pieceToMove.getCol(),possiblePair[0],possiblePair[1])){
								legalMoves.add(possiblePair);
							}
						}
					}
					//down right
					else if(possiblePair[0] >= pieceToMove.getRow() && possiblePair[1] >= pieceToMove.getCol()){
						if ((possiblePair[0] - pieceToMove.getRow()) == shortestDistances[7] &&board[possiblePair[0]][possiblePair[1]] != null && board[possiblePair[0]][possiblePair[1]].getColor() != pieceToMove.getColor()){
							if(checkForCheck(pieceToMove.getRow(),pieceToMove.getCol(),possiblePair[0],possiblePair[1])){
								legalMoves.add(possiblePair);
							}
						}
						else if((possiblePair[0] - pieceToMove.getRow()) < shortestDistances[7]){
							if(checkForCheck(pieceToMove.getRow(),pieceToMove.getCol(),possiblePair[0],possiblePair[1])){
								legalMoves.add(possiblePair);
							}
							
						}
					}
					//Check for castling
					
					if ("k".equals(pieceToMove.getName())){
						KingNormal kingToMove = (KingNormal) pieceToMove;
						if(!kingToMove.hasMoved && !kingInCheck(pieceToMove)){
							//check queenside (left)
							//Check that the corner rook hasn't moved
							if (board[pieceToMove.getRow()][0] != null && "r".equals(board[pieceToMove.getRow()][0].getName())){
								RookNormal rookToMove = (RookNormal)board[pieceToMove.getRow()][0];
								if (!rookToMove.hasMoved){
									if (board[pieceToMove.getRow()][1] == null && board[pieceToMove.getRow()][2] == null && board[pieceToMove.getRow()][3] == null){
										if (checkForCheck(pieceToMove.getRow(),pieceToMove.getCol(),pieceToMove.getRow(),pieceToMove.getCol()-1) && checkForCheck(pieceToMove.getRow(),pieceToMove.getCol(),pieceToMove.getRow(),pieceToMove.getCol()-2)){
											int[]castlePair = {pieceToMove.getRow(),pieceToMove.getCol()-2};
											legalMoves.add(castlePair);
										}
									}
								}
							}
							//check kingside (right)
							if (board[pieceToMove.getRow()][7] != null && "r".equals(board[pieceToMove.getRow()][7].getName())){
								//check that the squares in between the rook and king are empty
								RookNormal rookToMove = (RookNormal)board[pieceToMove.getRow()][7];
								if (!rookToMove.hasMoved){
									if (board[pieceToMove.getRow()][5] == null && board[pieceToMove.getRow()][6] == null){
										if (checkForCheck(pieceToMove.getRow(),pieceToMove.getCol(),pieceToMove.getRow(),pieceToMove.getCol()+1) && checkForCheck(pieceToMove.getRow(),pieceToMove.getCol(),pieceToMove.getRow(),pieceToMove.getCol()+2)){
											int[]castlePair = {pieceToMove.getRow(),pieceToMove.getCol()+2};
											legalMoves.add(castlePair);
										}
									}
								}
							}
						}
					}
				}
			}
			return legalMoves;
		}	
		//return null;
	}

	// get legal moves from position
	public ArrayList<int[]> getLegalMoves(int r, int c)
	{
		PieceNormal pieceToMove;
		if(r < 8 && r >= 0)
		{
			if(c < 8 && c >= 0)
			{
				pieceToMove = board[r][c];
				return getLegalMoves(pieceToMove);
			}
		}

		// invalid input so return empty list
		return new ArrayList<int[]>();
	}

	//check if a move is legal, then check if current turn's king is in check
	//true if valid move, false if not
	public boolean checkForCheck(int row, int col, int newrow, int newcol){
		System.out.println("Checking for check");
		//move the piece
		PieceNormal capturePiece = null;
		PieceNormal pieceToMove = board[row][col];
		if (board[row][col] != null){
			//move to new square
			board[row][col].move(newrow,newcol);
			//PieceNormal pieceToMove = board[row][col];
		}
		else{
			System.out.printf("No piece found on row %d col %d%n", row, col);
			return true;
		}
		if (board[newrow][newcol] != null){
			//if a piece is captured, store it so it can be restored after
			capturePiece = board[newrow][newcol];
			System.out.printf("Capturing %s on %d %d%n",capturePiece.getName(),capturePiece.getRow(),capturePiece.getCol());

		}
		//move to new square on the board
		board[newrow][newcol] = board[row][col];
		board[row][col] = null;

		//if it is white's turn, check if the white king is in check after the possible move
		if (whiteToMove){
			PieceNormal whiteKing = findWhiteKing();
			if (kingInCheck(whiteKing)){
				//move back
				board[newrow][newcol].move(row,col);
				board[row][col] = board[newrow][newcol];
				board[newrow][newcol] = capturePiece;
				return false;
			}
		}
		else{
			PieceNormal blackKing = findBlackKing();
			if (kingInCheck(blackKing)){
				//move back
				board[newrow][newcol].move(row,col);
				board[row][col] = board[newrow][newcol];
				board[newrow][newcol] = capturePiece;
				return false;
			}
		}
		board[newrow][newcol].move(row,col);
		board[row][col] = board[newrow][newcol];
		board[newrow][newcol] = capturePiece;
	//	System.out.printf("Moving %s %s to row %d col %d will not cause check!%n", pieceToMove.getStringColor(), pieceToMove.getName(),newrow,newcol);
		return true;
	}
	//true if king is in check, false if not in check
	public boolean kingInCheck(PieceNormal king){

		if (king==null){
			return false;
		}
		System.out.printf("Checking %s king for check\n",king.getStringColor());
		//check for knights
		for (int i = 0; i < board.length; i++){
			for (int j = 0; j < board[0].length; j++){
				if ((Math.abs(king.getRow() - i) == 2 && Math.abs(king.getCol() - j) == 1) || (Math.abs(king.getRow() - i) == 1 && Math.abs(king.getCol() - j) == 2)){
					if (board[i][j] != null && board[i][j].getColor()!=king.getColor() && "n".equals(board[i][j].getName())){
						return true;
					}
				}
				//return false;
			}
		}
		//check for pawns
		//if white king, only need to check top left and top right
		if (king.getColor()){
			if (king.getRow() < 7 && king.getCol() > 0 && board[king.getRow() + 1][king.getCol() - 1] != null){
				if (!board[king.getRow() + 1][king.getCol() - 1].getColor() && "p".equals(board[king.getRow() + 1][king.getCol() - 1].getName())){
				//	System.out.printf("%s %s found at row %d col %d%n", board[king.getRow() + 1][king.getCol() - 1],board[king.getRow() + 1][king.getCol() - 1].getStringColor(), king.getRow() + 1, king.getCol() +1 );
					return true;
				}

			}
			else if (king.getRow() < 7 && king.getCol() < 7 && board[king.getRow() + 1][king.getCol() + 1] != null){
				if (!board[king.getRow() + 1][king.getCol() + 1].getColor() && "p".equals(board[king.getRow() + 1][king.getCol() + 1].getName())){
				//	System.out.printf("%s %s found at row %d col %d%n", board[king.getRow() + 1][king.getCol() - 1],board[king.getRow() + 1][king.getCol() - 1].getStringColor(), king.getRow() + 1, king.getCol() +1 );
					return true;
				}
			}
		}
		//check pawns for black king
		//only need to check bottom left and right
		else if(!king.getColor()){
			if (king.getRow() > 0 && king.getCol() > 0 &&board[king.getRow() - 1][king.getCol() - 1] != null){
				if (!board[king.getRow() - 1][king.getCol() - 1].getColor() && "p".equals(board[king.getRow() - 1][king.getCol() - 1].getName())){
				//	System.out.printf("%s %s found at row %d col %d%n", board[king.getRow() + 1][king.getCol() - 1],board[king.getRow() + 1][king.getCol() - 1].getStringColor(), king.getRow() + 1, king.getCol() +1 );
					
					return true;
				}
			}
			else if (king.getRow() > 0 && king.getCol() < 7 &&board[king.getRow() - 1][king.getCol() + 1] != null){
				if (!board[king.getRow() - 1][king.getCol() + 1].getColor() && "p".equals(board[king.getRow() - 1][king.getCol() + 1].getName())){
				//	System.out.printf("%s %s found at row %d col %d%n", board[king.getRow() + 1][king.getCol() - 1],board[king.getRow() + 1][king.getCol() - 1].getStringColor(), king.getRow() + 1, king.getCol() +1 );

					return true;
				}
			}
		}
		//Check for rooks and queens
		//check up a column
		System.out.println("Checking up column");
		for (int i = king.getRow() - 1; i >= 0; i--){
			System.out.printf("Checking row %d\n",i);
			if (board[i][king.getCol()] != null && board[i][king.getCol()].getColor() != king.getColor()){
			System.out.printf("%s %s found at row %d col %d%n",board[i][king.getCol()].getStringColor(), board[i][king.getCol()].getName(),i,king.getCol());

				if ("q".equals(board[i][king.getCol()].getName()) || "r".equals(board[i][king.getCol()].getName())){
					System.out.printf("%s %s found at row %d col %d%n",board[i][king.getCol()].getStringColor(), board[i][king.getCol()].getName(),i,king.getCol());
					return true;
				}
				break;
			}
			else if(board[i][king.getCol()] != null){
				break;
			}
		}
		//check down a column
		System.out.println("Checking down column");

		for (int i = king.getRow() + 1; i < 8; i++){
			System.out.printf("Checking row %d\n",i);
			//System.out.println(i + " " + king.getCol());
			if (board[i][king.getCol()] != null && board[i][king.getCol()].getColor() != king.getColor()){
			System.out.printf("%s %s found at row %d col %d%n",board[i][king.getCol()].getStringColor(), board[i][king.getCol()].getName(),i,king.getCol());
				
				if ("q".equals(board[i][king.getCol()].getName()) || "r".equals(board[i][king.getCol()].getName())){

					//System.out.printf("%s %s found at row %d col %d%n",board[i][king.getCol()].getStringColor(), board[i][king.getCol()].getName(),i,king.getCol());
				
					return true;
				}
				//System.out.println(board[i][king.getCol()].getStringColor());
				break;
			}
			else if(board[i][king.getCol()] != null){
				break;
			}
		}
		//check to the left of the row
		for (int j = king.getCol() - 1; j >=0;j--){
			if (board[king.getRow()][j] != null && board[king.getRow()][j].getColor() != king.getColor()){
				if ("q".equals(board[king.getRow()][j].getName()) || "r".equals(board[king.getRow()][j].getName())){
					//System.out.printf("%s %s found at row %d col %d%n",board[king.getRow()][j].getStringColor(), board[king.getRow()][j].getName(),king.getRow(),j);
						
					return true;
				}
				break;
			}
			else if(board[king.getRow()][j] != null){
				break;
			}
		}
		//check to the right of the row
		for (int j = king.getCol() + 1; j < 8;j++){
			if (board[king.getRow()][j] != null && board[king.getRow()][j].getColor() != king.getColor()){
				if ("q".equals(board[king.getRow()][j].getName()) || "r".equals(board[king.getRow()][j].getName())){
					//System.out.printf("%s %s found at row %d col %d%n",board[king.getRow()][j].getStringColor(), board[king.getRow()][j].getName(),king.getRow(),j);

					return true;
				}
				break;
			}
			else if(board[king.getRow()][j] != null){
				break;
			}
		}

		//Check up left diagonal
		int kingRow = king.getRow() - 1;
		int kingCol = king.getCol() - 1;
		while(kingRow > -1 && kingCol > -1){
			if (board[kingRow][kingCol] != null){
				if (board[kingRow][kingCol].getColor() != king.getColor() && ("b".equals(board[kingRow][kingCol].getName()) || "q".equals(board[kingRow][kingCol].getName() ))){
					//System.out.printf("%s %s found at row %d col %d%n",board[kingRow][kingCol].getStringColor(), board[kingRow][kingCol].getName(),kingRow,kingCol);
					
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
		kingRow = king.getRow() - 1;
		kingCol = king.getCol() + 1;
		while(kingRow > -1 && kingCol < 8){
			if (board[kingRow][kingCol] != null){
				if (board[kingRow][kingCol].getColor() != king.getColor() && ("b".equals(board[kingRow][kingCol].getName()) || "q".equals(board[kingRow][kingCol].getName() ))){
					//System.out.printf("%s %s found at row %d col %d%n",board[kingRow][kingCol].getStringColor(), board[kingRow][kingCol].getName(),kingRow,kingCol);
				
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
		kingRow = king.getRow() + 1;
		kingCol = king.getCol() - 1;
		while(kingRow < 8 && kingCol > -1){
			if (board[kingRow][kingCol] != null){
				if (board[kingRow][kingCol].getColor() != king.getColor() && ("b".equals(board[kingRow][kingCol].getName()) || "q".equals(board[kingRow][kingCol].getName() ))){
					//System.out.printf("%s %s found at row %d col %d%n",board[kingRow][kingCol].getStringColor(), board[kingRow][kingCol].getName(),kingRow,kingCol);
				
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
		kingRow = king.getRow() + 1;
		kingCol = king.getCol() + 1;
		while(kingRow < 8 && kingCol < 8){
			if (board[kingRow][kingCol] != null){
				if (board[kingRow][kingCol].getColor() != king.getColor() && ("b".equals(board[kingRow][kingCol].getName()) || "q".equals(board[kingRow][kingCol].getName() ))){
					//System.out.printf("%s %s found at row %d col %d%n",board[kingRow][kingCol].getStringColor(), board[kingRow][kingCol].getName(),kingRow,kingCol);
					
					return true;
				}
				else if(board[kingRow][kingCol] != null){
					break;
				}
			}
			kingRow++;
			kingCol++;
		}
		//System.out.printf("%s king is safe!%n", king.getStringColor());
		return false;
	}
	
	public void printLegalMoves(int row, int col){
		PieceNormal pieceToMove = board[row][col];
		if(pieceToMove==null){
			System.out.printf("No piece found at row %d and column %d%n", row, col);
			return;
		}
		ArrayList<int[]> legalMoves = new ArrayList<int[]>();
		legalMoves = this.getLegalMoves(pieceToMove);
		if (legalMoves.size() > 0){
			System.out.printf("Legal moves for %s %s at row %d and column %d are: %n", pieceToMove.getStringColor(), pieceToMove.getName(), row,col);
		}
		else{
			System.out.printf("No legal moves for %s %s at row %d and column %d%n", pieceToMove.getStringColor(), pieceToMove.getName(), row,col);
		}
		for(int[] legalSquare:legalMoves){
			System.out.println(legalSquare[0] + " " + legalSquare[1]);
		}
		this.printBoard();
	}

	public boolean move(int row, int col, int newrow,int newcol){
		PieceNormal pieceToMove = board[row][col];

		//Create the algebraic chess notation from a move
		String prevMove = "";
		//transform the column to a rank
		char[]ranks = {'a','b','c','d','e','f','g','h'};
		//denote the piece name
		if (pieceToMove.getName() != "p"){
			prevMove += pieceToMove.getName().toUpperCase();
		}
		//denote the origin
		prevMove+= (ranks[col]) + Integer.toString((8-row));
		//denote a capture
		if (board[newrow][newcol] != null){
			prevMove += "x";
		}
		//check that piece to move exists
		if (pieceToMove != null){
			//check that the correct side is moving
			if(pieceToMove.getColor() == whiteToMove){
				//System.out.printf("Moving %s at row %d, col %d%n", pieceToMove.getName(), row,col);
				ArrayList<int[]> legalMoves = getLegalMoves(pieceToMove);
				//if(legalMoves.contains(newPos)){
				for(int[]legalPos:legalMoves){
					//check that the destination is a legal move
					if(legalPos[0] == newrow && legalPos[1] == newcol){
						//System.out.printf("Moving %s at row %d, col %d to row %d, col %d%n", pieceToMove.getName(), row,col,newrow,newcol);


						//Handle castling
						if ("k".equals(pieceToMove.getName()) && (newcol-col) > 1){
							//System.out.println("Kingside Castle");
							prevMove = "O-O";
							kingSideCastle(pieceToMove);
						}
						else if ("k".equals(pieceToMove.getName()) && (col-newcol) > 1){
							prevMove = "O-O-O";
							queenSideCastle(pieceToMove);
						}


						//check if pawn is capturing with enpassant
						else if ("p".equals(pieceToMove.getName()) && pieceToMove.getCol() != newcol && board[newrow][newcol] == null){
							System.out.println("Moving a pawn");
							//if the pawn is capturing
							if (pieceToMove.getCol() != newcol){

								System.out.println("Pawn is capturing");
								System.out.printf("Pawn col is %d\n",pieceToMove.getCol());
								System.out.printf("Pawn is moving to col %d\n",newcol);
								//if the destination is empty
								if (board[newrow][newcol] == null){
									System.out.println("Pawn is capturing a blank square, must be enpassant");
									pieceToMove.move(newrow,newcol);
									board[newrow][newcol] = pieceToMove;
									board[row][col] = null;
									board[row][newcol] = null;
									prevMove+='x';
								}
							}
						}
						else{
							pieceToMove.move(newrow,newcol);
							//System.out.println(pieceToMove.getRow() + pieceToMove.getCol());
							board[newrow][newcol] = pieceToMove;
							board[row][col] = null;
						}

						//remove potential for castling after a king or rook moves
						if ("k".equals(pieceToMove.getName())){
							KingNormal kingToMove = (KingNormal)pieceToMove;
							kingToMove.moved();
						} 
						if ("r".equals(pieceToMove.getName())){
							RookNormal rookToMove = (RookNormal)pieceToMove;
							rookToMove.moved();
						}
					}
				}
				//denote a check
				if(whiteKingInCheck() || blackKingInCheck()){
					prevMove += "+";
				}
				//denote checkmate
				if (whiteWin() || blackWin()){
					prevMove += "#";
				}
				
				//denote the destination
				prevMove += (ranks[newcol]) + Integer.toString((8-newrow));
				System.out.println(prevMove);
				previousMoves.push(prevMove);
				previousBoards.add(boardToString());
				System.out.println(previousMoves);
				printBoard();
				whiteToMove = !whiteToMove;
				this.oneFromTwo();
				return true;
			}
		}
		return false;
	}
	//rook moves to 7 to 5
	//king moves to 4 to 6
	public void kingSideCastle(PieceNormal king){
		if(king!= null && board[king.getRow()][7] != null){
			//move king
			king.move(king.getRow(),king.getCol()+2);
			//move rook
			board[king.getRow()][7].move(king.getRow(),5);
			//swap on board
			System.out.println(king.getRow() + " " + king.getCol());
			board[king.getRow()][6] = king; 
			board[king.getRow()][5] = board[king.getRow()][7];
			board[king.getRow()][4] = null;
			board[king.getRow()][7] = null;
			castleDirection = "r";
		}
		
		



	}
	//king moves 4 to 2
	//rook moves 0 to 3
	public void queenSideCastle(PieceNormal king){
		if (king != null && board[king.getRow()][0] != null){
			king.move(king.getRow(),2);
			board[king.getRow()][0].move(king.getRow(),king.getCol()-2);
			board[king.getRow()][2] = king;
			board[king.getRow()][3] = board[king.getRow()][0];
			board[king.getRow()][4] = null;
			board[king.getRow()][0] = null;
			castleDirection = "l";
		}
		


	}
	public PieceNormal findWhiteKing(){
		for(int i = 0; i < board.length; i++){
			for (int j = 0; j < board[0].length; j++){
				if(board[i][j] != null && board[i][j].getColor() && "k".equals(board[i][j].getName())){
					return board[i][j];
				}
			}
		}
		return null;
	}
	public PieceNormal findBlackKing(){
		for(int i = 0; i < board.length; i++){
			for (int j = 0; j < board[0].length; j++){
				if (board[i][j] != null && !board[i][j].getColor() && "k".equals(board[i][j].getName())){
					return board[i][j];
				}
			}
		}
		return null;
	}

	public boolean whiteKingInCheck(){
		PieceNormal king = findWhiteKing();
		ArrayList<int[]> legalMoves = new ArrayList<int[]>();
		for(int i = 0; i < board.length; i++){
			for (int j = 0; j < board[0].length; j++){
				//for each black piece
				if (board[i][j] != null && !board[i][j].getColor()){
					legalMoves = this.getLegalMoves(board[i][j]);
					for (int[]pair:legalMoves){
						if(pair[0] == king.getRow() && pair[1]==king.getCol()){
							return true;
						}
					}
				}
			}
		}
		return false;
	}
	public boolean blackKingInCheck(){
		PieceNormal king = findBlackKing();
		ArrayList<int[]> legalMoves = new ArrayList<int[]>();
		for(int i = 0; i < board.length; i++){
			for (int j = 0; j < board[0].length; j++){
				//for each black piece
				if (board[i][j] != null && board[i][j].getColor()){
					legalMoves = this.getLegalMoves(board[i][j]);
					for (int[]pair:legalMoves){
						if(pair[0] == king.getRow() && pair[1]==king.getCol()){
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
				if (board[i][j] != null && !board[i][j].getColor()){
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
				if (board[i][j] != null && board[i][j].getColor()){
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
				if (oneDimensional[i].getColor()){
					numWhiteMoves+=legalMoves.size();
				}
				else{
					numBlackMoves+=legalMoves.size();
				}
			}
		}
		if ((numWhiteMoves == 0 && !whiteKingInCheck() && whiteToMove) || (numBlackMoves == 0 && !blackKingInCheck() && !whiteToMove)){
			return true;
		}
		return false;
		
	}
	public void oneFromTwo(){
		//PieceNormal[] oneD = new PieceNormal[64];
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
					System.out.print(board[i][j].getName() + " ");
				}
				else System.out.print("0 ");
			}
			System.out.print("\n");
		}
		System.out.println("\n");
	}
	public String getMoveStack(){
		return previousMoves.toString();
	}
	public void clearBoard(){
		for (int i = 0; i < board.length;i++){
			for (int j = 0; j < board[0].length; j++){
				board[i][j] = null;
			}
		}
	}
	public void place(PieceNormal pieceToPlace, int row, int col){
		board[row][col] = pieceToPlace;
	}
	public void shiftLeft(){
		for (int i = 0; i < board.length; i++){
			PieceNormal tempPiece = board[i][0];
			for (int j = 1; j < board[i].length; j++){
				if(board[i][j] != null) {
					board[i][j].move(i, j - 1);
				}
					board[i][j - 1] = board[i][j];
			}
			board[i][7] = tempPiece;
		}

		for(int i = 0; i<board.length; i++)
		{
			for(int j = 0; j<board[i].length; j++)
			{
				if(board[i][j] != null)
				{
					board[i][j].setRow(i);
					board[i][j].setCol(j);
				}
			}
		}
	}
	public void shiftRight(){
		for (int i = 0; i <board.length ; i++){
			PieceNormal tempPiece =  board[i][board[i].length-1];
			for (int j = board.length-2; j >= 0; j--){
				if(board[i][j] != null) {
					board[i][j].move(i, j + 1);
				}
					board[i][j + 1] = board[i][j];
			}
			board[i][0] = tempPiece;
		}

		for(int i = 0; i<board.length; i++)
		{
			for(int j = 0; j<board[i].length; j++)
			{
				if(board[i][j] != null)
				{
					board[i][j].setRow(i);
					board[i][j].setCol(j);
				}
			}
		}
	}
	private String boardToString(){
		String out = "";
		for (int i = 0; i < board.length; i++){
			for (int j = 0; j < board[0].length; j++){
				if (board[i][j] == null){
					out += "0";
				}
				else{
					if (board[i][j].getColor()){
						out += board[i][j].getName().toUpperCase();
					}
					else{
						out += board[i][j].getName();
					}
				}
			}
		}
		return out;
	}
	public String getNotation(int[] pos1, int[] pos2)
	{
		String notation = board[pos1[0]][pos1[1]].getName().toUpperCase() + notationNumToAlpha(pos1[0]) + (pos1[1]+1);
		if(pos1.length != 2 || pos2.length != 2)
		{
			return("");
		}
		if(board[pos2[0]][pos2[1]] != null)
		{
			notation += "x" + board[pos2[0]][pos2[1]].getName().toUpperCase() + notationNumToAlpha(pos2[0]) + (pos2[1]+1);
		}
		else
		{
			notation += "-" + notationNumToAlpha(pos2[0]) + (pos2[1]+1);
		}
		return notation;
	}
	public String notationNumToAlpha(int a)
	{
		switch (a)
		{
			case 0:
				return "a";
			case 1:
				return "b";
			case 2:
				return "c";
			case 3:
				return "d";
			case 4:
				return "e";
			case 5:
				return "f";
			case 6:
				return "g";
			case 7:
				return "h";
			default:
				return "";

		}
	}
}