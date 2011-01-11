package uta.constraint;

import static org.junit.Assert.assertEquals;
import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import uta.api.Alternative;
import uta.api.Criterion;
import uta.api.IUtaSolver;
import uta.api.LinearFunction;
import uta.api.Ranking;
import uta.api.RankingUtils;
import uta.UtaSolverFactory;
import uta.star.UtaStarSolver;

public class ConstantKendallConstraintsManagerTest {

	private static final double PRECISION = 0.001;

	private static IUtaSolver starSolver;

	private static Ranking<Alternative> referenceRank;

	private static Alternative[] alts;

	private static Criterion[] criteria;

	private LinearFunction[] functions;

	@BeforeClass
	public static void init() {
		starSolver = new UtaStarSolver();

		// Create criteria
		Criterion price = new Criterion("price", 2, false);
		Criterion time = new Criterion("time", 3, false);
		Criterion comfort = new Criterion("comfort", 3, true);

		criteria = new Criterion[] { price, time, comfort };

		// Create actions
		Alternative rer = new Alternative(new double[] { 3, 10, 1 }, criteria);
		Alternative metro1 = new Alternative(new double[] { 4, 20, 2 }, criteria);
		Alternative metro2 = new Alternative(new double[] { 2, 20, 0 }, criteria);
		Alternative bus = new Alternative(new double[] { 6, 40, 0 }, criteria);
		Alternative taxi = new Alternative(new double[] { 30, 30, 3 }, criteria);

		alts = new Alternative[] { rer, metro1, metro2, bus, taxi };

		// Create ranking
		referenceRank = new Ranking<Alternative>(new double[] { 1, 2, 2, 3, 4 }, rer, metro1, metro2, bus, taxi);
	}

	@Before
	public void setUp() {
		functions = starSolver.solve(referenceRank, criteria, alts);
	}

	@After
	public void tearDown() {
		functions = null;
	}

	@Test
	public void testOverallFunctionality_WithoutConstraintsOfBestPoints() {
		Ranking<Alternative> finalRank = new RankingUtils().buildRank(functions, alts);

		ConstantKendallConstraintsManager tested = new ConstantKendallConstraintsManager(functions, referenceRank, finalRank);
		StandardConstraintsManager mgr = new StandardConstraintsManager(functions);

		// check that bounds for worst points have not been changed:
		for (LinearFunction linearFunction : functions) {
			Constraint constraint = mgr.getConstraintFor(linearFunction.getPoints().get(0));
			Assert.assertEquals(0d, constraint.getLowerBound());
			Assert.assertEquals(0d, constraint.getUpperBound());

			constraint = tested.getConstraintFor(linearFunction.getPoints().get(0));
			Assert.assertEquals(0d, constraint.getLowerBound());
			Assert.assertEquals(0d, constraint.getUpperBound());
		}

		// check that bound has been changed where it should have been:
		Constraint stdConstraint = mgr.getConstraintFor(mgr.functions[0].getPoints().get(1));
		Constraint constKendallConstraint = tested.getConstraintFor(tested.functions[0].getPoints().get(1));

		Assert.assertEquals(0.332, constKendallConstraint.getLowerBound(), PRECISION);
		Assert.assertEquals(stdConstraint.getUpperBound(), constKendallConstraint.getUpperBound());

		constKendallConstraint = tested.getConstraintFor(tested.functions[2].getPoints().get(1));
		Assert.assertEquals(0d, constKendallConstraint.getLowerBound(), PRECISION);
		Assert.assertEquals(0d, constKendallConstraint.getUpperBound(), PRECISION);
		constKendallConstraint = tested.getConstraintFor(tested.functions[2].getPoints().get(2));
		Assert.assertEquals(0d, constKendallConstraint.getLowerBound(), PRECISION);
		Assert.assertEquals(0d, constKendallConstraint.getUpperBound(), PRECISION);

		constKendallConstraint = tested.getConstraintFor(tested.functions[1].getPoints().get(1));
		Assert.assertEquals(0d, constKendallConstraint.getLowerBound(), PRECISION);
		Assert.assertEquals(0.332, constKendallConstraint.getUpperBound(), PRECISION);
		constKendallConstraint = tested.getConstraintFor(tested.functions[1].getPoints().get(2));
		Assert.assertEquals(0.0005, constKendallConstraint.getLowerBound(), PRECISION);
		Assert.assertEquals(0.666, constKendallConstraint.getUpperBound(), PRECISION);
	}

