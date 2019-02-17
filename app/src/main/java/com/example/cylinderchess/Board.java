import java.util.ArrayList; // import the ArrayList class
public class Board{
	Piece[][] board;
	Piece[] oneDimensional;
	boolean turn;
	public Board(Piece[][] board, Piece[] oneD){
		this.board = board;
		this.oneDimensional = oneD;
	}
	public void initializeBoard(){
		//Initializes a board
		//first row is white, white = false
		//bottom rows are black, black = true
		//board = new Piece[8][8];
		for (int i = 0; i < 8; i++){
			//board[0][i] = new Piece("", false, 0, i, false);
			board[1][i] = new Pawn("", false, 1, i, false);
			board[6][i] = new Pawn("",true, 6, i, false);
			//board[7][i] = new Piece("", true, 7, i, false);
			if (i == 0 || i == 7){
				//System.out.println("Creating rooks in row " + i);
				board[0][i] = new Rook("", false, 0, i, false);
				board[7][i] = new Rook("", true, 0, i, false);
			}
			if (i == 1 || i == 6){
				//System.out.println("Creating knights");
				board[0][i] = new Knight("", false, 0, i, false);
				board[7][i] = new Knight("", true, 0, i, false);
			}
			if (i == 2 || i == 5){
				//System.out.println("Creating bishops");
				board[0][i] = new Bishop("", false, 0, i, false);
				board[7][i] = new Bishop("", true, 0, i, false);
			}
			if (i == 3){
				//System.out.println("Creating kings");
				board[0][i] = new King("", false, 0, i, false);
				board[7][i] = new King("", true, 0, i, false);
			}
			if (i == 4){
				//System.out.println("Creating queens");
				board[0][i] = new Queen("", false, 0, i, false);
				board[7][i] = new Queen("", true, 0, i, false);
			}
		}
	}	
	public ArrayList<int[]> getLegalMoves(Piece pieceToMove){
		ArrayList<int[]> possibleMoves = new ArrayList<int[]>();
		possibleMoves = pieceToMove.getPossibleMoves();
		ArrayList<int[]> legalMoves = new ArrayList<int[]>();
		for (int[] possiblePair:possibleMoves){
			System.out.printf("Checking for piece at row %d, col %d%n",possiblePair[0],possiblePair[1]);
			if (board[possiblePair[0]][possiblePair[1]] == null){
				legalMoves.add(possiblePair);
			}
			else if (board[possiblePair[0]][possiblePair[1]].color != pieceToMove.color){
				legalMoves.add(possiblePair);
			}
		} 
		return legalMoves;
	}
	public void move(int row, int col, int newrow,int newcol){
		int[] newPos = {newrow,newcol};
		Piece pieceToMove = board[row][col];
		System.out.printf("Moving %s at row %d, col %d%n", pieceToMove.name, row,col);
		ArrayList<int[]> legalMoves = getLegalMoves(pieceToMove);
		if(legalMoves.contains(newPos)){
			pieceToMove.move(newrow,newcol);
			System.out.println(pieceToMove.x + pieceToMove.y);
			board[newrow][newcol] = pieceToMove;
			board[row][col] = null;
		}

	}
	public Piece findWhiteKing(){
		for(int i = 0; i < board.length; i++){
			for (int j = 0; j < board[0].length; j++){
				if (!board[i][j].color && "k".equals(board[i][j].name)){
					return board[i][j];
				}
			}
		}
	}
	public Piece findBlackKing(){
		for(int i = 0; i < board.length; i++){
			for (int j = 0; j < board[0].length; j++){
				if (board[i][j].color && "k".equals(board[i][j].name)){
					return board[i][j];
				}
			}
		}
	}
	public boolean whiteKingInCheck(Piece[][] board){
		Piece king = findWhiteKing();
		ArrayList<int[]> legalMoves = new ArrayList<int[]>();
		for(int i = 0; i < board.length; i++){
			for (int j = 0; j < board[0].length; j++){
				if (board[i][j].color){
					legalMoves = board[i][j].getLegalMoves();
					for (int[]pair:legalMoves){
						if(pair[0] == king.x && pair[1]==king.y){
							return true;
						}
					}
				}
			}
		}
		return false;
	}
	public boolean blackKingInCheck(Piece[][] board){
		return false;
	}
	public boolean whiteWin(Piece[][] board){
		return false;
	}
	public boolean blackWin(Piece[][] board){
		return false;
	}
	public Piece[] oneFromTwo(){
		//Piece[] oneD = new Piece[64];
		for (int i = 0; i < board.length; i++){
			for (int j = 0; j < board[i].length; j++){
				oneDimensional[(8*i) + j] = board[i][j];
			}
		}
		return oneDimensional;
	}
	public void printBoard(){
		for (int i = 0; i < board.length; i++){
			for (int j = 0; j < board[0].length; j++){
				if (board[i][j] != null){
					System.out.print(board[i][j].name + " ");
				}
				else System.out.print("0 ");
			}
			System.out.print("\n");
		}
		System.out.println("\n");
	}
}