package utafx.data.converter.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.bind.JAXBException;

import utafx.data.converter.ConvertType;
import utafx.data.converter.FileFormat;
import utafx.data.converter.PreferenceDataConverter;
import utafx.data.converter.PreferenceManager;
import utafx.data.exception.ConversionException;
import utafx.data.pref.jaxb.Alternative;
import utafx.data.pref.jaxb.Alternatives;
import utafx.data.pref.jaxb.Criteria;
import utafx.data.pref.jaxb.Criterion;
import utafx.data.pref.jaxb.Preferences;
import utafx.data.pref.jaxb.RefRank;
import utafx.data.pref.jaxb.RrItem;
import utafx.data.pref.jaxb.Value;
import utafx.data.util.CommonUtil;

public class Xml2CsvDataConverter implements PreferenceDataConverter {

    private final ConvertType type = new ConvertType(FileFormat.XML,
	    FileFormat.CSV);

    private final String DEFAULT_SEPARATOR = ";";
    private String separator;

    private int columns;

    private int rows;

    public void convert(InputStream input, OutputStream output)
	    throws ConversionException {
	PreferenceManager reader = new PreferenceManager();
	try {
	    Preferences prefs = reader.read(input);
	    String[][] data = createCSV(prefs);
	    save(data, output);
	} catch (JAXBException e) {
	    throw new ConversionException("JAXB error occured", e);
	} catch (IOException e) {
	    throw new ConversionException("I/O error occured", e);
	}

    }

    private void save(String[][] data, OutputStream output) throws IOException {
	byte[] sep = getSeparator().getBytes();
	for (int line = 0; line < data.length; line++) {
	    for (int col = 0; col < data[line].length; col++) {
		String value = data[line][col];
		if (CommonUtil.isNotEmpty(value)) {
		    output.write(value.getBytes());
		}
		output.write(sep);
	    }
	    output.write("\n".getBytes());
	}
	output.close();
    }

    private String getSeparator() {
	if (this.separator == null) {
	    separator = DEFAULT_SEPARATOR;
	}
	return separator;
    }

    private String[][] createCSV(Preferences prefs) {
	columns = 0;
	rows = 0;
	boolean criteriaExists = false;
	boolean alternsExists = false;
	boolean refRankExists = false;

	int critSize = prefs.getCriteria().getCriterion().size();
	if (critSize > 0) {
	    rows = 2;
	    columns += critSize;
	    criteriaExists = true;
	}
	int alternsSize = prefs.getAlternatives().getAlternative().size();
	if (alternsSize > 0) {
	    columns++;
	    rows += alternsSize;
	    alternsExists = true;
	}
	int rankSize = prefs.getRefRank().getItem().size();
	if (alternsExists && rankSize > 0) {
	    columns++;
	    refRankExists = true;
	}
	String[][] data = new String[rows][columns];
	if (criteriaExists) {
	    writeCriteria(data, prefs.getCriteria(), alternsExists,
		    refRankExists);
	}
	if (alternsExists) {
	    writeAlternatives(data, prefs.getAlternatives(), criteriaExists,
		    refRankExists);
	}
	if (refRankExists) {
	    writeReferenceRank(data, prefs.getRefRank(), criteriaExists);
	}
	return data;
    }

    private void writeReferenceRank(String[][] data, RefRank refRank,
	    boolean criteriaExists) {
	int line = criteriaExists ? 2 : 0;
	int col = 0;
	for (RrItem item : refRank.getItem()) {
	    data[line + item.getId()][col] = String.valueOf(item.getRank());
	}
    }

    private void writeAlternatives(String[][] data, Alternatives alternatives,
	    boolean criteriaExists, boolean refRankExists) {
	int line = criteriaExists ? 2 : 0;
	int offset = refRankExists ? 1 : 0;

	int aIndex = 0;
	for (Alternative a : alternatives.getAlternative()) {
	    data[line + aIndex][offset] = a.getName();
	    int vIndex = 0;
	    for (Value v : a.getValues().getValue()) {
		data[line + aIndex][vIndex + offset + 1] = v.getValue();
		vIndex++;
	    }
	    aIndex++;
	}
    }

    private void writeCriteria(String[][] data, Criteria criteria,
	    boolean alternsExists, boolean refRankExists) {
	int offset = 0;
	int line = 0;

	if (alternsExists)
	    offset++;
	if (refRankExists)
	    offset++;

	if (data[line] == null)
	    data[line] = new String[columns];

	int i = 0;
	for (Criterion c : criteria.getCriterion()) {
	    int index = i + offset;
	    data[line][index] = CommonUtil.getTypeAndSegments(c);
	    data[line + 1][index] = c.getName();
	    i++;
	}
    }

    public ConvertType getConversionType() {
	return type;
    }

    public void convert(Preferences preferences, OutputStream output)
	    throws ConversionException {
	try {
	    String[][] data = createCSV(preferences);
	    save(data, output);
	} catch (IOException e) {
	    throw new ConversionException("I/O error occured", e);
	}
    }

}
