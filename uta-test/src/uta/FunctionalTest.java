package uta;

import helpers.AssertHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class FunctionalTest {

	@Test
	public void testSolve01() {
		UtaStarSolver starSolver = new UtaStarSolver(false);

		// Create criteria
		Criterion price = new Criterion(30, 2, 2);
		Criterion time = new Criterion(40, 10, 3);
		Criterion comfort = new Criterion(0, 3, 3);

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
		Ranking<Alternative> ranking = new Ranking<Alternative>(new double[] { 1, 2, 2, 3, 4 }, rer, metro1, metro2, bus, taxi);

		LinearFunction[] actuals = starSolver.solve(ranking, criteria);

		double[][] expecteds = new double[3][];
		expecteds[0] = new double[] { 0, 0.5, 0.5 };
		expecteds[1] = new double[] { 0, 0.05, 0.05, 0.1 };
		expecteds[2] = new double[] { 0, 0, 0, 0.4 };

		for (int i = 0; i < expecteds.length; i++) {
			AssertHelper.assertArraysEqual(expecteds[i], actuals[i].getValues());
		}

	}

	@Test
	public void testSolve02() {
		IUtaSolver starSolver = new UtaStarSolver(false);

		// Create criteria
		Criterion price = new Criterion(2, false);
		Criterion time = new Criterion(3, false);
		Criterion comfort = new Criterion(3, true);

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
		Ranking<Alternative> ranking = new Ranking<Alternative>(new double[] { 1, 2, 2, 3, 4 }, rer, metro1, metro2, bus, taxi);

		LinearFunction[] actuals = starSolver.solve(ranking, criteria, Arrays.asList(rer, metro1, metro2, bus, taxi));

		double[][] expecteds = new double[3][];
		expecteds[0] = new double[] { 0, 0.5, 0.5 };
		expecteds[1] = new double[] { 0, 0.05, 0.05, 0.1 };
		expecteds[2] = new double[] { 0, 0, 0, 0.4 };

		for (int i = 0; i < expecteds.length; i++) {
			AssertHelper.assertArraysEqual(expecteds[i], actuals[i].getValues());
		}

	}

	@Test
	public void testSolve03() {
		IUtaSolver starSolver = new UtaStarSolver(true);

		// Create criteria
		Criterion price = new Criterion(2, false);
		Criterion time = new Criterion(3, false);
		Criterion comfort = new Criterion(3, true);

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
		Ranking<Alternative> ranking = new Ranking<Alternative>(new double[] { 1, 2, 2, 3, 4 }, rer, metro1, metro2, bus, taxi);

		LinearFunction[] actuals = starSolver.solve(ranking, criteria, Arrays.asList(rer, metro1, metro2, bus, taxi));

		double[][] expecteds = new double[3][];
		expecteds[0] = new double[] { 0, 0.333, 0.333 };
		expecteds[1] = new double[] { 0, 0d, 0.333, 0.666 };
		expecteds[2] = new double[] { 0, 0, 0, 0 };

		for (int i = 0; i < expecteds.length; i++) {
			AssertHelper.assertArraysEqual(expecteds[i], actuals[i].getValues());
		}

	}

	@Test
	public void testSolve04() {
		IUtaSolver starSolver = new UtaStarSolver(true);

		// Create criteria
		Criterion g1 = new Criterion(1, true);
		Criterion g2 = new Criterion(2, true);

		List<Criterion> criteria = new ArrayList<Criterion>();
		criteria.add(g1);
		criteria.add(g2);

		// Create actions
		Alternative a1 = new Alternative(new double[] { 4, 6 }, criteria);
		Alternative a2 = new Alternative(new double[] { 5, 5 }, criteria);
		Alternative a3 = new Alternative(new double[] { 6, 4 }, criteria);

		// Create ranking
		Ranking<Alternative> ranking = new Ranking<Alternative>(new double[] { 1, 2, 3 }, a1, a3, a2);

		LinearFunction[] actuals = starSolver.solve(ranking, criteria, Arrays.asList(a1, a3, a2));

		double[][] expecteds = new double[2][];
		expecteds[0] = new double[] { 0, 0.25 };
		expecteds[1] = new double[] { 0, 0d, 0.75 };

		for (int i = 0; i < expecteds.length; i++) {
			AssertHelper.assertArraysEqual(expecteds[i], actuals[i].getValues());
		}

	}

	@Test
	public void testSolve05() {
		IUtaSolver starSolver = new UtaStarSolver(true);

		// Create criteria
		Criterion g1 = new Criterion(1, true);
		Criterion g2 = new Criterion(1, true);

		List<Criterion> criteria = new ArrayList<Criterion>();
		criteria.add(g1);
		criteria.add(g2);

		// Create actions
		Alternative a1 = new Alternative(new double[] { 4, 6 }, criteria);
		Alternative a2 = new Alternative(new double[] { 5, 5 }, criteria);
		Alternative a3 = new Alternative(new double[] { 6, 4 }, criteria);

		// Create ranking
		Ranking<Alternative> ranking = new Ranking<Alternative>(new double[] { 1, 2, 3 }, a1, a3, a2);

		final List<Alternative> asList = Arrays.asList(a1, a3, a2);
		LinearFunction[] actuals = starSolver.solve(ranking, criteria, asList);

		double[][] expecteds = new double[2][];
		expecteds[0] = new double[] { 0, 0.25 };
		expecteds[1] = new double[] { 0, 0.75 };

		for (int i = 0; i < expecteds.length; i++) {
			AssertHelper.assertArraysEqual(expecteds[i], actuals[i].getValues());
		}

	}

}
