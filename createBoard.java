public class createBoard{
	public static void main(String[] args){
		Piece[][] board = initializeBoard();
		//System.out.println(board.length);
		//System.out.println(board[0][0].getName());
		//printBoard(board);
		move(board, 6,0,5,0);
		//printBoard(board);
		move(board, 6,1,4,1);
		//printBoard(board);
		move(board, 5,0,4,0);
		//printBoard(board);
		move(board, 0,0,1,1);
		move(board,1,0,2,0);
		move(board,1,1,3,1);

		
	}
	public static void printBoard(Piece[][] board){
		for (int i = 0; i < board.length; i++){
			for (int j = 0; j < board[0].length; j++){
				if (board[i][j] != null){
					System.out.print(board[i][j].getName() + " ");
				}
				else System.out.print("0 ");
			}
			System.out.print("\n");
		}
		System.out.println("\n");
	}
	public static void move(Piece[][] board, int originX, int originY, int newX, int newY){
		if (board[originX][originY] == null){
			System.out.println("No piece found");
		}
		Piece pieceToMove = board[originX][originY];
		System.out.printf("Attempting to move %s at x:%d,  y:%d.\n",pieceToMove.name,originX,originY );
		if ("p".equals(pieceToMove.name)){
			System.out.println("Typing to move a pawn");
			//if it is a black pawn and hasn't moved
			//Check if the space in front is empty
			if (board[newX][newY] == null){
				//if it is a black pawn that hasn't moved
				if (pieceToMove.color && originX == 6){
					System.out.printf("Tring to move an unmoved black pawn from %d, %d to %d, %d\n", originX,originY,newX,newY);
					//Move one or two spaces
					if (newX == 5 || newX == 4){
						//move the piece
						board[originX][originY].move(newX, newY);
						//change it on the board
						board[newX][newY] = board[originX][originY];
						board[originX][originY] = null;
						System.out.println("Pawn moved!");
					}
				}
				else if(!pieceToMove.color && originX == 1){
					System.out.printf("Tring to move an unmoved white pawn from %d, %d to %d, %d\n", originX,originY,newX,newY);
					board[originX][originY].move(newX, newY);
						board[newX][newY] = board[originX][originY];
						board[originX][originY] = null;
						System.out.println("Pawn moved!");
				}

				else if (Math.abs(newX - originX) == 1){
					System.out.printf("Tring to move a pawn from %d, %d to %d, %d\n", originX,originY,newX,newY);
					board[originX][originY].move(newX, newY);
					board[newX][newY] = board[originX][originY];
					board[originX][originY] = null;
					System.out.println("Pawn moved!");
				}
				else{
					System.out.println("Move failed");
				}
			}
			else{
				System.out.println("Cannot move pawn forward, space is full");
				
			}
			
			
		}
		printBoard(board);
	}
	public static Piece[][] initializeBoard(){
		//Initializes a board
		//first row is white, white = false
		//bottom rows are black, black = true
		Piece[][]board = new Piece[8][8];
		for (int i = 0; i < 8; i++){
			board[0][i] = new Piece("", false, 0, i, false);
			board[1][i] = new Piece("p", false, 1, i, false);
			board[6][i] = new Piece("p",true, 6, i, false);
			board[7][i] = new Piece("", true, 7, i, false);
			if (i == 0 || i == 7){
				//System.out.println("Creating rooks in row " + i);
				board[0][i].rook();
				board[7][i].rook();
			}
			if (i == 1 || i == 6){
				//System.out.println("Creating knights");
				board[0][i].knight();
				board[7][i].knight();
			}
			if (i == 2 || i == 5){
				//System.out.println("Creating bishops");
				board[0][i].bishop();
				board[7][i].bishop();
			}
			if (i == 3){
				//System.out.println("Creating kings");
				board[0][i].king();
				board[7][i].king();
			}
			if (i == 4){
				//System.out.println("Creating queens");
				board[0][i].queen();
				board[7][i].queen();
			}

		}

		//System.out.println(board[1][0].getName());
		//board[0] = {new piece("r", 0, 0), new piece("n", 0, 1), new piece("b", 0,2), new piece("k", 0, 3), new piece("q", 0, 4), new piece("b", 0, 5), new piece("n", 0, 6), new piece("r", 0, 7)};
		//board[7] = {new piece("r", 7, 0), new piece("n", 7, 1), new piece("b", 7,2), new piece("k", 7, 3), new piece("q", 7, 4), new piece("b", 7, 5), new piece("n", 7, 6), new piece("r", 7, 7)};
		return board;
	}	
}