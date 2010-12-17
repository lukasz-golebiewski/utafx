package utafx.data.converter.impl;

import utafx.data.converter.ConvertType;
import utafx.data.converter.FileFormat;
import utafx.data.selection.SelectionArea;

/**
 * Converts Microsoft Excel (2007) files to UtaFX preferences file (XML).
 * 
 * @author <a href="mailto:marzec12@poczta.onet.pl">Pawel Solarski</a>
 */
public class Excel2007DataConverter extends ExcelDataConverter {

    private final ConvertType conversionType = new ConvertType(FileFormat.XLSX,
	    FileFormat.XML);

    public Excel2007DataConverter(SelectionArea area) {
	super(area);
    }

    @Override
    public ConvertType getConversionType() {
	return conversionType;
    }
}
