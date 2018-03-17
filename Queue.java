//Filename:		Queue.java
//Name:			Tan Shi Terng Leon
//Declaration:	I did not pass my program to anyone in the class or copy anyone's work 
//				and I am willing to accept whatever penalty given to me and also to all
//				related parties involved

import java.util.ArrayList;

/**
 * 
 */

/**
 * @author Leon
 *
 */
public class Queue {

	private ArrayList<BackPackMain.StateNode> queue;
	
	Queue() {
		queue = new ArrayList<BackPackMain.StateNode>();
	}
	
	public boolean isEmpty() {
		return queue.isEmpty();
	}
	
	public BackPackMain.StateNode front() {
		return queue.get(0);
	}
	
	public void dequeue() {
		queue.remove(0);
	}
	
	public void enqueue(BackPackMain.StateNode state) {
		queue.add(state);
	}
}
