package utafx.data.converter;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import utafx.data.exception.ConversionException;
import utafx.data.exception.UnsupportedFormatException;
import utafx.data.selection.SelectionArea;

public class PreferenceConverter {

    private SelectionArea area;

    private Map<ConvertType, DataConverter> converters;

    public PreferenceConverter() {
	initializeConverters();
    }

    private void initializeConverters() {
	converters = new HashMap<ConvertType, DataConverter>();

	DataConverter xlsCon = new ExcelDataConverter(area);
	converters.put(new ConvertType(FileFormat.XLS, FileFormat.XML), xlsCon);
	converters
		.put(new ConvertType(FileFormat.XLSX, FileFormat.XML), xlsCon);
    }

    private String[] extensions = { ".xls", ".xlsx", ".csv", ".ods", ".xml" };

    /**
     * Converts given input file to output file. This method is used for the
     * bidiectional conversion, thus file extensions will be checked for source
     * and target conversion types.
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

    private ConvertType getConversionType(String inputPath, String outputPath) {
	String src = getExtension(inputPath);
	String dst = getExtension(outputPath);
	return new ConvertType(src, dst);
    }

    private String getExtension(String inputPath) {
	return inputPath.substring(inputPath.lastIndexOf(".") + 1);
    }

    public String[] getSupportedFormats() {
	return extensions;
    }
}
