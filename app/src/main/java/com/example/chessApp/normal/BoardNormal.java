package com.example.chessApp.normal;

import android.util.Log;
import com.example.chessApp.normal.PieceNormal;
import java.util.ArrayList;

public class BoardNormal
{
	public PieceNormal[][] board;
	public PieceNormal[] oneDimensional;
	private MoveStack previousMoves;
	private BoardHashTable previousBoards;
	private boolean whiteToMove;

	private String castleDirection = null;
	
	public BoardNormal()
	{
		oneDimensional = new PieceNormal[64];
		previousMoves = new MoveStack();
		previousBoards = new BoardHashTable();
		initializeBoard();
	}

	public BoardNormal(PieceNormal[][] board, PieceNormal[] oneD)
	{
		this.board = board;
		this.oneDimensional = oneD;

		previousMoves = new MoveStack();
		previousBoards = new BoardHashTable();
	}

	public BoardNormal(BoardNormal copyBoard)
	{
		previousMoves = copyBoard.getPreviousMoves();
		previousBoards = copyBoard.getPreviousBoards();
		oneDimensional = new PieceNormal[64];
		board = new PieceNormal[8][8];
		
		whiteToMove = copyBoard.getColorToMove();
		for(int r = 0; r < 8; r++)
		{
			for(int c = 0; c < 8; c++)
			{
				PieceImmutable piece = copyBoard.getPiece(r, c);
				if(piece == null)
					continue;
				
				if(piece.getName().equals("p")) {
					if(piece.getColorBoolean())
						board[r][c] = new PawnNormal(true, r, c);
					else
						board[r][c] = new PawnNormal(false, r, c);
				}
				else if(piece.getName().equals("n")) {
					if(piece.getColorBoolean())
						board[r][c] = new KnightNormal(true, r, c);
					else
						board[r][c] = new KnightNormal(false, r, c);
				}
				else if(piece.getName().equals("b")) {
					if(piece.getColorBoolean())
						board[r][c] = new BishopNormal(true, r, c);
					else
						board[r][c] = new BishopNormal(false, r, c);
				}
				else if(piece.getName().equals("r")) {
					if(piece.getColorBoolean())
						board[r][c] = new RookNormal(true, r, c);
					else
						board[r][c] = new RookNormal(false, r, c);
				}
				else if(piece.getName().equals("q")) {
					if(piece.getColorBoolean())
						board[r][c] = new QueenNormal(true, r, c);
					else
						board[r][c] = new QueenNormal(false, r, c);
				}
				else if(piece.getName().equals("k")) {
					if(piece.getColorBoolean())
						board[r][c] = new KingNormal(true, r, c);
					else
						board[r][c] = new KingNormal(false, r, c);
				}
			}
		}

		oneFromTwo();
	}

	public void initializeBoard()
	{
		// Initializes a board
		// bottom row is white, white = true
		// top rows are black, black = false
		whiteToMove = true;		

		board = new PieceNormal[8][8];
		for (int i = 0; i < 8; i++)
		{
			board[1][i] = new PawnNormal(false, 1, i);
			board[6][i] = new PawnNormal(true, 6, i);
			
			if (i == 0 || i == 7) {
				board[0][i] = new RookNormal(false, 0, i);
				board[7][i] = new RookNormal(true, 7, i);
			}
			if (i == 1 || i == 6) {
				board[0][i] = new KnightNormal(false, 0, i);
				board[7][i] = new KnightNormal(true, 7, i);
			}
			if (i == 2 || i == 5) {
				board[0][i] = new BishopNormal(false, 0, i);
				board[7][i] = new BishopNormal(true, 7, i);
			}
			if (i == 4) {
				board[0][i] = new KingNormal(false, 0, i);
				board[7][i] = new KingNormal(true, 7, i);
			}
			if (i == 3) {
				board[0][i] = new QueenNormal(false, 0, i);
				board[7][i] = new QueenNormal(true, 7, i);
			}
		}

		oneFromTwo();
	}

	public MoveStack getPreviousMoves()
	{
		return previousMoves;
	}

