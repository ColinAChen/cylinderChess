import java.util.ArrayList;

public class QueenNormal extends PieceNormal
{
	public QueenNormal(boolean color, int r, int c)
	{
		super("q", color, r, c);
	}

	public ArrayList<ArrayList<int[]>> getPossibleMoves()
	{
		ArrayList<int[]> upRight = new ArrayList<int[]>();
		ArrayList<int[]> upLeft = new ArrayList<int[]>();
		ArrayList<int[]> downRight = new ArrayList<int[]>();
		ArrayList<int[]> downLeft = new ArrayList<int[]>();
		ArrayList<int[]> left = new ArrayList<int[]>();
		ArrayList<int[]> right = new ArrayList<int[]>();
		ArrayList<int[]> up = new ArrayList<int[]>();
		ArrayList<int[]> down = new ArrayList<int[]>();

		int row = getRow();
		int col = getCol();
		int stepCount;

		// right
		for(int c = col + 1; c < 8; c++)
			right.add(new int[] { row, col, row, c });
		
		// left
		for(int c = col - 1; c >= 0; c--)
			left.add(new int[] { row, col, row, c });

		// down
		stepCount = 1;
		for(int r = row + 1; r < 8; r++)
		{
			if(col + stepCount < 8)
				downRight.add(new int[] { row, col, r, col + stepCount });
			
			if(col - stepCount >= 0)
				downLeft.add(new int[] { row, col, r, col - stepCount });
			
			down.add(new int[] { row, col, r, col });
			stepCount++;
		}

		// up
		stepCount = 1;
		for(int r = row - 1; r >= 0; r--)
		{
			if(col + stepCount < 8)
				upRight.add(new int[] { row, col, r, col + stepCount });
			
			if(col - stepCount >= 0)
				upLeft.add(new int[] { row, col, r, col - stepCount });
		
			up.add(new int[] { row, col, r, col });
			stepCount++;
		}

		ArrayList<ArrayList<int[]>> allMoves = new ArrayList<ArrayList<int[]>>();
		allMoves.add(upRight);
		allMoves.add(upLeft);
		allMoves.add(downRight);
		allMoves.add(downLeft);
		allMoves.add(left);
		allMoves.add(right);
		allMoves.add(up);
		allMoves.add(down);

		return allMoves;
	}
}