package utafx.data.converter.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

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

/**
 * Converts Microsoft Excel (97-2003) files to UtaFX preferences file (XML).
 * 
 * @author <a href="mailto:marzec12@poczta.onet.pl">Pawel Solarski</a>
 */
public class ExcelDataConverter implements DataConverter {

    private int sheetNumber;
    private String sheetName;
    private SelectionArea selectionArea;
    private ObjectFactory factory = new ObjectFactory();
    private Preferences pref;
    private Map<Integer, Integer> refRankMap = new HashMap<Integer, Integer>();
    /**
     * This member will hold the column index, where item name, number or
     * description is stored
     */
    private int descColumnIndex = -1;
    private final ConvertType conversionType = new ConvertType(FileFormat.XLS,
	    FileFormat.XML);
    private static final Logger LOG = Logger
	    .getLogger(ExcelDataConverter.class);
    private static final double MINIMUM_MATCH_RATE = 0.9;

    /**
     * Creates new converter, that will process the data from given sheet index,
     * limited to the provided area selection
     * 
     * @param sheetNo
     *            sheet number
     * @param area
     *            data selection area
     */
    public ExcelDataConverter(int sheetNo, SelectionArea sa) {
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
    public ExcelDataConverter(String sheetName, SelectionArea area) {
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
    public ExcelDataConverter(SelectionArea area) {
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
    public ExcelDataConverter() {
	this(0, null);
    }

    public void convert(InputStream input, OutputStream output)
	    throws ConversionException {
	try {
	    HSSFWorkbook wb = new HSSFWorkbook(input);
	    int number = getSheetNumber(wb);
	    if (number >= 0) {
		HSSFSheet sheet = wb.getSheetAt(number);
		if (sheet != null) {
		    checkSelectionArea(sheet);
		    pref = readPreferences(sheet);
		    save(pref, output);
		} else {
		    throw new ConversionException("Sheet \"" + sheetName
			    + "\" not found");
		}
	    } else {
		throw new ConversionException("Sheet name or index must be set");
	    }
	} catch (IOException e) {
	    throw new ConversionException("I/O error occured", e);
	} catch (JAXBException e) {
	    throw new ConversionException("Error in writing objects", e);
	}
    }

    private void checkSelectionArea(HSSFSheet sheet) {
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

    private CellAddress getEndAddress(CellAddress start, HSSFSheet sheet) {
	int startRow = start.getRow();
	int startCol = start.getColumn();
	HSSFRow row = sheet.getRow(startRow);
	int endCol = getLastNonEmptyCellAddress(row).getColumn();
	int endRow = startRow;
	for (int index = startRow + 1; index <= sheet.getLastRowNum(); index++) {
	    Row r = sheet.getRow(index);
	    Cell cell = r.getCell(startCol);
	    if (!isEmptyCell(cell)) {
		endRow = cell.getRowIndex();
	    }
	}
	if (endRow > startRow) {
	    return new CellAddress(endRow, endCol);
	} else {
	    return null;
	}
    }

    private boolean isEmptyCell(Cell cell) {
	return cell == null || cell.toString() == null
		|| cell.toString().trim().isEmpty();
    }

    private CellAddress getStartAddress(HSSFSheet sheet) {
	Iterator<Row> rowIter = sheet.iterator();
	while (rowIter.hasNext()) {
	    Row row = rowIter.next();
	    if (isCriteriaTypeRow(row)) {
		LOG.info("Found criteria types in row=" + (row.getRowNum() + 1));
		if (rowIter.hasNext()) {
		    return getFirstNonEmptyCellAddress(rowIter.next());
		} else {
		    LOG.error("Criteria names should be placed in row="
			    + row.getRowNum() + 2);
		    return null;
		}
	    }
	}
	return null;
    }

    private CellAddress getFirstNonEmptyCellAddress(Row row) {
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

    private CellAddress getLastNonEmptyCellAddress(Row row) {
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

    private boolean isCriteriaTypeRow(Row row) {
	int start = getFirstNonEmptyCellAddress(row).getColumn();
	int end = getLastNonEmptyCellAddress(row).getColumn();
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

    private boolean countAndDecide(boolean[] matches) {
	int matched = 0;
	for (boolean m : matches) {
	    if (m)
		matched++;
	}
	return (1.0 * matched / matches.length >= MINIMUM_MATCH_RATE);
    }

    private boolean isCriteriaCell(Cell cell) {
	String value = cell.getStringCellValue();
	if (value != null) {
	    value = value.trim().toLowerCase();
	    if (value.startsWith("g") || value.startsWith("c")) {
		return true;
	    }
	}
	return false;
    }

    private void save(Preferences pref, OutputStream output)
	    throws JAXBException {
	JAXBContext context = JAXBContext.newInstance("utafx.data.pref.jaxb");
	Marshaller marshaller = context.createMarshaller();
	marshaller.marshal(pref, output);
    }

    private Preferences readPreferences(HSSFSheet sheet) {
	Preferences pref = new ObjectFactory().createPreferences();
	Criteria criteria = readCriteria(sheet);
	pref.setCriteria(criteria);
	Alternatives alterns = readAlternatives(sheet);
	pref.setAlternatives(alterns);
	RefRank rr = readRefRank(sheet);
	pref.setRefRank(rr);
	return pref;
    }

    private Criteria readCriteria(HSSFSheet sheet) {
	Criteria c = factory.createCriteria();
	HSSFRow critNameRow = sheet.getRow(selectionArea.getStart().getRow());
	HSSFRow critTypeRow = sheet
		.getRow(selectionArea.getStart().getRow() - 1);
	int startCol = selectionArea.getStart().getColumn();
	int endCol = selectionArea.getEnd().getColumn();

	int id = 0;
	for (int col = startCol; col <= endCol; col++) {
	    Criterion criterion = factory.createCriterion();
	    HSSFCell nameCell = critNameRow.getCell(col);
	    CellAddress currentAddress = new CellAddress(
		    nameCell.getRowIndex(), nameCell.getColumnIndex());
	    String name = nameCell.getStringCellValue();
	    criterion.setName(name);

	    HSSFCell typeCell = critTypeRow.getCell(col);
	    if (typeCell == null) {
		LOG.warn(String
			.format("Could not find criteria type for cell %s=%s. Criterion will not be used.",
				currentAddress.getExcelFormat(), name));
		descColumnIndex = col;
		continue;
	    }
	    String typeValue = typeCell.toString();
	    CriteriaType type = getType(typeValue);
	    if (type == null) {
		LOG.warn(String
			.format("Could not find criteria type for cell %s=%s. Criterion will not be used.",
				currentAddress.getExcelFormat(), name));
		continue;
	    }
	    int segments = getSegments(typeValue);
	    criterion.setType(type);
	    criterion.setSegments(segments);
	    criterion.setId(id++);
	    LOG.info(String
		    .format("Found criterion (column %d): Id=%s, Name=\"%s\", type=\"%s\", segments=\"%s\"",
			    col + 1, criterion.getId(), criterion.getName(),
			    criterion.getType().name(), criterion.getSegments()));
	    c.getCriterion().add(criterion);
	}
	return c;
    }

    private CriteriaType getType(String cellValue) {
	if (cellValue != null) {
	    cellValue = cellValue.trim().toLowerCase();
	    if (cellValue.startsWith("g")) {
		return CriteriaType.GAIN;
	    } else if (cellValue.startsWith("c")) {
		return CriteriaType.COST;
	    }
	}
	return null;
    }

    private int getSegments(String typeValue) {
	String[] parts = typeValue.split("\\s*,\\s*");
	if (parts.length == 1) {
	    // return default segments value
	    return 2;
	} else if (parts.length > 1) {
	    try {
		return Integer.parseInt(parts[1]);
	    } catch (NumberFormatException e) {
		LOG.error(
			String.format(
				"Incorrect segments value: %s. Criterion will not be used.",
				parts[1]), e);
		return -1;
	    }
	} else {
	    // something really really wrong happened
	    LOG.error(String.format(
		    "Could not get the segments value from \"%s\"", typeValue));
	    return -1;
	}
    }

    private Alternatives readAlternatives(HSSFSheet sheet) {
	int startRow = selectionArea.getStart().getRow() + 1;
	int endRow = selectionArea.getEnd().getRow();
	Alternatives alterns = factory.createAlternatives();
	int id = 0;
	for (int r = startRow; r <= endRow; r++) {
	    Alternative a = readAlternative(sheet, r);
	    a.setId(id++);
	    LOG.info(String.format("Found alternative (row %d): %s", r+1,
		    getAlternativeStr(a)));
	    alterns.getAlternative().add(a);
	    int rank = -1;
	    if ((rank = getRank(sheet, r)) != -1) {
		refRankMap.put(a.getId(), rank);
	    }
	}
	return alterns;
    }

    private String getAlternativeStr(Alternative a) {
	StringBuilder sb = new StringBuilder();
	sb.append(a.getName());
	sb.append(" - ");
	for (Value v : a.getValues().getValue()) {
	    sb.append(v.getValue());
	    sb.append(", ");
	}
	return sb.toString();
    }

    private int getRank(HSSFSheet sheet, int row) {
	int startCol = selectionArea.getStart().getColumn();
	if (startCol > 0) {
	    HSSFCell rankCell = sheet.getRow(row).getCell(startCol - 1);
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
		    }
		}
	    }
	}
	return -1;
    }

    private Alternative readAlternative(HSSFSheet sheet, int rowIndex) {
	int startCol = selectionArea.getStart().getColumn();
	int endCol = selectionArea.getEnd().getColumn();

	HSSFRow row = sheet.getRow(rowIndex);
	Alternative a = new Alternative();
	AltValues values = new AltValues();

	int critId = 0;
	for (int c = startCol; c <= endCol; c++) {
	    String cellValue = row.getCell(c).getStringCellValue();
	    if (c == descColumnIndex) {
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

    private RefRank readRefRank(HSSFSheet sheet) {
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

    private int getSheetNumber(HSSFWorkbook wb) {
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
}
