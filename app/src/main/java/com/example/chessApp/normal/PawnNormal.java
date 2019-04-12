import java.util.ArrayList;

public class PawnNormal extends PieceNormal
{
	public PawnNormal(boolean color, int r, int c)
	{
		super("p", color, r, c);
	}

	private boolean isOnBoard(int r, int c)
	{
		if(r >= 0 && r < 8)
		{
			if(c >= 0 && c < 8)
				return true;
		}

		return false;
	}
	
	public ArrayList<ArrayList<int[]>> getPossibleMoves()
	{
		ArrayList<ArrayList<int[]>> allMoves = new ArrayList<ArrayList<int[]>>();
		
		ArrayList<int[]> forward = new ArrayList<int[]>();
		ArrayList<int[]> captureLeft = new ArrayList<int[]>();
		ArrayList<int[]> captureRight = new ArrayList<int[]>();
			
		int r = getRow();
		int c = getCol();

		// white
		if(getColor())
		{
			if(isOnBoard(r - 1, c))
				forward.add(new int[] { r, c, r - 1, c });
								
			if(isOnBoard(r - 1, c - 1))
				captureLeft.add(new int[] { r, c, r - 1, c - 1 });
				
			if(isOnBoard(r - 1, c + 1))
				captureRight.add(new int[] { r, c, r - 1, c + 1 });
		
			// has not moved
			if(getRow() == 6)
				forward.add(new int[] { r, c, r - 2, c });	
		}
		
		// black
		else
		{
			if(isOnBoard(r + 1, c))
				forward.add(new int[] { r, c, r + 1, c });
								
			if(isOnBoard(r + 1, c - 1))
				captureLeft.add(new int[] { r, c, r + 1, c - 1 });
				
			if(isOnBoard(r + 1, c + 1))
				captureRight.add(new int[] { r, c, r + 1, c + 1 });
		
			// has not moved
			if(getRow() == 1)
				forward.add(new int[] { r, c, r + 2, c });
		}

		if(!forward.isEmpty())
			allMoves.add(forward);
		if(!captureLeft.isEmpty())
			allMoves.add(captureLeft);
		if(!captureRight.isEmpty())
			allMoves.add(captureRight);

		return allMoves;
	}
}