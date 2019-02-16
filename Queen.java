public class Queen extends Chesspiece
{
	public Queen(int r, int c, boolean color, Chessboard b)
	{
		super(r, c, color, "Queen", b);
	}
	
	public boolean isValidMove(int r, int c)
	{
		// temporary
		return true;
	}
}