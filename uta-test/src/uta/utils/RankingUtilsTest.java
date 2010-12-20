package uta.utils;

import uta.api.RankingUtils;
import uta.api.LinearFunction;
import uta.api.Alternative;
import uta.api.Ranking;
import uta.api.Criterion;
import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

public class RankingUtilsTest {

	@Test
	public void test1() {
		RankingUtils tested = new RankingUtils();

		Alternative bus = new Alternative(null, null, null);
		Alternative metro1 = new Alternative(null, null, null);
		Alternative rer = new Alternative(null, null, null);

		Ranking<Alternative> rankRef = new Ranking<Alternative>(new double[] { 1, 2, 3 }, bus, rer, metro1);

		Ranking<Alternative> rank2 = new Ranking<Alternative>(new double[] { 1, 2, 3 }, bus, rer, metro1);

		double result = tested.getKendallsCoefficient(rankRef, rank2);
		Assert.assertEquals(result, 1d);

	}

	@Test
	public void test2() {
		RankingUtils tested = new RankingUtils();

		Alternative bus = new Alternative(null, null, null);
		Alternative metro1 = new Alternative(null, null, null);
		Alternative rer = new Alternative(null, null, null);

		Ranking<Alternative> rankRef = new Ranking<Alternative>(new double[] { 1, 2, 3 }, bus, rer, metro1);

		Ranking<Alternative> rank2 = new Ranking<Alternative>(new double[] { 3, 2, 1 }, bus, rer, metro1);

		double result = tested.getKendallsCoefficient(rankRef, rank2);
		Assert.assertEquals(result, -1d);
	}

	@Test
	public void test3_irrelevantOrder() {
		RankingUtils tested = new RankingUtils();

		Alternative bus = new Alternative(null, null, null);
		Alternative metro1 = new Alternative(null, null, null);
		Alternative rer = new Alternative(null, null, null);

		Ranking<Alternative> rankRef = new Ranking<Alternative>(new double[] { 1, 2, 3 }, bus, metro1, rer);

		Ranking<Alternative> rank2 = new Ranking<Alternative>(new double[] { 1, 3, 2 }, bus, rer, metro1);

		double result = tested.getKendallsCoefficient(rankRef, rank2);
		Assert.assertEquals(result, 1d);
	}

	@Test
	public void testBuildRank() {
		RankingUtils tested = new RankingUtils();
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
		LinearFunction f2 = new LinearFunction(new double[] { 40, 30, 20, 10 }, new double[] { 0, 0.05, 0.05, 0.1 }, time);
		LinearFunction f3 = new LinearFunction(new double[] { 0, 1, 2, 3 }, new double[] { 0, 0, 0, 0.4 }, comfort);

		Ranking<Alternative> result = tested.buildRank(new LinearFunction[] { f1, f2, f3 }, new Alternative[] { metro3, bus, taxi, rer,
				metro2, metro1 });

		Assert.assertEquals(1d, result.getRank(rer));
		Assert.assertEquals(2d, result.getRank(metro1));
		Assert.assertEquals(2d, result.getRank(metro2));
		Assert.assertEquals(2d, result.getRank(metro3));
		Assert.assertEquals(3d, result.getRank(bus));
		Assert.assertEquals(4d, result.getRank(taxi));

	}

}
