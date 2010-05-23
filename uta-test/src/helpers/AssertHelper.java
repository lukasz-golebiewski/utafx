package helpers;

import org.junit.Assert;

public class AssertHelper {

	private static double default_precision = 0.01;

	public static void assertArraysEqual(double[] expected, double[] actual, double precision) {
		if (expected.length != actual.length) {
			Assert.fail("Arrays' lengths different");
		}
		for (int i = 0; i < actual.length; i++) {
			if (Math.abs(expected[i] - actual[i]) > precision) {
				Assert.fail("Expected element " + expected[i] + " but was " + actual[i] + " at position [" + i + "]");
			}
		}
	}

	public static void assertArraysEqual(double[] expected, double[] actual) {
		assertArraysEqual(expected, actual, default_precision);
	}
}
