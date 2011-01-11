package utafx.data.selection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class CellAddressTest {
    @Test
    public void testCoordinateExcelBased() {
	CellAddress c = new CellAddress(1, "A");
	assertNotNull(c);
	assertEquals(0, c.getRow());
	assertEquals(0, c.getColumn());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCoordinateColumnNameOutOfRange() {
	new CellAddress(1, "IZ");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCoordinateColumnNameTooLong() {
	new CellAddress(1, "aav");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCoordinateColumnNameEmpty() {
	new CellAddress(1, "    ");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCoordinateColumnNameAsNumber() {
	new CellAddress(1, "1");
    }

    @Test
    public void testCoordinateNonNegative() {
	CellAddress c = new CellAddress(0, 0);
	assertNotNull(c);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCoordinateNegativeRow() {
	new CellAddress(-1, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCoordinateNegativeColumn() {
	new CellAddress(0, -1);
    }

    @Test
    public void testGetRow() {
	CellAddress c = new CellAddress(0, 0);
	assertEquals(0, c.getRow());
    }

    @Test
    public void testSetRow() {
	CellAddress c = new CellAddress(0, 0);
	assertEquals(0, c.getRow());
	c.setRow(12);
	assertEquals(12, c.getRow());
    }

    @Test
    public void testGetColumn() {
	CellAddress c = new CellAddress(0, 0);
	assertEquals(0, c.getColumn());
    }

    @Test
    public void testSetColumn() {
	CellAddress c = new CellAddress(0, 0);
	assertEquals(0, c.getColumn());
	c.setColumn(2);
	assertEquals(2, c.getColumn());
    }

    @Test
    public void testToString() {
	CellAddress c = new CellAddress(1, 2);
	assertEquals("[1, 2] (internal)\n[C, 2] (excel)", c.toString());
    }

}
