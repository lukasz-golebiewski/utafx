package uta.constraint;

import java.util.HashMap;
import java.util.Map;
import uta.api.Alternative;
import uta.api.LinearFunction;
import uta.api.Point;
import uta.api.Ranking;
import uta.utils.RankingUtils;

public class ConstantKendallConstraintsManager extends StandardConstraintsManager {

	private double searchPrecision = 0.001;
	private static final RankingUtils RANKING_UTILS = new RankingUtils();
	private final Ranking<Alternative> referenceRank;
	private Ranking<Alternative> finalRank;
	private final double currentKendall;

	protected Map<Point, Constraint> modifiedConstraints = new HashMap<Point, Constraint>();

	public ConstantKendallConstraintsManager(LinearFunction[] functions, Ranking<Alternative> referenceRank, Ranking<Alternative> finalRank) {
		super(functions);
		this.referenceRank = referenceRank;
		this.finalRank = finalRank;
		this.currentKendall = RANKING_UTILS.getKendallsCoefficient(this.referenceRank, this.finalRank);
		// constraints built by super.buildConstraints() need to be updated
		initModifiedConstraints();
		updateConstraints();
	}

	private void initModifiedConstraints() {
		for (Point p : constraints.keySet()) {
			Constraint constraint = constraints.get(p);
			double lowerBound = constraint.getLowerBound();
			double upperBound = constraint.getUpperBound();
			updateOrCreateConstraint(modifiedConstraints, p, lowerBound, upperBound);
		}
	}

	private boolean isBestPoint(Point p) {
		for (LinearFunction f : functions) {
			if (f.getBestPoint().equals(p))
				return true;
		}
		return false;
	}

	private void updateConstraints() {
		// iterate over middle points first
		for (Point p : constraints.keySet()) {
			if (!isBestPoint(p)) {
				updatePoint(p);
			}
		}
		// iterate over best points
		for (Point p : constraints.keySet()) {
			if (isBestPoint(p)) {
				updatePoint(p);
			}
		}

	}

	private void updatePoint(Point p) {
		Constraint c = constraints.get(p);

		double newConstraintValue = search(p, c, c.getUpperBound(), p.getY(), true);
		// if (Math.abs(middle - c.getUpperBound()) > 0) {
		setNewUpperBound(p, newConstraintValue);
		// }

		newConstraintValue = search(p, c, p.getY(), c.getLowerBound(), false);
		// if (Math.abs(middle - c.getLowerBound()) > 0) {
		setNewLowerBound(p, newConstraintValue);
		// }
	}

	private double search(Point p, Constraint c, double high, double low, boolean searchUpwards) {
		double middle = (high + low) / 2;
		double prevMiddle = high;
		double previousHigh = high;
		double previousLow = low;

		while (Math.abs(middle - prevMiddle) > searchPrecision) {
			prevMiddle = middle;
			boolean isWorse = isKendallWorseWhenPointHasValue(p, middle);
			// conditions are inverted when we are searching downwards
			if (isWorse == searchUpwards) {
				previousHigh = middle;
				middle = (middle + previousLow) / 2;
			} else {
				previousLow = middle;
				middle = (middle + previousHigh) / 2;
			}
		}
		return middle;
	}

	private void setNewLowerBound(Point p, double middle) {
		modifiedConstraints.get(p).setLowerBound(middle);
	}

	private void setNewUpperBound(Point p, double middle) {
		modifiedConstraints.get(p).setUpperBound(middle);
	}

	boolean isKendallWorseWhenPointHasValue(Point p, double value) {
		return isBestPoint(p) ? isKendallWorseWhenBestPointHasValue(p, value) : isKendallWorseWhenMiddlePointHasValue(p, value);
	}

	boolean isKendallWorseWhenMiddlePointHasValue(Point p, double value) {
		LinearFunction[] deepCopy = new LinearFunction[functions.length];
		int functionIndex = 0;
		for (int i = 0; i < functions.length; i++) {
			if (functions[i].containsPoint(p)) {
				functionIndex = i;
			}
			deepCopy[i] = new LinearFunction(functions[i]);
		}

		deepCopy[functionIndex].setPointAt(p.getX(), new Point(p.getX(), value));
		Ranking<Alternative> newRank = RANKING_UTILS.buildRank(deepCopy, referenceRank.getAlternatives());
		double newKendall = RANKING_UTILS.getKendallsCoefficient(referenceRank, newRank);
		return newKendall < currentKendall;
	}

	boolean isKendallWorseWhenBestPointHasValue(Point p, double value) {
		LinearFunction[] deepCopy = new LinearFunction[functions.length];
		int functionIndex = 0;
		for (int i = 0; i < functions.length; i++) {
			if (functions[i].containsPoint(p)) {
				functionIndex = i;
			}
			deepCopy[i] = new LinearFunction(functions[i]);
		}
		Point newPoint = new Point(p.getX(), value);

		Map<Point, Constraint> constraints = new HashMap<Point, Constraint>();
		Constraint newConstraint = new Constraint(newPoint);
		constraints.put(newPoint, newConstraint);
		newConstraint.setLowerBound(this.constraints.get(p).getLowerBound());
		newConstraint.setUpperBound(this.constraints.get(p).getUpperBound());

		deepCopy[functionIndex].setPointAt(p.getX(), newPoint);

		for (int i = 0; i < functions.length; i++) {
			Constraint c = this.constraints.get(functions[i].getBestPoint());
			constraints.put(deepCopy[i].getBestPoint(), c);
		}

		doBestPointUpdate(deepCopy[functionIndex], deepCopy, false, constraints);

		Ranking<Alternative> newRank = RANKING_UTILS.buildRank(deepCopy, referenceRank.getAlternatives());
		double newKendall = RANKING_UTILS.getKendallsCoefficient(referenceRank, newRank);
		return newKendall < currentKendall;
	}

	@Override
	public void update(LinearFunction function, Point changedPoint) {
		super.update(function, changedPoint);
		// clear all
		// constraints.clear();
		// modifiedConstraints.clear();

		// rebuild constraints
		buildConstraints();
		initModifiedConstraints();
		updateConstraints();
	}

	public Constraint[] getConstraints() {
		return modifiedConstraints.values().toArray(new Constraint[constraints.size()]);
	}

	public Constraint getConstraintFor(Point p) {
		return modifiedConstraints.get(p);
	}

}
