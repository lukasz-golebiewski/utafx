package uta.constraint;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import uta.api.LinearFunction;
import uta.api.Point;

public class StandardConstraintsManagerTest {

	private static final double PRECISION = 0.0001;

	@Test
	public void testConstraintsManager_simpleTest() {
		LinearFunction f1 = new LinearFunction(new double[] { 21, 9, 5 }, new double[] { 0, 0.4, 0.6 }, null);
		LinearFunction f2 = new LinearFunction(new double[] { 21, 9, 5 }, new double[] { 0, 0.3, 0.4 }, null);

		LinearFunction[] functions = new LinearFunction[] { f1, f2 };

		ConstraintsManager tested = new StandardConstraintsManager(functions);

		// check f1 constraints:
		List<Point> points = f1.getPoints();
		Constraint c = tested.getConstraintFor(points.get(0));
		assertEquals(0, c.getLowerBound(), PRECISION);
		assertEquals(0, c.getUpperBound(), PRECISION);

		c = tested.getConstraintFor(points.get(1));
		assertEquals(0, c.getLowerBound(), PRECISION);
		assertEquals(0.6, c.getUpperBound(), PRECISION);

		c = tested.getConstraintFor(points.get(2));
		assertEquals(0.4, c.getLowerBound(), PRECISION);
		assertEquals(0.7, c.getUpperBound(), PRECISION);

		// check f2 constraints:
		points = f2.getPoints();
		c = tested.getConstraintFor(points.get(0));
		assertEquals(0, c.getLowerBound(), PRECISION);
		assertEquals(0, c.getUpperBound(), PRECISION);

		c = tested.getConstraintFor(points.get(1));
		assertEquals(0, c.getLowerBound(), PRECISION);
		assertEquals(0.4, c.getUpperBound(), PRECISION);

		c = tested.getConstraintFor(points.get(2));
		assertEquals(0.3, c.getLowerBound(), PRECISION);
		assertEquals(0.6, c.getUpperBound(), PRECISION);
	}

	@Test
	public void testConstraintsManager_sophisticatedTest() {
		LinearFunction f1 = new LinearFunction(new double[] { 21, 9 }, new double[] { 0, 0.3 }, null);
		LinearFunction f2 = new LinearFunction(new double[] { 21, 9, 5 }, new double[] { 0, 0.3, 0.4 }, null);
		LinearFunction f3 = new LinearFunction(new double[] { 2, 9, 15 }, new double[] { 0.1, 0.3, 0.3 }, null);

		LinearFunction[] functions = new LinearFunction[] { f1, f2, f3 };

		ConstraintsManager tested = new StandardConstraintsManager(functions);

		// check f1 constraints:
		List<Point> points = f1.getPoints();
		Constraint c = tested.getConstraintFor(points.get(0));
		assertEquals(0, c.getLowerBound(), PRECISION);
		assertEquals(0, c.getUpperBound(), PRECISION);

		c = tested.getConstraintFor(points.get(1));
		assertEquals(0, c.getLowerBound(), PRECISION);
		assertEquals(0.4, c.getUpperBound(), PRECISION);

		// check f2 constraints:
		points = f2.getPoints();
		c = tested.getConstraintFor(points.get(0));
		assertEquals(0, c.getLowerBound(), PRECISION);
		assertEquals(0, c.getUpperBound(), PRECISION);

		c = tested.getConstraintFor(points.get(1));
		assertEquals(0, c.getLowerBound(), PRECISION);
		assertEquals(0.4, c.getUpperBound(), PRECISION);

		c = tested.getConstraintFor(points.get(2));
		assertEquals(0.3, c.getLowerBound(), PRECISION);
		assertEquals(0.7, c.getUpperBound(), PRECISION);

		// check f3 constraints:
		points = f3.getPoints();
		c = tested.getConstraintFor(points.get(0));
		assertEquals(0, c.getLowerBound(), PRECISION);
		assertEquals(0, c.getUpperBound(), PRECISION);

		c = tested.getConstraintFor(points.get(1));
		assertEquals(0.1, c.getLowerBound(), PRECISION);
		assertEquals(0.3, c.getUpperBound(), PRECISION);

		c = tested.getConstraintFor(points.get(2));
		assertEquals(0.3, c.getLowerBound(), PRECISION);
		assertEquals(0.7, c.getUpperBound(), PRECISION);
	}

