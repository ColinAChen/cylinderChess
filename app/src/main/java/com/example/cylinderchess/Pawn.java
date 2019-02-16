public class Pawn extends Piece{
	public boolean isLegitMove(int newx, int newy){
		//check that piece exists
		if (board[super.x][super.y] == null){
			System.out.println("No piece found");
			return false;
		}
		//check that destination is either empty or of the other color
		if (board[newx][newy] != null || (board[newx][newy].color).equals(super.color)){
			return false;
		}
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
					Pawn.queen();
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
					Pawn.queen();
				}
				//otherwise is invalid move
				else{
					return false;
				}				
			}

		}
	}
}




