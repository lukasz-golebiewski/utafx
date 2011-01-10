package utafx.data.converter.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import utafx.data.converter.ConvertType;
import utafx.data.converter.DataConverter;
import utafx.data.converter.FileFormat;
import utafx.data.exception.ConversionException;
import utafx.data.pref.jaxb.AltValues;
import utafx.data.pref.jaxb.Alternative;
import utafx.data.pref.jaxb.Alternatives;
import utafx.data.pref.jaxb.Criteria;
import utafx.data.pref.jaxb.CriteriaType;
import utafx.data.pref.jaxb.Criterion;
import utafx.data.pref.jaxb.ObjectFactory;
import utafx.data.pref.jaxb.Preferences;
import utafx.data.pref.jaxb.RefRank;
import utafx.data.pref.jaxb.RrItem;
import utafx.data.pref.jaxb.Value;
import utafx.data.selection.CellAddress;
import utafx.data.selection.SelectionArea;
import utafx.data.util.CommonUtil;
import utafx.data.util.WorkBookUtil;

/**
 * Converts Microsoft Excel (97-2003) files to UtaFX preferences file (XML).
 * 
 * @author <a href="mailto:marzec12@poczta.onet.pl">Pawel Solarski</a>
 */
public class XlsDataConverter extends XmlPreferenceDataWriter implements
	DataConverter {

    private static final Logger LOG = Logger.getLogger(XlsDataConverter.class);

    private int sheetNumber;
    private String sheetName;
    private SelectionArea selectionArea;
    private ObjectFactory factory = new ObjectFactory();
    private Map<Integer, Integer> refRankMap = new HashMap<Integer, Integer>();
    /**
     * This member will hold the column index, where first criteria is stored
     */
    private int critFirstColumnIndex = -1;
    private final ConvertType conversionType = new ConvertType(FileFormat.XLS,
	    FileFormat.XML);

    private static final double MINIMUM_MATCH_RATE = 0.9;

    private static final String GAIN_ABBREV_STRING = "g";
    private static final String COST_ABBREV_STRING = "c";

    /**
     * Creates new converter, that will process the data from given sheet index,
     * limited to the provided area selection
     * 
     * @param sheetNo
     *            sheet number
     * @param area
     *            data selection area
     */
    public XlsDataConverter(int sheetNo, SelectionArea sa) {
	if (sheetNo < 0) {
	    throw new IllegalArgumentException(
		    "Sheet number cannot be negative");
	}
	this.sheetNumber = sheetNo;
	this.selectionArea = sa;
    }

    /**
     * Creates new converter, that will process the data from given sheet name,
     * limited to the provided area selection
     * 
     * @param sheetName
     *            sheet name
     * @param area
     *            selection area
     */
    public XlsDataConverter(String sheetName, SelectionArea area) {
	this.sheetName = sheetName;
	this.selectionArea = area;
	this.sheetNumber = -1;
    }

    /**
     * Creates new converter, that will process the data from sheet index 0,
     * limited to the provided area selection
     * 
     * @param area
     *            selection area
     */
    public XlsDataConverter(SelectionArea area) {
	this(0, area);
    }

    /**
     * Creates new converter, that will process data from sheet index 0, based
     * on area that will be dynamically obtained from the source. The selection
     * area will start at the first not empty cell (moving from top-left
     * direction), up to last not empty cell (bottom-right direction).
     * <p>
     * Cell is considered empty if it is null or contains only white spaces
     * </p>
     * 
     * 
     */
    public XlsDataConverter() {
	this(0, null);
    }

    public void convert(InputStream input, OutputStream output)
	    throws ConversionException {
	try {
	    Sheet sheet = getWorkingSheet(input);
	    checkSelectionArea(sheet);
	    Preferences pref = readPreferences(sheet);
	    write(pref, output);
	} catch (IOException e) {
	    throw new ConversionException("I/O error occured", e);
	}
    }

    private Sheet getWorkingSheet(InputStream xls) throws IOException {
	Workbook wb;
	wb = getWorkbook(xls);
	int number = getSheetNumber(wb);
	if (number >= 0) {
	    Sheet sheet = wb.getSheetAt(number);
	    if (sheet != null) {
		return sheet;
	    } else {
		throw new IllegalArgumentException("Sheet \"" + sheetName
			+ "\" not found");
	    }
	} else {
	    throw new IllegalArgumentException(
		    "Sheet name or index must be set");
	}
    }

    protected Workbook getWorkbook(InputStream input) throws IOException {
	return new HSSFWorkbook(input);
    }

    private void checkSelectionArea(Sheet sheet) {
	if (selectionArea == null) {
	    CellAddress start = getStartAddress(sheet);
	    LOG.info("First data cell address set to: "
		    + start.getExcelFormat());
	    CellAddress end = getEndAddress(start, sheet);
	    LOG.info("Last data cell address set to: " + end.getExcelFormat());
	    selectionArea = new SelectionArea(start, end);
	} else if (selectionArea.getEnd() == null) {
	    CellAddress end = getEndAddress(selectionArea.getStart(), sheet);
	    selectionArea.setEnd(end);
	}
    }

    private CellAddress getEndAddress(CellAddress start, Sheet sheet) {
	int startRow = start.getRow();
	int startCol = start.getColumn();
	Row row = sheet.getRow(startRow);
	int endCol = WorkBookUtil.getLastNonEmptyCellAddress(row).getColumn();
	int endRow = startRow;
	for (int index = startRow + 1; index <= sheet.getLastRowNum(); index++) {
	    Row r = sheet.getRow(index);
	    Cell cell = r.getCell(startCol);
	    if (!WorkBookUtil.isEmptyCell(cell)) {
		endRow = cell.getRowIndex();
	    }
	}
	if (endRow > startRow) {
	    return new CellAddress(endRow, endCol);
	} else {
	    return null;
	}
    }

    private boolean isCriteriaCell(Cell cell) {
	String value = cell.getStringCellValue();
	if (value != null) {
	    value = value.trim().toLowerCase();
	    if (value.startsWith(GAIN_ABBREV_STRING)
		    || value.startsWith(COST_ABBREV_STRING)) {
		return true;
	    }
	}
	return false;
    }

    private CellAddress getStartAddress(Sheet sheet) {
	Iterator<Row> rowIter = sheet.iterator();
	while (rowIter.hasNext()) {
	    Row row = rowIter.next();
	    if (isCriteriaTypeRow(row)) {
		int rowStart = row.getRowNum();
		int colStart = WorkBookUtil.getFirstNonEmptyCellAddress(row)
			.getColumn() - 1;
		LOG.info("Found criteria types in row=" + (rowStart + 1));
		if (rowIter.hasNext()) {
		    return new CellAddress(rowStart, colStart);
		} else {
		    LOG.error("Criteria names should be placed in row="
			    + row.getRowNum() + 2);
		    return null;
		}
	    }
	}
	return null;
    }

    private boolean isCriteriaTypeRow(Row row) {
	int start = WorkBookUtil.getFirstNonEmptyCellAddress(row).getColumn();
	int end = WorkBookUtil.getLastNonEmptyCellAddress(row).getColumn();
	if (start == -1 || end == -1) {
	    return false;
	}
	boolean[] matches = new boolean[end - start + 1];
	for (int index = start; index <= end; index++) {
	    Cell cell = row.getCell(index);
	    if (cell != null && isCriteriaCell(cell)) {
		matches[index - start] = true;
	    }
	}
	return countAndDecide(matches);
    }

    private boolean countAndDecide(boolean[] flags) {
	int trueCount = 0;
	for (boolean f : flags) {
	    if (f) {
		trueCount++;
	    }
	}
	return (1.0 * trueCount / flags.length > MINIMUM_MATCH_RATE);
    }

    private Preferences readPreferences(Sheet sheet) {
	Preferences pref = new ObjectFactory().createPreferences();
	Criteria criteria = readCriteria(sheet);
	pref.setCriteria(criteria);
	Alternatives alterns = readAlternatives(sheet);
	pref.setAlternatives(alterns);
	RefRank rr = readRefRank(sheet);
	pref.setRefRank(rr);
	return pref;
    }

    // TODO: refactor
    private Criteria readCriteria(Sheet sheet) {
	Criteria c = factory.createCriteria();
	Row critNameRow = sheet.getRow(selectionArea.getStart().getRow() + 1);
	Row critTypeRow = sheet.getRow(selectionArea.getStart().getRow());
	int startCol = selectionArea.getStart().getColumn();
	int endCol = selectionArea.getEnd().getColumn();

	int id = 0;
	for (int col = startCol; col <= endCol; col++) {
	    Criterion criterion = factory.createCriterion();
	    Cell nameCell = critNameRow.getCell(col);
	    CellAddress currentAddress = new CellAddress(
		    nameCell.getRowIndex(), nameCell.getColumnIndex());
	    String name = nameCell.getStringCellValue();
	    criterion.setName(name);

	    Cell typeCell = critTypeRow.getCell(col);
	    if (typeCell == null) {
		LOG.warn(String
			.format("Could not find criteria type for cell %s=\"%s\". Criterion will not be used.",
				currentAddress.getExcelFormat(), name));
		continue;
	    } else if (critFirstColumnIndex == -1) {
		critFirstColumnIndex = col;
	    }
	    String typeValue = typeCell.toString();
	    CriteriaType type = CommonUtil.getType(typeValue);

	    if (type == null) {
		LOG.warn(String
			.format("Incorrect criteria type for cell %s. Criterion %s will not be used.",
				currentAddress.getExcelFormat(), name));
		continue;
	    }
	    int segments = getSegments(name, typeValue);
	    if (segments < 0) {
		LOG.warn(String
			.format("Incorrect segments value for cell %s. Criterion %s will not be used.",
				currentAddress.getExcelFormat(), name));
		continue;
	    } else {
		criterion.setType(type);
		criterion.setSegments(segments);
		criterion.setId(id++);
		LOG.info(String.format("Found criterion (column %d): %s",
			col + 1, CommonUtil.toString(criterion)));
		c.getCriterion().add(criterion);
	    }
	}
	return c;
    }

    private int getSegments(String name, String typeValue) {
	int value = -1;
	try {
	    value = CommonUtil.getSegments(typeValue);
	} catch (NumberFormatException e) {
	    LOG.error(
		    String.format(
			    "Incorrect segments value: %s. Criterion %s will not be used.",
			    typeValue, name), e);
	}
	return value;
    }

    private Alternatives readAlternatives(Sheet sheet) {
	int startRow = selectionArea.getStart().getRow() + 2;
	int endRow = selectionArea.getEnd().getRow();
	Alternatives alterns = factory.createAlternatives();
	int id = 0;
	for (int r = startRow; r < endRow + 1; r++) {
	    Alternative a = readAlternative(sheet, r);
	    a.setId(id++);
	    alterns.getAlternative().add(a);
	    LOG.info(String.format("Found alternative (row %d): %s", r + 1,
		    CommonUtil.toString(a)));

	    int rank = -1;
	    if ((rank = getRank(sheet, r)) != -1) {
		refRankMap.put(a.getId(), rank);
	    }
	}
	return alterns;
    }

    private int getRank(Sheet sheet, int row) {
	int startCol = selectionArea.getStart().getColumn();
	if (startCol > 0) {
	    Cell rankCell = sheet.getRow(row).getCell(startCol - 1);
	    if (rankCell != null
		    && rankCell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
		int rank = (int) (rankCell.getNumericCellValue());
		if (rank > 0) {
		    if (rank != rankCell.getNumericCellValue()) {
			LOG.warn(String.format(
				"Rounded rank value (cell %s=%f -> %d)",
				new CellAddress(row, startCol - 1)
					.getExcelFormat(), rankCell
					.getNumericCellValue(), rank));
		    } else {
			LOG.info(String.format("Found rank value (cell %s=%d)",
				new CellAddress(row, startCol - 1)
					.getExcelFormat(), rank));
			return rank;
		    }
		}
	    }
	}
	return -1;
    }

    private Alternative readAlternative(Sheet sheet, int rowIndex) {
	int startCol = selectionArea.getStart().getColumn();
	int endCol = selectionArea.getEnd().getColumn();
	int altDescColumnIndex = critFirstColumnIndex - 1;
	Row row = sheet.getRow(rowIndex);
	Alternative a = new Alternative();
	AltValues values = new AltValues();

	int critId = 0;
	for (int c = startCol; c <= endCol; c++) {
	    String cellValue = row.getCell(c).getStringCellValue();
	    if (c == altDescColumnIndex) {
		a.setName(cellValue);
	    } else {
		Value v = new Value();
		v.setId(critId++);
		v.setValue(cellValue);
		values.getValue().add(v);
	    }
	}
	a.setValues(values);
	return a;
    }

    private RefRank readRefRank(Sheet sheet) {
	RefRank rr = factory.createRefRank();
	if (!refRankMap.isEmpty()) {
	    for (Entry<Integer, Integer> entry : refRankMap.entrySet()) {
		RrItem item = new RrItem();
		item.setId(entry.getKey());
		item.setRank(entry.getValue());
		rr.getItem().add(item);
	    }
	}
	return rr;
    }

    private int getSheetNumber(Workbook wb) {
	if (sheetNumber > -1) {
	    return sheetNumber;
	} else if (sheetName != null) {
	    return wb.getSheetIndex(sheetName);
	} else {
	    return -1;
	}
    }

    public SelectionArea getSelectionArea() {
	return selectionArea;
    }

    public void setSelectionData(SelectionArea ds) {
	this.selectionArea = ds;
    }

    public int getSheetNumber() {
	return sheetNumber;
    }

    public String getSheetName() {
	return sheetName;
    }

    public void setSheetNumber(int sheetNumber) {
	this.sheetNumber = sheetNumber;
    }

    public void setSheetName(String sheetName) {
	this.sheetName = sheetName;
    }

    public ConvertType getConversionType() {
	return conversionType;
    }

    public Preferences read(InputStream xls) throws IOException {
	Sheet sheet = getWorkingSheet(xls);
	checkSelectionArea(sheet);
	return readPreferences(sheet);
    }
}
