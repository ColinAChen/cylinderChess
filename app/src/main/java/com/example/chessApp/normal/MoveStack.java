package com.example.chessApp.normal;
public class MoveStack implements MoveStackInterface{
	private class Node{
		String move;
		Node next;
		Node(String m){
			move = m;
			next = null;
		}
	}
	private Node top;
	private int numItems;
	public MoveStack(){
		top = null;
		numItems = 0;
	}
	
	public void push(String moveToAdd){
		Node newMove = new Node(moveToAdd);
		//System.out.println(newMove.move);
		newMove.next = top;
		top = newMove;
		System.out.println(top.move);
		numItems++;

	}
	public String pop(){
		Node nodeToReturn = top;
		top = top.next;
		numItems--;
		return nodeToReturn.move;

	}
	public String peek(){
		if (top!=null){
			return (top.move);
		}
		return "Stack is empty!";
		
	}
	/*
	public String[] popAll(){
		Node N = top;
		String[] items = 
		while(N!=null){

		}
	}*/
	public String toString(){
		String[] inOrder = new String[numItems];
		String finalString = "";
		Node N = top;
		//System.out.println("Adding to inOrder");
		for (int i = numItems-1; i > -1; i--){
			//System.out.println(N.move);
			if (N!=null){
				inOrder[i] = N.move;
				N = N.next;
			}

			if (N!= null){
				inOrder[i] += " " + N.move;
				N = N.next;
			}
		}
		//System.out.println("Removing from inOrder");
		for(int j = 0; j < numItems; j++){

			//System.out.println(inOrder[j]);
			finalString += (j+1) + ". " + inOrder[j] + "\n";
		}
		return finalString;
	}
}