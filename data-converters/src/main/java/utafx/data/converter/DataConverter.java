package utafx.data.converter;

import java.io.InputStream;
import java.io.OutputStream;

import utafx.data.exception.ConversionException;

/**
 * Generic interface for converting streams.
 * 
 * @author <a href="mailto:marzec12@poczta.onet.pl">Pawel Solarski</a>
 */
public interface DataConverter {
    /**
     * Converts data from input stream to output stream.
     * 
     * @param input
     * @param output
     * @throws ConversionException
     */
    void convert(InputStream input, OutputStream output)
	    throws ConversionException;

    /**
     * Returns the conversion type this converter supports. The returned object
     * contains the type of input and output based on file extension format.
     *  
     * @return conversion types this converter supports
     */
    ConvertType getConversionType();
}
