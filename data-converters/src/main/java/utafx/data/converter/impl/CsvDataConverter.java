package utafx.data.converter.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.apache.log4j.Logger;

import utafx.data.converter.ConvertType;
import utafx.data.converter.DataConverter;
import utafx.data.converter.FileFormat;
import utafx.data.converter.PreferenceManager;
import utafx.data.exception.ConversionException;
import utafx.data.pref.jaxb.AltValues;
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

/**
 * Converts CSV (comma separated values) files to UtaFX preferences file (XML).
 * 
 * @author <a href="mailto:marzec12@poczta.onet.pl">Pawel Solarski</a>
 */
public class CsvDataConverter implements DataConverter {

    private static Logger LOG = Logger.getLogger(CsvDataConverter.class);

    /** default separator = ";" */
    private static final String DEFAULT_SEPARATOR = ";";
    private static final int CRIT_TYPES_LINE = 0;
    private static final int CRIT_NAMES_LINE = 1;
    private static final int ALTERNS_DATA_LINE = 2;

    private final ConvertType conversionType = new ConvertType(FileFormat.CSV,
	    FileFormat.XML);
    private String separator;
    /** data holds comma separated values */
    private String[][] data;

    /**
     * Creates CSV converter with specified comma separator
     * 
     * @param separator
     */
    public CsvDataConverter(String separator) {
	this.separator = separator;
    }

    /**
     * Creates CSV converter with {@link #DEFAULT_SEPARATOR}
     * 
     * @param separator
     */
    public CsvDataConverter() {
	this(DEFAULT_SEPARATOR);
    }

    public void convert(InputStream input, OutputStream output)
	    throws ConversionException {
	try {
	    readData(input);
	    Preferences pref = readPreferences();
	    save(pref, output);
	} catch (IOException e) {
	    throw new ConversionException("I/O error occured", e);
	} catch (JAXBException e) {
	    throw new ConversionException("JAXB error occured", e);
	}
    }

    private void save(Preferences pref, OutputStream output)
	    throws JAXBException, IOException {
	PreferenceManager writer = new PreferenceManager();
	writer.write(pref, output);
    }

    private Preferences readPreferences() {
	Criteria criteria = readCriteria();
	Alternatives alterns = readAltenatives();
	RefRank rr = readReferenceRank();
	Preferences pref = new Preferences();
	pref.setCriteria(criteria);
	pref.setAlternatives(alterns);
	pref.setRefRank(rr);
	return pref;
    }

    private Criteria readCriteria() {
	if (data.length > 1) {
	    String[] criteria = data[CRIT_NAMES_LINE];
	    int index = CommonUtil.getFirstNotEmptyValueIndex(criteria);
	    return createCriteria(index, criteria.length - 1);
	}
	return null;
    }

    /**
     * Create criteria starting from startIndex up to endIndex. The value at
     * position startIndex may reflect the name column, so this will not be
     * treated as criterion.
     * 
     * @param startIndex
     *            index of (possible) first criterion name
     * @param endIndex
     *            index of last criterion name
     * @return criteria build from the given range [startIndex..endIndex]
     */
    private Criteria createCriteria(int startIndex, int endIndex) {
	Criteria criteria = new Criteria();
	int id = 0;
	for (int index = startIndex; index < endIndex + 1; index++) {
	    String name = data[CRIT_NAMES_LINE][index];
	    String type = data[CRIT_TYPES_LINE][index];
	    if (type == null || type.trim().isEmpty()) {
		LOG.warn(String
			.format("Could not find criteria type (line %d, column %d). Criterion \"%s\" will not be used.",
				CRIT_TYPES_LINE + 1, index + 1, name));

		continue;
	    }
	    Criterion c = createCriterion(name, type, index);
	    if (c != null) {
		c.setId(id++);
		criteria.getCriterion().add(c);
		LOG.info(String
			.format("Found criterion (column %d): id=%s, name=\"%s\", type=\"%s\", segments=\"%s\"",
				index + 1, c.getId(), c.getName(), c.getType()
					.name(), c.getSegments()));
	    }
	}
	return criteria;
    }

