//Filename:		SortedList.java
//Name:			Tan Shi Terng Leon
//Declaration:	I did not pass my program to anyone in the class or copy anyone's work 
//				and I am willing to accept whatever penalty given to me and also to all
//				related parties involved

import java.util.ArrayList;
import java.util.Collections;

/**
 * 
 */

/**
 * @author Leon
 *
 */


public class SortedList {

	/**
	 * 
	 */
	private ArrayList<BackPackMain.StateNode> list;
	
	public SortedList() {
		list = new ArrayList<BackPackMain.StateNode>();
	}
	
	public boolean isEmpty() {
		return list.isEmpty();
	}
	
	public BackPackMain.StateNode front() {
		return list.get(0);
	}
	
	public BackPackMain.StateNode back() {
		return list.get(list.size() - 1);
	}
	
	public void popFront() {
		list.remove(0);
	}
	
	public void popBack() {
		list.remove(list.size() - 1);
	}
	
	//Inserts a StateNode object at its position according to its upper bound
	public void insert(BackPackMain.StateNode state) {
		int pos = Collections.binarySearch(list, state);	//Finds the position
		
		//Adds the StateNode object
		if (pos < 0)
			list.add(-pos - 1, state);
		else
			list.add(pos, state);
	}
	
	//Removes all the StateNode objects have upper bounds lower than the given state's value
	public void prune (BackPackMain.StateNode bestState) {
		int i = 0;
		
		while (i < list.size() && list.get(i).getBound() <= bestState.getValue()) {
			if (list.get(i).getBound() <= bestState.getValue()) {
				list.remove(i);
				i--;
			}
			i++;
		}
		
	}

}
