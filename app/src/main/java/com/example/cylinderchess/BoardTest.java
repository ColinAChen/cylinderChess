public class BoardTest{
	public static void main(String[] args){
		Piece[][] boardArray = new Piece[8][8];
		Piece[] oneD = new Piece[64];
		Board board = new Board(boardArray, oneD);
		board.initializeBoard();
		board.printBoard();
		board.move(6,0,6,2);
		board.printBoard();
	}
}