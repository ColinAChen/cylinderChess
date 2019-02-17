import java.util.ArrayList; // import the ArrayList class

public class PieceTest{
	public static void main (String[] args){
		//Pawn testPawn = new Pawn("p", false, 1,3,false);
		Rook testKing = new Rook("k", false, 4, 5, false);
		//System.out.println(testPawn.isLegitMove(4,3));
		ArrayList<int[]>possibleMoves = testKing.getPossibleMoves();
		System.out.println(testKing.name);
		for (int[] pair : possibleMoves){
			for (int element: pair){
				System.out.print(element + " ");
			}
			System.out.println("");
		}	
	}
}