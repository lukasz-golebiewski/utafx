package uta;

import junit.framework.Assert;

import org.junit.Test;

public class ReferenceRankingTest {

	@Test
	public void testGetSuccessor() {
		double[] ranking = new double[] { 1, 2, 3 };
		Alternative a1, a2, a3;
		a1 = new Alternative();
		a2 = new Alternative();
		a3 = new Alternative();

		Ranking<Alternative> tested = new Ranking<Alternative>(ranking, a1, a2, a3);
		Assert.assertEquals(a2, tested.getSuccessor(a1));
		Assert.assertEquals(a3, tested.getSuccessor(a2));
	}

	@Test
	public void testGetSuccessor2() {
		double[] ranking = new double[] { 1, 3, 2 };
		Alternative a1, a2, a3;
		a1 = new Alternative();
		a2 = new Alternative();
		a3 = new Alternative();

		Ranking<Alternative> tested = new Ranking<Alternative>(ranking, a1, a2, a3);
		Assert.assertEquals(a3, tested.getSuccessor(a1));
		Assert.assertEquals(a2, tested.getSuccessor(a3));
		Assert.assertEquals(null, tested.getSuccessor(a2));
	}

	@Test
	public void testGetSuccessor3() {
		double[] ranking = new double[] { 1, 2, 2 };
		Alternative a1, a2, a3;
		a1 = new Alternative();
		a2 = new Alternative();
		a3 = new Alternative();

		Ranking<Alternative> tested = new Ranking<Alternative>(ranking, a1, a3, a2);
		Assert.assertEquals(a3, tested.getSuccessor(a1));
		Assert.assertEquals(a2, tested.getSuccessor(a3));
		Assert.assertEquals(null, tested.getSuccessor(a2));
	}

	@Test
	public void testGetRank() {
		double[] ranking = new double[] { 1, 2, 2 };
		Alternative a1, a2, a3;
		a1 = new Alternative();
		a2 = new Alternative();
		a3 = new Alternative();

		Ranking<Alternative> tested = new Ranking<Alternative>(ranking, a1, a3, a2);
		Assert.assertEquals(2d, tested.getRank(a3));
		Assert.assertEquals(2d, tested.getRank(a2));
		Assert.assertEquals(1d, tested.getRank(a1));
	}

	@Test
	public void testSameRank() {
		double[] ranking = new double[] { 1, 2, 2 };
		Alternative a1, a2, a3;
		a1 = new Alternative();
		a2 = new Alternative();
		a3 = new Alternative();

		Ranking<Alternative> tested = new Ranking<Alternative>(ranking, a1, a3, a2);
		Assert.assertTrue(tested.sameRank(a2, a3));
		Assert.assertTrue(tested.sameRank(a3, a2));
		Assert.assertFalse(tested.sameRank(a1, a2));
		Assert.assertFalse(tested.sameRank(a1, a3));
	}
}
