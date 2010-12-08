package uta.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import junit.framework.Assert;

import org.junit.Test;

public class LinearFunctionTest {

	private static final double DELTA = 0.0000001;

	@Test
	public void testGetValue_testOnCharacteristicPoints() {
		LinearFunction tested = new LinearFunction(new double[] { 5, 9, 21 }, new double[] { 0, 0.4, 0.6 }, null);

		Assert.assertEquals(0d, tested.getValueAt(5));
		Assert.assertEquals(0.4, tested.getValueAt(9));
		Assert.assertEquals(0.6, tested.getValueAt(21));
	}

	@Test
	public void testGetValue_testBetweenCharacteristicPoints() {
		LinearFunction tested = new LinearFunction(new double[] { 5, 9, 21 }, new double[] { 0, 0.4, 0.6 }, null);

		Assert.assertEquals(0d, tested.getValueAt(4));
		Assert.assertEquals(0d, tested.getValueAt(5));
		Assert.assertEquals(0.3, tested.getValueAt(8), DELTA);
		Assert.assertEquals(0.2, tested.getValueAt(7), DELTA);
		Assert.assertEquals(0.6d, tested.getValueAt(21), DELTA);
		Assert.assertEquals(0.6d, tested.getValueAt(22), DELTA);

	}

	@Test
	public void testGetValue_testBetweenCharacteristicPoints_Decreasing() {
		LinearFunction tested = new LinearFunction(new double[] { 21, 9, 5 }, new double[] { 0, 0.4, 0.6 }, null);

		Assert.assertEquals(0d, tested.getValueAt(22), DELTA);
		Assert.assertEquals(0d, tested.getValueAt(21), DELTA);
		Assert.assertEquals(0.2, tested.getValueAt(15), DELTA);
		Assert.assertEquals(0.5, tested.getValueAt(7), DELTA);
		Assert.assertEquals(0.55, tested.getValueAt(6), DELTA);
		Assert.assertEquals(0.6, tested.getValueAt(5), DELTA);
		Assert.assertEquals(0.6, tested.getValueAt(4.5), DELTA);
	}

	@Test
	public void testIsIncreasing1() {
		LinearFunction tested = new LinearFunction(new double[] { 21, 9, 5 }, new double[] { 0, 0.4, 0.6 }, null);
		Assert.assertFalse(tested.isIncreasing());
	}

	@Test
	public void testIsIncreasing2() {
		LinearFunction tested = new LinearFunction(new double[] { 9, 9, 15 }, new double[] { 0, 0.4, 0.6 }, null);
		Assert.assertTrue(tested.isIncreasing());
	}

	@Test
	public void testGetBetterNeighbor() {
		LinearFunction tested = new LinearFunction(new double[] { 9, 9, 15 }, new double[] { 0, 0.4, 0.6 }, null);
		assertNull(tested.getBetterNeighbor(tested.getPoints().get(2)));
	}

	@Test
	public void testGetBetterNeighbor02() {
		LinearFunction tested = new LinearFunction(new double[] { 9, 9, 15 }, new double[] { 0, 0.4, 0.6 }, null);
		assertNotNull(tested.getBetterNeighbor(tested.getPoints().get(1)));
		assertEquals(tested.getPoints().get(2), tested.getBetterNeighbor(tested.getPoints().get(1)));
	}

	@Test
	public void testGetWorseNeighbor() {
		LinearFunction tested = new LinearFunction(new double[] { 15, 9, 4 }, new double[] { 0.6, 0.4, 0.3 }, null);
		assertNull(tested.getWorseNeighbor(tested.getPoints().get(0)));
	}

	@Test
	public void testGetWorseNeighbor02() {
		LinearFunction tested = new LinearFunction(new double[] { 15, 14, 11 }, new double[] { 0.6, 0.4, 0.3 }, null);
		Point worseNeighbor = tested.getWorseNeighbor(tested.getPoints().get(1));
		assertNotNull(worseNeighbor);
		assertEquals(tested.getPoints().get(0), worseNeighbor);
	}

	@Test
	public void testCopyingConstructor() {
		LinearFunction f = new LinearFunction(new double[] { 15, 14, 11 }, new double[] { 0.6, 0.4, 0.3 }, new Criterion(1, false));
		LinearFunction copyOfF = new LinearFunction(f);

		assertFalse(f.getCriterion() == copyOfF.getCriterion());
		assertEquals(f.getCriterion(), copyOfF.getCriterion());

		assertFalse(f.getPoints() == copyOfF.getPoints());
		assertFalse(f.getPoints().equals(copyOfF.getPoints()));

	}
}
