package uta.constraint;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import uta.api.LinearFunction;
import uta.api.Point;

class StandardConstraintsManager extends AbstractConstraintsManager {

	private static final double PRECISION = 0.000000000001;

	StandardConstraintsManager(LinearFunction[] functions) {
		super(functions);
		this.buildConstraints();
	}

	protected void updateOrCreateConstraint(Map<Point, Constraint> constraints, Point p, double lowerBound, double upperBound) {
		Constraint constraint = constraints.get(p);
		if (constraint == null) {
			constraints.put(p, new Constraint(p, lowerBound, upperBound));
		} else {
			constraint.setLowerBound(lowerBound);
			constraint.setUpperBound(upperBound);
		}
	}

	protected void buildConstraints() {
		for (LinearFunction f : functions) {
			for (Point p : f.getPoints()) {
				// Constraint c = new Constraint(p);
				Point betterNeighbor = f.getBetterNeighbor(p);
				Point worseNeighbor = f.getWorseNeighbor(p);

				double lowerBound, upperBound;

				if (null == worseNeighbor) {
					// c.setUpperBound(betterNeighbor.getY());
					lowerBound = 0;
					upperBound = 0;
				} else if (null == betterNeighbor) {
					lowerBound = worseNeighbor.getY();
					upperBound = (p.getY()); // temp
				} else {
					upperBound = (betterNeighbor.getY());
					lowerBound = (worseNeighbor.getY());
				}

				// constraints.put(p, new Constraint(p, lowerBound,
				// upperBound));
				updateOrCreateConstraint(constraints, p, lowerBound, upperBound);
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
		} else if (null == betterNeighbor) {
			// changing best point's value
			if (function.getWorseNeighbor(worseNeighbor) != null)
				constraints.get(worseNeighbor).setUpperBound(changedPoint.getY());
			doBestPointUpdate(function, functions, true, constraints);
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

	void doBestPointUpdate(LinearFunction function, LinearFunction[] functions, boolean modifyConstraints,
			Map<Point, Constraint> constraints) {
		double sumOfAllBestPointsValues = 0d;
		for (LinearFunction f : functions) {
			sumOfAllBestPointsValues += f.getBestPoint().getY();
		}

		compensate(function, functions, sumOfAllBestPointsValues, modifyConstraints, constraints);
	}

	void compensate(LinearFunction function, LinearFunction[] functions, double sumOfAllBestPointsValues, boolean modifyConstraints,
			Map<Point, Constraint> constraints) {
		double missingValue = sumOfAllBestPointsValues - 1.0;

		// missing value should be added/subtracted to/from other best points
		// proportions should be kept
		List<LinearFunction> funcs = new ArrayList<LinearFunction>();
		funcs.addAll(Arrays.asList(functions));
		while (Math.abs(missingValue) > PRECISION) {
			missingValue = subtractMissingValue(funcs, function, missingValue, modifyConstraints, constraints);
		}
	}

	double subtractMissingValue(List<LinearFunction> funcs, LinearFunction function, double missingValue, boolean modifyConstraints,
			Map<Point, Constraint> constraints) {
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
			if (modifyConstraints && f.getWorseNeighbor(worseNeighbor) != null)
				constraints.get(worseNeighbor).setUpperBound(p.getY());
		}

		funcs.removeAll(toRemove);
		return missingValue;
	}

}