	@Test
	public void testOverallFunctionality_OnlyBestPoints() {
		Ranking<Alternative> finalRank = new RankingUtils().buildRank(functions, alts);

		ConstantKendallConstraintsManager tested = new ConstantKendallConstraintsManager(functions, referenceRank, finalRank);

		// check that bound has been changed where it should have been:
		// price
		assertEquals(0.333, tested.getConstraintFor(functions[0].getBestPoint()).getLowerBound(), PRECISION);
		assertEquals(0.333, tested.getConstraintFor(functions[0].getBestPoint()).getUpperBound(), PRECISION);
		// time
		// assertEquals(0.34,
		assertEquals(0.666, tested.getConstraintFor(functions[1].getBestPoint()).getLowerBound(), PRECISION);
		assertEquals(0.666, tested.getConstraintFor(functions[1].getBestPoint()).getUpperBound(), PRECISION);
		// comfort
		assertEquals(0.00, tested.getConstraintFor(functions[2].getBestPoint()).getLowerBound(), PRECISION);
		assertEquals(0.332, tested.getConstraintFor(functions[2].getBestPoint()).getUpperBound(), PRECISION);

	}

	@Test
	public void testUpdate_BestPoint() {
		Ranking<Alternative> finalRank = new RankingUtils().buildRank(functions, alts);

		ConstantKendallConstraintsManager tested = new ConstantKendallConstraintsManager(functions, referenceRank, finalRank);

		// check that boundaries has been changed where it should have been:
		// price
		assertEquals(0.333, tested.getConstraintFor(functions[0].getBestPoint()).getLowerBound(), PRECISION);
		assertEquals(0.333, tested.getConstraintFor(functions[0].getBestPoint()).getUpperBound(), PRECISION);
		// time
		// assertEquals(0.34,
		assertEquals(0.666, tested.getConstraintFor(functions[1].getBestPoint()).getLowerBound(), PRECISION);
		assertEquals(0.666, tested.getConstraintFor(functions[1].getBestPoint()).getUpperBound(), PRECISION);
		// comfort
		assertEquals(0.00, tested.getConstraintFor(functions[2].getBestPoint()).getLowerBound(), PRECISION);
		assertEquals(0.332, tested.getConstraintFor(functions[2].getBestPoint()).getUpperBound(), PRECISION);

		functions[2].getBestPoint().setY(0.1);
		tested.update(functions[2], functions[2].getBestPoint());

		assertEquals(0.566, functions[1].getBestPoint().getY(), PRECISION);
		assertEquals(0.1, functions[2].getBestPoint().getY(), PRECISION);
		// check that bound has been changed where it should have been:
		// price
		assertEquals(0.3333, tested.getConstraintFor(functions[0].getBestPoint()).getLowerBound(), PRECISION);
		assertEquals(0.3333, tested.getConstraintFor(functions[0].getBestPoint()).getUpperBound(), PRECISION);
		// time
		// assertEquals(0.34,
		assertEquals(0.566, tested.getConstraintFor(functions[1].getBestPoint()).getLowerBound(), PRECISION);
		assertEquals(0.666, tested.getConstraintFor(functions[1].getBestPoint()).getUpperBound(), PRECISION);
		// comfort
		assertEquals(0.10, tested.getConstraintFor(functions[2].getBestPoint()).getLowerBound(), PRECISION);
		assertEquals(0.332, tested.getConstraintFor(functions[2].getBestPoint()).getUpperBound(), PRECISION);

	}

