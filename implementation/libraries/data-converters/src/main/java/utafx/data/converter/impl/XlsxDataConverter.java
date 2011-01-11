package utafx.data.converter.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import utafx.data.converter.ConvertType;
import utafx.data.converter.FileFormat;
import utafx.data.exception.ConversionException;
import utafx.data.selection.SelectionArea;

/**
 * Converts Microsoft Excel (2007) files to UtaFX preferences file (XML).
 * 
 * @author <a href="mailto:marzec12@poczta.onet.pl">Pawel Solarski</a>
 */
public class XlsxDataConverter extends XlsDataConverter {

    private final ConvertType conversionType = new ConvertType(FileFormat.XLSX,
	    FileFormat.XML);

    public XlsxDataConverter(SelectionArea area) {
	super(area);
    }
    
    public XlsxDataConverter() {
	super();
    }

    @Override
    public void convert(InputStream input, OutputStream output)
	    throws ConversionException {
	super.convert(input, output);
    }

    @Override
    protected Workbook getWorkbook(InputStream input) throws IOException {
	return new XSSFWorkbook(input);
    }

    @Override
    public ConvertType getConversionType() {
	return conversionType;
    }
}
