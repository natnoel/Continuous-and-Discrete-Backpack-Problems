//Filename:		Item.java
//Name:			Tan Shi Terng Leon
//Declaration:	I did not pass my program to anyone in the class or copy anyone's work 
//				and I am willing to accept whatever penalty given to me and also to all
//				related parties involved

import java.text.DecimalFormat;

/**
 * 
 */

/**
 * @author Leon
 *
 */
public class Item implements Comparable<Item> {
	
	private int itemNo;		//A unique number to identify each item
	private double weight, value, vwRatio, proportion;
	
	//Format to represent the proportion
	static private DecimalFormat fmt = new DecimalFormat("0.00000");
	
	Item (double weight, double value, double proportion) {
		this.weight = weight;
		this.value = value;
		this.proportion = proportion;
		vwRatio = computeRatio();
	}
	
	public Item(Item item) {
		itemNo = item.itemNo;
		weight = item.weight;
		value = item.value;
		proportion = item.proportion;
		vwRatio = computeRatio();
	}
	
	private double computeRatio() {
		return value / weight;
	}
	
	public double getWeight() {
		return weight;
	}
	
	public double getValue() {
		return value;
	}
	
	public double getVWRatio() {
		return vwRatio;
	}
	
	public double getProportion() {
		return proportion;
	}
	
	public void setProportion(double proportion) {
		this.proportion = proportion;
	}
	
	public int getItemNo() {
		return itemNo;
	}
	
	public void setItemNo(int itemNo) {
		this.itemNo = itemNo;
	}
	
	//Compares items by their value to weight ratio
	public int compareTo(Item i) {
		
		int result;
		
		if (vwRatio < i.vwRatio)
			result = -1;
		else if (vwRatio == i.vwRatio)
			result = 0;
		else
			result = 1;
		
		return result;
	}
	
	public String toString() {
		String output = itemNo + "\t" + value + "\t" + weight + "\t" + fmt.format(proportion) + '\n';
		return output;
	}

}