	@Test
	public void testUpdate_MiddlePoint() {
		Ranking<Alternative> finalRank = new RankingUtils().buildRank(functions, alts);

		ConstantKendallConstraintsManager tested = new ConstantKendallConstraintsManager(functions, referenceRank, finalRank);

		// check that boundaries has been changed where it should have been:
		// price
		assertEquals(0.333, tested.getConstraintFor(functions[0].getBestPoint()).getLowerBound(), PRECISION);
		assertEquals(0.333, tested.getConstraintFor(functions[0].getBestPoint()).getUpperBound(), PRECISION);
		// time
		// assertEquals(0.34,
		assertEquals(0.666, tested.getConstraintFor(functions[1].getBestPoint()).getLowerBound(), PRECISION);
		assertEquals(0.667, tested.getConstraintFor(functions[1].getBestPoint()).getUpperBound(), PRECISION);
		// comfort
		assertEquals(0.00, tested.getConstraintFor(functions[2].getBestPoint()).getLowerBound(), PRECISION);
		assertEquals(0.333, tested.getConstraintFor(functions[2].getBestPoint()).getUpperBound(), PRECISION);

		functions[1].getPoints().get(2).setY(0.1);
		tested.update(functions[1], functions[1].getPoints().get(2));

		// check that bound has been changed where it should have been:
		// price
		assertEquals(0.333, tested.getConstraintFor(functions[0].getBestPoint()).getLowerBound(), PRECISION);
		assertEquals(0.333, tested.getConstraintFor(functions[0].getBestPoint()).getUpperBound(), PRECISION);
		// time
		// assertEquals(0.34,
		assertEquals(0.666, tested.getConstraintFor(functions[1].getBestPoint()).getLowerBound(), PRECISION);
		assertEquals(0.667, tested.getConstraintFor(functions[1].getBestPoint()).getUpperBound(), PRECISION);
		// comfort
		assertEquals(0.00, tested.getConstraintFor(functions[2].getBestPoint()).getLowerBound(), PRECISION);
		assertEquals(0.333, tested.getConstraintFor(functions[2].getBestPoint()).getUpperBound(), PRECISION);

		// check that bounds for worst points have not been changed:
		for (LinearFunction linearFunction : functions) {
			Constraint constraint = tested.getConstraintFor(linearFunction.getPoints().get(0));
			Assert.assertEquals(0d, constraint.getLowerBound());
			Assert.assertEquals(0d, constraint.getUpperBound());
		}

		// check that bound has been changed where it should have been:
		Constraint constKendallConstraint = tested.getConstraintFor(tested.functions[0].getPoints().get(1));
		Assert.assertEquals(0.332, constKendallConstraint.getLowerBound(), PRECISION);

		constKendallConstraint = tested.getConstraintFor(tested.functions[2].getPoints().get(1));
		Assert.assertEquals(0d, constKendallConstraint.getLowerBound(), PRECISION);
		Assert.assertEquals(0d, constKendallConstraint.getUpperBound(), PRECISION);
		constKendallConstraint = tested.getConstraintFor(tested.functions[2].getPoints().get(2));
		Assert.assertEquals(0d, constKendallConstraint.getLowerBound(), PRECISION);
		Assert.assertEquals(0d, constKendallConstraint.getUpperBound(), PRECISION);

		constKendallConstraint = tested.getConstraintFor(tested.functions[1].getPoints().get(1));
		Assert.assertEquals(0d, constKendallConstraint.getLowerBound(), PRECISION);
		Assert.assertEquals(0.099, constKendallConstraint.getUpperBound(), PRECISION);
		constKendallConstraint = tested.getConstraintFor(tested.functions[1].getPoints().get(2));
		Assert.assertEquals(0.001, constKendallConstraint.getLowerBound(), PRECISION);
		Assert.assertEquals(0.666, constKendallConstraint.getUpperBound(), PRECISION);
	}

