public class Bishop extends Chesspiece
{
	public Bishop(int r, int c, boolean color, Chessboard b)
	{
		super(r, c, color, "Bishop", b);
	}
	
	public boolean isValidMove(int r, int c)
	{
		// temporary
		return true;
	}
}