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
	 * @param noOfSegments
	 *            - number of linear segments
	 * @param gain
	 *            - true if this criterion is of <b>gain</b> type (false if it's
	 *            a <b>cost</b>)
	 */
	public Criterion(int noOfSegments, boolean gain) {
		this.noOfSegments = noOfSegments;
		this.isGain = gain;
	}

	/**
	 * For tests.
	 * 
	 * @param worstValue
	 * @param bestValue
	 * @param noOfSegments
	 */
	Criterion(double worstValue, double bestValue, int noOfSegments) {
		this.worstValue = worstValue;
		this.bestValue = bestValue;
		this.noOfSegments = noOfSegments;
	}

	public Criterion(String name, int noOfSegments, boolean isGain) {
		this(name, isGain, noOfSegments);
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

	public boolean isGain() {
		if (isGain == null) {
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(bestValue);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((isGain == null) ? 0 : isGain.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + noOfSegments;
		temp = Double.doubleToLongBits(worstValue);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Criterion other = (Criterion) obj;
		if (Double.doubleToLongBits(bestValue) != Double.doubleToLongBits(other.bestValue))
			return false;
		if (isGain == null) {
			if (other.isGain != null)
				return false;
		} else if (!isGain.equals(other.isGain))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (noOfSegments != other.noOfSegments)
			return false;
		if (Double.doubleToLongBits(worstValue) != Double.doubleToLongBits(other.worstValue))
			return false;
		return true;
	}
}
