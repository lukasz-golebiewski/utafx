package utafx.data.converter;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import utafx.data.converter.impl.CsvDataConverter;
import utafx.data.converter.impl.XlsDataConverter;
import utafx.data.converter.impl.XlsxDataConverter;
import utafx.data.converter.impl.Xml2CsvDataConverter;
import utafx.data.converter.impl.Xml2XlsDataConverter;
import utafx.data.converter.impl.Xml2XlsxDataConverter;
import utafx.data.exception.ConversionException;
import utafx.data.exception.UnsupportedFormatException;
import utafx.data.pref.jaxb.Preferences;
import utafx.data.selection.SelectionArea;

public class ConversionManager {

    private SelectionArea area;

    private Map<ConvertType, DataConverter> converters;

    public ConversionManager() {
	initializeConverters();
    }

    private void initializeConverters() {
	converters = new HashMap<ConvertType, DataConverter>();
	addConverter(new XlsDataConverter(area));
	addConverter(new XlsxDataConverter(area));
	addConverter(new CsvDataConverter());
	addConverter(new Xml2XlsDataConverter());
	addConverter(new Xml2XlsxDataConverter());
	addConverter(new Xml2CsvDataConverter());
    }

    private void addConverter(DataConverter converter) {
	if (converter != null) {
	    converters.put(converter.getConversionType(), converter);
	}
    }

    /**
     * Converts given input file to output file. This method can be used for any
     * conversion, thus file extensions will be checked for source and target
     * conversion types.
     * <p>
     * If there is no converter that could handle specified input and output
     * types, {@link UnsupportedFormatException} will be thrown.
     * </p>
     * 
     * @param inputPath
     *            path to input file
     * @param outputPath
     *            path where to store file
     * @throws IOException
     *             if any I/O error occurs
     * @throws ConversionException
     *             if conversion didn't succeed for some reason
     * @throws UnsupportedFormatException
     *             if the data format is not supported
     * 
     */
    public void convert(String inputPath, String outputPath)
	    throws UnsupportedFormatException, ConversionException, IOException {
	ConvertType ct = getConversionType(inputPath, outputPath);
	DataConverter concreteConverter = converters.get(ct);
	if (concreteConverter != null) {
	    concreteConverter.convert(new FileInputStream(inputPath),
		    new FileOutputStream(outputPath));
	} else {
	    throw new UnsupportedFormatException(String.format(
		    "Conversion \"%s\" -> \"%s\" is not supported", ct
			    .getSourceFormat().toString(), ct
			    .getDestinationFormat().toString()));
	}
    }

    public void write(Preferences preferences, String outputPath)
	    throws UnsupportedFormatException, IOException {
	ConvertType ct = getConversionType("someFile.xml", outputPath);
	PreferenceDataWriter writer = converters.get(ct);
	if (writer != null) {
	    writer.write(preferences, new FileOutputStream(outputPath));
	} else {
	    throw new UnsupportedFormatException(String.format(
		    "Conversion \"%s\" -> \"%s\" is not supported", ct
			    .getSourceFormat().toString(), ct
			    .getDestinationFormat().toString()));
	}
    }

    public Preferences read(String inputPath)
	    throws UnsupportedFormatException, IOException {
	ConvertType ct = getConversionType(inputPath, "someFile.xml");
	PreferenceDataReader reader = converters.get(ct);	
	if (reader != null) {
	    return reader.read(new FileInputStream(inputPath));
	} else {
	    throw new UnsupportedFormatException(String.format(
		    "Conversion \"%s\" -> \"%s\" is not supported", ct
			    .getSourceFormat().toString(), ct
			    .getDestinationFormat().toString()));
	}
    }

    public ConvertType getConversionType(String inputPath, String outputPath) {
	String src = getExtension(inputPath);
	String dst = getExtension(outputPath);
	return new ConvertType(src, dst);
    }

    private String getExtension(String inputPath) {
	return inputPath.substring(inputPath.lastIndexOf(".") + 1);
    }

    public ConvertType[] getSupportedConversions() {
	return converters.keySet().toArray(new ConvertType[converters.size()]);
    }

    public DataConverter getConverter(ConvertType cType) {
	return converters.get(cType);
    }
}
