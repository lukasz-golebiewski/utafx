package utafx.data.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import utafx.data.selection.CellAddress;

public class WorkBookUtil {

    private WorkBookUtil() {
    }

    public static boolean isEmptyCell(Cell cell) {
	return cell == null || cell.toString() == null
		|| cell.toString().trim().isEmpty();
    }

    public static CellAddress getFirstNonEmptyCellAddress(Row row) {
	Iterator<Cell> iter = row.cellIterator();
	while (iter.hasNext()) {
	    Cell c = iter.next();
	    if (c != null) {
		String value = c.toString();
		if (value != null && !value.trim().isEmpty()) {
		    return new CellAddress(c.getRowIndex(), c.getColumnIndex());
		}
	    }
	}
	return null;
    }

    public static CellAddress getLastNonEmptyCellAddress(Row row) {
	Iterator<Cell> iter = row.cellIterator();
	Cell last = null;
	while (iter.hasNext()) {
	    Cell c = iter.next();
	    if (c != null) {
		String value = c.toString();
		if (value != null && !value.trim().isEmpty()) {
		    last = c;
		}
	    }
	}
	return last != null ? new CellAddress(last.getRowIndex(),
		last.getColumnIndex()) : null;
    }

    public static Cell createStringCellWithValue(Row row, int index,
	    String value) {
	if (row != null) {
	    Cell cell = row.createCell(index);
	    cell.setCellType(Cell.CELL_TYPE_STRING);
	    cell.setCellValue(value);
	    return cell;
	} else {
	    throw new IllegalArgumentException("Row cannot be null");
	}
    }

    public static Workbook readExcel(String path) throws IOException {
	try {
	    return new HSSFWorkbook(new FileInputStream(path));
	} catch (IOException e) {
	    throw e;
	} catch (Exception e) {
	    return new XSSFWorkbook(path);
	}
    }

    public static String[] getRowValues(Row row, boolean fromZeroIndex) {
	if (row != null && row.getFirstCellNum() != -1) {
	    int start = fromZeroIndex ? 0 : row.getFirstCellNum();
	    int endPlusOne = row.getLastCellNum();
	    int len = endPlusOne - start;
	    String[] values = new String[len];

	    int i = 0;
	    for (int index = start; index < row.getLastCellNum(); index++) {
		Cell cell = row.getCell(index);
		if (cell != null) {
		    values[i++] = row.getCell(index).toString();
		} else {
		    values[i++] = null;
		}
	    }
	    return values;
	}
	return null;
    }

    public static String[][] getPreview(String filePath, int sheetIndex)
	    throws IOException {
	Workbook wb = readExcel(filePath);
	Sheet sheet = wb.getSheetAt(sheetIndex);
	String[][] preview = null;
	if (sheet != null) {
	    int firstRow = sheet.getFirstRowNum();
	    int lastCol = getLastCol(sheet);
	    int lastRow = sheet.getLastRowNum();
	    int rows = lastRow - firstRow + 1;

	    if (firstRow > -1 && lastCol > -1) {
		preview = new String[rows][lastCol + 1];
		for (int i = 0; i < rows; i++) {
		    Row r = sheet.getRow(i + firstRow);
		    for (int col = 0; col < lastCol+1; col++) {
			Cell cell = r.getCell(col);
			if (!isEmptyCell(cell)) {
			    preview[i][col] = cell.toString();
			}
		    }
		}
	    }
	    return preview;
	} else {
	    throw new RuntimeException("No sheet found at index " + sheetIndex);
	}
    }

    private static int getLastCol(Sheet sheet) {
	int max = -1;
	for (int ind = sheet.getFirstRowNum(); ind < sheet.getLastRowNum(); ind++) {
	    max = Math.max(max,
		    WorkBookUtil.getLastNonEmptyCellAddress(sheet.getRow(ind))
			    .getColumn());
	}
	return max;
    }
}
