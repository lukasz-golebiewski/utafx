package uta;

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
}
