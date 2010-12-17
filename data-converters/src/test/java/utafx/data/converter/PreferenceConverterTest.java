package utafx.data.converter;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;

public class PreferenceConverterTest {

    @Test
    /**
     * smoke test to see if preference (XML) file will be generated from excel file 
     */
    public void testConvertXls() throws Exception {
	String inFile = ClassLoader.getSystemResource("xls/simple.xls").getFile();
	PreferenceConverter pc = new PreferenceConverter();
	String outFile = "./tmp/xls_simple.xml";
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
     * smoke test to see if preference (XML) file will be generated from cvs file 
     */
    public void testConvertCsv() throws Exception {
	String inFile = ClassLoader.getSystemResource("csv/simple.csv").getFile();
	PreferenceConverter pc = new PreferenceConverter();
	String outFile = "./tmp/csv_simple.xml";
	new File("./tmp").mkdirs();
	pc.convert(inFile, outFile);
	assertTrue(new File(outFile).exists());
	assertTrue(new File(outFile).length() > 0);
    }

    @Test
    /**
     * smoke test to see if preference (XML) file will be generated from OpenOfficeCalc file 
     */
    public void testConvertOoc() {
	fail("not done yet");
    }

    @Test
    /**
     * smoke test to see if excel file will be generated from preference file 
     */
    public void testConvertPref2Xls() {
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
     * smoke test to see if open office calc file will be generated from preference file 
     */
    public void testConvertPref2Ooc() {
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
