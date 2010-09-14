package uta;

public class Criterion {

	private double worstValue;
	private double bestValue;
	private int noOfSegments;
	private Boolean isGain;

	/**
	 * 
	 * @param noOfSegments - number of linear segments
	 * @param gain - true if this criterion is of <b>gain</b> type (false if it's a <b>cost</b>)
	 */
	public Criterion(int noOfSegments, boolean gain) {
		this.noOfSegments = noOfSegments;
		this.isGain = gain;
	}
	
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
	
	public void setBestValue(double value) {
		this.bestValue = value;
	}
	
	public void setWorstValue(double value) {
		this.worstValue = value;
	}
	
	public boolean isGain(){
		if(isGain == null){
			return bestValue > worstValue;
		}
		return isGain;
	}

}