	@Test
	public void testUpdate_increaseBest() {
		LinearFunction f1 = new LinearFunction(new double[] { 21, 9 }, new double[] { 0, 0.3 }, null);
		LinearFunction f2 = new LinearFunction(new double[] { 21, 9, 5 }, new double[] { 0, 0.3, 0.4 }, null);
		LinearFunction f3 = new LinearFunction(new double[] { 2, 9, 15 }, new double[] { 0.0, 0.3, 0.3 }, null);

		LinearFunction[] functions = new LinearFunction[] { f1, f2, f3 };

		ConstraintsManager tested = new StandardConstraintsManager(functions);

		Point f2BestPoint = f2.getBestPoint();

		f2BestPoint.setY(0.6);
		tested.update(f2, f2BestPoint);

		// check points' values:
		assertEquals(0.1, f1.getBestPoint().getY(), PRECISION);
		assertEquals(0.6, f2BestPoint.getY(), PRECISION);
		assertEquals(0.3, f3.getBestPoint().getY(), PRECISION);

		// check f1 constraints:
		List<Point> points = f1.getPoints();
		Constraint c = tested.getConstraintFor(points.get(0));
		assertEquals(0, c.getLowerBound(), PRECISION);
		assertEquals(0, c.getUpperBound(), PRECISION);

		c = tested.getConstraintFor(points.get(1));
		assertEquals(0, c.getLowerBound(), PRECISION);
		assertEquals(0.4, c.getUpperBound(), PRECISION);

		// check f2 constraints:
		points = f2.getPoints();
		c = tested.getConstraintFor(points.get(0));
		assertEquals(0, c.getLowerBound(), PRECISION);
		assertEquals(0, c.getUpperBound(), PRECISION);

		c = tested.getConstraintFor(points.get(1));
		assertEquals(0, c.getLowerBound(), PRECISION);
		assertEquals(0.6, c.getUpperBound(), PRECISION);

		c = tested.getConstraintFor(points.get(2));
		assertEquals(0.3, c.getLowerBound(), PRECISION);
		assertEquals(0.7, c.getUpperBound(), PRECISION);

		// check f3 constraints:
		points = f3.getPoints();
		c = tested.getConstraintFor(points.get(0));
		assertEquals(0, c.getLowerBound(), PRECISION);
		assertEquals(0, c.getUpperBound(), PRECISION);

		c = tested.getConstraintFor(points.get(1));
		assertEquals(0.0, c.getLowerBound(), PRECISION);
		assertEquals(0.3, c.getUpperBound(), PRECISION);

		c = tested.getConstraintFor(points.get(2));
		assertEquals(0.3, c.getLowerBound(), PRECISION);
		assertEquals(0.7, c.getUpperBound(), PRECISION);

	}

