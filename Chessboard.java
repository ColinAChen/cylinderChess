public class Chessboard
{
	public final boolean BLACK = true;
	public final boolean WHITE = false;

	private Chesspiece[][] board;
	private boolean isWhiteTurn;
	
	// initializes board to start of game
	public Chessboard()
	{
		initializeBoard();
		isWhiteTurn = true;
	}

	// prints board to console
	public void printBoard()
	{
		for(int r = 0; r < 8; r++)
		{
			for(int c = 0; c < 8; c++)
			{
				if(board[r][c] != null)
					System.out.print(board[r][c].getType() + " ");
				else
					System.out.print("null ");
			}
			System.out.println();
		}
	}

	public void makeMove()
	{

	}
	
	private void initializeBoard()
	{
		//Initializes a board
		//first row is white, white = false
		//bottom rows are black, black = true
		board = new Chesspiece[8][8];
		for (int i = 0; i < 8; i++)
		{
			board[1][i] = new Pawn(1, i, WHITE);
			board[6][i] = new Pawn(6, i, BLACK);
			if (i == 0 || i == 7)
			{
				//System.out.println("Creating rooks in row " + i);
				board[0][i] = new Rook(0, i, WHITE);
				board[7][i] = new Rook(7, i, BLACK);
			}
			if (i == 1 || i == 6)
			{
				//System.out.println("Creating knights");
				board[0][i] = new Knight(0, i, WHITE);
				board[7][i] = new Knight(7, i, BLACK);
			}
			if (i == 2 || i == 5)
			{
				//System.out.println("Creating bishops");
				board[0][i] = new Bishop(0, i, WHITE);
				board[7][i] = new Bishop(7, i, BLACK);
			}
			if (i == 3)
			{
				//System.out.println("Creating kings");
				board[0][i] = new King(0, i, WHITE);
				board[7][i] = new King(7, i, BLACK);
			}
			if (i == 4)
			{
				//System.out.println("Creating queens");
				board[0][i] = new Queen(0, i, WHITE);
				board[7][i] = new Queen(7, i, BLACK);
			}
		}
	}
}