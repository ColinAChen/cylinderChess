// train neural net
import java.util.ArrayList;

public class test
{
	public static void main(String[] args)
	{
		train();
	}

	public static void train()
	{
		// initialize neural net with the given hidden layers 
		int[] layers = new int[] { 30, 10 };
		NeuralNet testNet = new NeuralNet(layers);

		// to initialize neural net based on a previously trained neural net use:
		// 	NeuralNet testNet = new NeuralNet();
		// this will create a neural net based on the file listed in the method loadFromFile() in the neuralNet class
		// neural net parameters are recorded to the file listed in the method recordToFile() in the neuralNet class
		// recordToFile() DOES overwrite previously recorded parameters, so to save multiple sets of parameters 
		// you must change the file listed in the method
		// remember to change the file you load from if you do this
		
		// create board and computer players to train neural net
		BoardNormal board = new BoardNormal();
		ArrayList<double[][]> trainingData = new ArrayList<double[][]>();
		
		ComputerPlayerTrainer p1;
		ComputerPlayerTrainer p2;
		
		boolean p1Turn;
		int moveCounter = 0;
		int gameCounter = 0;
		int setCounter = 0;

		// train for 50 sets of 20 games (this gives ~140,000 training samples and takes ~30min)
		while(setCounter < 50)
		{
			gameCounter = 0;

			// uncomment the below statement if you want to train after every set, rather than all at once at the end of all sets
			// if you do this, move the stochasticGradientDescent() method block inside the setCounter while loop
			// trainingData = new ArrayList<double[][]>();

			while(gameCounter < 20)
			{
				// initialize computer players
				board = new BoardNormal();
				p1 = new ComputerPlayerTrainer(board, true, 1, trainingData, testNet);	
				p2 = new ComputerPlayerTrainer(board, false, 1, trainingData, testNet);
		
				p1Turn = true;
				moveCounter = 0;

				// play until a game ends, or the game reaches 400 moves (each time any player makes a move counts as 1 move)
				// the results of unfinished games are thrown away
				while(!checkBoardState(board) && moveCounter < 400)
				{
					if(p1Turn)
					{
						p1.action();
						// uncomment both sets of print blocks to see the games being played
						// System.out.println("P1 move " + moveCounter);
						// board.printBoard();
						// System.out.println();
					}
					else
					{
						p2.action();	
						// System.out.println("P2 move " + moveCounter);
						// board.printBoard();
						// System.out.println();
					}
			
					p1Turn = !p1Turn;
					moveCounter++;
				}
				
				if(moveCounter != 400)
				{
					p1.releaseData();
					p2.releaseData();
				}
	
				// set this to false if you don't want to see the training data size
				if(setCounter >= 0)
					System.out.println("training data size for set " + setCounter + ", game " + gameCounter + ": " + trainingData.size());	
		
				gameCounter++;
			}
		
			setCounter++;
		}

		// block that trains the neural net
		double[][][] trainingInput = new double[trainingData.size()][][];
		for(int i = 0; i < trainingData.size(); i++)
			trainingInput[i] = trainingData.get(i); 
		testNet.stochasticGradientDescent(trainingInput, 1000, 3, 100);
		// evluates the neural net after training
		// NOTE: evaluateNet() returns the average of the differences between the correct evaluation and the neural net's evaluation
		// 	 the LOWER the score, the better the neural net has done
		// Also, this evaluation is done on the same set of data that the neural net trained with (this can be changed by changing the given parameter)
		System.out.println("evaluation accuracy: " + testNet.evaluateNet(trainingInput));
		
		// record network layer sizes, weights, and biases
		testNet.recordToFile();

		// play a sample test game after training
		// this game is limited to 100 moves
		board = new BoardNormal();
		p1 = new ComputerPlayerTrainer(board, true, 1, trainingData, testNet);	
		p2 = new ComputerPlayerTrainer(board, false, 1, trainingData, testNet);
		
		p1Turn = true;
		moveCounter = 0;
		while(!checkBoardState(board) && moveCounter < 100)
		{
			if(p1Turn)
			{
				p1.action();
				System.out.println("P1 move " + moveCounter);
				board.printBoard();
				System.out.println();
			}
			else
			{
				p2.action();	
				System.out.println("P2 move " + moveCounter);
				board.printBoard();
				System.out.println();
			}
		
			p1Turn = !p1Turn;
			moveCounter++;
		}
			
		String result = "unfinished game";
		if(board.whiteWin())
			result = "white win";
		else if(board.blackWin())
			result = "black win";
		else if(board.stalemate())
			result = "stalemate";
		System.out.println("end of game " + gameCounter + " result was " + result);
	}

	public static boolean checkBoardState(BoardNormal board)
	{
		if(board.whiteWin() || board.blackWin() || board.stalemate())
			return true;

		return false;
	}
}