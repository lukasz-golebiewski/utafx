package utafx.data.converter.impl;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import utafx.data.converter.ConvertType;
import utafx.data.converter.FileFormat;

public class Xml2XlsxDataConverter extends Xml2XlsDataConverter {

    @Override
    public ConvertType getConversionType() {
	return new ConvertType(FileFormat.XML, FileFormat.XLSX);
    }

    @Override
    protected Workbook createWorkbook() {
	return new XSSFWorkbook();
    }
}