	@Test
	public void testUpdate_increaseBest02() {
		LinearFunction f1 = new LinearFunction(new double[] { 21, 9 }, new double[] { 0, 0.3 }, null);
		LinearFunction f2 = new LinearFunction(new double[] { 21, 9, 5 }, new double[] { 0, 0.3, 0.4 }, null);
		LinearFunction f3 = new LinearFunction(new double[] { 2, 9, 15 }, new double[] { 0.0, 0.2, 0.3 }, null);

		LinearFunction[] functions = new LinearFunction[] { f1, f2, f3 };

		ConstraintsManager tested = new StandardConstraintsManager(functions);

		Point f2BestPoint = f2.getBestPoint();

		f2BestPoint.setY(0.6);
		tested.update(f2, f2BestPoint);

		// check points' values:
		assertEquals(0.2, f1.getBestPoint().getY(), PRECISION);
		assertEquals(0.6, f2BestPoint.getY(), PRECISION);
		assertEquals(0.2, f3.getBestPoint().getY(), PRECISION);

		// check f1 constraints:
		List<Point> points = f1.getPoints();
		Constraint c = tested.getConstraintFor(points.get(0));
		assertEquals(0, c.getLowerBound(), PRECISION);
		assertEquals(0, c.getUpperBound(), PRECISION);

		c = tested.getConstraintFor(points.get(1));
		assertEquals(0, c.getLowerBound(), PRECISION);
		assertEquals(0.5, c.getUpperBound(), PRECISION);

		// check f2 constraints:
		points = f2.getPoints();
		c = tested.getConstraintFor(points.get(0));
		assertEquals(0, c.getLowerBound(), PRECISION);
		assertEquals(0, c.getUpperBound(), PRECISION);

		c = tested.getConstraintFor(points.get(1));
		assertEquals(0, c.getLowerBound(), PRECISION);
		assertEquals(0.6, c.getUpperBound(), PRECISION);

		c = tested.getConstraintFor(points.get(2));
		assertEquals(0.6, points.get(2).getY(), PRECISION);
		assertEquals(0.3, c.getLowerBound(), PRECISION);
		assertEquals(0.8, c.getUpperBound(), PRECISION);

		// check f3 constraints:
		points = f3.getPoints();
		c = tested.getConstraintFor(points.get(0));
		assertEquals(0, c.getLowerBound(), PRECISION);
		assertEquals(0, c.getUpperBound(), PRECISION);

		c = tested.getConstraintFor(points.get(1));
		assertEquals(0.0, c.getLowerBound(), PRECISION);
		assertEquals(0.2, c.getUpperBound(), PRECISION);

		c = tested.getConstraintFor(points.get(2));
		assertEquals(0.2, c.getLowerBound(), PRECISION);
		assertEquals(0.7, c.getUpperBound(), PRECISION);

	}

	@Test
	public void testUpdate_decreaseBest() {
		LinearFunction f1 = new LinearFunction(new double[] { 21, 9 }, new double[] { 0, 0.3 }, null);
		LinearFunction f2 = new LinearFunction(new double[] { 21, 9, 5 }, new double[] { 0, 0.3, 0.4 }, null);
		LinearFunction f3 = new LinearFunction(new double[] { 2, 9, 15 }, new double[] { 0.0, 0.2, 0.3 }, null);

		LinearFunction[] functions = new LinearFunction[] { f1, f2, f3 };

		ConstraintsManager tested = new StandardConstraintsManager(functions);

		Point f2BestPoint = f2.getBestPoint();

		f2BestPoint.setY(0.35);
		tested.update(f2, f2BestPoint);

		// check points' values:
		assertEquals(0.325, f1.getBestPoint().getY(), PRECISION);
		assertEquals(0.35, f2BestPoint.getY(), PRECISION);
		assertEquals(0.325, f3.getBestPoint().getY(), PRECISION);

		// check f1 constraints:
		List<Point> points = f1.getPoints();
		Constraint c = tested.getConstraintFor(points.get(0));
		assertEquals(0, c.getLowerBound(), PRECISION);
		assertEquals(0, c.getUpperBound(), PRECISION);

		c = tested.getConstraintFor(points.get(1));
		assertEquals(0, c.getLowerBound(), PRECISION);
		assertEquals(0.5, c.getUpperBound(), PRECISION);

		// check f2 constraints:
		points = f2.getPoints();
		c = tested.getConstraintFor(points.get(0));
		assertEquals(0, c.getLowerBound(), PRECISION);
		assertEquals(0, c.getUpperBound(), PRECISION);

		c = tested.getConstraintFor(points.get(1));
		assertEquals(0, c.getLowerBound(), PRECISION);
		assertEquals(0.35, c.getUpperBound(), PRECISION);

		c = tested.getConstraintFor(points.get(2));
		assertEquals(0.35, f2.getBestPoint().getY(), PRECISION);
		assertEquals(0.3, c.getLowerBound(), PRECISION);
		assertEquals(0.8, c.getUpperBound(), PRECISION);

		// check f3 constraints:
		points = f3.getPoints();
		c = tested.getConstraintFor(points.get(0));
		assertEquals(0, c.getLowerBound(), PRECISION);
		assertEquals(0, c.getUpperBound(), PRECISION);

		c = tested.getConstraintFor(points.get(1));
		assertEquals(0.0, c.getLowerBound(), PRECISION);
		assertEquals(0.325, c.getUpperBound(), PRECISION);

		c = tested.getConstraintFor(points.get(2));
		assertEquals(0.2, c.getLowerBound(), PRECISION);
		assertEquals(0.7, c.getUpperBound(), PRECISION);

		assertEquals(0.325, f3.getBestPoint().getY(), PRECISION);
	}