    private Criterion createCriterion(String name, String type, int column) {
	if (type != null && !type.trim().isEmpty()) {
	    CriteriaType cType = CommonUtil.getType(type);
	    int segments = getSegments(name, type);
	    if (cType != null && segments > 0) {
		Criterion c = new Criterion();
		c.setName(name);
		c.setType(cType);
		c.setSegments(segments);
		return c;
	    } else if (cType == null) {
		LOG.warn(String
			.format("Incorect criteria type in %s (line %d, column %d). Criterion %s will not be used.",
				type, CRIT_TYPES_LINE + 1, column + 1, name));

	    } else {
		LOG.warn(String
			.format("Incorrect segments value in %s (line %d, column %d). Criterion %s will not be used.",
				type, CRIT_TYPES_LINE + 1, column + 1, name));
	    }
	}
	return null;
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

    private Alternatives readAltenatives() {
	Alternatives alternatives = new Alternatives();
	int id = 0;
	for (int line = ALTERNS_DATA_LINE; line < data.length; line++) {
	    int startIndex = CommonUtil
		    .getFirstNotEmptyValueIndex(data[CRIT_TYPES_LINE])-1;
	    Alternative a = createAlternative(startIndex, data[line]);
	    if (a != null) {
		a.setId(id++);
		alternatives.getAlternative().add(a);
		LOG.info(String.format("Found alternative (line %d): %s",
			line + 1, CommonUtil.toString(a)));
	    }
	}
	return alternatives;
    }

    private Alternative createAlternative(int startIndex, String[] adata) {
	Alternative a = new Alternative();
	a.setName(adata[startIndex]);
	AltValues values = new AltValues();
	List<Value> valueList = values.getValue();
	for (int index = startIndex + 1; index < adata.length; index++) {
	    Value v = new Value();
	    v.setId(index - startIndex - 1);
	    v.setValue(adata[index]);
	    valueList.add(v);
	}
	a.setValues(values);
	return a;
    }

    private RefRank readReferenceRank() {
	RefRank rank = new RefRank();
	int rankCol;
	String[] criteria = data[CRIT_NAMES_LINE];
	int index = CommonUtil.getFirstNotEmptyValueIndex(criteria);
	if (index > 0) {
	    rankCol = index - 1;
	    int id = 0;
	    for (int line = ALTERNS_DATA_LINE; line < data.length; line++) {
		String srank = (data[line][rankCol]);
		if (srank != null && !srank.isEmpty())
		    try {
			int r = Integer.parseInt(srank);
			RrItem item = new RrItem();
			item.setId(id++);
			item.setRank(r);
			rank.getItem().add(item);
			LOG.info(String.format(
				"Found rank value=\"%d\" (line %d, column %d)",
				r, line + 1, rankCol + 1));

		    } catch (NumberFormatException e) {
			LOG.error(String.format(
				"Found incorect rank value=\"%s\" (line %d, column %d)",
				srank, line + 1, rankCol + 1));
		    }
	    }
	} else {
	    LOG.info("Reference rank data not found.\nTo specify reference rank, insert data into the column left to the alternatives names column");
	}
	return rank;
    }

    private void readData(InputStream input) throws IOException {
	BufferedReader reader = new BufferedReader(new InputStreamReader(input));
	String line = "";
	List<String[]> lineList = new ArrayList<String[]>();
	while ((line = reader.readLine()) != null) {
	    lineList.add(line.split(separator));
	}
	this.data = lineList.toArray(new String[lineList.size()][]);
    }

    public ConvertType getConversionType() {
	return conversionType;
    }

    public String getSeparator() {
	return separator;
    }

    public void setSeparator(String separator) {
	this.separator = separator;
    }
}