	public BoardHashTable getPreviousBoards()
	{
		return previousBoards;
	}

	public boolean getColorToMove()
	{
		return whiteToMove;
	}

	public PieceImmutable getPiece(int r, int c)
	{
		if(board[r][c] == null)
			return null;

		PieceImmutable piece = new PieceImmutable(board[r][c]);
		return piece;
	}

	public boolean move(int row, int col, int newRow, int newCol) 
	{
		PieceNormal pieceToMove = board[row][col];

		// Create the algebraic chess notation from a move
		String prevMove = "";
		// transform the column to a rank
		char[]ranks = {'a','b','c','d','e','f','g','h'};
		// denote the piece name
		if (pieceToMove == null) 
		{
			return false;
		}
		if (!pieceToMove.getName().equals("p"))
		{
			prevMove += pieceToMove.getName().toUpperCase();
		}
		// denote the origin
		prevMove += (ranks[col]) + Integer.toString((8-row));
		// denote a capture
		if (board[newRow][newCol] != null) {
			prevMove += "x";
		}
		
		// check that piece to move exists
		if (pieceToMove != null) 
		{
			// check that the correct side is moving
			if (pieceToMove.getColor() == whiteToMove)
			{
				// System.out.printf("Moving %s at row %d, col %d%n", pieceToMove.getName(), row, col);
				
				ArrayList<int[]> legalMoves = getLegalMoves(pieceToMove);
				for (int[] legalPos : legalMoves) 
				{
			
					// check that the destination is a legal move
					if (legalPos[2] == newRow && legalPos[3] == newCol)
					{
						// System.out.printf("Moving %s at row %d, col %d to row %d, col %d%n", pieceToMove.getName(), row, col, newRow, newCol);

						// Handle castling
						if ("k".equals(pieceToMove.getName()) && (newCol - col) > 1)	
						{
							// System.out.println("Kingside Castle");
							prevMove = "O-O";
							kingSideCastle(pieceToMove);
						}
						else if ("k".equals(pieceToMove.getName()) && (col - newCol) > 1)
						{
							prevMove = "O-O-O";
							queenSideCastle(pieceToMove);
						}

						//check if pawn is capturing with enpassant
						if ("p".equals(pieceToMove.getName()) && pieceToMove.getCol() != newCol && board[newRow][newCol] == null)
						{
							pieceToMove.move(newRow,newCol);
							board[newRow][newCol] = pieceToMove;
							board[row][col] = null;
							board[row][newCol] = null;
							prevMove += 'x';
						}
						else
						{
							pieceToMove.move(newRow, newCol);
							// System.out.println(pieceToMove.getRow() + pieceToMove.getCol());
							board[newRow][newCol] = pieceToMove;
							board[row][col] = null;
						}

						// remove potential for castling after a king or rook moves
						if ("k".equals(pieceToMove.getName()))
						{
							KingNormal kingToMove = (KingNormal) pieceToMove;
							kingToMove.moved();
						} 
						if ("r".equals(pieceToMove.getName()))
						{
							RookNormal rookToMove = (RookNormal) pieceToMove;
							rookToMove.moved();
						}
					}
				}
	
				// denote a check
				if(whiteKingInCheck() || blackKingInCheck())
					prevMove += "+";
				
				// denote checkmate
				if (whiteWin() || blackWin())
					prevMove += "#";
				
				// denote the destination
				prevMove += (ranks[newCol]) + Integer.toString((8-newRow));
				// System.out.println(prevMove);
				previousMoves.push(prevMove);
				previousBoards.add(boardToString());
				// System.out.println(previousMoves);
				// printBoard();
				whiteToMove = !whiteToMove;
				oneFromTwo();
				
				return true;
			}
		}

		return false;
	}

