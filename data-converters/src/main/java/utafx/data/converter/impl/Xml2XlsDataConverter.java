package utafx.data.converter.impl;

import java.io.InputStream;
import java.io.OutputStream;

import utafx.data.converter.ConvertType;
import utafx.data.converter.DataConverter;
import utafx.data.converter.FileFormat;
import utafx.data.exception.ConversionException;

public class Xml2XlsDataConverter implements DataConverter {

    private final ConvertType conversionType = new ConvertType(FileFormat.XML,
	    FileFormat.XLS);

    public void convert(InputStream input, OutputStream output)
	    throws ConversionException {
	// TODO Auto-generated method stub

    }

    public ConvertType getConversionType() {
	return this.conversionType;
    }

}
