package utafx.data.converter;

import java.io.InputStream;
import java.io.OutputStream;

import utafx.data.exception.ConversionException;

public interface DataConverter {
    void convert(InputStream input, OutputStream output)
	    throws ConversionException;
}
