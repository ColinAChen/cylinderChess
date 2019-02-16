import java.util.ArrayList;

public class rooktest{
    public static void main(String[]args){
        Rook r = new Rook(b,5,5);
        ArrayList<int[]>moves = r.getPossibleMoves();
        for(int x=0;x<moves.size();x++)
        {
            System.out.print(moves.get(x)[0] + " , ");
            System.out.println(moves.get(x)[1]);
        }
    }
}