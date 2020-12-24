public class Chesspiece
{
	public final boolean BLACK = true;
	public final boolean WHITE = false;

	private int row;
	private int col;
	// true is black, false is white
	private boolean color;
	// parent chessboard
	private Chessboard board;
	
	// maybe switch type to int?
	private String type;

	public Chesspiece(int r, int c, boolean color, String type, Chessboard b)
	{
		row = r;
		col = c;
		this.color = color;
		this.type = type;
		board = b;
	}
	
	public int getRow()
	{
		return row;
	}
	
	public int getCol()
	{
		return col;
	}

	public boolean getColor()
	{
		return color;
	}

	// maybe switch type to int? 
	public String getType()
	{
		return type;
	}

	public Chessboard getBoard()
	{
		return board;
	}

	// moves piece to position (r, c)
	public void move(int r, int c)
	{
		row = r;
		col = c;
	}

	// returns true if the piece can move to the given square, ignoring checks
	// to be defined in each subclass
	public boolean getPossibleMove(int r, int c) 
	{
		// so that it compiles
		return true;
	}
}