package utafx.data.util;

import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import utafx.data.selection.CellAddress;

public class WorkBookUtil {

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
}
