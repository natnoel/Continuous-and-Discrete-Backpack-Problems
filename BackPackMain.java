//Filename:		BackPackMain.java
//Name:			Tan Shi Terng Leon
//Declaration:	I did not pass my program to anyone in the class or copy anyone's work 
//				and I am willing to accept whatever penalty given to me and also to all
//				related parties involved

import java.text.DecimalFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;


/**
 * 
 */

/**
 * @author Leon
 *
 */
public class BackPackMain {
	
	static private Vector<Item> allItems;	//All the possible items to be put in
	static private double capacity;			//The capacity of the backpack
	static private int totalItems;			//Total number of items
	
	static private StateNode bestState;		//The best solution discovered so far
	static private int nodesTraversed;		//Number of nodes traversed to get the solution
	
	public static void main(String[] args) {
		
		//Allocating space for variables
		allItems = new Vector<Item>();
		
		//Gets information from the file
		readFile(getFileName());
		
		//Sorts the items in decreasing order of their value to weight ratio
		Comparator<Item> c = Collections.reverseOrder();
		Collections.sort(allItems, c);
		
		//Gives each item a unique number
		labelItems();
		
		//Displays the data read from the file
		displayData();
		
		System.out.println("===================================");
		
		//Find the solution for the continuous problem
		solveContinuous();
		
		System.out.println("===================================");
		
		//Find the solution for the discrete problem using the breadth-first method
		solveDiscreteBreadthFirst();
		
		System.out.println("===================================");
		
		//Find the solution for the discrete problem using the depth-first method
		solveDiscreteDepthFirst();
		
		System.out.println("===================================");
		
		//Find the solution for the discrete problem using the best-first method
		//(processing the nodes in order of their upper bound)
		solveDiscreteBestFirst();
		
		System.out.println("\nThank you and have nice day! :)");
		
	}
	
	//Prompts the user to enter the data file name
	private static String getFileName() {
		String fileName;
		Scanner scn = new Scanner(System.in);
		
		System.out.print("Please enter the data file name: ");
		fileName = scn.nextLine();
		
		scn.close();
		
		return fileName;
	}
	
	//Reads in the data from file
	private static void readFile(String fileName) {
		try {
			Scanner scanner = new Scanner(new File(fileName));
			
			if (scanner.hasNextDouble())
				capacity = scanner.nextDouble();	//Reads in the capacity
			if (scanner.hasNextDouble())
				totalItems = scanner.nextInt();		//Reads in the total items
			
			while (scanner.hasNextDouble()) {		//Reads and stores each item
				double weight = 0, value = 0;
				weight = scanner.nextDouble();		//Reads in the weight of the item
				if (scanner.hasNextDouble())
					value = scanner.nextDouble();	//Reads in the value of the item
				
				Item item = new Item(weight, value, 1);	//Create a new item object
				
				allItems.add(item);					//Stores item to the vector
			}
			
			scanner.close();
				
		} catch (FileNotFoundException e) {			//If file is not found
			System.out.println(e);
			System.exit(-1);
		}
	}
	
	//Labels each item with a unique number
	private static void labelItems() {
		for (int i = 0; i < allItems.size(); i++) {
			allItems.get(i).setItemNo(i);
		}
	}
	
	//Displays data read from file
	private static void displayData() {
		
		System.out.println(	"Capacity of the backpack: " + capacity + "\n" +
							"Number of items: " + totalItems + "\n");
		
		System.out.println(StateNode.printItems(allItems));
	}
	
	//Solve the continuous problem
	private static void solveContinuous() {

		StateNode soln = new StateNode();
		
		int i = 0;
		
		//Adds items according in descending order according to their value/weight ratio
		//until the next item exceeds the capacity
		while (i < totalItems && (soln.getWeight() + allItems.get(i).getWeight() <= capacity)) {
			Item nextItem = allItems.get(i);
			soln.addItem(nextItem);
			i++;
		}
		
		//Divide and take a portion of the next item such that the portion fits in exactly to the capacity
		if (i < totalItems) {
			Item lastItem = new Item(allItems.get(i));
			double proportion = (capacity - soln.getWeight()) / lastItem.getWeight();
			lastItem.setProportion(proportion);
			soln.addItem(lastItem);
		}
		
		//Print the solution
		System.out.print("<Continous Backpack>\n\n" + soln);
	}
	
	//Represents a state of the backpack having a certain combination of items inside
	//Used for finding discrete solution
	static class StateNode implements Comparable<StateNode> {
		
