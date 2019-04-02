package com.example.chessApp.cylinder;

// test settings for computer player

public class test
{
	public static void main(String[] args)
	{
		
		BoardCylinder testBoard = new BoardCylinder();
		testBoard.printBoard();
		
		// white computer player with depth 4
		ComputerPlayer testPlayer = new ComputerPlayer(testBoard, true, 4);
		// black computer player with depth 4
		ComputerPlayer testPlayer2 = new ComputerPlayer(testBoard, false, 4);

		// play 10 moves 
		for(int i = 0; i < 10; i++)
		{
			System.out.println("Move: " + i);
			testPlayer.action();
			testBoard.printBoard();
	
			testPlayer2.action();
			testBoard.printBoard();
			
			System.out.println();
		}	
	}
}