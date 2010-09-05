package uta;

import helpers.AssertHelper;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class FunctionalTest {

	@Test
	public void testSolve01() {
		IUtaSolver starSolver = new UtaStarSolver();

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
	public void myTest01() {
		IUtaSolver starSolver = new UtaStarSolver();

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
		Ranking<Alternative> ranking = new Ranking<Alternative>(new double[] { 1, 2, 3 }, bus, rer, metro1);

		LinearFunction[] actuals = starSolver.solve(ranking, criteria);

		// double[][] expecteds = new double[3][];
		// expecteds[0] = new double[] { 0, 0.5, 0.5 };
		// expecteds[1] = new double[] { 0, 0.05, 0.05, 0.1 };
		// expecteds[2] = new double[] { 0, 0, 0, 0.4 };

		// for (int i = 0; i < expecteds.length; i++) {
		// AssertHelper.assertArraysEqual(expecteds[i], actuals[i].getValues());
		// }

	}
}
