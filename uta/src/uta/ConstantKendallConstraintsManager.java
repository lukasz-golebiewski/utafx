package uta;

import java.util.HashMap;
import java.util.Map;

public class ConstantKendallConstraintsManager extends StandardConstraintsManager {

	private double searchPrecision = 0.01;
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
			Constraint constraint = new Constraint(p);
			constraint.setLowerBound(constraints.get(p).getLowerBound());
			constraint.setUpperBound(constraints.get(p).getUpperBound());
			modifiedConstraints.put(p, constraint);
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
		for (Point p : constraints.keySet()) {
			if (!isBestPoint(p)) {
				Constraint c = constraints.get(p);

				double middle = (c.getUpperBound() + p.getY()) / 2;
				double oldMiddle = p.getY();

				while (Math.abs(middle - oldMiddle) > searchPrecision) {
					oldMiddle = middle;
					if (isKendallWorseAtPoint(p, middle)) {
						middle = (middle + p.getY()) / 2;
					} else {
						middle = (middle + c.getUpperBound()) / 2;
					}
				}
				if (Math.abs(middle - c.getUpperBound()) > 0) {
					setNewUpperBound(p, middle);
				}

				middle = (p.getY() + c.getLowerBound()) / 2;
				oldMiddle = p.getY();
				while (Math.abs(middle - oldMiddle) > searchPrecision) {
					oldMiddle = middle;

					if (isKendallWorseAtPoint(p, middle)) {
						middle = (middle + p.getY()) / 2;
					} else {
						middle = (middle + c.getLowerBound()) / 2;
						// middle = middle - p.getY();
					}

				}
				if (Math.abs(middle - c.getLowerBound()) > 0) {
					setNewLowerBound(p, middle);
				}
			}
		}
		// iterate over best points
		for (Point p : constraints.keySet()) {
			if (isBestPoint(p)) {
				// updateBestPointsConstraints(p);
				Constraint c = constraints.get(p);

				double middle = (c.getUpperBound() + p.getY()) / 2;
				double oldMiddle = p.getY();

				while (Math.abs(middle - oldMiddle) > searchPrecision) {
					oldMiddle = middle;
					if (isKendallWorseAtBestPoint(p, middle)) {
						middle = (middle + p.getY()) / 2;
					} else {
						middle = (middle + c.getUpperBound()) / 2;
					}
				}
				if (Math.abs(middle - c.getUpperBound()) > 0) {
					setNewUpperBound(p, middle);
				}

				middle = (p.getY() + c.getLowerBound()) / 2;
				oldMiddle = p.getY();
				while (Math.abs(middle - oldMiddle) > searchPrecision) {
					oldMiddle = middle;

					if (isKendallWorseAtBestPoint(p, middle)) {
						middle = (middle + p.getY()) / 2;
					} else {
						middle = (middle + c.getLowerBound()) / 2;
						// middle = middle - p.getY();
					}

				}
				if (Math.abs(middle - c.getLowerBound()) > 0) {
					setNewLowerBound(p, middle);
				}
			}

		}

	}

	private void setNewLowerBound(Point p, double middle) {
		modifiedConstraints.get(p).setLowerBound(middle);
	}

	private void setNewUpperBound(Point p, double middle) {
		modifiedConstraints.get(p).setUpperBound(middle);
	}

	boolean isKendallWorseAtPoint(Point p, double value) {
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

	boolean isKendallWorseAtBestPoint(Point p, double value) {
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

	}

	public Constraint[] getConstraints() {
		return modifiedConstraints.values().toArray(new Constraint[constraints.size()]);
	}

	public Constraint getConstraintFor(Point p) {
		return modifiedConstraints.get(p);
	}

}
