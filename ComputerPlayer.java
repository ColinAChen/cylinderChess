import java.util.ArrayList;

public class ComputerPlayer
{
	private BoardCylinder board;
	private boolean color;
	private int finalDepth;

	final boolean WHITE = true;
	final boolean BLACK = false;
	
	public ComputerPlayer(BoardCylinder b, boolean color, int depth)
	{
		board = b;
		this.color = color;
		finalDepth = depth;
	}

	public void action()
	{
		ArrayList<int[]> moves = generateMoves(board, color);
		int maxEval = -1000;
		int pos = 0;
		for(int i = 0; i < moves.size(); i++)
		{
			int[] m = moves.get(i);
			BoardCylinder testBoard = new BoardCylinder(board);
			testBoard.move(m[0], m[1], m[2], m[3]);
			int evaluation = alphaBeta(testBoard, 1, !color, -1000, 1000);
			if(evaluation > maxEval)
			{
				maxEval = evaluation;	
				pos = i;
			}
		}

		int[] finalMove = moves.get(pos);
		board.move(finalMove[0], finalMove[1], finalMove[2], finalMove[3]);
	}

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
		
		ArrayList<int[]> moves = generateMoves(board, color);
		
		if(color == this.color)
		{
			int maxEval = -1000;
			for(int[] m : moves)
			{
				BoardCylinder testBoard = new BoardCylinder(board);
				board.move(m[0], m[1], m[2], m[3]);
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
		else
		{
			int minEval = 1000;
			for(int[] m : moves)
			{
				BoardCylinder testBoard = new BoardCylinder(board);
				board.move(m[0], m[1], m[2], m[3]);
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

				if(test.getColor() == "white" && color == WHITE || test.getColor() == "black" && color == BLACK)
				{
					ArrayList<int[]> moves = board.getLegalMoves(r, c);
					for(int[] m : moves)
						allMoves.add(new int[] { r, c, m[0], m[1] });
				}
			}
		}

		return allMoves;
	}

	private int evaluateBoard(BoardCylinder board)
	{
		int evaluation = 0;
		
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
	
		for(int r = 0; r < 8; r++)
		{
			for(int c = 0; c < 8; c++)		
			{
				PieceImmutable test = board.getPiece(r, c);
				if(test == null)
					continue;
	
				if(test.getColor() == "white" && color == WHITE || test.getColor() == "black" && color == BLACK)
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