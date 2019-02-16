import java.util.ArrayList; // import the ArrayList class
public class Pawn extends Piece{
	String name;
	boolean color;
	int x;
	int y;
	boolean isAttacked;
	ArrayList<Integer[]> possibleMoves;
		public Pawn(String name, boolean color, int x,int y, boolean isAttacked){
		this.name = name;
		this.color = color;
		this.x = x;
		this.y = y;
		this.isAttacked = isAttacked;
	}
	public boolean isLegitMove(int newx, int newy){
		//check that piece exists
		/*
		if (board[super.x][super.y] == null){
			System.out.println("No piece found");
			return false;
		}
		*/
		//check that destination is either empty or of the other color
		/*
		if (board[newx][newy] != null || (board[newx][newy].color).equals(super.color)){
			return false;
		}*/
		//invalid move if moving more than one square left or right
		if (Math.abs(newy - super.y) > 1){
			return false;
		}
		//DESTINATION WILL EITHER BE EMPTY OR OF OTHER SQUARE
		//movement for straight
		if (super.y == newy){
			//movement for black
			//unmoved black pawn
			if (super.color && super.x == 6){
				//can move one or two squares
				if (newx == 5 || newx == 4){
					return true;
				}
				//move one square up
				else if(super.x - newx == 1){
					return true;
				}
				//promote if pawn reaches top row
				else if(newx == 0){
					this.queen();
				}
				//otherwise is in invalid move up
				else{
					return false;
				}
			}
			// movement for white pawn
			else{
				// unmoved white pawn
				if (super.x == 1 && newx == 3){
					return true;
				}
				// move one square
				else if(newx-super.x == 1){
					return true;
				}
				//promote if pawn reaches top row{
				else if(newx == 7){
					this.queen();
				}
				//otherwise is invalid move
				else{
					return false;
				}				
			}
		}
		return false;
	}
	/*
	public ArrayList<Integer[]> getPossibleMoves(){
		int[] pair = new int[2];
		ArrayList<Integer[]> possibleMoves = new ArrayList<Integer[]>();
		for (int i = 0; i < 8; i++){
			for (int j = 0; j < 8; j++){
				if (this.isLegitMove(i,j)){
					pair[0] = i;
					pair[1] = j;
					possibleMoves.add(pair);

				}
			}
		}
		return possibleMoves;
	}*/


}




