package uta;

public class Constraint {

	private Point point;

	private double upperBound;

	private double lowerBound;

	public Constraint(Point point) {
		this.upperBound = 0;
		this.lowerBound = 0;
		this.point = point;
	}

	public void setUpperBound(double upperBound) {
		this.upperBound = upperBound;
	}

	public double getUpperBound() {
		return upperBound;
	}

	public void setLowerBound(double lowerBound) {
		this.lowerBound = lowerBound;
	}

	public double getLowerBound() {
		return lowerBound;
	}

	public Point getPoint() {
		return point;
	}

}
