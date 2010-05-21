package uta;

import static org.junit.Assert.assertArrayEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class UtaStarSolverTest {

	@Test
	public void testSolve01() {
		IUtaSolver starSolver = new UtaStarSolver();

		// Create criteria
		Criterion price = new Criterion(30, 3, 2);
		Criterion time = new Criterion(40, 10, 3);
		Criterion comfort = new Criterion(0, 3, 3);

		List<Criterion> criteria = new ArrayList<Criterion>();
		criteria.add(price);
		criteria.add(time);
		criteria.add(comfort);

		// Create actions
		Action rer = new Action(new double[] { 3, 10, 1 }, criteria);
		Action metro1 = new Action(new double[] { 4, 20, 2 }, criteria);
		Action metro2 = new Action(new double[] { 2, 20, 0 }, criteria);
		Action bus = new Action(new double[] { 6, 40, 0 }, criteria);
		Action taxi = new Action(new double[] { 30, 30, 3 }, criteria);

		// Create ranking
		ReferenceRanking ranking = new ReferenceRanking(new double[] { 1, 2, 2, 3, 4 }, rer, metro1, metro2, bus, taxi);

		LinearFunction[] actuals = starSolver.solve(ranking, criteria);
		LinearFunction[] expecteds = new LinearFunction[3];

		expecteds[0] = new LinearFunction(new double[] { 30, 16, 2 }, new double[] { 0, 0.5, 0.5 });
		expecteds[1] = new LinearFunction(new double[] { 40, 30, 20, 10 }, new double[] { 0, 0.05, 0.05, 0.1 });
		expecteds[2] = new LinearFunction(new double[] { 0, 1, 2, 3 }, new double[] { 0, 0, 0, 0.4 });

		assertArrayEquals(expecteds, actuals);
	}
}
