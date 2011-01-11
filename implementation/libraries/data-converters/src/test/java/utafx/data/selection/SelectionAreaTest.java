package utafx.data.selection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class SelectionAreaTest {
    @Test
    public void testCreatingAreaNoArgs() {
	SelectionArea area = new SelectionArea();
	assertNull(area.getEnd());
	assertNotNull(area.getStart());
    }

    @Test
    public void testCreatingAreaWithStartAddress() {
	SelectionArea area = new SelectionArea(new CellAddress(1, 123));
	assertNull(area.getEnd());
	assertNotNull(area.getStart());
	assertEquals(1, area.getStart().getRow());
	assertEquals(123, area.getStart().getColumn());
    }

    @Test
    public void testCreatingAreaWithStartAndEndAddress() {
	SelectionArea area = new SelectionArea(new CellAddress(0, 0),
		new CellAddress(10, 6));
	assertNotNull(area.getStart());
	assertNotNull(area.getEnd());
	assertEquals(0, area.getStart().getRow());
	assertEquals(0, area.getStart().getColumn());
	assertEquals(10, area.getEnd().getRow());
	assertEquals(6, area.getEnd().getColumn());
    }

    @Test
    public void testSettingNewStartAddress() {
	SelectionArea area = new SelectionArea(new CellAddress(0, 0),
		new CellAddress(10, 6));
	area.setStart(new CellAddress(13, 16));
	assertEquals(13, area.getStart().getRow());
	assertEquals(16, area.getStart().getColumn());
    }
    
    @Test
    public void testSettingNewEndAddress() {
	SelectionArea area = new SelectionArea(new CellAddress(0, 0),
		new CellAddress(10, 6));
	area.setEnd(new CellAddress(100, 106));
	assertEquals(100, area.getEnd().getRow());
	assertEquals(106, area.getEnd().getColumn());
    }
}
