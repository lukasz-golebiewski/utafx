package utafx.data.util;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.Test;

import utafx.data.selection.CellAddress;

public class WorkBookUtilTest {

    @Test
    public void testIsEmptyCell() {
	Workbook wb = new HSSFWorkbook();

	Cell cell = null;
	assertTrue(WorkBookUtil.isEmptyCell(cell));

	cell = wb.createSheet().createRow(0).createCell(0);
	assertTrue(WorkBookUtil.isEmptyCell(cell));

	cell.setCellValue("");
	assertTrue(WorkBookUtil.isEmptyCell(cell));

	cell.setCellValue("   ");
	assertTrue(WorkBookUtil.isEmptyCell(cell));

	cell.setCellValue(" a ");
	assertFalse(WorkBookUtil.isEmptyCell(cell));

    }

    @Test
    public void testGetFirstNonEmptyCellAddress() {
	Workbook wb = new HSSFWorkbook();
	Sheet sheet = wb.createSheet();
	String[] data;
	Row row = null;

	row = sheet.createRow(0);
	assertEquals(null, WorkBookUtil.getFirstNonEmptyCellAddress(row));

	data = new String[] { "", "", "", "" };
	row = createRow(sheet, 0, data);
	assertEquals(null, WorkBookUtil.getFirstNonEmptyCellAddress(row));

	data = new String[] { " ", "   ", "    ", "      " };
	row = createRow(sheet, 0, data);
	assertEquals(null, WorkBookUtil.getFirstNonEmptyCellAddress(row));

	data = new String[] { "a", "b", "c", "d" };
	row = createRow(sheet, 0, data);
	assertEquals(new CellAddress(0, 0),
		WorkBookUtil.getFirstNonEmptyCellAddress(row));

	data = new String[] { "", "b", "c", "d" };
	row = createRow(sheet, 1, data);
	assertEquals(new CellAddress(1, 1),
		WorkBookUtil.getFirstNonEmptyCellAddress(row));

	data = new String[] { "", " ", "c", "d" };
	row = createRow(sheet, 2, data);
	assertEquals(new CellAddress(2, 2),
		WorkBookUtil.getFirstNonEmptyCellAddress(row));

	data = new String[] { "", " ", "  ", "d" };
	row = createRow(sheet, 3, data);
	assertEquals(new CellAddress(3, 3),
		WorkBookUtil.getFirstNonEmptyCellAddress(row));
    }

    private Row createRow(Sheet sheet, int rowIndex, String[] data) {
	if (data != null) {
	    Row r = sheet.createRow(rowIndex);
	    for (int i = 0; i < data.length; i++) {
		if (data[i] != null) {
		    Cell c = r.createCell(i);
		    c.setCellValue(data[i]);
		}
	    }
	    return r;
	}
	return null;
    }

    @Test
    public void testGetLastNonEmptyCellAddress() {
	Workbook wb = new HSSFWorkbook();
	Sheet sheet = wb.createSheet();
	String[] data;
	Row row = null;

	row = sheet.createRow(0);
	assertEquals(null, WorkBookUtil.getLastNonEmptyCellAddress(row));

	data = new String[] { "", "", "", "" };
	row = createRow(sheet, 0, data);
	assertEquals(null, WorkBookUtil.getLastNonEmptyCellAddress(row));

	data = new String[] { " ", "   ", "    ", "      " };
	row = createRow(sheet, 0, data);
	assertEquals(null, WorkBookUtil.getLastNonEmptyCellAddress(row));

	data = new String[] { "a", "b", "c", "d" };
	row = createRow(sheet, 3, data);
	assertEquals(new CellAddress(3, 3),
		WorkBookUtil.getLastNonEmptyCellAddress(row));

	data = new String[] { "a", "b", "c", "" };
	row = createRow(sheet, 2, data);
	assertEquals(new CellAddress(2, 2),
		WorkBookUtil.getLastNonEmptyCellAddress(row));

	data = new String[] { "a", "b", "", "" };
	row = createRow(sheet, 1, data);
	assertEquals(new CellAddress(1, 1),
		WorkBookUtil.getLastNonEmptyCellAddress(row));

	data = new String[] { "a", "", "", "" };
	row = createRow(sheet, 0, data);
	assertEquals(new CellAddress(0, 0),
		WorkBookUtil.getLastNonEmptyCellAddress(row));
    }

    @Test
    public void testCreateStringCellWithValue() {
	Workbook wb = new HSSFWorkbook();
	Sheet sheet = wb.createSheet();
	Row r = sheet.createRow(0);
	Cell cell = WorkBookUtil.createStringCellWithValue(r, 0, "someValue");
	assertNotNull(cell);
	assertSame(r, cell.getRow());
	assertEquals("someValue", cell.getStringCellValue());
	assertEquals("someValue", cell.toString());
	assertEquals(Cell.CELL_TYPE_STRING, cell.getCellType());
	assertEquals(0, cell.getColumnIndex());
    }

    @Test
    public void testReadExcel() throws IOException {
	String path = ClassLoader.getSystemResource("xls/simple.xls").getFile();
	Workbook wb = WorkBookUtil.readExcel(path);
	assertNotNull(wb);
	assertEquals(1, wb.getNumberOfSheets());
	assertEquals("simple", wb.getSheetAt(0).getSheetName());
	assertEquals(10, wb.getSheetAt(0).getLastRowNum());
    }

    @Test
    public void testGetRowValuesFromZeroIndex() {
	Workbook wb = new HSSFWorkbook();
	Sheet sheet = wb.createSheet();

	String[] data = { null, null, "", " ", "a", "b" };
	Row row = createRow(sheet, 0, data);
	String[] rowData = WorkBookUtil.getRowValues(row, true);
	for (int i = 0; i < rowData.length; i++) {
	    if (data[i] == null) {
		assertNull(rowData[i]);
	    } else {
		assertEquals(data[i], rowData[i]);
	    }
	}
    }

    @Test
    public void testGetRowValuesFromFirstNotEmptyCell() {
	Workbook wb = new HSSFWorkbook();
	Sheet sheet = wb.createSheet();

	String[] data = { null, null, "", " ", "a", "b" };
	Row row = createRow(sheet, 0, data);
	String[] rowData = WorkBookUtil.getRowValues(row, false);
	assertArrayEquals(new String[] { "", " ", "a", "b" }, rowData);
    }
}
