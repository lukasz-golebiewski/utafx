package uta;

public class Criterion {

        private String name;
        private double worstValue;
	private double bestValue;
	private int noOfSegments;
	private Boolean isGain;

        public Criterion(String name, boolean isGain, int noOfSegents) {
            this(noOfSegents, isGain);
            this.name = name;
        }
        
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
			return (isGain = bestValue > worstValue);
		}
		return isGain;
	}

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }    
}