	// get legal moves, but without checking for check
	public ArrayList<int[]> getProbableMoves(PieceNormal pieceToMove)
	{
		if(pieceToMove == null)
			return new ArrayList<int[]>();
		
		ArrayList<int[]> probableMoves = new ArrayList<int[]>();
		ArrayList<ArrayList<int[]>> possibleMoves = pieceToMove.getPossibleMoves();

		for(ArrayList<int[]> path : possibleMoves)
		{
			for(int[] move : path)
			{
				// case of pawn moves
				if(pieceToMove.getName().equals("p"))
				{
					// moving forward
					if(move[3] == pieceToMove.getCol())
					{
						// destination square is unoccupied
						if(board[move[2]][move[3]] == null)
							probableMoves.add(move);
						// destination square is occupied
						else
							break;
					}

					// move is a capture
					else if(move[3] != pieceToMove.getCol())
					{
						// destination square is occupied
						if(board[move[2]][move[3]] != null)
						{
							if(board[move[2]][move[3]].getColor() != pieceToMove.getColor())
								probableMoves.add(move);
						}
						
						// destination square is unoccupied
						else
						{
							// pawn has potential for enpassant
							if((pieceToMove.getColor() && pieceToMove.getRow() == 3) || (!pieceToMove.getColor() && pieceToMove.getRow() == 4))
							{	
								String[] ranks = {"a","b","c","d","e","f","g","h"};
								String prevMoveString = previousMoves.peek();	
								if(prevMoveString.substring(0, 1).equals(ranks[move[3]]))
								{
									int startRow = 8 - Integer.parseInt(prevMoveString.substring(1, 2));
									int endRow = 8 - Integer.parseInt(prevMoveString.substring(prevMoveString.length() - 1, prevMoveString.length()));
									if(Math.abs(endRow - startRow) == 2)
										probableMoves.add(move);
								}
							}
						}
					}

					continue;
				}

				if(board[move[2]][move[3]] != null)
				{
					if(board[move[2]][move[3]].getColor() != pieceToMove.getColor())
						probableMoves.add(move);

					break;
				}
				
				probableMoves.add(move);
			}
		}

		return probableMoves;
	}
	
