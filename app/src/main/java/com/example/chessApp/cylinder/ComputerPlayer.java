import java.util.ArrayList;

public class ComputerPlayer
{
		// board the computer player makes moves onto
	private BoardCylinder board;
		// color of computer player
	private boolean color;
		// number of levels deep to search
		// setting this past 4 is not recommended, as this greatly increases the search time 
	private int finalDepth;

	final boolean WHITE = true;
	final boolean BLACK = false;
	
	// construct a computer by passing in a reference to the board to be played on, the color, and the depth
	public ComputerPlayer(BoardCylinder b, boolean color, int depth)
	{
		board = b;
		this.color = color;
		finalDepth = depth;
	}

	// call action() to make a move onto board
	public void action()
	{
		// generate all possible moves 
		ArrayList<int[]> moves = generateMoves(board, color);
		int maxEval = -1000;
		int pos = 0;
		// evaluate each move
		for(int i = 0; i < moves.size(); i++)
		{
			int[] m = moves.get(i);
			BoardCylinder testBoard = new BoardCylinder(board);
			testBoard.setColorToMove(color);
			testBoard.move(m[0], m[1], m[2], m[3]);

			int evaluation = alphaBeta(testBoard, 1, !color, -1000, 1000);

			if(evaluation > maxEval)
			{
				maxEval = evaluation;	
				pos = i;
			}
		}
		
		

		// choose the best move
		int[] finalMove = moves.get(pos);
		board.setColorToMove(color);
		board.move(finalMove[0], finalMove[1], finalMove[2], finalMove[3]);
	}

	// alpha-beta search function
	private int alphaBeta(BoardCylinder board, int depth, boolean color, int alpha, int beta)
	{
		int evaluation = evaluateBoard(board);

		// return if final depth reached or game finished
		if(depth == finalDepth)
			return evaluation;

		if(evaluation == 1000 || evaluation == -1000)
			return evaluation;

		if(board.stalemate())
			return 0;
		
		// recursively evaluate each possible move from the current board
		ArrayList<int[]> moves = generateMoves(board, color);
		// maximizing player
		if(color == this.color)
		{
			int maxEval = -1000;
			for(int[] m : moves)
			{
				BoardCylinder testBoard = new BoardCylinder(board);	
				testBoard.setColorToMove(color);
				testBoard.move(m[0], m[1], m[2], m[3]);
				evaluation = alphaBeta(testBoard, depth + 1, !color, alpha, beta);
				
				if(evaluation > maxEval)
				{
					maxEval = evaluation;
					if(maxEval > alpha)
						alpha = maxEval;
				}
					
				if(alpha >= beta)
					break;
			}
	
			return maxEval;
		}	
		// minimizing player
		else
		{
			int minEval = 1000;
			for(int[] m : moves)
			{
				BoardCylinder testBoard = new BoardCylinder(board);
				testBoard.setColorToMove(color);
				testBoard.move(m[0], m[1], m[2], m[3]);
				evaluation = alphaBeta(testBoard, depth + 1, !color, alpha, beta);
			
				if(evaluation < minEval)
				{
					minEval = evaluation;
					if(minEval < beta)
						beta = minEval;
				}
			
				if(alpha >= beta)
					break;
			}

			return minEval;
		}
	}

	// generate all possible moves for the given color and board
	private ArrayList<int[]> generateMoves(BoardCylinder board, boolean color)
	{
		ArrayList<int[]> allMoves = new ArrayList<int[]>();
		for(int r = 0; r < 8; r++)
		{
			for(int c = 0; c < 8; c++)
			{
				PieceImmutable test = board.getPiece(r, c);
				if(test == null)
					continue;

				if(test.getColorBoolean() == color)
				{
					ArrayList<int[]> moves = board.getLegalMoves(r, c);
					for(int[] m : moves)
						allMoves.add(new int[] { r, c, m[0], m[1] });
				}
			}
		}

		return allMoves;
	}

	// evaluate how much the computer player is winning on the current board
	private int evaluateBoard(BoardCylinder board)
	{
		int evaluation = 0;
		
		// test for game over
		if(board.stalemate())
			return evaluation;

		if(board.whiteWin())	
		{
			if(color == WHITE)
				evaluation = 1000;
			else
				evaluation = -1000;
		
			return evaluation;
		}

		if(board.blackWin())
		{
			if(color == WHITE)
				evaluation = -1000;
			else
				evaluation = 1000;
			
			return evaluation;
		}
	
		// evaluate based on the difference between computer player's piece values and the opponent's piece values 
		for(int r = 0; r < 8; r++)
		{
			for(int c = 0; c < 8; c++)		
			{
				PieceImmutable test = board.getPiece(r, c);
				if(test == null)
					continue;
	
				if(test.getColorBoolean() == color)
				{
					if(test.getName() == "p")
						evaluation += 1;
					else if(test.getName() == "n")
						evaluation += 3;	
					else if(test.getName() == "b")
						evaluation += 3;
					else if(test.getName() == "r")
						evaluation += 5;
					else if(test.getName() == "q")
						evaluation += 9;
				}
				else 
				{
					if(test.getName() == "p")
						evaluation -= 1;
					else if(test.getName() == "n")	
						evaluation -= 3;
					else if(test.getName() == "b")
						evaluation -= 3;
					else if(test.getName() == "r") 
						evaluation -= 5;
					else if(test.getName() == "q")
						evaluation -= 9;
				}
			}
		}

		return evaluation;
	}
}