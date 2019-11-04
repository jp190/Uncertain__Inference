package bn.util;

import java.util.HashMap;

import bn.core.RandomVariable;

public class Pair {
	
	private HashMap<RandomVariable, Object> hashmap;
	public double weight;
	
	
	public Pair(HashMap<RandomVariable, Object> hashmap, double weight) {
		this.hashmap = hashmap;
		this.weight = weight;
	}
	public HashMap<RandomVariable, Object> getFirst() {
		return hashmap;
	}
	public double getSecond() {
		return weight;
	}

	@Override
	public String toString() {
		return "< " + getFirst().toString() + " , " + getSecond()
				+ " > ";
	}
}
