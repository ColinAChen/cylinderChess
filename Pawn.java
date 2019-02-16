public class Pawn extends Chesspiece
{
	public Pawn(int r, int c, boolean color, Chessboard b)
	{
		super(r, c, color, "Pawn", b);
	}
	
	// list of all possible moves a pawn could make 
	public boolean isPossibleMove(int r, int c)
	{
		// white possible moves are: (r + 1, c), (r + 2, c), (r, c + 1), (c, c - 1)
		if(getColor() == BLACK)
		{
			if(r == getRow() + 1)
				return true;
		}

		return false;
	}
}