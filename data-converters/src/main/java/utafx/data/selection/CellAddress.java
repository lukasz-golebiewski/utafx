package utafx.data.selection;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * This class represents the spreadsheet cell address
 * 
 * @author <a href="mailto:marzec12@poczta.onet.pl">Pawel Solarski</a>
 */
public class CellAddress {
    private static final Object MAX_COL_WIDTH = 2;
    private static Map<String, Integer> columnNumbers;
    private static final int MAX_COL_FIRST;
    private static final int MAX_COL_SECOND;
    private int row;
    private int column;    

    static {
	String validStr = "abcdefghijklmnopqrstuvwxyz";
	char[] valid = validStr.toCharArray();
	columnNumbers = new HashMap<String, Integer>(valid.length);
	for (int i = 0; i < valid.length; i++) {
	    columnNumbers.put(String.valueOf(valid[i]), Integer.valueOf(i));
	}
	MAX_COL_FIRST = columnNumbers.get("i");
	MAX_COL_SECOND = columnNumbers.get("v");
    }

    /**
     * @param row
     *            row number, must be &gt;=0
     * @param col
     *            column number, must be &gt;=0
     */
    public CellAddress(int row, int col) {
	if (row > -1 && col > -1) {
	    this.row = row;
	    this.column = col;
	} else {
	    throw new IllegalArgumentException(String.format(
		    "Arguments cannot be negative: row=%d col=%s", row, col));
	}
    }

    /**
     * Create coordinate based on excel format
     * 
     * @param row
     *            row number, must be &gt;=1
     * @param col
     *            column name, must be in [A...IV] range
     */
    public CellAddress(int row, String column) {
	if (row > 0) {
	    this.row = row - 1;
	    this.column = translateColumnName(column);
	} else {
	    throw new IllegalArgumentException(String.format(
		    "Row must be have positive value : row=%d", row));
	}
    }

    private int translateColumnName(String column) {
	checkValidity(column);
	int colNumber = getColumnNumber(column);
	return colNumber;
    }

    private int getColumnNumber(String column) {
	if (column.length() == 1) {
	    return columnNumbers.get(column.toLowerCase());
	} else {
	    return 26
		    * (columnNumbers.get(String.valueOf(column.charAt(0))) + 1)
		    + columnNumbers.get(String.valueOf(column.charAt(1))) - 1;

	}
    }

    private void checkValidity(String column) {
	column = column.trim();
	if (column.isEmpty()) {
	    throw new IllegalArgumentException("Column name cannot be empty");
	}
	if (column.length() > 2) {
	    throw new IllegalArgumentException(String.format(
		    "Column name length=\"%d\" is too high. Max is %d",
		    column.length(), MAX_COL_WIDTH));
	}
	if (!isValidColumnIdentifier(column)) {
	    throw new IllegalArgumentException(
		    String.format(
			    "Column name \"%s\" is not valid column identifier",
			    column));
	}
    }

    private static boolean isValidColumnIdentifier(String column) {
	column = column.trim().toLowerCase();
	int len = column.length();
	if (len == 1) {
	    return columnNumbers.containsKey(column);
	} else if (len == 2) {
	    String first = String.valueOf(column.charAt(0));
	    int firstV = columnNumbers.get(first);
	    String second = String.valueOf(column.charAt(1));
	    int secondV = columnNumbers.get(second);
	    return (firstV <= MAX_COL_FIRST && secondV <= MAX_COL_SECOND);
	} else {
	    return false;
	}
    }

    public int getRow() {
	return row;
    }

    public void setRow(int row) {
	this.row = row;
    }

    public int getColumn() {
	return column;
    }

    public void setColumn(int col) {
	this.column = col;
    }

    @Override
    public String toString() {
	StringBuilder str = new StringBuilder();
	str.append(String.format("[%d, %d] (internal)", row, column));
	str.append(String.format("\n%s (excel)", getExcelFormat()));
	return str.toString();
    }

    public String getExcelFormat() {
	return String.format("[%s, %d]", getColumnString(column), row + 1);
    }

    private String getColumnString(int column) {
	for (Entry<String, Integer> entry : columnNumbers.entrySet()) {
	    if (entry.getValue() == column) {
		return entry.getKey().toUpperCase();
	    }
	}
	return null;
    }
}
