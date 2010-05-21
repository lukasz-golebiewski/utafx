package uta;

public class Criterion {

	private double worstValue;
	private double bestValue;
	private int noOfSegments;

	public Criterion(double worstValue, double bestValue, int noOfSegments) {
		this.worstValue = worstValue;
		this.bestValue = bestValue;
		this.noOfSegments = noOfSegments;
	}

	public int getNoOfSegments() {
		return noOfSegments;
	}

	public double getWorstValue() {
		return worstValue;
	}

	public double getBestValue() {
		return bestValue;
	}

}