	public ArrayList<int[]> getLegalMoves(PieceNormal pieceToMove)
	{
		if(pieceToMove == null)
			return new ArrayList<int[]>();
		
		ArrayList<int[]> legalMoves = new ArrayList<int[]>();
		ArrayList<int[]> probableMoves = getProbableMoves(pieceToMove);

		for(int[] move : probableMoves)
		{
			if(checkForCheck(move))
				legalMoves.add(move);
		}

		// check for castling
		if(pieceToMove.getName().equals("k"))
		{
			KingNormal kingToMove = (KingNormal) pieceToMove;
			if(!kingToMove.hasMoved() && !kingInCheck(pieceToMove))
			{
				int row = pieceToMove.getRow();
				int col = pieceToMove.getCol();

				// check queenside	
				if(board[row][0] != null && board[row][0].getName().equals("r"))
				{
					RookNormal rookToMove = (RookNormal) board[row][0];
					if (!rookToMove.hasMoved())
					{
						boolean canCastle = true;
						for(int c = col - 1; c > 0; c--)
						{
							if(board[row][c] != null || !checkForCheck(new int[] { row, col, row, c }))
								canCastle = false; 
						}
					
						if(canCastle)
							legalMoves.add(new int[] { row, col, row, col - 2 });
					}
				}

				// check kingside	
				if(board[row][7] != null && board[row][7].getName().equals("r"))
				{
					RookNormal rookToMove = (RookNormal) board[row][7];
					if (!rookToMove.hasMoved())
					{
						boolean canCastle = true;
						for(int c = col + 1; c < 7; c++)
						{
							if(board[row][c] != null || !checkForCheck(new int[] { row, col, row, c }))
								canCastle = false; 
						}
					
						if(canCastle)
							legalMoves.add(new int[] { row, col, row, col + 2 });
					}
				}	
			}
		}

		return legalMoves;
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

	// check if a move is legal, then check if current turn's king is in check
	// true if valid move, false if not
	public boolean checkForCheck(int[] move)
	{
		int row = move[0];
		int col = move[1];
		int newRow = move[2];
		int newCol = move[3];

		boolean isNotCheck;

		// move the piece
		PieceNormal capturePiece = null;
		PieceNormal pieceToMove = board[row][col];
		if (pieceToMove != null) {
			// move to new square
			pieceToMove.move(newRow, newCol);
		}
		else {
			// System.out.printf("No piece found on row %d col %d%n", row, col);
			return true;
		}
		if (board[newRow][newCol] != null) {
			// if a piece is captured, store it so it can be restored after
			capturePiece = board[newRow][newCol];
			// System.out.printf("Capturing %s on %d %d%n", capturePiece.getName(), capturePiece.getRow(), capturePiece.getCol());
		}
		else if(pieceToMove.getName().equals("p") && newCol != col && board[newRow][newCol] == null) {
			// check for enpassant		
			capturePiece = board[row][newCol];	
			// System.out.printf("Capturing %s on %d %d%n", capturePiece.getName(), capturePiece.getRow(), capturePiece.getCol());
		}

		// remove capture piece in case of en passant
		if(capturePiece != null)
			board[capturePiece.getRow()][capturePiece.getCol()] = null;
		// move to new square on the board
		board[newRow][newCol] = board[row][col];
		board[row][col] = null;

		// find king
		PieceNormal king;
		if (whiteToMove) {
			king = findWhiteKing();
		}
		else {
			king = findBlackKing();
		}

		if (kingInCheck(king)) 
			isNotCheck = false;
		else
			isNotCheck = true;
	
		// move back
		pieceToMove.move(row, col);
		board[row][col] = board[newRow][newCol];
		// replace capture piece in this order to accomodate en passant
		board[newRow][newCol] = null;
		if(capturePiece != null)
			board[capturePiece.getRow()][capturePiece.getCol()] = capturePiece;
		
		// System.out.printf("Moving %s %s to row %d col %d will not cause check!%n", pieceToMove.getStringColor(), pieceToMove.getName(), newRow, newCol);
		return isNotCheck;
	}
	
	// true if king is in check, false if not in check
	private boolean kingInCheck(PieceNormal king)
	{
		if(king == null)	
			return false;

		// generate all probable moves for opposite color to king
		ArrayList<int[]> probableMoves = new ArrayList<int[]>();
		for(int r = 0; r < 8; r++)
		{
			for(int c = 0; c < 8; c++)
			{
				if(board[r][c] != null && board[r][c].getColor() != king.getColor())
				{
					ArrayList<int[]> moves = getProbableMoves(board[r][c]);
					for(int[] move : moves)
						probableMoves.add(move);
				}
			}
		}

		for(int[] move : probableMoves)
		{
			// ignore forward pawn moves
			// MOVE[0], MOVE[1] WAS NULL ON BOARD - INDICATION OF SOMETHING WRONG?
			if(board[move[0]][move[1]] != null && board[move[0]][move[1]].getName().equals("p") && move[1] == move[3])
				continue;

			if(move[2] == king.getRow() && move[3] == king.getCol())	
				return true;
		}
		
		return false;
	}

	private PieceNormal findWhiteKing()
	{
		for(int r = 0; r < 8; r++) 
		{
			for (int c = 0; c < 8; c++) 
			{
				if(board[r][c] != null && board[r][c].getColor() && "k".equals(board[r][c].getName())) 
					return board[r][c];
			}
		}

		return null;
	}

	private PieceNormal findBlackKing()
	{
		for(int r = 0; r < 8; r++) 
		{
			for (int c = 0; c < 8; c++) 
			{
				if(board[r][c] != null && !board[r][c].getColor() && "k".equals(board[r][c].getName())) 
					return board[r][c];
			}
		}

		return null;
	}

	// rook moves from 7 to 5
	// king moves from 4 to 6
	private void kingSideCastle(PieceNormal king) {
		if(king != null && board[king.getRow()][7] != null) {
			//move king
			king.move(king.getRow(), king.getCol() + 2);
			//move rook
			board[king.getRow()][7].move(king.getRow(), 5);
			//swap on board
			board[king.getRow()][6] = king; 
			board[king.getRow()][5] = board[king.getRow()][7];
			board[king.getRow()][4] = null;
			board[king.getRow()][7] = null;
			
			castleDirection = "r";
		}
	}

	// king moves from 4 to 2
	// rook moves from 0 to 3
	public void queenSideCastle(PieceNormal king) {
		if (king != null && board[king.getRow()][0] != null) {
			// move king
			king.move(king.getRow(), king.getCol() - 2);
			// move rook
			board[king.getRow()][0].move(king.getRow(), 3);
			// swap on board
			board[king.getRow()][2] = king;
			board[king.getRow()][3] = board[king.getRow()][0];
			board[king.getRow()][4] = null;
			board[king.getRow()][0] = null;
			
			castleDirection = "l";
		}
	}

	public boolean whiteKingInCheck()
	{
		PieceNormal king = findWhiteKing();
		return kingInCheck(king);
	}

	public boolean blackKingInCheck()
	{
		PieceNormal king = findBlackKing();
		return kingInCheck(king);
	}

	// stalemate if the color to move has no legal moves and is not in check
	public boolean stalemate()
	{
		if(whiteToMove && whiteKingInCheck())
			return false;
	
		if(!whiteToMove && blackKingInCheck())
			return false;

		for(int r = 0; r < 8; r++)
		{
			for(int c = 0; c < 8; c++)
			{
				if(board[r][c] == null)
					continue;
			
				// get legal moves of only the side to move
				if(board[r][c].getColor() == whiteToMove)
				{
					ArrayList<int[]> moves = getLegalMoves(board[r][c]);
					if(moves.size() != 0)
						return false;
				}
			}
		}

		return true;		
	}

	
	// A color wins IF:
	// The other color's king has no legal moves
	// The other color has no move to capture the piece attacking the king that does not place them in check
	// CALL DURING THE POTENTIALLY LOSING COLOR'S TURN

	public boolean whiteWin()
	{
		// black king must be in check
		if(!blackKingInCheck())
			return false;

		// there must be no black piece with a legal move
		for (int r = 0; r < 8; r++)
		{
			for (int c = 0; c < 8; c++)
			{
				if (board[r][c] != null && !board[r][c].getColor())
				{
					ArrayList<int[]> moves = getLegalMoves(board[r][c]);
					if(moves.size() != 0)
						return false;
				}	
			}
		}
		
		return true;
	}

	public boolean blackWin()
	{
		// white king must be in check
		if(!whiteKingInCheck())
			return false;

		// there must be no white piece with a legal move
		for (int r = 0; r < 8; r++)
		{
			for (int c = 0; c < 8; c++)
			{
				if (board[r][c] != null && board[r][c].getColor())
				{
					ArrayList<int[]> moves = getLegalMoves(board[r][c]);
					if(moves.size() != 0)
						return false;
				}	
			}
		}
		
		return true;
	}

	public void printBoard()
	{
		for (int i = 0; i < board.length; i++)
		{
			for (int j = 0; j < board[0].length; j++)
			{
				if (board[i][j] != null)
				{
					if(board[i][j].getColor())
						System.out.print(board[i][j].getName().toUpperCase() + " ");
					else
						System.out.print(board[i][j].getName() + " ");
				}
				else 
					System.out.print("0 ");
			}
			System.out.print("\n");
		}
		System.out.println("\n");
	}

	public void printLegalMoves(int row, int col) {
		PieceNormal pieceToMove = board[row][col];

		if(pieceToMove == null) {
			System.out.printf("No piece found at row %d and column %d%n", row, col);
			return;
		}

		ArrayList<int[]> legalMoves = new ArrayList<int[]>();
		legalMoves = this.getLegalMoves(pieceToMove);
		if (legalMoves.size() > 0) {
			System.out.printf("Legal moves for %s %s at row %d and column %d are: %n", pieceToMove.getStringColor(), pieceToMove.getName(), row, col);
		}
		else {
			System.out.printf("No legal moves for %s %s at row %d and column %d%n", pieceToMove.getStringColor(), pieceToMove.getName(), row, col);
		}
		
		for(int[] legalSquare:legalMoves) {
			System.out.println(legalSquare[0] + " " + legalSquare[1]);
		}
		
		printBoard();
	}

	public String getMoveStack() {
		return previousMoves.toString();
	}

	public void clearBoard() { 
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				board[i][j] = null;
			}
		}
	}

	public void place(PieceNormal pieceToPlace, int row, int col) {
		board[row][col] = pieceToPlace;
	}

	public void shiftLeft() {
		for (int i = 0; i < board.length; i++) {
			PieceNormal tempPiece = board[i][0];
			for (int j = 1; j < board[i].length; j++) {
				if(board[i][j] != null) {
					board[i][j].move(i, j - 1);
				}
					board[i][j - 1] = board[i][j];
			}
			board[i][7] = tempPiece;
		}

		for(int i = 0; i < board.length; i++)
		{
			for(int j = 0; j < board[i].length; j++)
			{
				if(board[i][j] != null)
					board[i][j].move(i, j);
			}
		}
	}

	public void shiftRight() {
		for (int i = 0; i <board.length ; i++) {
			PieceNormal tempPiece =  board[i][board[i].length-1];
			for (int j = board.length-2; j >= 0; j--) {
				if(board[i][j] != null) {
					board[i][j].move(i, j + 1);
				}
					board[i][j + 1] = board[i][j];
			}
			board[i][0] = tempPiece;
		}

		for(int i = 0; i < board.length; i++)
		{
			for(int j = 0; j < board[i].length; j++)
			{
				if(board[i][j] != null)
					board[i][j].move(i, j);
			}
		}
	}

	public String boardToString() {
		String out = "";
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[0].length; j++) {
				if (board[i][j] == null) {
					out += "0";
				}
				else {
					if (board[i][j].getColor()) {
						out += board[i][j].getName().toUpperCase();
					}
					else { 
						out += board[i][j].getName();
					}
				}
			}
		}
		return out;
	}

	public void oneFromTwo()
	{
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				oneDimensional[(8*i) + j] = board[i][j];
			}
		}
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

	public boolean pawnPromotion()
	{
		for(int i = 0; i < 8; i++)
		{
			if(board[0][i] != null && board[0][i].getName().equals("p"))
				return true;
		
			if(board[7][i] != null && board[7][i].getName().equals("p"))
				return true;
		}
		
		return false;
	}

	public void promote(String piece)
	{
		for(int i = 0; i < 8; i++)
		{
			if(board[0][i] != null && board[0][i].getName().equals("p"))
			{
				board[0][i] = new QueenNormal(true, i, 0);
			}

			if(board[7][i] != null && board[7][i].getName().equals("p"))
			{
				board[7][i] = new QueenNormal(false, i, 7);
			}
		}
	} 
		
	// convert board to input for neuralNet
	public double[] toInput(boolean color)
	{
		double[] input = new double[65];
		int pos = 0;
		for(int r = 0; r < 8; r++)
			for(int c = 0; c < 8; c++)
			{
				if(board[r][c] == null)
					input[pos] = 0;
				else if(board[r][c].getColor() == true)
				{
					if(board[r][c].getName().equals("p"))
						input[pos] = 6;
					else if(board[r][c].getName().equals("r"))
						input[pos] = 1;
					else if(board[r][c].getName().equals("n"))
						input[pos] = 2;
					else if(board[r][c].getName().equals("b"))
						input[pos] = 3;
					else if(board[r][c].getName().equals("q"))
						input[pos] = 4;
					else if(board[r][c].getName().equals("k"))
						input[pos] = 5;
				}
				else if(board[r][c].getColor() == false)
				{
					if(board[r][c].getName().equals("p"))
						input[pos] = -6;
					else if(board[r][c].getName().equals("r"))
						input[pos] = -1;
					else if(board[r][c].getName().equals("n"))
						input[pos] = -2;
					else if(board[r][c].getName().equals("b"))
						input[pos] = -3;
					else if(board[r][c].getName().equals("q"))
						input[pos] = -4;
					else if(board[r][c].getName().equals("k"))
						input[pos] = -5;
				}						
				
				pos++;
			}
		
		if(color)
			input[pos] = 1;
		else
			input[pos] = 0;
			
		return input;
	}
}