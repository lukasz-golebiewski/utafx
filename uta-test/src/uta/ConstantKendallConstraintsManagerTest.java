package uta;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

public class ConstantKendallConstraintsManagerTest {

	private static final double PRECISION = 0.01;

	@Test
	public void testConstantKendallConstraintsManager() {
		IUtaSolver starSolver = new UtaStarSolver(true);

		// Create criteria
		Criterion price = new Criterion("a", 2, false);
		Criterion time = new Criterion("b", 3, false);
		Criterion comfort = new Criterion("c", 3, true);

		List<Criterion> criteria = new ArrayList<Criterion>();
		criteria.add(price);
		criteria.add(time);
		criteria.add(comfort);

		// Create actions
		Alternative rer = new Alternative(new double[] { 3, 10, 1 }, criteria);
		Alternative metro1 = new Alternative(new double[] { 4, 20, 2 }, criteria);
		Alternative metro2 = new Alternative(new double[] { 2, 20, 0 }, criteria);
		Alternative bus = new Alternative(new double[] { 6, 40, 0 }, criteria);
		Alternative taxi = new Alternative(new double[] { 30, 30, 3 }, criteria);

		// Create ranking
		Ranking<Alternative> referenceRank = new Ranking<Alternative>(new double[] { 1, 2, 2, 3, 4 }, rer, metro1, metro2, bus, taxi);

		LinearFunction[] functions = starSolver.solve(referenceRank, criteria, Arrays.asList(rer, metro1, metro2, bus, taxi));

		Ranking<Alternative> finalRank = new RankingUtils().buildRank(functions, new Alternative[] { rer, metro1, metro2, bus, taxi });
		ConstantKendallConstraintsManager tested = new ConstantKendallConstraintsManager(functions, referenceRank, finalRank);

	}

	@Test
	public void testOverallFunctionality_WithoutConstraintsOfBestPoints() {
		IUtaSolver starSolver = new UtaStarSolver();

		// Create criteria
		Criterion price = new Criterion("price", 2, false);
		Criterion time = new Criterion("time", 3, false);
		Criterion comfort = new Criterion("comfort", 3, true);

		List<Criterion> criteria = new ArrayList<Criterion>();
		criteria.add(price);
		criteria.add(time);
		criteria.add(comfort);

		// Create actions
		Alternative rer = new Alternative(new double[] { 3, 10, 1 }, criteria);
		Alternative metro1 = new Alternative(new double[] { 4, 20, 2 }, criteria);
		Alternative metro2 = new Alternative(new double[] { 2, 20, 0 }, criteria);
		Alternative bus = new Alternative(new double[] { 6, 40, 0 }, criteria);
		Alternative taxi = new Alternative(new double[] { 30, 30, 3 }, criteria);

		Alternative[] alts = new Alternative[] { rer, metro1, metro2, bus, taxi };

		// Create ranking
		Ranking<Alternative> referenceRank = new Ranking<Alternative>(new double[] { 1, 2, 2, 3, 4 }, rer, metro1, metro2, bus, taxi);

		LinearFunction[] functions = starSolver.solve(referenceRank, criteria, Arrays.asList(rer, metro1, metro2, bus, taxi));

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

		Assert.assertEquals(0.328, constKendallConstraint.getLowerBound(), PRECISION);
		Assert.assertEquals(stdConstraint.getUpperBound(), constKendallConstraint.getUpperBound());

		constKendallConstraint = tested.getConstraintFor(tested.functions[2].getPoints().get(1));
		Assert.assertEquals(0d, constKendallConstraint.getLowerBound(), PRECISION);
		Assert.assertEquals(0d, constKendallConstraint.getUpperBound(), PRECISION);
		constKendallConstraint = tested.getConstraintFor(tested.functions[2].getPoints().get(2));
		Assert.assertEquals(0d, constKendallConstraint.getLowerBound(), PRECISION);
		Assert.assertEquals(0d, constKendallConstraint.getUpperBound(), PRECISION);

		constKendallConstraint = tested.getConstraintFor(tested.functions[1].getPoints().get(1));
		Assert.assertEquals(0d, constKendallConstraint.getLowerBound(), PRECISION);
		Assert.assertEquals(0.328, constKendallConstraint.getUpperBound(), PRECISION);
		constKendallConstraint = tested.getConstraintFor(tested.functions[1].getPoints().get(2));
		Assert.assertEquals(0.005, constKendallConstraint.getLowerBound(), PRECISION);
		Assert.assertEquals(0.661, constKendallConstraint.getUpperBound(), PRECISION);
	}

	@Test
	public void testOverallFunctionality_OnlyBestPoints() {
		IUtaSolver starSolver = new UtaStarSolver();

		// Create criteria
		Criterion price = new Criterion("price", 2, false);
		Criterion time = new Criterion("time", 3, false);
		Criterion comfort = new Criterion("comfort", 3, true);

		List<Criterion> criteria = new ArrayList<Criterion>();
		criteria.add(price);
		criteria.add(time);
		criteria.add(comfort);

		// Create actions
		Alternative rer = new Alternative(new double[] { 3, 10, 1 }, criteria);
		Alternative metro1 = new Alternative(new double[] { 4, 20, 2 }, criteria);
		Alternative metro2 = new Alternative(new double[] { 2, 20, 0 }, criteria);
		Alternative bus = new Alternative(new double[] { 6, 40, 0 }, criteria);
		Alternative taxi = new Alternative(new double[] { 30, 30, 3 }, criteria);

		Alternative[] alts = new Alternative[] { rer, metro1, metro2, bus, taxi };

		// Create ranking
		Ranking<Alternative> referenceRank = new Ranking<Alternative>(new double[] { 1, 2, 2, 3, 4 }, rer, metro1, metro2, bus, taxi);

		LinearFunction[] functions = starSolver.solve(referenceRank, criteria, Arrays.asList(rer, metro1, metro2, bus, taxi));

		Ranking<Alternative> finalRank = new RankingUtils().buildRank(functions, alts);

		ConstantKendallConstraintsManager tested = new ConstantKendallConstraintsManager(functions, referenceRank, finalRank);
		// StandardConstraintsManager mgr = new
		// StandardConstraintsManager(functions);

		// check that bound has been changed where it should have been:
		// price
		assertEquals(0.33, tested.getConstraintFor(functions[0].getBestPoint()).getLowerBound(), PRECISION);
		assertEquals(0.33, tested.getConstraintFor(functions[0].getBestPoint()).getUpperBound(), PRECISION);
		// time
		// assertEquals(0.66,
		// tested.getConstraintFor(functions[1].getBestPoint()).getLowerBound(),
		// PRECISION);
		assertEquals(0.34, tested.getConstraintFor(functions[1].getBestPoint()).getLowerBound(), PRECISION);
		assertEquals(0.67, tested.getConstraintFor(functions[1].getBestPoint()).getUpperBound(), PRECISION);
		// comfort
		assertEquals(0.00, tested.getConstraintFor(functions[2].getBestPoint()).getLowerBound(), PRECISION);
		assertEquals(0.32, tested.getConstraintFor(functions[2].getBestPoint()).getUpperBound(), PRECISION);

		System.out.println();
	}
}
