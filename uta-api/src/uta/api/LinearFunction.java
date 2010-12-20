package uta.api;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Piecewise linear function representing utility of a certain criterion.
 * 
 * @author masklin
 * 
 */
public class LinearFunction {

	private final Criterion criterion;

	/**
	 * List of characteristic points. Point representing the worst value is
	 * always at index = 0 and the best value is at last index.
	 */
	private final List<Point> characteristicPoints = new ArrayList<Point>();

	public LinearFunction(Criterion criterion) {
		this.criterion = criterion;
		double interval = (criterion.getBestValue() - criterion.getWorstValue()) / criterion.getNoOfSegments();

		characteristicPoints.add(new Point(criterion.getWorstValue(), 0));

		for (int i = 1; i < (criterion.getNoOfSegments() + 1); i++) {
			characteristicPoints.add(new Point(characteristicPoints.get(i - 1).getX() + interval, 0));
		}
	}

	public LinearFunction(double[] characteristicPoints, double[] values, Criterion criterion) {
		for (int i = 0; i < characteristicPoints.length; i++) {
			this.characteristicPoints.add(new Point(characteristicPoints[i], values[i]));
		}
		this.criterion = criterion;
	}

	/**
	 * Copy constructor.
	 * 
	 * @param toCopy
	 */
	public LinearFunction(LinearFunction toCopy) {
		this.criterion = new Criterion(toCopy.criterion.getName(), toCopy.criterion.isGain(), toCopy.criterion.getNoOfSegments());
		for (Point p : toCopy.characteristicPoints) {
			this.characteristicPoints.add(new Point(p.getX(), p.getY()));
		}
	}

	public Double[] getValues() {
		Double[] result = new Double[characteristicPoints.size()];
		int i = 0;
		for (Iterator<Point> iterator = characteristicPoints.iterator(); iterator.hasNext(); i++) {
			result[i] = iterator.next().getY();
		}
		return result;
	}

	public void addValue(double val, int index) {
		characteristicPoints.get(index).setY(val);
	}

	public List<Point> getPoints() {
		return Collections.unmodifiableList(characteristicPoints);
	}

	public Double[] getCharacteristicPoints() {
		Double[] result = new Double[characteristicPoints.size()];
		int i = 0;
		for (Iterator<Point> iterator = characteristicPoints.iterator(); iterator.hasNext(); i++) {
			result[i] = iterator.next().getX();
		}
		return result;
	}

	public int getNoOfPoints() {
		return characteristicPoints.size();
	}

	public Criterion getCriterion() {
		return criterion;
	}

	public boolean isIncreasing() {
		return characteristicPoints.get(characteristicPoints.size() - 1).getX() - characteristicPoints.get(0).getX() > 0;
	}

	/**
	 * Returns the value of this function in the specified point.
	 * 
	 * @param point
	 *            - at the x axis
	 * @return value from the y axis
	 */
	public double getValueAt(double point) {
		Double temp = null;
		int index = -1;

		if (this.isIncreasing()) {
			if (characteristicPoints.get(0).getX() > point) {
				return characteristicPoints.get(0).getY();
			}

			for (int i = 0; i < characteristicPoints.size(); i++) {
				if (characteristicPoints.get(i).getX() > point) {
					temp = characteristicPoints.get(i).getX();
					index = i;
					break;
				}
			}
		} else {
			if (characteristicPoints.get(0).getX() < point) {
				return characteristicPoints.get(0).getY();
			}

			for (int i = 0; i < characteristicPoints.size(); i++) {
				if (characteristicPoints.get(i).getX() < point) {
					temp = characteristicPoints.get(i).getX();
					index = i;
					break;
				}
			}
		}

		int i = index;
		if (temp == null) {
			return characteristicPoints.get(characteristicPoints.size() - 1).getY();
		}

		double interval = Math.abs(characteristicPoints.get(i).getX() - characteristicPoints.get(i - 1).getX());
		double distance = Math.abs(point - characteristicPoints.get(i - 1).getX());

		double val = characteristicPoints.get(i).getY() - characteristicPoints.get(i - 1).getY();
		double temp2 = val / interval;
		return distance * temp2 + characteristicPoints.get(i - 1).getY();

	}

	public Point getBetterNeighbor(Point p) {
		int index = characteristicPoints.indexOf(p);
		if (index == -1)
			throw new RuntimeException("Tried to get a neighbor of a point which doesn't belong to this function!");
		if (++index > characteristicPoints.size() - 1)
			return null;

		return characteristicPoints.get(index);
	}

	public Point getWorseNeighbor(Point p) {
		int index = characteristicPoints.indexOf(p);
		if (index == -1)
			throw new RuntimeException("Tried to get a neighbor of a point which doesn't belong to this function!");

		if (--index < 0)
			return null;

		return characteristicPoints.get(index);
	}

	public Point getBestPoint() {
		return characteristicPoints.get(characteristicPoints.size() - 1);
	}

	public void setPointAt(double x, Point point) {
		for (int i = 0; i < characteristicPoints.size(); i++) {
			if (characteristicPoints.get(i).getX() == point.getX()) {
				characteristicPoints.set(i, point);
				return;
			}
		}
	}

	public boolean containsPoint(Point p) {
		return characteristicPoints.contains(p);
	}
}
