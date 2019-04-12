import java.util.ArrayList;

// wrapper class for pieceCylinders
// has read only access to the given pieceCylinder

public class PieceImmutable
{
	PieceNormal piece;

	public PieceImmutable(PieceNormal p)
	{
		piece = p;
	}

	public boolean getColor()
	{
		return piece.getColor();
	}

	public String getName()
	{
		if(piece != null)
			return piece.getName();

		return null;
	}

	public int getRow()
	{
		if(piece != null)
			return piece.getRow();

		return -1;
	}

	public int getCol()
	{
		if(piece != null)
			return piece.getCol();

		return -1;
	}

	public ArrayList<ArrayList<int[]>> getPossibleMoves()	
	{
		if(piece != null)
			return piece.getPossibleMoves();

		return null;
	}
}