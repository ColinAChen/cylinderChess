import java.util.ArrayList;

public class PieceImmutable
{
	PieceCylinder piece;

	public PieceImmutable(PieceCylinder p)
	{
		piece = p;
	}

	public String getColor()
	{
		if(piece != null)
			return piece.getColor();

		return null;
	}

	public String getName() 	
	{
		if(piece != null)
			return piece.getName();
	
		return null;
	}

	public ArrayList<int[]> getPossibleMoves()
	{
		if(piece != null)
			return piece.getPossibleMoves();
	
		return null;
	}

	public int[] getPosition()
	{
		if(piece != null)
			return piece.getPosition();
	
		return null;
	}
}