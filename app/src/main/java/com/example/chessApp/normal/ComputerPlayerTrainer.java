import java.util.ArrayList;
import java.util.Random;

public class ComputerPlayerTrainer
{
	// board the computer player makes moves onto
	private BoardNormal board;
	// color of computer player
	private boolean color;
	// number of levels deep to search
	// setting this past 4 is not recommended, as this greatly increases the search time 
	private int finalDepth;

	private ArrayList<double[][]> trainingData;
	private NeuralNet net;
	private ArrayList<double[]> pV;
	private int moveCount = 0;

	final boolean WHITE = true;
	final boolean BLACK = false;
	
	// construct a computer by passing in a reference to the board to be played on, the color, and the depth
	public ComputerPlayerTrainer(BoardNormal b, boolean color, int depth, ArrayList<double[][]> trainingData, NeuralNet net)
	{
		board = b;
		this.color = color;
		finalDepth = depth;

		this.trainingData = trainingData;
		this.net = net;
		pV = new ArrayList<double[]>();
	}

	public void releaseData()
	{
		double evaluation = evaluateBoard(board);
		for(double[] move : pV)
		{
			double[][] trainingSample = new double[2][];
			trainingSample[0] = move;
			trainingSample[1] = new double[] { evaluation };
			trainingData.add(trainingSample);
		}
	}

	// call action() to make a move onto board
	public void action()
	{
		moveCount++;

		// generate all possible moves 
		ArrayList<int[]> moves = generateMoves(board, color);
		double maxEval = -1;
		int pos = 0;
		ArrayList<int[]> bestMoves = new ArrayList<int[]>();	
		// evaluate each move
		for(int i = 0; i < moves.size(); i++)
		{
			int[] m = moves.get(i);
			BoardNormal testBoard = new BoardNormal(board);
			testBoard.move(m[0], m[1], m[2], m[3]);

			if(testBoard.pawnPromotion())
			{
				testBoard.promote("q");
			}

			double evaluation = alphaBeta(testBoard, 1, !color, -1000, 1000);

			if(evaluation > maxEval)
			{
				bestMoves = new ArrayList<int[]>();
				bestMoves.add(m);
				maxEval = evaluation;	
				pos = i;
			}
			if(evaluation == maxEval)
			{
				bestMoves.add(m);
			}
		}
		
		

		// choose the best move
		Random randNum = new Random();
		int finalMovePos = randNum.nextInt(bestMoves.size());
		int[] finalMove = bestMoves.get(finalMovePos);
		board.move(finalMove[0], finalMove[1], finalMove[2], finalMove[3]);
	
		double[] pVMove = board.toInput(color);
		pV.add(pVMove);
	}

	public boolean gameOver(BoardNormal board)
	{
		if(evaluateBoard(board) == 1 || evaluateBoard(board) == 0 || board.stalemate())
		{
			return true;
		}

		return false;
	}

	// alpha-beta search function
	private double alphaBeta(BoardNormal board, int depth, boolean color, double alpha, double beta)
	{
		double evaluation = net.activateLayers(board.toInput(color))[0];
		double trueEval = evaluateBoard(board);		

		// return if final depth reached or game finished
		if(trueEval == 1 || trueEval == 0)
		{
			double[][] trainingSample = new double[2][];
			trainingSample[0] = board.toInput(color);
			trainingSample[1] = new double[] { trueEval };
			trainingData.add(trainingSample);

			return trueEval;
		}

		if(board.stalemate())
		{
			double[][] trainingSample = new double[2][];
			trainingSample[0] = board.toInput(color);
			trainingSample[1] = new double[] { 0.5 };
			trainingData.add(trainingSample);

			return 0.5;
		}

		if(depth == finalDepth)
			return evaluation;

		// recursively evaluate each possible move from the current board
		ArrayList<int[]> moves = generateMoves(board, color);
		// maximizing player
		if(color == this.color)
		{
			double maxEval = -1000;
			for(int[] m : moves)
			{
				BoardNormal testBoard = new BoardNormal(board);	
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
			double minEval = 1000;
			for(int[] m : moves)
			{
				BoardNormal testBoard = new BoardNormal(board);
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
	private ArrayList<int[]> generateMoves(BoardNormal board, boolean color)
	{
		ArrayList<int[]> allMoves = new ArrayList<int[]>();
		for(int r = 0; r < 8; r++)
		{
			for(int c = 0; c < 8; c++)
			{
				PieceImmutable test = board.getPiece(r, c);
				if(test == null)
					continue;

				if(test.getColor() == color)
				{
					ArrayList<int[]> moves = board.getLegalMoves(r, c);
					for(int[] m : moves)
						allMoves.add(m);
				}
			}
		}

		return allMoves;
	}

	// evaluate how much the computer player is winning on the current board
	private double evaluateBoard(BoardNormal board)
	{
		double evaluation = 0.5;
		
		// test for game over
		if(board.stalemate())
			return evaluation;

		if(board.whiteWin())	
		{
			if(color == WHITE)
				evaluation = 1;
			else
				evaluation = 0;
		
			return evaluation;
		}

		if(board.blackWin())
		{
			if(color == WHITE)
				evaluation = 0;
			else
				evaluation = 1;
			
			return evaluation;
		}

		return evaluation;
	}
}