	@Test
	public void testUpdate_increaseBest03() {
		LinearFunction f1 = new LinearFunction(new double[] { 21, 9 }, new double[] { 0, 0.3 }, null);
		LinearFunction f2 = new LinearFunction(new double[] { 21, 9, 5 }, new double[] { 0, 0.1, 0.4 }, null);
		LinearFunction f3 = new LinearFunction(new double[] { 2, 9, 15 }, new double[] { 0.0, 0.2, 0.3 }, null);

		LinearFunction[] functions = new LinearFunction[] { f1, f2, f3 };

		ConstraintsManager tested = new StandardConstraintsManager(functions);

		Point f2BestPoint = f2.getBestPoint();

		f2BestPoint.setY(0.7);
		tested.update(f2, f2BestPoint);

		// check points' values:
		assertEquals(0.1, f1.getBestPoint().getY(), PRECISION);
		assertEquals(0.7, f2BestPoint.getY(), PRECISION);
		assertEquals(0.2, f3.getBestPoint().getY(), PRECISION);
	}

	@Test
	public void testUpdate_increaseBest04() {
		LinearFunction f1 = new LinearFunction(new double[] { 21, 9 }, new double[] { 0, 0.1 }, null);
		LinearFunction f2 = new LinearFunction(new double[] { 21, 9, 5 }, new double[] { 0, 0.1, 0.4 }, null);
		LinearFunction f3 = new LinearFunction(new double[] { 2, 9, 15 }, new double[] { 0.0, 0.2, 0.3 }, null);
		LinearFunction f4 = new LinearFunction(new double[] { 2, 9, 15 }, new double[] { 0.0, 0.2, 0.2 }, null);

		LinearFunction[] functions = new LinearFunction[] { f1, f2, f3, f4 };

		ConstraintsManager tested = new StandardConstraintsManager(functions);

		Point f2BestPoint = f2.getBestPoint();

		f2BestPoint.setY(0.6);
		tested.update(f2, f2BestPoint);

		// check points' values:
		assertEquals(0.0, f1.getBestPoint().getY(), PRECISION);
		assertEquals(0.6, f2BestPoint.getY(), PRECISION);
		assertEquals(0.2, f3.getBestPoint().getY(), PRECISION);
		assertEquals(0.2, f4.getBestPoint().getY(), PRECISION);
	}

	@Test
	public void testUpdate_increaseMiddle() {
		LinearFunction f1 = new LinearFunction(new double[] { 21, 9 }, new double[] { 0, 0.3 }, null);
		LinearFunction f2 = new LinearFunction(new double[] { 21, 9, 5 }, new double[] { 0, 0.3, 0.4 }, null);
		LinearFunction f3 = new LinearFunction(new double[] { 2, 9, 15 }, new double[] { 0.0, 0.3, 0.3 }, null);

		LinearFunction[] functions = new LinearFunction[] { f1, f2, f3 };

		ConstraintsManager tested = new StandardConstraintsManager(functions);

		Point f2MiddlePoint = f2.getPoints().get(1);

		f2MiddlePoint.setY(0.35);
		tested.update(f2, f2MiddlePoint);

		// check points' values:
		assertEquals(0.3, f1.getBestPoint().getY(), PRECISION);
		assertEquals(0.4, f2.getBestPoint().getY(), PRECISION);
		assertEquals(0.3, f3.getBestPoint().getY(), PRECISION);

		// check f1 constraints:
		List<Point> points = f1.getPoints();
		Constraint c = tested.getConstraintFor(points.get(0));
		assertEquals(0, c.getLowerBound(), PRECISION);
		assertEquals(0, c.getUpperBound(), PRECISION);

		c = tested.getConstraintFor(points.get(1));
		assertEquals(0, c.getLowerBound(), PRECISION);
		assertEquals(0.35, c.getUpperBound(), PRECISION);

		// check f2 constraints:
		points = f2.getPoints();
		c = tested.getConstraintFor(points.get(0));
		assertEquals(0, c.getLowerBound(), PRECISION);
		assertEquals(0, c.getUpperBound(), PRECISION);

		c = tested.getConstraintFor(points.get(1));
		assertEquals(0, c.getLowerBound(), PRECISION);
		assertEquals(0.4, c.getUpperBound(), PRECISION);

		c = tested.getConstraintFor(points.get(2));
		assertEquals(0.35, c.getLowerBound(), PRECISION);
		assertEquals(0.7, c.getUpperBound(), PRECISION);

		// check f3 constraints:
		points = f3.getPoints();
		c = tested.getConstraintFor(points.get(0));
		assertEquals(0, c.getLowerBound(), PRECISION);
		assertEquals(0, c.getUpperBound(), PRECISION);

		c = tested.getConstraintFor(points.get(1));
		assertEquals(0.0, c.getLowerBound(), PRECISION);
		assertEquals(0.3, c.getUpperBound(), PRECISION);

		c = tested.getConstraintFor(points.get(2));
		assertEquals(0.3, c.getLowerBound(), PRECISION);
		assertEquals(0.65, c.getUpperBound(), PRECISION);

	}

