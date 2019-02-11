public class Piece{
	String name;
	boolean color;
	int x;
	int y;
	boolean isAttacked;

	public Piece(String name, boolean color, int x,int y, boolean isAttacked){
		this.name = name;
		this.color = color;
		this.x = x;
		this.y = y;
		this.isAttacked = isAttacked;
	}
	public void move(int xMove, int yMove){
		x+=xMove;
		y+=yMove;
	}
	public void changeColor(){
		color = !color;
	}
	public String getColor(){
		if (color){
			return "b";
		}
		else{
			return "w";
		}
	}
	public String getName(){
		return name;
	}
	public boolean isAttacked(){
		return isAttacked;
	}
	
	public void rook(){
		name = "r";
	}
	public void knight(){
		name = "n";
	}
	public void bishop(){
		name = "b";
	}
	public void king(){
		name = "k";
	}
	public void queen(){
		name = "q";
	}
	public void pawn(){
		name = "p";
	}
	

}

