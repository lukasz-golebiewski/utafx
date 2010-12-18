package utafx.data.converter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.Ignore;
import org.junit.Test;

import utafx.data.converter.impl.XlsDataConverter;
import utafx.data.converter.impl.Xml2XlsDataConverter;
import utafx.data.pref.jaxb.Alternative;
import utafx.data.pref.jaxb.Alternatives;
import utafx.data.pref.jaxb.Criteria;
import utafx.data.pref.jaxb.CriteriaType;
import utafx.data.pref.jaxb.Criterion;
import utafx.data.pref.jaxb.Preferences;
import utafx.data.util.WorkBookUtil;

public class XmlSupportTest {
    @Test
    public void testConverting_Xml_To_Csv() {
	fail("not done yet");
    }

    @Test
    public void testConverting_Xml_To_Xls() throws Exception {
	Xml2XlsDataConverter converter = new Xml2XlsDataConverter();
	String inputPath = ClassLoader.getSystemResource("xml/simple.xml")
		.getFile();
	String outputPath = "./tmp/simple_xml.xls";
	testXlsFile(inputPath, outputPath, converter, new XlsDataConverter());
    }

    private void testXlsFile(String inputPath, String outputPath,
	    DataConverter converter, XlsDataConverter checker)  throws Exception{	
	converter.convert(new FileInputStream(inputPath), new FileOutputStream(
		outputPath));
	Workbook converted = WorkBookUtil.readExcel(outputPath);
	assertNotNull(converted);
	
	Sheet sheet = converted.getSheetAt(0);
	
    }

    @Test
    @Ignore
    public void testConverting_Xml_To_Ods() {
	fail("not done yet");
    }
}
