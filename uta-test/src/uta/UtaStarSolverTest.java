package uta;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

public class UtaStarSolverTest {

	// @Test
	public void testBuildRank() {
		UtaStarSolver tested = new UtaStarSolver();

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

		double[][] expecteds = new double[3][];
		expecteds[0] = new double[] { 0, 0.5, 0.5 };
		expecteds[1] = new double[] { 0, 0.05, 0.05, 0.1 };
		expecteds[2] = new double[] { 0, 0, 0, 0.4 };

		LinearFunction f1 = new LinearFunction(new double[] { 30, 16, 2 }, new double[] { 0, 0.5, 0.5 }, price);
		LinearFunction f2 = new LinearFunction(new double[] { 40, 30, 20, 10 }, new double[] { 0, 0.05, 0.05, 0.1 }, time);
		LinearFunction f3 = new LinearFunction(new double[] { 0, 1, 2, 3 }, new double[] { 0, 0, 0, 0.4 }, comfort);

		Ranking<Alternative> result = tested.buildRank(new LinearFunction[] { f1, f2, f3 }, new Alternative[] { rer, metro1, metro2, bus,
				taxi });

		Assert.assertEquals(1, result.getRank(rer));
		Assert.assertEquals(2, result.getRank(metro1));
		Assert.assertEquals(2, result.getRank(metro2));
		Assert.assertEquals(3, result.getRank(bus));
		Assert.assertEquals(4, result.getRank(taxi));

	}
}
