public class King extends Chesspiece
{
	public King(int r, int c, boolean color, Chessboard b)
	{
		super(r, c, color, "King", b);
	}
	
	public boolean isValidMove(int r, int c)
	{
		// temporary
		return true;
	}
}