	@Test
	public void testUpdate_MiddlePoint_SetValueAndRestorePrevious() {
		Ranking<Alternative> finalRank = new RankingUtils().buildRank(functions, alts);

		ConstantKendallConstraintsManager tested = new ConstantKendallConstraintsManager(functions, referenceRank, finalRank);

		Constraint[] constraintsBefore = tested.getConstraints();

		// check that boundaries has been changed where it should have been:
		// price
		assertEquals(0.333, tested.getConstraintFor(functions[0].getBestPoint()).getLowerBound(), PRECISION);
		assertEquals(0.333, tested.getConstraintFor(functions[0].getBestPoint()).getUpperBound(), PRECISION);
		// time
		// assertEquals(0.34,
		assertEquals(0.666, tested.getConstraintFor(functions[1].getBestPoint()).getLowerBound(), PRECISION);
		assertEquals(0.667, tested.getConstraintFor(functions[1].getBestPoint()).getUpperBound(), PRECISION);
		// comfort
		assertEquals(0.00, tested.getConstraintFor(functions[2].getBestPoint()).getLowerBound(), PRECISION);
		assertEquals(0.333, tested.getConstraintFor(functions[2].getBestPoint()).getUpperBound(), PRECISION);

		double previousValue = functions[1].getPoints().get(2).getY();

		functions[1].getPoints().get(2).setY(0.1);
		tested.update(functions[1], functions[1].getPoints().get(2));

		// check that bound has been changed where it should have been:
		// price
		assertEquals(0.333, tested.getConstraintFor(functions[0].getBestPoint()).getLowerBound(), PRECISION);
		assertEquals(0.333, tested.getConstraintFor(functions[0].getBestPoint()).getUpperBound(), PRECISION);
		// time
		// assertEquals(0.34,
		assertEquals(0.666, tested.getConstraintFor(functions[1].getBestPoint()).getLowerBound(), PRECISION);
		assertEquals(0.667, tested.getConstraintFor(functions[1].getBestPoint()).getUpperBound(), PRECISION);
		// comfort
		assertEquals(0.00, tested.getConstraintFor(functions[2].getBestPoint()).getLowerBound(), PRECISION);
		assertEquals(0.333, tested.getConstraintFor(functions[2].getBestPoint()).getUpperBound(), PRECISION);

		// check that bounds for worst points have not been changed:
		for (LinearFunction linearFunction : functions) {
			Constraint constraint = tested.getConstraintFor(linearFunction.getPoints().get(0));
			Assert.assertEquals(0d, constraint.getLowerBound());
			Assert.assertEquals(0d, constraint.getUpperBound());
		}

		// check that bound has been changed where it should have been:
		Constraint constKendallConstraint = tested.getConstraintFor(tested.functions[0].getPoints().get(1));
		Assert.assertEquals(0.332, constKendallConstraint.getLowerBound(), PRECISION);

		constKendallConstraint = tested.getConstraintFor(tested.functions[2].getPoints().get(1));
		Assert.assertEquals(0d, constKendallConstraint.getLowerBound(), PRECISION);
		Assert.assertEquals(0d, constKendallConstraint.getUpperBound(), PRECISION);
		constKendallConstraint = tested.getConstraintFor(tested.functions[2].getPoints().get(2));
		Assert.assertEquals(0d, constKendallConstraint.getLowerBound(), PRECISION);
		Assert.assertEquals(0d, constKendallConstraint.getUpperBound(), PRECISION);

		constKendallConstraint = tested.getConstraintFor(tested.functions[1].getPoints().get(1));
		Assert.assertEquals(0d, constKendallConstraint.getLowerBound(), PRECISION);
		Assert.assertEquals(0.099, constKendallConstraint.getUpperBound(), PRECISION);
		constKendallConstraint = tested.getConstraintFor(tested.functions[1].getPoints().get(2));
		Assert.assertEquals(0.001, constKendallConstraint.getLowerBound(), PRECISION);
		Assert.assertEquals(0.666, constKendallConstraint.getUpperBound(), PRECISION);

		// restore previous value
		functions[1].getPoints().get(2).setY(previousValue);
		tested.update(functions[1], functions[1].getPoints().get(2));

		// price
		assertEquals(0.333, tested.getConstraintFor(functions[0].getBestPoint()).getLowerBound(), PRECISION);
		assertEquals(0.333, tested.getConstraintFor(functions[0].getBestPoint()).getUpperBound(), PRECISION);
		// time
		// assertEquals(0.34,
		assertEquals(0.666, tested.getConstraintFor(functions[1].getBestPoint()).getLowerBound(), PRECISION);
		assertEquals(0.667, tested.getConstraintFor(functions[1].getBestPoint()).getUpperBound(), PRECISION);
		// comfort
		assertEquals(0.00, tested.getConstraintFor(functions[2].getBestPoint()).getLowerBound(), PRECISION);
		assertEquals(0.333, tested.getConstraintFor(functions[2].getBestPoint()).getUpperBound(), PRECISION);

		// check that bound has been changed where it should have been:

		constKendallConstraint = tested.getConstraintFor(tested.functions[0].getPoints().get(1));
		Assert.assertEquals(0.332, constKendallConstraint.getLowerBound(), PRECISION);

		constKendallConstraint = tested.getConstraintFor(tested.functions[2].getPoints().get(1));
		Assert.assertEquals(0d, constKendallConstraint.getLowerBound(), PRECISION);
		Assert.assertEquals(0d, constKendallConstraint.getUpperBound(), PRECISION);
		constKendallConstraint = tested.getConstraintFor(tested.functions[2].getPoints().get(2));
		Assert.assertEquals(0d, constKendallConstraint.getLowerBound(), PRECISION);
		Assert.assertEquals(0d, constKendallConstraint.getUpperBound(), PRECISION);

		constKendallConstraint = tested.getConstraintFor(tested.functions[1].getPoints().get(1));
		Assert.assertEquals(0d, constKendallConstraint.getLowerBound(), PRECISION);
		Assert.assertEquals(0.332, constKendallConstraint.getUpperBound(), PRECISION);
		constKendallConstraint = tested.getConstraintFor(tested.functions[1].getPoints().get(2));
		Assert.assertEquals(0.0005, constKendallConstraint.getLowerBound(), PRECISION);
		Assert.assertEquals(0.666, constKendallConstraint.getUpperBound(), PRECISION);

		//
		Constraint[] constraintsAfter = tested.getConstraints();
		for (int i = 0; i < constraintsBefore.length; i++) {
			Assert.assertEquals(constraintsBefore[i], constraintsAfter[i]);
		}
		// Arrays.deepEquals(constraintsBefore, tested.getConstraints());
	}
}
