package uta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class StandardConstraintsManager implements ConstraintsManager {

	private LinearFunction[] functions;

	private Map<Point, Constraint> constraints = new HashMap<Point, Constraint>();

	StandardConstraintsManager(LinearFunction[] functions) {
		this.functions = functions;
		this.buildConstraints();
	}

	private void buildConstraints() {
		for (LinearFunction f : functions) {
			for (Point p : f.getPoints()) {
				Constraint c = new Constraint(p);
				Point betterNeighbor = f.getBetterNeighbor(p);
				Point worseNeighbor = f.getWorseNeighbor(p);

				if (null == worseNeighbor) {
					// c.setUpperBound(betterNeighbor.getY());
					c.setUpperBound(0);
				} else if (null == betterNeighbor) {
					c.setLowerBound(worseNeighbor.getY());
					c.setUpperBound(p.getY()); // temp
				} else {
					c.setUpperBound(betterNeighbor.getY());
					c.setLowerBound(worseNeighbor.getY());
				}

				constraints.put(p, c);
			}
		}
		for (LinearFunction f : functions) {
			Point p = f.getBestPoint();
			double upperBound = calculateUpperBound(f);
			constraints.get(p).setUpperBound(upperBound);
		}
	}

	private double calculateUpperBound(LinearFunction currentFun) {
		double upperBound = 1;
		for (LinearFunction f : functions) {
			if (currentFun.equals(f))
				continue;
			upperBound -= constraints.get(f.getBestPoint()).getLowerBound();
		}
		return upperBound;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see uta.ConstraintsManager#update(uta.LinearFunction, uta.Point)
	 */
	public void update(LinearFunction function, Point changedPoint) {
		Point betterNeighbor = function.getBetterNeighbor(changedPoint);
		Point worseNeighbor = function.getWorseNeighbor(changedPoint);
		if (null == worseNeighbor) {
			throw new RuntimeException("Changing value of the point with worst evaluation is not permitted!");
			// Constraint c = constraints.get(betterNeighbor);
			// c.setLowerBound(changedPoint.getY());
		} else if (null == betterNeighbor) {
			// changing best point's value
			if (function.getWorseNeighbor(worseNeighbor) != null)
				constraints.get(worseNeighbor).setUpperBound(changedPoint.getY());
			onBestPointUpdate(function, changedPoint);
		} else {
			// changing point in the middle
			Constraint c = null;
			if (function.getWorseNeighbor(worseNeighbor) != null) {
				c = constraints.get(worseNeighbor);
				c.setUpperBound(changedPoint.getY());
			}

			c = constraints.get(betterNeighbor);
			c.setLowerBound(changedPoint.getY());

			// special case - next to best:
			if (function.getBetterNeighbor(betterNeighbor) == null) {
				// update all the best upper bounds
				for (LinearFunction f : functions) {
					if (!f.equals(function)) {
						double upperBound = calculateUpperBound(f);
						constraints.get(f.getBestPoint()).setUpperBound(upperBound);
					}
				}
			}
		}
	}

	private void onBestPointUpdate(LinearFunction function, Point changedPoint) {
		double sumOfAllBestPointsValues = 0d;
		for (LinearFunction f : functions) {
			sumOfAllBestPointsValues += f.getBestPoint().getY();
		}

		double missingValue = sumOfAllBestPointsValues - 1.0;

		// missing value should be added/subtracted to/from other best points
		// proportions should be kept
		List<LinearFunction> funcs = new ArrayList<LinearFunction>();
		funcs.addAll(Arrays.asList(functions));
		while (missingValue != 0) {
			missingValue = subtractMissingValue(funcs, function, missingValue);
		}
	}

	private double subtractMissingValue(List<LinearFunction> funcs, LinearFunction function, double missingValue) {
		int divisor = funcs.size() > 1 ? funcs.size() - 1 : 1;
		double singleModificationSize = Math.abs(missingValue / divisor);

		List<LinearFunction> toRemove = new ArrayList<LinearFunction>();

		for (LinearFunction f : funcs) {
			if (f.equals(function))
				continue;

			Point p = f.getBestPoint();
			Constraint c = constraints.get(p);

			if (missingValue < 0d) {
				// add
				if (singleModificationSize < (c.getUpperBound() - p.getY())) {
					p.setY(p.getY() + singleModificationSize);
					missingValue += singleModificationSize;

				} else {
					// is this possible? NOPE
					missingValue += c.getUpperBound() - p.getY();
					p.setY(c.getUpperBound());
					toRemove.add(f);
				}
			} else {
				// subtract
				if (singleModificationSize < (p.getY() - c.getLowerBound())) {
					p.setY(p.getY() - singleModificationSize);
					missingValue -= singleModificationSize;
				} else {
					missingValue -= p.getY() - c.getLowerBound();
					p.setY(c.getLowerBound());
					toRemove.add(f);
				}
			}
			Point worseNeighbor = f.getWorseNeighbor(p);
			if (f.getWorseNeighbor(worseNeighbor) != null)
				constraints.get(worseNeighbor).setUpperBound(p.getY());
		}

		funcs.removeAll(toRemove);
		return missingValue;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see uta.ConstraintsManager#getConstraints()
	 */
	public Constraint[] getConstraints() {
		return constraints.values().toArray(new Constraint[constraints.size()]);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see uta.ConstraintsManager#getConstraintFor(uta.Point)
	 */
	public Constraint getConstraintFor(Point p) {
		return constraints.get(p);
	}

}
