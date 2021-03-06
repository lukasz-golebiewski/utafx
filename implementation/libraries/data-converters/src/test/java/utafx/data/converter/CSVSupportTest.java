package utafx.data.converter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;

import org.junit.Test;

import utafx.data.converter.impl.CsvDataConverter;
import utafx.data.pref.jaxb.Alternative;
import utafx.data.pref.jaxb.Alternatives;
import utafx.data.pref.jaxb.Criteria;
import utafx.data.pref.jaxb.CriteriaType;
import utafx.data.pref.jaxb.Criterion;
import utafx.data.pref.jaxb.Preferences;
import utafx.data.util.FileUtil;

public class CSVSupportTest {
    @Test
    public void testConverting_Csv_To_Xml() throws Exception {
	String inputFile = ClassLoader.getSystemResource("csv/simple.csv")
		.getFile();
	String outputFile = "./tmp/simple_csv.xml";
	FileUtil.delete(outputFile);
	DataConverter converter = new CsvDataConverter();
	testXmlFile(inputFile, outputFile, converter);
    }

    /**
     * Converts csv file to xml file and checks the generated xml file content
     * 
     * @param inputFile
     * @param outputFile
     * @param converter
     * @throws Exception
     */
    void testXmlFile(String inputFile, String outputFile,
	    DataConverter converter) throws Exception {
	File fout = new File(outputFile);
	converter.convert(new FileInputStream(inputFile), new FileOutputStream(
		outputFile));

	assertTrue(fout.exists());
	assertTrue(fout.length() > 0);

	// now lets try to read it
	Preferences pref = new PreferenceManager().read(new FileInputStream(
		fout));
	assertNotNull(pref);

	// testing criteria
	Criteria c = pref.getCriteria();
	assertNotNull(c);

	List<Criterion> criterions = c.getCriterion();
	assertNotNull(criterions);
	assertEquals(6, criterions.size());
	int[] segments = { 2, 2, 1, 2, 3, 4 };
	CriteriaType[] types = { CriteriaType.GAIN, CriteriaType.COST,
		CriteriaType.GAIN, CriteriaType.COST, CriteriaType.GAIN,
		CriteriaType.COST };
	for (int i = 0; i < criterions.size(); i++) {
	    assertEquals(i, criterions.get(i).getId());
	    assertEquals("Kryterium" + (i + 1), criterions.get(i).getName());
	    assertSame(types[i], criterions.get(i).getType());
	    assertEquals(segments[i], criterions.get(i).getSegments());
	}

	// testing alternatives
	Alternatives alterns = pref.getAlternatives();
	assertNotNull(alterns);

	List<Alternative> aas = alterns.getAlternative();
	assertNotNull(aas);
	assertEquals(9, aas.size());
	for (int i = 0; i < aas.size(); i++) {
	    assertEquals(i, aas.get(i).getId());
	    assertEquals("Alternatywa " + (i + 1), aas.get(i).getName());
	    for (int j = 0; j < criterions.size(); j++) {
		assertEquals(String.format("Wartosc A%d%d", i + 1, j + 1), aas
			.get(i).getValues().getValue().get(j).getValue());
		assertEquals(Integer.valueOf(j), aas.get(i).getValues()
			.getValue().get(j).getId());
	    }
	}
	assertNotNull(pref.getRefRank());
    }
}
