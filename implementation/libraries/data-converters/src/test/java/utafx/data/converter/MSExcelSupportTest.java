package utafx.data.converter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Test;

import utafx.data.converter.impl.XlsDataConverter;
import utafx.data.converter.impl.XlsxDataConverter;
import utafx.data.pref.jaxb.Alternative;
import utafx.data.pref.jaxb.Alternatives;
import utafx.data.pref.jaxb.Criteria;
import utafx.data.pref.jaxb.CriteriaType;
import utafx.data.pref.jaxb.Criterion;
import utafx.data.pref.jaxb.Preferences;
import utafx.data.pref.jaxb.RefRank;
import utafx.data.pref.jaxb.RrItem;
import utafx.data.selection.CellAddress;
import utafx.data.selection.SelectionArea;
import utafx.data.util.FileUtil;

public class MSExcelSupportTest {

    @Test
    public void testConverting_XLS_97_2003_To_Xml_With_Selection_Area()
	    throws Exception {
	String inputFile = ClassLoader.getSystemResource("xls/simple.xls")
		.getFile();
	String outputFile = "./tmp/simple_xls.xml";
	FileUtil.delete(outputFile);
	SelectionArea sa = new SelectionArea(new CellAddress(1, "B"),
		new CellAddress(11, "H"));
	XlsDataConverter converter = new XlsDataConverter(sa);
	testXmlFile(inputFile, outputFile, sa, converter);
    }

    @Test
    public void testConverting_XLS_97_2003_To_Xml_No_Selection_Area()
	    throws Exception {
	String inputFile = ClassLoader.getSystemResource("xls/simple_no_area.xls")
		.getFile();
	String outputFile = "./tmp/simple_xls_no_area.xml";
	FileUtil.delete(outputFile);
	SelectionArea sa = null;
	XlsDataConverter converter = new XlsDataConverter(sa);
	testXmlFile(inputFile, outputFile, sa, converter);
    }

    @Test
    public void testConverting_XLS_97_2003_To_Xml_With_Selection_Area_No_End_Address()
	    throws Exception {
	String inputFile = ClassLoader.getSystemResource("xls/simple.xls")
		.getFile();
	String outputFile = "./tmp/simple_xls_no_end_address.xml";
	FileUtil.delete(outputFile);
	SelectionArea sa = new SelectionArea(new CellAddress(1, "B"));
	XlsDataConverter converter = new XlsDataConverter(sa);
	testXmlFile(inputFile, outputFile, sa, converter);
    }

    void testXmlFile(String inputFile, String outputFile, SelectionArea sa,
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
	assertEquals(4, pref.getRefRank().getItem().size());
	Map<String, Integer> expected = new HashMap<String, Integer>();
	expected.put("Alternatywa 1", 1);
	expected.put("Alternatywa 3", 2);
	expected.put("Alternatywa 4", 2);
	expected.put("Alternatywa 7", 3);
	testReferenceRank(expected, pref);
    }

    void testReferenceRank(Map<String, Integer> expected, Preferences pref) {
	for (Entry<String, Integer> entry : expected.entrySet()) {
	    int id = getAlternativeId(entry.getKey(), pref.getAlternatives());
	    int rank = getRankForId(id, pref.getRefRank());
	    assertEquals(entry.getValue(), Integer.valueOf(rank));
	}
    }

    private int getRankForId(int id, RefRank refRank) {
	for (RrItem item : refRank.getItem()) {
	    if (item.getId() == id) {
		return item.getRank();
	    }
	}
	return -1;
    }

    private int getAlternativeId(String key, Alternatives alternatives) {
	for (Alternative a : alternatives.getAlternative()) {
	    if (a.getName().equals(key)) {
		return a.getId();
	    }
	}
	return -1;
    }

    @Test
    public void testConverting_XLS_2007_To_Xml() throws Exception {
	String inputFile = ClassLoader.getSystemResource("xls/simple.xlsx")
		.getFile();
	String outputFile = "./tmp/simple_xlsx.xml";
	FileUtil.delete(outputFile);
	SelectionArea sa = new SelectionArea(new CellAddress(1, "B"),
		new CellAddress(11, "H"));
	DataConverter converter = new XlsxDataConverter(sa);
	testXmlFile(inputFile, outputFile, sa, converter);
    }
}
