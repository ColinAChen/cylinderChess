//STACK ADT FOR STORING PREVIOUS MOVES
public interface MoveStackInterface{

	public void push(String moveToAdd);
	public String pop();
	public String peek();
	public String toString();

}