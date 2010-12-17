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
import utafx.data.pref.jaxb.Alternatives;
import utafx.data.pref.jaxb.Criteria;
import utafx.data.pref.jaxb.Preferences;
import utafx.data.pref.jaxb.RefRank;

/**
 * Converts CSV (comma separated values) files to UtaFX preferences file (XML).
 * 
 * @author <a href="mailto:marzec12@poczta.onet.pl">Pawel Solarski</a>
 */
public class CsvDataConverter implements DataConverter {

    /** default separator = ";" */
    private static final String DEFAULT_SEPARATOR = ";";
    private static Logger LOG = Logger.getLogger(CsvDataConverter.class);
    private final ConvertType conversionType = new ConvertType(FileFormat.CSV,
	    FileFormat.XML);
    private String separator;
    private String[] lines;

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
	    readLines(input);
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
	RefRank rr = reaReferenceRank();
	Preferences pref = new Preferences();
	pref.setCriteria(criteria);
	pref.setAlternatives(alterns);
	pref.setRefRank(rr);
	return pref;
    }

    private Criteria readCriteria() {
	// TODO Auto-generated method stub
	return null;
    }

    private Alternatives readAltenatives() {
	// TODO Auto-generated method stub
	return null;
    }

    private RefRank reaReferenceRank() {
	// TODO Auto-generated method stub
	return null;
    }

    private void readLines(InputStream input) throws IOException {
	BufferedReader reader = new BufferedReader(new InputStreamReader(input));
	String line = "";
	List<String> lineList = new ArrayList<String>();
	while ((line = reader.readLine()) != null) {
	    lineList.add(line);
	}
	this.lines = lineList.toArray(new String[lineList.size()]);
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
