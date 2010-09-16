package uta;

import java.util.ArrayList;
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
	 * List of characteristic points.
	 */
	private final List<Double> chPs;
	/**
	 * List of values.
	 */

	private final List<Double> vals;

	LinearFunction(Criterion criterion) {
		chPs = new ArrayList<Double>();
		vals = new ArrayList<Double>();

		this.criterion = criterion;
		double interval = (criterion.getBestValue() - criterion.getWorstValue()) / criterion.getNoOfSegments();

		chPs.add(criterion.getWorstValue());
		for (int i = 1; i < (criterion.getNoOfSegments() + 1); i++) {
			chPs.add(chPs.get(i - 1) + interval);
		}
	}

	LinearFunction(double[] characteristicPoints, double[] values, Criterion criterion) {
		chPs = new ArrayList<Double>();
		for (int i = 0; i < characteristicPoints.length; i++) {
			chPs.add(characteristicPoints[i]);
		}

		vals = new ArrayList<Double>();
		for (int i = 0; i < values.length; i++) {
			vals.add(values[i]);
		}

		this.criterion = criterion;
	}

	public Double[] getValues() {
		return vals.toArray(new Double[] {});
	}

	public void addValue(double val, int index) {
		while (index > vals.size()) {
			vals.add(0d);
		}
		vals.add(index, val);
	}

	public Double[] getCharacteristicPoints() {
		return chPs.toArray(new Double[] {});
	}

	public int getNoOfPoints() {
		return chPs.size();
	}

	public Criterion getCriterion() {
		return criterion;
	}

	public boolean isIncreasing() {
		return chPs.get(chPs.size() - 1) - chPs.get(0) > 0;
	}

	/**
	 * Returns the value of this function in the specified point.
	 * 
	 * @param point
	 *            - at the x axis
	 * @return value from the y axis
	 */
	public double getValueAt(double point) {
		int indexOf = chPs.indexOf(point);
		if (indexOf >= 0) {
			return vals.get(indexOf);
		}

		Double temp = null;
		int index = -1;

		if (this.isIncreasing()) {
			if (chPs.get(0) > point) {
				return vals.get(0);
			}

			for (int i = 0; i < chPs.size(); i++) {
				if (chPs.get(i) > point) {
					temp = chPs.get(i);
					index = i;
					break;
				}
			}
		} else {
			if (chPs.get(0) < point) {
				return vals.get(0);
			}

			for (int i = 0; i < chPs.size(); i++) {
				if (chPs.get(i) < point) {
					temp = chPs.get(i);
					index = i;
					break;
				}
			}
		}

		int i = index;
		if (temp == null) {
			return vals.get(vals.size() - 1);
		}

		double interval = Math.abs(chPs.get(i) - chPs.get(i - 1));
		double distance = Math.abs(point - chPs.get(i - 1));

		double val = vals.get(i) - vals.get(i - 1);
		double temp2 = val / interval;
		return distance * temp2 + vals.get(i - 1);

	}
}
