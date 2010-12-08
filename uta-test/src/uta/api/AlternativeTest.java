package uta.api;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

public class AlternativeTest {

	@Test
	public void testGetGeneralUtil() {
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
		LinearFunction f2 = new LinearFunction(new double[] { 40, 30, 20, 10 }, new double[] { 0, 0.05, 0.05, 0.1 }, time);
		LinearFunction f3 = new LinearFunction(new double[] { 0, 1, 2, 3 }, new double[] { 0, 0, 0, 0.4 }, comfort);

		double result = rer.getGeneralUtil(new LinearFunction[] { f1, f2, f3 });
		Assert.assertEquals(0.6, result, 0.00000001);

		result = metro1.getGeneralUtil(new LinearFunction[] { f1, f2, f3 });
		Assert.assertEquals(0.55, result, 0.00000001);

	}
}
