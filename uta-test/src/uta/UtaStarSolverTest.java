package uta;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import junit.framework.Assert;

import org.apache.commons.math.optimization.linear.LinearConstraint;
import org.junit.Test;

public class UtaStarSolverTest {

	@Test
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
		Alternative metro3 = new Alternative(new double[] { 2, 20, 0 }, criteria);
		Alternative bus = new Alternative(new double[] { 6, 40, 0 }, criteria);
		Alternative taxi = new Alternative(new double[] { 30, 30, 3 }, criteria);

		double[][] expecteds = new double[3][];
		expecteds[0] = new double[] { 0, 0.5, 0.5 };
		expecteds[1] = new double[] { 0, 0.05, 0.05, 0.1 };
		expecteds[2] = new double[] { 0, 0, 0, 0.4 };

		LinearFunction f1 = new LinearFunction(new double[] { 30, 16, 2 }, new double[] { 0, 0.5, 0.5 }, price);
		LinearFunction f2 = new LinearFunction(new double[] { 40, 30, 20, 10 }, new double[] { 0, 0.05, 0.05, 0.1 },
				time);
		LinearFunction f3 = new LinearFunction(new double[] { 0, 1, 2, 3 }, new double[] { 0, 0, 0, 0.4 }, comfort);

		Ranking<Alternative> result = tested.buildRank(new LinearFunction[] { f1, f2, f3 }, new Alternative[] { metro3,
				bus, taxi, rer, metro2, metro1 });

		Assert.assertEquals(1d, result.getRank(rer));
		Assert.assertEquals(2d, result.getRank(metro1));
		Assert.assertEquals(2d, result.getRank(metro2));
		Assert.assertEquals(2d, result.getRank(metro3));
		Assert.assertEquals(3d, result.getRank(bus));
		Assert.assertEquals(4d, result.getRank(taxi));

	}

	@Test
	public void testGetGeneralUtil() {
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

		double[][] expecteds = new double[3][];
		expecteds[0] = new double[] { 0, 0.5, 0.5 };
		expecteds[1] = new double[] { 0, 0.05, 0.05, 0.1 };
		expecteds[2] = new double[] { 0, 0, 0, 0.4 };

		LinearFunction f1 = new LinearFunction(new double[] { 30, 16, 2 }, new double[] { 0, 0.5, 0.5 }, price);
		LinearFunction f2 = new LinearFunction(new double[] { 40, 30, 20, 10 }, new double[] { 0, 0.05, 0.05, 0.1 },
				time);
		LinearFunction f3 = new LinearFunction(new double[] { 0, 1, 2, 3 }, new double[] { 0, 0, 0, 0.4 }, comfort);

		double result = tested.getGeneralUtil(new LinearFunction[] { f1, f2, f3 }, rer);
		Assert.assertEquals(0.6, result, 0.00000001);

		result = tested.getGeneralUtil(new LinearFunction[] { f1, f2, f3 }, metro1);
		Assert.assertEquals(0.55, result, 0.00000001);

	}

	@Test
	public void testDoPostOptimalAnalysis() {
		UtaStarSolver tested = new UtaStarSolver();

		WVariablesRepresentation[] wReps = new WVariablesRepresentation[5];

		double[][] wCoefficients = new double[][] { new double[] { 1, 0.93 }, new double[] { 1, 1, 1 },
				new double[] { 1, 0, 0 } };
		wReps[0] = new WVariablesRepresentation(wCoefficients, null);

		wCoefficients = new double[][] { new double[] { 1, 0.86 }, new double[] { 1, 1, 0 }, new double[] { 1, 1, 0 } };
		wReps[1] = new WVariablesRepresentation(wCoefficients, null);

		wCoefficients = new double[][] { new double[] { 1, 1 }, new double[] { 1, 1, 0 }, new double[] { 0, 0, 0 } };
		wReps[2] = new WVariablesRepresentation(wCoefficients, null);

		wCoefficients = new double[][] { new double[] { 1, 0.71 }, new double[] { 0, 0, 0 }, new double[] { 0, 0, 0 } };
		wReps[3] = new WVariablesRepresentation(wCoefficients, null);

		wCoefficients = new double[][] { new double[] { 0, 0 }, new double[] { 1, 0, 0 }, new double[] { 0, 1, 1 } };
		wReps[4] = new WVariablesRepresentation(wCoefficients, null);

		// Create criteria
		Criterion price = new Criterion(30, 2, 2);
		Criterion time = new Criterion(40, 10, 3);
		Criterion comfort = new Criterion(0, 3, 3);

		List<Criterion> criteria = new ArrayList<Criterion>();
		criteria.add(price);
		criteria.add(time);
		criteria.add(comfort);

		Alternative rer = new Alternative(new double[] { 3, 10, 1 }, criteria);
		Alternative metro1 = new Alternative(new double[] { 4, 20, 2 }, criteria);
		Alternative metro2 = new Alternative(new double[] { 2, 20, 0 }, criteria);
		Alternative bus = new Alternative(new double[] { 6, 40, 0 }, criteria);
		Alternative taxi = new Alternative(new double[] { 30, 30, 3 }, criteria);

		List<Alternative> alternatives = Arrays.asList(rer, metro1, metro2, bus, taxi);
		List<LinearConstraint> constraints = tested.prepareConstraints(wReps, new Ranking<Alternative>(new double[] {
				1, 2, 2, 3, 4 }, rer, metro1, metro2, bus, taxi));

		final double precision = 0.001;

		LinearFunction[] result = tested.doPostOptimalAnalysis(0, wReps, constraints, alternatives, criteria);
		Double[] vals;

		vals = result[0].getValues();
		assertEquals(0d, vals[0], 0.0001);
		assertEquals(0.387, vals[1], precision);
		assertEquals(0.502, vals[2], precision);

		vals = result[1].getValues();
		assertEquals(0d, vals[0], 0.0001);
		assertEquals(0d, vals[1], 0.0001);
		assertEquals(0.0166, vals[2], precision);
		assertEquals(0.341, vals[3], precision);

		vals = result[2].getValues();
		assertEquals(0d, vals[0], 0.0001);
		assertEquals(0.016, vals[1], precision);
		assertEquals(0.016, vals[2], precision);
		assertEquals(0.155, vals[3], precision);

	}
}