		private int level;						//Level of the node in the tree
		private double value, weight, bound;	//Total value, weight of the items
												//and the upper bound of the node
		private Vector<Item> chosen;			//Items in the backpack
		
		//Formats to display the value and weight
		static private DecimalFormat valueFmt = new DecimalFormat("0.00");
		static private DecimalFormat weightFmt = new DecimalFormat("0.00");
		
		StateNode() {
			level = 0;
			value = 0;
			weight = 0;
			bound = computeBound();
			chosen = new Vector<Item>();
		}
		
		StateNode(int level, double value, double weight, double bound, Vector<Item> chosen) {
			this.level = level;
			this.value = value;
			this.weight = weight;
			this.bound = bound;
			this.chosen = new Vector<Item>(chosen);
		}
		
		StateNode(StateNode stateNode) {
			level = stateNode.level;
			value = stateNode.value;
			weight = stateNode.weight;
			chosen = new Vector<Item>(stateNode.chosen);
		}
		
		public int getLevel() {
			return level;
		}
		
		public void setLevel(int level) {
			this.level = level;
		}
		
		public double getValue() {
			return value;
		}
		
		public void setValue(double value) {
			this.value = value;
		}
		
		public double getWeight() {
			return weight;
		}
		
		public void setWeight(double weight) {
			this.weight = weight;
		}
		
		public double getBound() {
			return bound;
		}
		
		//Computes and returns the upper bound for the state
		//A rough estimation of the maximum value it can reach by branching out
		public double computeBound() {
			double boundedWeight = weight;
			double bound = value;
			int i = level;
			
			//Adds in the next item according to its value/weight ratio until capacity is exceeded
			while (i < totalItems && boundedWeight + allItems.get(i).getWeight() <= capacity) {
				Item next = allItems.get(i);
				boundedWeight += next.getWeight();	//Calculates new total weight
				bound += next.getValue();			//Calculates new total value
				
				i++;
			}
			
			//Divides and adds in a portion of the final item such that its capacity is full
			if (i < totalItems)
				bound += (capacity - boundedWeight) * allItems.get(i).getVWRatio(); //Computes new total value
			
			return bound;	//Returns the total value
		}
		
		public Vector<Item> getChosen() {
			return chosen;
		}
		
		public void setChosen(Vector<Item> chosen) {
			this.chosen.clear();
			value = 0;
			weight = 0;
			
			for (Item item : chosen)
				addItem(item);
		}
		
		//Adds an item into the backpack and updates its total value and weight
		public void addItem(Item item) {
			chosen.add(item);
			value += (item.getValue() * item.getProportion());
			weight += (item.getWeight() * item.getProportion());
		}
		
		//Creates another state where the next item is added or not added at the next level of the tree
		public StateNode nextAdded(boolean added) {
			
			StateNode nextState = new StateNode(this);
			nextState.level = level + 1;				//Sets the new level
			if (added == true)							//If next item is added
				nextState.addItem(allItems.get(level));	//Adds the next item
			nextState.bound = nextState.computeBound();	//Computes and updates the new upper bound
			
			return nextState;							//Returns the new StateNode object
		}
		
		//Prints the solution
		public String toString() {
			String s = printItems (chosen);
			
			s += "\nTotal Value is: " + valueFmt.format(value) + "\n" +
					"Total Weight is: " + weightFmt.format(weight) + "\n";
			
			return s;
		}
		
		//Prints the list of items inside
		static String printItems(Vector<Item> chosen) {
			String s = "ItemNo.\tValue\tWeight\tProportion\n" +
					"----------------------------------\n";
		
			for (Item i : chosen) {
				s += i;
			}
			
			return s;
		}
		
		//Comparing states according to their upper bound
		public int compareTo(StateNode node) {
			int value;
			
			if (bound < node.bound)
				value = -1;
			else if (bound == node.bound)
				value = 0;
			else
				value = 1;
			
			return value;
		}
		
	}
	
