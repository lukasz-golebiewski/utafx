package uta.constraint;

import uta.api.Point;

public class Constraint {

	private Point point;

	private double upperBound;

	private double lowerBound;

	public Constraint(Point point) {
		this.point = point;
		this.upperBound = 0;
		this.lowerBound = 0;
	}

	public Constraint(Point point, double lowerBound, double upperBound) {
		this.point = point;
		this.lowerBound = lowerBound;
		this.upperBound = upperBound;
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
