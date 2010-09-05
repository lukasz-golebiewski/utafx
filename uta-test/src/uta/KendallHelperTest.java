package uta;

import junit.framework.Assert;

import org.junit.Test;

public class KendallHelperTest {

	@Test
	public void test1() {
		KendallHelper tested = new KendallHelper();

		Alternative bus = new Alternative(null, null, null);
		Alternative metro1 = new Alternative(null, null, null);
		Alternative rer = new Alternative(null, null, null);

		Ranking<Alternative> rankRef = new Ranking<Alternative>(new double[] { 1, 2, 3 }, bus, rer, metro1);

		Ranking<Alternative> rank2 = new Ranking<Alternative>(new double[] { 1, 2, 3 }, bus, rer, metro1);

		double result = tested.getCoefficient(rankRef, rank2);
		Assert.assertEquals(result, 1d);

	}

	@Test
	public void test2() {
		KendallHelper tested = new KendallHelper();

		Alternative bus = new Alternative(null, null, null);
		Alternative metro1 = new Alternative(null, null, null);
		Alternative rer = new Alternative(null, null, null);

		Ranking<Alternative> rankRef = new Ranking<Alternative>(new double[] { 1, 2, 3 }, bus, rer, metro1);

		Ranking<Alternative> rank2 = new Ranking<Alternative>(new double[] { 3, 2, 1 }, bus, rer, metro1);

		double result = tested.getCoefficient(rankRef, rank2);
		Assert.assertEquals(result, -1d);
	}

	@Test
	public void test3_irrelevantOrder() {
		KendallHelper tested = new KendallHelper();

		Alternative bus = new Alternative(null, null, null);
		Alternative metro1 = new Alternative(null, null, null);
		Alternative rer = new Alternative(null, null, null);

		Ranking<Alternative> rankRef = new Ranking<Alternative>(new double[] { 1, 2, 3 }, bus, metro1, rer);

		Ranking<Alternative> rank2 = new Ranking<Alternative>(new double[] { 1, 3, 2 }, bus, rer, metro1);

		double result = tested.getCoefficient(rankRef, rank2);
		Assert.assertEquals(result, 1d);
	}

}
