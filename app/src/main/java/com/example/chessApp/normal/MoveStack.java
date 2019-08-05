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
		if(top == null)
			return "Stack is empty!";

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

	public int getNumItems()
	{
		return numItems;
	}

	public MoveStack getCopy()
	{
		MoveStack copy = new MoveStack();
		Node[] moves = new Node[numItems];
		
		Node curr = top;
		int i = 0;
		while(curr != null)
		{
			moves[i] = curr;
			curr = curr.next;
			i++;
		}

		for(i = numItems - 1; i >= 0; i--)
		{
			copy.push(moves[i].move);	
		}
		
		return copy;
	}

	public String toString(){
		String[] inOrder = new String[(numItems + 1) / 2];
		String finalString = "";
		Node N = top;
		//System.out.println("Adding to inOrder");
		for (int i = inOrder.length - 1; i > -1; i--){
			if((N == top) && (numItems % 2 == 1)) {
			    inOrder[i] = N.move;
			    N = N.next;
			    continue;
            }
            if (N != null && N.next != null) {
                inOrder[i] = N.next.move;
                inOrder[i] += " " + N.move;
                N = N.next;
                N = N.next;
            }
		}
		//System.out.println("Removing from inOrder");
		for(int j = 0; j < inOrder.length; j++){

			//System.out.println(inOrder[j]);
			finalString += (j+1) + ". " + inOrder[j] + "\n";
		}
		return finalString;
	}
}