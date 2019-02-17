import java.util.ArrayList; // import the ArrayList class
public class Bishop extends Piece{
	
	ArrayList<int[]> possibleMoves;
	public Bishop(String name, boolean color, int x,int y, boolean isAttacked){
		super("b",color,x,y,isAttacked);
	}
	public boolean isLegitMove(int newx, int newy){
		//ensure not trying to move off the board
		if (newx < 0 || newx > 7){
			return false;
		}
		if (Math.abs(newx-x) == Math.abs(newy-y)){
			return true;
		}
		return false;
	}
	
	//return all possible moves for this piece given its current position
	//does NOT take into account current board position
	public ArrayList<int[]> getPossibleMoves(){
		ArrayList<int[]> possibleMoves = new ArrayList<int[]>();
		for (int i = 0; i < 8; i++){
			for (int j = 0; j < 8; j++){
				if (this.isLegitMove(i,j)){
					int[] pair = {i,j};
					possibleMoves.add(pair);
				}
			}
		}
		return possibleMoves;
	}
}




