//Board Hash Table implementation
package com.example.chessApp.normal;
public class BoardHashTable implements BoardHashTableInterface{
	private class Node{
		String board;
		int numRepeats;
		Node next;
		Node(String b){
			board = b;
			next = null;
		}
	}
	private Node[] table;
	private int numItems;
	final int TABLE_SIZE = 101;

	public BoardHashTable(){
		table = new Node[TABLE_SIZE];
		numItems = 0;
	}
	private float strToFloat(String in){
		//System.out.printf("Converting %s to ascii\n",in);
		String ascii = "";
		for(int i = 0; i < in.length(); i++){
			char tempChar = in.charAt(i);
			int asciiNum = (int)(tempChar);
			String asciiStr = Integer.toString(asciiNum);
			ascii += asciiStr;
		}
		//System.out.printf("%s in ascii is %s\n", in, ascii);
		//int finalInt = Integer.parseInt();
		return Float.parseFloat(ascii);
	}
	private int hash(String currBoard){
		//System.out.printf("Hash index of %s is %f\n",currBoard,strToFloat(currBoard)%TABLE_SIZE);
		float tempFloat = strToFloat(currBoard);
		//tempFloat = tempFloat;
		return (int)(tempFloat%TABLE_SIZE);
	}
	public void add(String currBoard){
		int hashIndex = hash(currBoard);
		Node newNode = new Node(currBoard);
		System.out.printf("Adding %s to hash table at %d\n",currBoard,hashIndex);
		if (table[hashIndex] == null){
			System.out.printf("Nothing found at index %d\n",hashIndex);
			table[hashIndex] = newNode;
		}
		else
		{
			Node curr = table[hashIndex];
			// check if current board is within the chain
			while(curr != null)
			{
				if(currBoard.equals(curr.board))
				{
					curr.numRepeats++;
					System.out.println("increased num repeats");
					break;
				}

				curr = curr.next;
			}
			// if current board is a new board, add to the chain
			if(curr == null)
			{
				newNode.next = table[hashIndex];
				table[hashIndex] = newNode;
				numItems++;
			}
		}
	}

	public boolean threefoldRepetition(){
		for (int i = 0; i < TABLE_SIZE; i++){
			Node top = table[i];
			while (top != null){
				// 2 repeats means board position has been reached 3 times
				if(top.numRepeats > 1){
					return true;
				}
				top = top.next;
			}
		}
		return false;
	}
	public boolean fiftyMoves(){
		int numMoves = 0;;
		for (int i = 0; i < TABLE_SIZE; i++){
			Node top = table[i];
			while (top != null){
				numMoves += top.numRepeats;
				top = top.next;
			}
		}
		if (numMoves >= 50){
			return true;
		}
		return false;
	}
	public void deleteTable(){
		table = null;
	}
	public String toString(){
		String finalString = "";
		for (int i = 0; i < TABLE_SIZE; i++){
			Node top = table[i];
			while (top != null){
				if(top.numRepeats > 2){
					finalString += top.board;
				}
				top = top.next;
			}
		}
		return finalString;
	}


}