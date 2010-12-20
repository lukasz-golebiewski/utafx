package uta.api;

/**
 * Holds two elements of any types. 
 * @author Lukasz Golebiewski
 *
 * @param <T1> - type of the first element
 * @param <T2> - type of the second element
 */
public class Pair<T1, T2> {

	private T1 first;
	private T2 second;
	
	public Pair(T1 first, T2 second) {
		this.first = first;
		this.second = second;
	}

	public T1 getFirst() {
		return first;
	}

	public T2 getSecond() {
		return second;
	}

}