	@Test
	public void testUpdate_decreaseMiddle4Segments() {
		LinearFunction f1 = new LinearFunction(new double[] { 21, 9 }, new double[] { 0, 0.3 }, null);
		LinearFunction f2 = new LinearFunction(new double[] { 21, 9, 5, 1 }, new double[] { 0, 0.3, 0.4, 0.4 }, null);
		LinearFunction f3 = new LinearFunction(new double[] { 2, 9, 15 }, new double[] { 0.0, 0.3, 0.3 }, null);

		LinearFunction[] functions = new LinearFunction[] { f1, f2, f3 };

		ConstraintsManager tested = new StandardConstraintsManager(functions);

		Point f2MiddlePoint = f2.getPoints().get(2);

		f2MiddlePoint.setY(0.35);
		tested.update(f2, f2MiddlePoint);

		// check points' values:
		assertEquals(0.3, f1.getBestPoint().getY(), PRECISION);
		assertEquals(0.4, f2.getBestPoint().getY(), PRECISION);
		assertEquals(0.3, f3.getBestPoint().getY(), PRECISION);

		// check f1 constraints:
		List<Point> points = f1.getPoints();
		Constraint c = tested.getConstraintFor(points.get(0));
		assertEquals(0, c.getLowerBound(), PRECISION);
		assertEquals(0, c.getUpperBound(), PRECISION);

		c = tested.getConstraintFor(points.get(1));
		assertEquals(0, c.getLowerBound(), PRECISION);
		assertEquals(0.35, c.getUpperBound(), PRECISION);

		// check f2 constraints:
		points = f2.getPoints();
		c = tested.getConstraintFor(points.get(0));
		assertEquals(0, c.getLowerBound(), PRECISION);
		assertEquals(0, c.getUpperBound(), PRECISION);

		c = tested.getConstraintFor(points.get(1));
		assertEquals(0, c.getLowerBound(), PRECISION);
		assertEquals(0.35, c.getUpperBound(), PRECISION);

		c = tested.getConstraintFor(points.get(2));
		assertEquals(0.3, c.getLowerBound(), PRECISION);
		assertEquals(0.4, c.getUpperBound(), PRECISION);

		c = tested.getConstraintFor(points.get(3));
		assertEquals(0.35, c.getLowerBound(), PRECISION);
		assertEquals(0.7, c.getUpperBound(), PRECISION);

		// check f3 constraints:
		points = f3.getPoints();
		c = tested.getConstraintFor(points.get(0));
		assertEquals(0, c.getLowerBound(), PRECISION);
		assertEquals(0, c.getUpperBound(), PRECISION);

		c = tested.getConstraintFor(points.get(1));
		assertEquals(0.0, c.getLowerBound(), PRECISION);
		assertEquals(0.3, c.getUpperBound(), PRECISION);

		c = tested.getConstraintFor(points.get(2));
		assertEquals(0.3, c.getLowerBound(), PRECISION);
		assertEquals(0.65, c.getUpperBound(), PRECISION);

	}
}
