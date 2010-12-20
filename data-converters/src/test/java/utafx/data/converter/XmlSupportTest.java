package utafx.data.converter;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.Ignore;
import org.junit.Test;

import utafx.data.converter.impl.CsvDataConverter;
import utafx.data.converter.impl.XlsDataConverter;
import utafx.data.converter.impl.XlsxDataConverter;
import utafx.data.converter.impl.Xml2XlsDataConverter;
import utafx.data.util.CommonUtil;
import utafx.data.util.WorkBookUtil;

public class XmlSupportTest {
    @Test
    public void testConverting_Xml_To_Csv_To_Xml() throws Exception {
	String inputPath = ClassLoader.getSystemResource("xml/simple.xml")
		.getFile();
	String outputPath = "./tmp/simple_xml.csv";
	PreferenceConverter pc = new PreferenceConverter();
	pc.convert(inputPath, outputPath);
	CSVSupportTest csvTest = new CSVSupportTest();
	csvTest.testXmlFile(outputPath, outputPath + ".xml",
		new CsvDataConverter());
    }

    @Test
    public void testConverting_Xml_To_Xls() throws Exception {
	Xml2XlsDataConverter converter = new Xml2XlsDataConverter();
	String inputPath = ClassLoader.getSystemResource("xml/simple.xml")
		.getFile();
	String outputPath = "./tmp/simple_xml.xls";
	testXlsFile(inputPath, outputPath, converter, new XlsDataConverter());
    }

    @Test
    public void testConverting_Xml_To_Xlsx() throws Exception {
	Xml2XlsDataConverter converter = new Xml2XlsDataConverter();
	String inputPath = ClassLoader.getSystemResource("xml/simple.xml")
		.getFile();
	String outputPath = "./tmp/simple_xml.xlsx";
	testXlsFile(inputPath, outputPath, converter, new XlsxDataConverter());
    }

    private void testXlsFile(String inputPath, String outputPath,
	    DataConverter converter, XlsDataConverter checker) throws Exception {
	converter.convert(new FileInputStream(inputPath), new FileOutputStream(
		outputPath));
	Workbook converted = WorkBookUtil.readExcel(outputPath);
	assertNotNull(converted);
	assertTrue(converted.getNumberOfSheets() > 0);
	Sheet sheet = converted.getSheetAt(0);
	assertNotNull(sheet);

	// lets check first row - criteria types
	Row row0 = sheet.getRow(0);
	assertNotNull(row0);
	String[] exp = { null, null, "g, 2", "c, 2", "g, 1", "c, 2", "g, 3",
		"c, 4" };
	assertArrayEquals(exp, WorkBookUtil.getRowValues(row0, true));
	exp = new String[] { "g, 2", "c, 2", "g, 1", "c, 2", "g, 3", "c, 4" };
	assertArrayEquals(exp, WorkBookUtil.getRowValues(row0, false));

	// second row - criteria names
	Row row1 = sheet.getRow(1);
	assertNotNull(row1);
	int firstCritNameIndex = 2;
	String[] actual = WorkBookUtil.getRowValues(row1, true);
	for (int i = 0; i < firstCritNameIndex; i++) {
	    assertTrue(CommonUtil.isEmpty(actual[i]));
	}
	for (int i = firstCritNameIndex; i < actual.length; i++) {
	    int num = i - firstCritNameIndex + 1;
	    assertEquals("Kryterium" + num, actual[i]);
	}

	// alternatives
	assertEquals(10, sheet.getLastRowNum());
	int rowStart = 2;
	int rowEnd = sheet.getLastRowNum();
	int altValueColIndex = 2;
	int altNameCol = 1;
	for (int index = rowStart; index < rowEnd + 1; index++) {
	    Row aRow = sheet.getRow(index);
	    assertNotNull(aRow);
	    String[] aValues = WorkBookUtil.getRowValues(aRow, true);
	    assertEquals("Alternatywa " + (index - rowStart + 1),
		    aValues[altNameCol]);
	    for (int i = altValueColIndex; i < aValues.length; i++) {
		assertEquals(String.format("Wartosc A%d%d",
			(index - rowStart + 1), (i - altValueColIndex + 1)),
			aValues[i]);
	    }
	}

	// reference rank
	Integer[] ranks = new Integer[rowEnd - rowStart + 1];
	ranks[0] = 1;
	ranks[2] = 2;
	ranks[3] = 2;
	ranks[6] = 3;

	for (int i = rowStart; i < rowEnd + 1; i++) {
	    Row r = sheet.getRow(i);
	    Cell cell = r.getCell(0);
	    String cellValue = cell != null ? cell.toString() : null;
	    if (cellValue == null) {
		assertNull(ranks[i - rowStart]);
	    } else {
		assertEquals(ranks[i - rowStart].intValue(),
			(int) (Double.parseDouble(cellValue)));
	    }
	}
    }

    @Test
    @Ignore
    public void testConverting_Xml_To_Ods() {
	fail("not done yet");
    }

    @Test
    public void testConverting_Xml_To_Xls_To_Xml() throws Exception {
	Xml2XlsDataConverter converter = new Xml2XlsDataConverter();
	String inputPath = ClassLoader.getSystemResource("xml/simple.xml")
		.getFile();
	String outputPath = "./tmp/simple_xml.xls";
	testXlsFile(inputPath, outputPath, converter, new XlsDataConverter());
	MSExcelSupportTest xlsTest = new MSExcelSupportTest();
	xlsTest.testXmlFile(outputPath, outputPath + ".xml", null,
		new XlsDataConverter());
    }
}