	//Solves the discrete problem using the breadth-first traversal method
	private static void solveDiscreteBreadthFirst() {
		
		Queue queue = new Queue();			//Creates the queue
		StateNode root = new StateNode();	//Initialize the root
		StateNode bestState = root;			//Initialize the best solution so far
		StateNode currNode;
		int nodesTraversed = 0;
		
		queue.enqueue(root);	//Adds first node (empty state of the bag)
		
		while (!queue.isEmpty()) {
			currNode = queue.front();	//Takes first item from the queue
			queue.dequeue();			//Dequeues the first item
			
			if (currNode.getBound() > bestState.getValue()) {
				//Next item added
				StateNode nextAdded = currNode.nextAdded(true);
					
				if (nextAdded.getWeight() <= capacity) {	//If next added node within capacity
					
					//If the next added node is a better solution than the best solution found so far
					if (nextAdded.getValue() > bestState.getValue())
						bestState = nextAdded;
					
					//If next added node has a potential to reach a better solution
					if (nextAdded.getBound() > bestState.getValue())
						queue.enqueue(nextAdded);
				}
						
				//Next item not added
				StateNode nextNotAdded = currNode.nextAdded(false);
				
				//If next not added node has a potential to reach a better solution
				if (nextNotAdded.getBound() > bestState.getValue())
					queue.enqueue(nextNotAdded);
			}
			nodesTraversed++;	//Updates total nodes traversed
			
			//Remove the "//"s below to trace the solution
			//System.out.println(currNode + "Upper bound: " + currNode.getBound() + "\n" +
			//		"Node " + nodesTraversed + "\n");
		}
		
		//Prints the solution
		System.out.println("<Discrete Backpack (Breadth-First)>\n\n" + bestState +
				"Number of nodes traversed: " + nodesTraversed);
	}
	
	private static void solveDiscreteDepthFirst() {
		StateNode root = new StateNode();	//Initialize the root node
		bestState = root;					//Initialize the best solution node
		nodesTraversed = 0;
		
		solveDepthFirst(root);
		
		//Prints solution
		System.out.println("<Discrete Backpack (Depth-First)>\n\n" + bestState +
						"Number of nodes traversed: " + nodesTraversed);
	}
	
	private static void solveDepthFirst(StateNode root) {
		
		//If current node has a potential to reach a better solution
		if (root.getBound() > bestState.getValue()) {
			
			//Next item added
			StateNode nextAdded = root.nextAdded(true);
			
			//If weight is within capacity
			if (nextAdded.getWeight() <= capacity) {
				//If next added node is a better solution than the best solution found so far
				if (nextAdded.getValue() > bestState.getValue())
					bestState = nextAdded;
				
				solveDepthFirst(nextAdded);	//Recursive call
			}
			
			//Next item not added
			StateNode nextNotAdded = root.nextAdded(false);
			
			//If next not added node has a potential to reach a better solution
			if (nextNotAdded.getBound() > bestState.getValue())
				solveDepthFirst(nextNotAdded);
		}
		
		nodesTraversed++;	//Updates the total nodes traversed
		
		//Remove the "//"s below to trace the solution
		//System.out.println(root + "Upper bound: " + root.getBound() + "\n" +
		//		"Node " + nodesTraversed + "\n");
	}
	
	//Solves the discrete problem by branching out from the live node with
	//the highest upper bound at each turn
	private static void solveDiscreteBestFirst() {
		SortedList liveStates = new SortedList();	//Create a list that maintains its sorted order
		StateNode root = new StateNode();			//Initializes the root node
		StateNode bestState = root;					//Initializes the best node
		StateNode currNode;
		int nodesTraversed = 0;
		
		liveStates.insert(root);	//Inserts the empty root node(backpack starts with no items in it)
		
		while (!liveStates.isEmpty()) {
			currNode = liveStates.back();	//Gets the node with the highest upper bound
											//(the potential to get the highest value)
			liveStates.popBack();			//Removes the node from list
			
			//Next item added
			StateNode nextAdded = currNode.nextAdded(true);
			
			//If weight is within capacity
			if (nextAdded.getWeight() <= capacity) {
				
				//If next added node is a better solution than the current best
				if (nextAdded.getValue() > bestState.getValue()) {
					bestState = nextAdded;
					
					//Removes the rest of the nodes that have an upper bound
					//lower than the value of the new best solution
					liveStates.prune(bestState);
				}
				
				//If next added node has potential to reach a better solution
				if (nextAdded.getBound() > bestState.getValue())
					liveStates.insert(nextAdded);
			}
			
			//Next item not added
			StateNode nextNotAdded = currNode.nextAdded(false);
			
			//If next not added node has a potential to reach a better solution
			if (nextNotAdded.getBound() > bestState.getValue())
				liveStates.insert(nextNotAdded);
			
			nodesTraversed++;	//Updates total nodes traversed
			
			//Remove the "//" below to trace the solution
			//System.out.println(currNode + "Upper bound: " + currNode.getBound() + "\n" +
			//		"Node " + nodesTraversed + "\n");
			
		}
		
		//Prints the solution
		System.out.println("<Discrete Backpack (Best-First)>\n\n" + bestState +
				"Number of nodes traversed: " + nodesTraversed);
	}

}
