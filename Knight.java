public class Knight extends Chesspiece
{
	public Knight(int r, int c, boolean color, Chessboard b)
	{
		super(r, c, color, "Knight", b);
	}
	
	public boolean isValidMove(int r, int c)
	{
		// temporary
		return true;
	}
}