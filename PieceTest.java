import java.util.ArrayList; // import the ArrayList class

public class PieceTest{
	public static void main (String[] args){
		Pawn testPawn = new Pawn("p", false, 1,3,false);
		//System.out.println(testPawn.isLegitMove(4,3));
		ArrayList<int[]>possibleMoves = testPawn.getPossibleMoves();
		for (int[] pair : possibleMoves){
			for (int element: pair){
				System.out.print(element + " ");
			}
			System.out.println("");
		}	
	}
}