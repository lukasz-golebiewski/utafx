package utafx.data.converter;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;

import utafx.data.FileUtil;

public class PreferenceConverterTest {

    @Test
    /**
     * smoke test to see if preference (XML) file will be generated from excel file 
     */
    public void testConvertXls() throws Exception {
	String inFile = ClassLoader.getSystemResource("xls/simple.xls").getFile();
	PreferenceConverter pc = new PreferenceConverter();
	String outFile = "./tmp/xls_smoke.xml";
	new File("./tmp").mkdirs();
	File fout = new File(outFile);
	if(fout.exists()){
	    fout.delete();
	}
	pc.convert(inFile, outFile);	
	assertTrue(fout.exists());
	assertTrue(fout.length() > 0);	
    }
    
    
    @Test
    /**
     * smoke test to see if preference (XML) file will be generated from excel 2007 file 
     */
    public void testConvertXls2007() throws Exception {
	String inFile = ClassLoader.getSystemResource("xls/simple.xlsx").getFile();
	PreferenceConverter pc = new PreferenceConverter();
	String outFile = "./tmp/xlsx_smoke.xml";
	FileUtil.delete(outFile);
	FileUtil.createDirectory("./tmp/");
	File fout = new File(outFile);
	if(fout.exists()){
	    fout.delete();
	}
	pc.convert(inFile, outFile);	
	assertTrue(fout.exists());
	assertTrue(fout.length() > 0);	
    }

    @Test
    /**
     * smoke test to see if preference (XML) file will be generated from cvs file 
     */
    public void testConvertCsv() throws Exception {
	String inFile = ClassLoader.getSystemResource("csv/simple.csv").getFile();
	PreferenceConverter pc = new PreferenceConverter();
	String outFile = "./tmp/csv_smoke.xml";
	FileUtil.delete(outFile);
	new File("./tmp").mkdirs();
	pc.convert(inFile, outFile);
	assertTrue(new File(outFile).exists());
	assertTrue(new File(outFile).length() > 0);
    }

    @Test
    /**
     * smoke test to see if excel file will be generated from preference file 
     */
    public void testConvertPref2Xls() throws Exception{
	String inFile = ClassLoader.getSystemResource("xml/simple.xml").getFile();
	PreferenceConverter pc = new PreferenceConverter();
	String outFile = "./tmp/xml_smoke.xls";
	FileUtil.delete(outFile);
	new File("./tmp").mkdirs();
	pc.convert(inFile, outFile);
	assertTrue(new File(outFile).exists());
	assertTrue(new File(outFile).length() > 0);
    }
    
    @Test
    /**
     * smoke test to see if excel file will be generated from preference file 
     */
    public void testConvertPref2Xls2007() {
	fail("not done yet");
    }

    @Test
    /**
     * smoke test to see if excel file will be generated from preference file 
     */
    public void testConvertPref2CSV() {
	fail("not done yet");
    }

    @Test
    /**
     * test if generated excel file can be read and converted back to preference file
     */
    public void testBothDirectionConversionXls() {
	fail("not done yet");
    }
}
