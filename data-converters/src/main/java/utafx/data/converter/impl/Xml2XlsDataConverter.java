package utafx.data.converter.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collections;

import javax.xml.bind.JAXBException;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import utafx.data.converter.ConvertType;
import utafx.data.converter.FileFormat;
import utafx.data.converter.PreferenceDataConverter;
import utafx.data.converter.PreferenceManager;
import utafx.data.exception.ConversionException;
import utafx.data.pref.jaxb.Alternative;
import utafx.data.pref.jaxb.Alternatives;
import utafx.data.pref.jaxb.Criteria;
import utafx.data.pref.jaxb.CriteriaType;
import utafx.data.pref.jaxb.Criterion;
import utafx.data.pref.jaxb.Preferences;
import utafx.data.pref.jaxb.RefRank;
import utafx.data.pref.jaxb.RrItem;
import utafx.data.pref.jaxb.Value;
import utafx.data.util.CommonUtil;
import utafx.data.util.WorkBookUtil;

public class Xml2XlsDataConverter implements PreferenceDataConverter {

    // private final Logger LOG = Logger.getLogger(Xml2XlsDataConverter.class);
    private static final String DEFAULT_SHEET_NAME = "Preferences";

    private static final int REF_RANK_COLUMN_IDX = 0;
    private static final int ALT_NAMES_COLUMN_IDX = 1;
    private static final int CRIT_TYPES_ROW_IDX = 0;
    private static final int CRIT_NAMES_ROW_IDX = 1;
    private static final int CRITERIA_START_COLUMN = 2;
    private static final int ALT_VALUES_ROW_START_INDEX = 2;

    private final ConvertType conversionType = new ConvertType(FileFormat.XML,
	    FileFormat.XLS);

    private String sheetName;

    // private SelectionArea selectionArea;

    public Xml2XlsDataConverter() {
	// selectionArea = new SelectionArea(new CellAddress(1, 1));
    }

    public void convert(InputStream input, OutputStream output)
	    throws ConversionException {
	PreferenceManager reader = new PreferenceManager();
	try {
	    Preferences prefs = reader.read(input);
	    Workbook wb = createXls(prefs);
	    saveAndClose(wb, output);
	} catch (JAXBException e) {
	    throw new ConversionException("JAXB error occured", e);
	} catch (IOException e) {
	    throw new ConversionException("I/O error occured", e);
	}
    }

    private void saveAndClose(Workbook wb, OutputStream output)
	    throws IOException {
	wb.write(output);
	output.close();
    }

    private Workbook createXls(Preferences prefs) {
	Workbook wb = createWorkbook();
	String name = getSheetName();
	Sheet sheet = wb.createSheet(name);
	writeData(sheet, prefs);
	return wb;
    }

    private void writeData(Sheet sheet, Preferences prefs) {
	// CellAddress start = getStart();
	writeCriterias(sheet, prefs.getCriteria());
	writeAlternatives(sheet, prefs.getAlternatives());
	writeReferenceRank(sheet, prefs.getRefRank());
    }

    private void writeCriterias(Sheet sheet, Criteria criteria) {
	if (criteria != null && criteria.getCriterion().size() > 0) {
	    Collections.sort(criteria.getCriterion(),
		    CommonUtil.createCriteriaComparator());
	}
	Row typesRow = sheet.createRow(CRIT_TYPES_ROW_IDX);
	Row namesRow = sheet.createRow(CRIT_NAMES_ROW_IDX);

	// blank cell, this is ref rank column
	namesRow.createCell(REF_RANK_COLUMN_IDX);
	// blank cell, this is alternative names column
	namesRow.createCell(ALT_NAMES_COLUMN_IDX);

	int index = CRITERIA_START_COLUMN;
	for (Criterion criterion : criteria.getCriterion()) {
	    createCritNameCell(namesRow, index, criterion);
	    createCritTypeCell(typesRow, index, criterion);
	    index++;
	}
    }

    private void createCritTypeCell(Row row, int index, Criterion criterion) {
	String typeShort = criterion.getType() == CriteriaType.COST ? "c" : "g";
	int segments = criterion.getSegments();
	String strValue = String.format("%s, %d", typeShort, segments);
	WorkBookUtil.createStringCellWithValue(row, index, strValue);
    }

    private void createCritNameCell(Row row, int index, Criterion criterion) {
	WorkBookUtil.createStringCellWithValue(row, index, criterion.getName());
    }

    private void writeAlternatives(Sheet sheet, Alternatives alternatives) {
	int index = ALT_VALUES_ROW_START_INDEX;
	for (Alternative a : alternatives.getAlternative()) {
	    Row aRow = sheet.createRow(index);
	    WorkBookUtil.createStringCellWithValue(aRow, ALT_NAMES_COLUMN_IDX,
		    a.getName());
	    int vIndex = ALT_NAMES_COLUMN_IDX + 1;
	    for (Value v : a.getValues().getValue()) {
		WorkBookUtil.createStringCellWithValue(aRow, vIndex,
			v.getValue());
		vIndex++;
	    }
	    index++;
	}
    }

    private void writeReferenceRank(Sheet sheet, RefRank refRank) {
	final int startRow = ALT_VALUES_ROW_START_INDEX;
	final int col = REF_RANK_COLUMN_IDX;
	for (RrItem item : refRank.getItem()) {
	    int row = startRow + item.getId();
	    Cell rrCell = sheet.getRow(row).createCell(col);
	    rrCell.setCellValue(item.getRank());
	}
    }

    // private CellAddress getStart() {
    // if (selectionArea != null) {
    // CellAddress start = selectionArea.getStart();
    // if (start != null && start.getColumn() > 0 && start.getRow() > 0) {
    // return start;
    // }
    // }
    // // leave one row for criteria types, and one column for reference rank
    // return new CellAddress(1, 1);
    // }

    public String getSheetName() {
	return CommonUtil.isNotEmpty(sheetName) ? sheetName
		: DEFAULT_SHEET_NAME;

    }

    public void setSheetName(String sheetName) {
	this.sheetName = sheetName;
    }

    protected Workbook createWorkbook() {
	return new HSSFWorkbook();
    }

    public ConvertType getConversionType() {
	return this.conversionType;
    }

    public void convert(Preferences preferences, OutputStream output)
	    throws ConversionException {
	try {
	    Workbook wb = createXls(preferences);
	    saveAndClose(wb, output);
	} catch (IOException e) {
	    throw new ConversionException("I/O error occured", e);
	}
    }
}
