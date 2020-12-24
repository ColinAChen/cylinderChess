public class Rook extends Chesspiece
{
	public Rook(int r, int c, boolean color, Chessboard b)
	{
		super(r, c, color, "Rook", b);
	}
	
	public boolean isValidMove(int r, int c)
	{
		// temporary
		return true;
	}
}